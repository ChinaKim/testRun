package com.zhongli.main.zhonglitenghui.until;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.zhongli.main.zhonglitenghui.R;
import com.zhongli.main.zhonglitenghui.app.InitApplication;
import com.zhongli.main.zhonglitenghui.bean.Video;
import com.zhongli.main.zhonglitenghui.bean.VideoDown;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 278877385 on 2015/3/23.
 */
public class DownVideo extends AsyncTask<Void, Integer, Void> {
    /**
     * 文件开始下载的位置
     */
    private long startPosition;

    /**
     * 文件结束下载的位置
     */
    private long endPosition;

    /**
     * 文件的总长度
     */
    private long fileLenght;

    /**
     * 视频的文件夹
     */
    private File videoFiles;
    /**
     * 视频文件
     */
    private File pathFile;
    /**
     * 下载地址
     */
    private String urlVideo;
    private Context context;
    private Video video;
    private SharedPreferences sp;
    private long currentTime;
    private long nextTime;
    private Handler handler;
    private int position;
    private VideoDown videoDown;

    public DownVideo(Context context, SharedPreferences sp, Video video, File videoFiles, int position, Handler handler) {
        this.context = context;
        this.video = video;
        this.videoFiles = videoFiles;
        this.sp = sp;
        this.urlVideo = this.context.getResources().getString(R.string.url_root) + video.getUrl();
        this.handler = handler;
        this.position = position;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //记录开始文件
        startPosition = sp.getLong("START" + video.getVideoName(), 0);
        //创建文件
        pathFile = new File(videoFiles.getAbsolutePath() + "//" + video.getVideoName() + ".mp4");
        if (!pathFile.exists()) {
            try {
                pathFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        videoDown = new VideoDown();
        Message msg = handler.obtainMessage();
        msg.what = 1;
        videoDown.setPosition(position);
        videoDown.setDownLoad(0);
        msg.obj = videoDown;
//        roundProgressBar.setVisibility(View.VISIBLE);
//        roundProgressBar.setMax(100);
        endPosition = sp.getLong("End" + video.getVideoName(), 0);
        handler.sendMessage(msg);
//        if (endPosition != 0) {
//            int proess = (int) ((startPosition / (float) endPosition) * 100);
//            roundProgressBar.setProgress(proess);
//        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        long nextStartPosition = 0;
        RandomAccessFile loadFile = null;
        InputStream is = null;
        try {
            URL url = new URL(urlVideo);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Range", "bytes=" + startPosition
                    + "-");
            int returnCode = connection.getResponseCode();
            if (returnCode == 206) {
                is = connection.getInputStream();
                //创建随机读取文件流

                loadFile = new RandomAccessFile(pathFile,
                        "rwd");
                if(endPosition == 0){
                    fileLenght = connection.getContentLength();
                    endPosition = fileLenght;
                    sp.edit().putLong("End" + video.getVideoName(), endPosition).commit();
                    loadFile.setLength(endPosition);
                }
                loadFile.seek(startPosition);
                //下载量
                int total = 0;
                int len = 0;
                byte[] bytesBuffer = new byte[4096];
                while ((len = is.read(bytesBuffer)) != -1 && video.isDown()) {
                    currentTime = System.currentTimeMillis();
                    if (currentTime - nextTime >= 1000) {
                        //更新进度
                        int proess = (int) (((startPosition + total) / (float) endPosition) * 100);
                        publishProgress(proess);
                        nextTime = currentTime;
                    }
                    loadFile.write(bytesBuffer, 0, len);
                    total += len;
                    // 下次下载的其实位置
                    nextStartPosition = total + startPosition;
                    //存放下载位置
                    //sp.edit().putLong("START" + video.getVideoName(), nextStartPosition).commit();
                }
                //下载完成标志位
                video.setDown(false);
                if (nextStartPosition == endPosition) {
                    InitApplication.appLog.i("下载完成。。。。。");
                    sp.edit().putBoolean(video.getVideoName(), true).commit();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (loadFile != null) {
                try {
                    loadFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!sp.getBoolean(video.getVideoName(), false)) {
                sp.edit().putLong("START" + video.getVideoName(), nextStartPosition).commit();
            }
    }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //下载完成
        if (sp.getBoolean(video.getVideoName(), false)) {
            InitApplication.downCount--;
            Message msg = handler.obtainMessage();
            msg.what = 3;
            videoDown.setPosition(position);
            videoDown.setDownLoad(100);
            msg.obj = videoDown;
            handler.sendMessage(msg);
            //roundProgressBar.setVisibility(View.GONE);
            Toast.makeText(context, video.getVideoName() + ".mp4 下载完成", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Message msg = handler.obtainMessage();
        msg.what = 2;
        videoDown.setPosition(position);
        videoDown.setDownLoad(values[0]);
        msg.obj = videoDown;
        handler.sendMessage(msg);
        //roundProgressBar.setProgress(values[0]);
    }

    public long getStartPosition() {
        return startPosition;
    }

    public long getEndPosition() {
        return endPosition;
    }

}
