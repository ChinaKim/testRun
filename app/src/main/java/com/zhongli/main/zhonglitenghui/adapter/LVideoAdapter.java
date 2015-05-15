package com.zhongli.main.zhonglitenghui.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongli.main.zhonglitenghui.R;
import com.zhongli.main.zhonglitenghui.app.InitApplication;
import com.zhongli.main.zhonglitenghui.bean.Video;
import com.zhongli.main.zhonglitenghui.bean.VideoDown;
import com.zhongli.main.zhonglitenghui.custom.RoundProgressBar;
import com.zhongli.main.zhonglitenghui.until.DownVideo;
import com.zhongli.main.zhonglitenghui.volley.BitmapCache;
import com.zhongli.main.zhonglitenghui.volley.VolleyManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 278877385 on 2015/3/20.
 */
public class LVideoAdapter extends BaseAdapter {
    private Context context;
    private List<Video> vList;
    private int size;
    private String filePath;
    private SharedPreferences sp;
    private static ExecutorService LIMITED_TASK_EXECUTOR;
    private List<RoundProgressBar> rList;
    private List<ImageView> iList;
    private Map<Integer, DownVideo> mapDown = new HashMap<Integer, DownVideo>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //更新UI
            VideoDown videoDown = (VideoDown) msg.obj;
            switch (msg.what) {
                case 1:
                    //初始化
                    rList.get(videoDown.getPosition()).setVisibility(View.VISIBLE);
                    rList.get(videoDown.getPosition()).setMax(100);
                    iList.get(videoDown.getPosition()).setVisibility(View.GONE);
                    if (mapDown.get(videoDown.getPosition()).getEndPosition() != 0) {
                        int proess = (int) ((mapDown.get(videoDown.getPosition()).getStartPosition() / (float) mapDown.get(videoDown.getPosition()).getEndPosition()) * 100);
                        rList.get(videoDown.getPosition()).setProgress(proess);
                    }
                    break;
                case 2:
                    //更新操作
                    rList.get(videoDown.getPosition()).setProgress(videoDown.getDownLoad());
                    break;
                case 3:
                    //完成操作
                    rList.get(videoDown.getPosition()).setVisibility(View.GONE);
                    iList.get(videoDown.getPosition()).setVisibility(View.VISIBLE);
                    iList.get(videoDown.getPosition()).setImageResource(R.drawable.video_play);
                    break;
            }

        }
    };

    public LVideoAdapter(Context context, List<Video> vList) {
        this.context = context;
        this.vList = vList;
        size = vList.size();
        //视频存放的目录
        filePath = context.getExternalCacheDir().getAbsolutePath() + "/video";
        sp = context.getSharedPreferences("vidoList.xml", Activity.MODE_PRIVATE);
        //创建只有5个线程的线程池
        LIMITED_TASK_EXECUTOR = Executors.newFixedThreadPool(5);
        rList = new ArrayList<RoundProgressBar>();
        iList = new ArrayList<ImageView>();
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return vList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final ViewHold hold;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_video_list, null);
            hold = new ViewHold();
            hold.img_video_view = (ImageView) convertView.findViewById(R.id.img_video_view);
            hold.tv_vido_name = (TextView) convertView.findViewById(R.id.tv_vido_name);
            hold.img_download = (ImageView) convertView.findViewById(R.id.img_download);
            hold.rp_video_view = (RoundProgressBar) convertView.findViewById(R.id.rp_video_view);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }
        rList.add(hold.rp_video_view);
        iList.add(hold.img_download);
        //加载默认图
        String imgUrl = context.getResources().getString(R.string.url_root) + vList.get(position).getMurl();
        boolean flag = BitmapCache.getInstern().getSDCardBitmap(imgUrl, hold.img_video_view, new BitmapCache.CallBackSDcardImg() {
            @Override
            public void setImgToView(Bitmap bitmap, ImageView imgView) {
                new ObjectAnimator().ofFloat(imgView, "alpha", 0.3f, 1.0f).setDuration(500).start();
                imgView.setImageBitmap(bitmap);
            }
        });
        if (!flag) {
            VolleyManager.loadImage(hold.img_video_view, imgUrl, R.drawable.img_def_url);
        }
//        hold.img_video_view.setDefaultImageResId(R.drawable.img_def_url);
//        hold.img_video_view.setImageUrl(imgUrl, VolleyManager.getImageLoader());
        //添加描述
        hold.tv_vido_name.setText(vList.get(position).getVideoName());
        if (sp.getBoolean(vList.get(position).getVideoName(), false)) {
            hold.img_download.setImageResource(R.drawable.video_play);
        }

        hold.img_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Video video = vList.get(position);
                String videoName = vList.get(position).getVideoName();
                File fileC = new File(filePath);
                File fileV = new File(fileC.getAbsoluteFile() + "/" + videoName + ".mp4");
                if (isDownLoadFinsh(fileC, videoName)) {
                    //播放视频
                    //调用系统自带的播放器
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(fileV.getAbsolutePath()), "video/mp4");
                    context.startActivity(intent);
                    /*Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("path",filePath);
                    intent.putExtras(bundle);
                    intent.setClass(context, VideoActivity.class);
                    context.startActivity(intent);*/

                } else {
                    //下载视频
                    video.setDown(true);
                    InitApplication.downCount++;
                    if (InitApplication.downCount <= 5) {
                        DownVideo downVideo = new DownVideo(context, sp, video, fileC, position, handler);
                        downVideo.executeOnExecutor(LIMITED_TASK_EXECUTOR);
                        //放入Map集合中
                        mapDown.put(position, downVideo);

                    } else {
                        Toast.makeText(context, "同时只能下载5个视频", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return convertView;
    }

    public class ViewHold {
        public ImageView img_video_view;
        public TextView tv_vido_name;
        public ImageView img_download;
        public RoundProgressBar rp_video_view;
    }

    /**
     * 判断是否下载完成
     *
     * @return
     */
    protected boolean isDownLoadFinsh(File file, String videoName) {
        if (!file.exists()) {
            file.mkdirs();
            return false;
        } else {
            return sp.getBoolean(videoName, false);
        }
    }
}
