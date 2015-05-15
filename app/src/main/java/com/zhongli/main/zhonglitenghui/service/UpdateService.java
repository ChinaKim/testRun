package com.zhongli.main.zhonglitenghui.service;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongli.main.zhonglitenghui.MainActivity;
import com.zhongli.main.zhonglitenghui.R;
import com.zhongli.main.zhonglitenghui.app.InitApplication;
import com.zhongli.main.zhonglitenghui.until.OptAnimationLoader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author ewewewrt Email 278877385@qq.com 说明 自动更新的服务
 * @date 2014-5-13下午4:58:43
 */
public class UpdateService extends Service {
    private static String DIRECTORY_PATH;
    private static final int NOTIFICATION_ID = 1;
    private static final int SPACE_LENGHT_AS = 1;
    private static final int SPACE_LENGHT_NO = 2;
    private static final int DOWN_SUCCEED_OK = 3;
    private static final int DOWN_FAILE_OK = 4;
    private static final int DOWN_UPDATE_UI = 5;
    private SharedPreferences sp;
    private List<File> fList = new ArrayList<File>();
    private AlterDialog updateDialog;
    private Notification updateNotification;
    private NotificationManager notificationManager;
    private PendingIntent contentIntent;
    private String xmlUrlPath;
    private int progressId = R.id.pb_update;
    private int tvApknameId = R.id.tv_apkname;
    private int tvLoadTextId = R.id.tv_update_progress;
    private int tvSpeedTextId = R.id.tv_update_speed;
    private int count = 0;
    private long nextTime;
    private long currentTime;
    private long[] startPosition;
    private long[] endPosition;
    private boolean downLoadFlag;
    // 已经下载的数据
    private long progressLoad = 0;
    // 一段时间内下载的数据
    private long speedLoad = 0;
    // 下载的速度
    private int speedDowan = 0;
    private AppVersion av;
    public static long fileLength;
    public static String pathFile;
    // Dialog的布局
    public static int updatMessageLayoutId;
    // 开启的线程的数量
    private static int threadCount = 3;
    // 每个线程负责下载的程序块的大小
    private static long indexLength;
    // 程序的下载地址
    private static String urlStr;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // 有剩余控件开始下载
                case SPACE_LENGHT_AS:
                    Log.i("tag", "开始下载。。。。。。。。。");
                    // 初始化threadCount对应的线程数去下载
                    notificationManager.cancel(NOTIFICATION_ID);
                    updateNotification = creatUpdateNotification();
                    updateNotification.contentView.setProgressBar(progressId, 100,
                            0, true);
                    updateNotification.contentView.setTextViewText(tvApknameId,
                            av.getVersionName());
                    updateNotification.contentView.setTextViewText(tvLoadTextId,
                            "0%");
                    updateNotification.contentView.setTextViewText(tvSpeedTextId,
                            "0/KB");
                    notificationManager.notify(NOTIFICATION_ID, updateNotification);
                    UpdateService.this.startForeground(NOTIFICATION_ID,
                            updateNotification);
                    downLoadAPKThread();
                    break;
                // SDcard剩余控件不足显示提示框
                case SPACE_LENGHT_NO:
                    Log.i("tag", "剩余空间不足。。。。。");
                    notificationManager.cancel(NOTIFICATION_ID);
                    updateNotification = creatFailNotification();
                    notificationManager.notify(NOTIFICATION_ID, updateNotification);
                    updateDialog = new AlterDialog.Builder(UpdateService.this)
                            .setOKButton("确定", new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    updateDialog.dismiss();
                                }
                            }).setContentText("您的存储卡剩余空间不足").show();
                    break;
                // 下载成功
                case DOWN_SUCCEED_OK:
                    notificationManager.cancel(NOTIFICATION_ID);
                    updateNotification = creatSucceedNotification(new File(pathFile));
                    notificationManager.notify(NOTIFICATION_ID, updateNotification);
                    updateDialog = new AlterDialog.Builder(UpdateService.this)
                            .setOKButton("安装", new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    notificationManager.cancel(NOTIFICATION_ID);
                                    Intent install = installApk(new File(pathFile));
                                    UpdateService.this.startActivity(install);
                                    updateDialog.dismiss();
                                }
                            }).setContentText("新版下载成功").show();
                    UpdateService.this.stopForeground(true);
                    break;
                // 下载失败
                case DOWN_FAILE_OK:
                    notificationManager.cancel(NOTIFICATION_ID);
                    updateNotification = creatFailNotification();
                    notificationManager.notify(NOTIFICATION_ID, updateNotification);
                    updateDialog = new AlterDialog.Builder(UpdateService.this)
                            .setOKButton("取消", new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    updateDialog.dismiss();
                                }
                            }).setContentText("新版下载失败").show();
                    deleteFile(new File(getApplicationContext()
                            .getExternalCacheDir().getAbsolutePath() + "//APK"));
                    UpdateService.this.stopForeground(true);
                    break;
                // 更新Notification
                case DOWN_UPDATE_UI:
                    int pd = (int) (((float) progressLoad / fileLength) * 100);
                    updateNotification.contentView.setProgressBar(progressId, 100,
                            pd, true);
                    updateNotification.contentView.setTextViewText(tvLoadTextId, pd
                            + "%");
                    updateNotification.contentView.setTextViewText(tvSpeedTextId,
                            speedDowan + "/KB");
                    notificationManager.notify(NOTIFICATION_ID, updateNotification);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     *   
     *
     * @param file
     * @return
     * @version 1.0 法方说明  安装包 
     * @date 2014-5-14 上午11:41:19 
     */
    public boolean deleteFile(File file) {
        if (file.exists() && file.isDirectory()) {

            for (File chlidFile : file.listFiles()) {
                deleteFile(chlidFile);
            }
        }
        if (file.exists()) {
            file.delete();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("tag", "初始化一个服务。。。。。");
        // 存储文件大小和大小分段
        sp = this.getSharedPreferences("pathLenght", Service.MODE_PRIVATE);
        // 初始化标题管理者
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // 初始化Dialog布局
        updatMessageLayoutId = R.layout.dialog_upd;
        // 初始化APK的存储路径
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            DIRECTORY_PATH = this.getExternalCacheDir().getAbsolutePath() + "/APK";
        }else{
            DIRECTORY_PATH = this.getCacheDir().getAbsolutePath() + "/APK";
        }
        pathFile = DIRECTORY_PATH;
        Log.i("tag", "文件的路路径为" + pathFile);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("tag", "关闭一个服务...");
    }

    private void downLoadAPKThread() {
        // 定义了所有记录文件块的数组
        downLoadFlag = true;
        startPosition = new long[threadCount];
        endPosition = new long[threadCount];
        for (int threadId = 1; threadId <= threadCount; threadId++) {
            File file = new File(getApplicationContext().getExternalCacheDir()
                    .getAbsolutePath()
                    + "/APK//"
                    + getPackageName()
                    + "_"
                    + threadId + ".log");
            fList.add(file);
            File indexfile = fList.get(threadId - 1);
            if (indexfile.exists() && indexfile.length() > 0) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(indexfile);
                    byte[] temp = new byte[1024];
                    int len = fis.read(temp);
                    startPosition[threadId - 1] = Integer.valueOf(new String(
                            temp, 0, len));
                } catch (IOException e) {

                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            } else {
                startPosition[threadId - 1] = (threadId - 1) * indexLength;
            }
            endPosition[threadId - 1] = (threadId * indexLength) - 1;
            if (threadId == threadCount) {
                // 最后一下线程下载剩余部分
                endPosition[threadId - 1] = fileLength;
            }
            System.out.println("线程" + threadId + ":开始下载的位置:"
                    + startPosition[threadId - 1] + "线程" + threadId
                    + ":结束下载的位置:" + endPosition[threadId - 1]);
            // 计算总的精度
            progressLoad += (endPosition[threadId - 1] - startPosition[threadId - 1]);
            if (threadId == threadCount) {
                progressLoad = fileLength - progressLoad;
            }
        }
        // 下载
        for (int threadId = 1; threadId <= threadCount; threadId++) {
            // 开启线程下载
            new LoaderThread(threadId, startPosition[threadId - 1],
                    endPosition[threadId - 1], pathFile, urlStr).start();
        }
    }

    /**
     * 负责下载的线程
     */
    public class LoaderThread extends Thread {
        private int threadId;
        private long startPosition;
        private long endPosition;
        private String pathFile;
        private String urlStr;

        public LoaderThread(int threadId, long startPosition, long endPosition,
                            String pathFile, String urlStr) {
            super();
            this.threadId = threadId;
            this.startPosition = startPosition;
            this.endPosition = endPosition;
            this.pathFile = pathFile;
            this.urlStr = urlStr;
        }

        @Override
        public void run() {
            super.run();
            URL url;
            try {
                url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                // 设置Range头对文件下载位置进行索引
                connection.setRequestProperty("Range", "bytes=" + startPosition
                        + "-" + endPosition);
                int returnCode = connection.getResponseCode();
                if (returnCode == 206) {
                    File indexfile = fList.get(threadId - 1);
                    InputStream is = connection.getInputStream();
                    RandomAccessFile loadFile = new RandomAccessFile(pathFile,
                            "rwd");
                    loadFile.seek(startPosition);
                    int len = 0;
                    byte[] bytesBuffer = new byte[1024];
                    // 记录下载了多少
                    int total = 0;
                    while ((len = is.read(bytesBuffer)) != -1) {
                        currentTime = System.currentTimeMillis();
                        if (currentTime - nextTime >= 2000) {
                            float indexTime = (float) (currentTime - nextTime) / 1000;
                            speedDowan = (int) (speedLoad / (indexTime) / 1024);
                            speedLoad = 0;
                            nextTime = currentTime;
                            sendToUIThread(DOWN_UPDATE_UI);
                        }
                        loadFile.write(bytesBuffer, 0, len);
                        total += len;
                        // 计算总的下载进度
                        speedLoad += len;
                        progressLoad += len;
                        // 下次下载的其实位置
                        long nextStartPosition = total + startPosition;
                        // 将下载的进度存入临时记录文件中
                        RandomAccessFile accessFileIndex = new RandomAccessFile(
                                indexfile, "rwd");
                        accessFileIndex.write((nextStartPosition + "")
                                .getBytes());
                        accessFileIndex.close();
                    }
                    is.close();
                    loadFile.close();
                }
            } catch (MalformedURLException e) {
                downLoadFlag = false;
                e.printStackTrace();
            } catch (IOException e) {
                downLoadFlag = false;
                e.printStackTrace();
            } finally {
                synchronized (UpdateService.class) {
                    System.out.println("线程" + threadId + ":所对应的块下载完成");
                    // 删除原来指定的标志位
                    count++;
                    if (count == threadCount) {
                        if (downLoadFlag) {
                            for (int i = 0; i < count; i++) {
                                // 删除标志位临时文件
                                File file = fList.get(i);
                                if (file.exists()) {
                                    file.delete();
                                }
                            }
                            System.out.println("下载完成");
                            sendToUIThread(DOWN_SUCCEED_OK);
                        } else {
                            System.out.println("下载失败");
                            sendToUIThread(DOWN_FAILE_OK);
                        }
                    }
                }
            }
        }
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.i("tag", "服务开始运行了。。。。。。。。。。。。。。。");
        // 判断下载好没安装
        if (isNoInstall()) {
            // 替换安装
            //判断版本号
            if (isThisNewApk()) {
                //被安装删除安装包
                File parentFile = new File(DIRECTORY_PATH);
                final PackageManager packageManager = this.getPackageManager();
                // 遍历此目录下所有apk文件
                parentFile.listFiles(new FileFilter() {
                                         @Override
                                         public boolean accept(File pathname) {
                                             if (pathname.getName().endsWith(".apk")) {
                                                 PackageInfo packageInfo = packageManager
                                                         .getPackageArchiveInfo(pathname.getPath(),
                                                                 PackageManager.GET_ACTIVITIES);
                                                 if (packageInfo == null) {
                                                     return false;
                                                 }
                                                 // 将相同包名的apk文件都删除
                                                 if (getApplicationContext().getPackageName().equals(
                                                         packageInfo.packageName)) {
                                                     pathname.delete();
                                                 }
                                             }
                                             return false;
                                         }
                                     }
                );
            } else {
                //提示安装
                updateDialog = new AlterDialog.Builder(UpdateService.this)
                        .setOKButton("确定", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 如果要替换安装 则该目录下只有一个文件
                                File file = new File(DIRECTORY_PATH);
                                File[] files = file.listFiles();
                                Intent install = installApk(files[0]);
                                UpdateService.this.startActivity(install);
                                updateDialog.dismiss();
                                //停止服务
                                //UpdateService.this.stopService(intent);
                            }
                        }).setNoButton("取消", new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                updateDialog.dismiss();
                            }
                        }).setContentText("新版本已经下载完成，可以安装。").show();
            }
        } else {
            File file = new File(DIRECTORY_PATH);
            if (file.exists() && file.listFiles().length > 1) {
                // 上次任务被意外暂停了
                Log.i("tag", "断点下载启动");
                count = threadCount - (file.listFiles().length - 1);
                fileLength = sp.getLong("fileLength", 0);
                indexLength = sp.getLong("indexLength", 0);
                urlStr = sp.getString("urlStr", null);
                pathFile = sp.getString("pathFile", pathFile);
                notificationManager.cancel(NOTIFICATION_ID);
                updateNotification = creatUpdateNotification();
                updateNotification.contentView.setProgressBar(progressId, 100,
                        0, true);
                updateNotification.contentView.setTextViewText(tvApknameId,
                        "xx");
                updateNotification.contentView.setTextViewText(tvLoadTextId,
                        "0%");
                updateNotification.contentView.setTextViewText(tvSpeedTextId,
                        "0/KB");
                notificationManager.notify(NOTIFICATION_ID, updateNotification);
                UpdateService.this.startForeground(NOTIFICATION_ID,
                        updateNotification);
                downLoadAPKThread();
            } else {
                count = 0;
                // 访问版本Xml
                // 初始化XML路径
                xmlUrlPath = intent.getStringExtra("XML_PATH");
                new PullXMLAsy(intent).execute();
            }
        }
        return START_NOT_STICKY;
    }

    private PendingIntent getPendingIntent(Intent intent) {
        // 创建Notification
        Intent notificationIntent = intent;
        // 如果当前Activity启动在前台，则不开启新的Activity。
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        return contentIntent;
    }

    /**
     *   
     *
     * @return
     * @version 1.0 法方说明   创建下载的Notification
     * @date 2014-5-9 下午4:03:25 
     */
    private Notification creatUpdateNotification() {
        contentIntent = getPendingIntent(new Intent(this, MainActivity.class));
        Notification notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
        notification.tickerText = "TickerText:您有新短消息，请注意查收！";
        notification.when = System.currentTimeMillis();
        notification.flags = Notification.FLAG_NO_CLEAR;// 不能够自动清除
        RemoteViews contentView = new RemoteViews(getPackageName(),
                R.layout.notification_update);
        notification.contentView = contentView;
        // Intent intent = new Intent(Intent.ACTION_MAIN);
        // PendingIntent contentIntent = PendingIntent.getActivity(this, 1,
        // intent, 1);
        notification.contentIntent = contentIntent;
        return notification;
    }

    /**
     *   
     *
     * @return
     * @version 1.0 法方说明   创建下载失败的Notification
     * @date 2014-5-9 下午4:03:25 
     */
    @SuppressWarnings("deprecation")
    private Notification creatFailNotification() {
        contentIntent = getPendingIntent(new Intent(this, MainActivity.class));
        Notification notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
        notification.tickerText = "下载已经中断";
        notification.when = System.currentTimeMillis();
        notification.setLatestEventInfo(this, "Notification Title", "下载失败",
                contentIntent);
        notification.number = 1;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        return notification;
    }

    /**
     *   
     *
     * @return
     * @version 1.0 法方说明   创建下载失败的成功
     * @date 2014-5-9 下午4:03:25 
     */
    @SuppressWarnings("deprecation")
    private Notification creatSucceedNotification(File file) {
        contentIntent = getPendingIntent(new Intent(this, MainActivity.class));
        Notification notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
        notification.tickerText = "下载已经完成";
        notification.when = System.currentTimeMillis();
        notification.setLatestEventInfo(this, "Notification Title", "下载成功",
                contentIntent);
        notification.number = 1;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        return notification;
    }

    /**
     *   
     *
     * @return
     * @version 1.0 法方说明   判断APK是否替换安装
     * @date 2014-5-9 下午4:35:04 
     */
    private boolean isNoInstall() {
        File file = new File(DIRECTORY_PATH);
        Log.i("tag", "文件的目录为" + file.getAbsolutePath());
        // 如果文件全部下载好则返回安装
        if (file.exists() && file.listFiles().length == 1) {
            return true;
        }
        return false;
    }

    /**
     * 获取已经下载的安装包是否被安装
     *
     * @return
     */
    private boolean isThisNewApk() {

        File file = new File(DIRECTORY_PATH);
        // 如果文件全部下载好则返回安装
        if (file.exists() && file.listFiles().length == 1) {
            File fileApk = file.listFiles()[0];
            float currentVersion = Float
                    .valueOf(getVersion(getApplicationContext()
                            .getPackageManager()));
            String fileName = fileApk.getName();
            fileName = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf("."));
            if (fileName.equals(String.valueOf(currentVersion))) {
                return true;
            }
        }
        return false;
    }

    /**
     *   
     *
     * @param
     * @return
     * @version 1.0 法方说明  判断SDcard的剩余空间是否足够 
     * @date 2014-5-9 下午4:47:14 
     */
    @SuppressWarnings("static-access")
    private boolean isAvailableSdcard() {
        CheckSDcard checkSDcard = new CheckSDcard();

        Log.i("tag",
                "下载目录为" + DIRECTORY_PATH + "大小为"
                        + checkSDcard.getAvailableStore());
        if (fileLength < checkSDcard.getAvailableStore()) {
            return true;
        } else {
            return false;
        }
    }

    private static class CheckSDcard {

        @SuppressWarnings("deprecation")
        public static long getAvailableStore() {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File sdcardDir = Environment.getExternalStorageDirectory();
                StatFs sf = new StatFs(sdcardDir.getPath());
                long blockSize = sf.getBlockSize();
                long blockCount = sf.getBlockCount();
                long availCount = sf.getAvailableBlocks();
                Log.d("", "block大小:" + blockSize + ",block数目:" + blockCount
                        + ",总大小:" + blockSize * blockCount / 1024 + "KB");
                Log.d("", "可用的block数目：:" + availCount + ",剩余空间:" + availCount
                        * blockSize / 1024 + "KB");
                return availCount * blockSize;
            } else {
                return 0;
            }

        }
    }

    /**
     *   
     *
     * @param 
     * @return
     * @throws java.io.IOException
     * @throws org.xmlpull.v1.XmlPullParserException
     * @version 1.0 法方说明   解析Xml 版本
     * @date 2014-5-13 下午1:40:40 
     */
    private AppVersion getAppVersion() throws IOException,
            XmlPullParserException {
        String url_update = xmlUrlPath;
        if (url_update != null) {
            URL url = new URL(url_update);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream is = conn.getInputStream();
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(is, "UTF-8");

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            av = new AppVersion();
                            break;
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("code")) {
                                av.setVersionCode(parser.nextText());
                            } else if (parser.getName().equals("name")) {
                                av.setVersionName(parser.nextText());
                            } else if (parser.getName().equals("context")) {
                                av.setVersionContext(parser.nextText());

                            } else if (parser.getName().equals("title")) {
                                av.setVersionTilte(parser.nextText());

                            } else if (parser.getName().equals("path")) {
                                av.setPath(parser.nextText());
                            }
                            break;
                    }
                    eventType = parser.next();
                }

            }
            return av;
        }
        return null;
    }

    /**
     *   
     *
     * @param pg
     * @return
     * @version 1.0 法方说明   获取当前版本号
     * @date 2014-5-13 下午1:59:42 
     */
    private String getVersion(PackageManager pg) {
        PackageInfo packageInfo;
        try {
            packageInfo = pg.getPackageInfo(getApplicationContext()
                    .getPackageName(), 0);
            String version = packageInfo.versionName;
            return version;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            Log.i("tag", "获取应用版本号错误");
            return "";
        }
    }

    /**
     * @version 1.0 法方说明  :替换安装
     * @date 2014-5-13 下午1:33:43 
     */
    private Intent installApk(File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * @author ewewewrt Email 278877385@qq.com 说明 :解析Xml的异步线程
     * @date 2014-5-13下午1:56:37
     */
    private class PullXMLAsy extends AsyncTask<Void, Void, AppVersion> {
        private Intent intent;

        public PullXMLAsy(Intent intent) {
            this.intent = intent;
        }

        @Override
        protected AppVersion doInBackground(Void... params) {
            AppVersion appVersion = null;
            try {
                appVersion = getAppVersion();
                InitApplication.appLog.i("解析XML===========1");
            } catch (IOException e) {
                Log.i("Tag", "IO流错误");
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                Log.i("Tag", "XML解析出错");
                e.printStackTrace();
            }
            return appVersion;
        }

        @Override
        protected void onPostExecute(final AppVersion result) {
            super.onPostExecute(result);
            if (result != null) {
                InitApplication.appLog.i("版本匹配===========2");
                // 匹配版本号
                float currentVersion = Float
                        .valueOf(getVersion(getApplicationContext()
                                .getPackageManager()));
                // 解析出的版本号
                float updateVersion = Float.valueOf(result.getVersionCode());
                if (updateVersion > currentVersion) {
                    InitApplication.appLog.i("开启下载===========3");
                    // 开启下载对话框
                    updateDialog = new AlterDialog.Builder(UpdateService.this)
                            .setOKButton("确定", new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // 获取下载地址
                                    urlStr = result.getPath();
                                    // 获取版本下载地址
                                    Editor ed = sp.edit();
                                    ed.putString("urlStr", urlStr);
                                    ed.commit();
                                    // 获取要下载的文件的大小
                                    FutureTask<Long> task = new FutureTask<Long>(
                                            new ThreadGetLenght(urlStr));
                                    new Thread(task).start();
                                    updateDialog.dismiss();
                                }
                            })
                            .setNoButton("取消", new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // 点击取消
                                    updateDialog.dismiss();

                                }
                            })
                            .setContentText(
                                    av.getVersionTilte() + "\n\r"
                                            + av.getVersionContext()).show();
                } else {
                    // 提示不需要下载 提示最新版本,检测版本更新时使用
                    if (intent.getIntExtra("isNew", 0) == 1) {
                        updateDialog = new AlterDialog.Builder(
                                UpdateService.this).setContentText("已经是最新版本")
                                .setOKButton("确定", new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        updateDialog.dismiss();
                                        UpdateService.this.stopService(intent);
                                    }
                                }).show();
                    }
                    return;
                }
            } else {
                if (intent.getIntExtra("isNew", 0) == 1) {
                    Toast.makeText(getApplicationContext(), "已经是最新版本",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class AppVersion {
        private String versionCode;
        private String versionName;
        private String versionContext;
        private String versionTilte;
        private String path;

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getVersionContext() {
            return versionContext;
        }

        public void setVersionContext(String versionContext) {
            this.versionContext = versionContext;
        }

        public String getVersionTilte() {
            return versionTilte;
        }

        public void setVersionTilte(String versionTilte) {
            this.versionTilte = versionTilte;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        @SuppressWarnings("unused")
        public AppVersion(String versionCode, String versionName,
                          String versionContext, String versionTilte, String path) {
            super();
            this.versionCode = versionCode;
            this.versionName = versionName;
            this.versionContext = versionContext;
            this.versionTilte = versionTilte;
            this.path = path;
        }

        public AppVersion() {
            super();
        }

    }

    private class ThreadGetLenght implements Callable<Long> {
        private String urlStr;

        public ThreadGetLenght(String urlStr) {
            super();
            this.urlStr = urlStr;
        }

        @Override
        public Long call() throws Exception {

            return getFileLenght(urlStr);
        }

    }

    private long getFileLenght(String urlStr) {
        try {
            Log.i("tag", "获取下载文件的长度....." + urlStr);
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            int returnCode = connection.getResponseCode();
            Log.i("tag", "发送消息下载文件。。。" + returnCode);
            if (returnCode == 200) {
                // 线获取文件的长度
                Log.i("tag", "计算文件长度");
                fileLength = connection.getContentLength();
                // 判断手机用户的剩余空间
                if (!isAvailableSdcard()) {
                    // 剩余空间不足
                    Log.i("tag", "手机剩余空间不足");
                    sendToUIThread(SPACE_LENGHT_NO);
                } else {
                    indexLength = (long) (fileLength / threadCount);
                    // 存入Sp中
                    Editor ed = sp.edit();
                    ed.putLong("fileLength", fileLength);
                    ed.putLong("indexLength", indexLength);
                    // 先创建一个临时文件
                    File file = new File(pathFile);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    pathFile += "//" + av.getVersionName();
                    ed.putString("pathFile", pathFile);
                    ed.commit();
                    RandomAccessFile accessFile = new RandomAccessFile(
                            pathFile, "rwd");
                    // 指定临时文件的长度
                    accessFile.setLength(fileLength);
                    accessFile.close();
                    Log.i("tag", "发送消息下载文件。。。2");
                    sendToUIThread(SPACE_LENGHT_AS);
                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return fileLength;
    }

    private void sendToUIThread(int what) {
        // 跟UI线程交互
        Message msg = myHandler.obtainMessage();
        msg.what = what;
        myHandler.sendMessage(msg);
    }

    /**
     * @author ewewewrt Email 278877385@qq.com 说明 自定义的Dialog
     * @date 2014-5-13下午4:58:46
     */
    private static class AlterDialog extends Dialog {
        private static TextView tv_content;
        private static Button btn_ok;
        private static Button btn_cancel;
        private static View tv_line;
        private static Window dialogWindow;
        private View mDialogView;
        private AnimationSet mModalInAnim;
        private AnimationSet mModalOutAnim;

        @SuppressWarnings("unused")
        public AlterDialog(Context context) {
            this(context, R.style.alert_dialog, updatMessageLayoutId);
        }

        public AlterDialog(Context context, int theme, int layoutId) {
            super(context, theme);
            dialogWindow = this.getWindow();
            mDialogView = dialogWindow.getDecorView().findViewById(
                    android.R.id.content);
            setContentView(layoutId);
            initView();
            mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(
                    getContext(), R.anim.modal_in);
            mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(
                    getContext(), R.anim.modal_out);
            mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mDialogView.setVisibility(View.GONE);
                    mDialogView.post(new Runnable() {
                        @Override
                        public void run() {
                            AlterDialog.super.dismiss();
                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation arg0) {
                    // TODO Auto-generated method stub

                }
            });
        }

        @Override
        protected void onStart() {
            super.onStart();
            mDialogView.startAnimation(mModalInAnim);
        }

        public void dismiss() {
            mDialogView.startAnimation(mModalOutAnim);
        }

        private void initView() {
            tv_content = (TextView) findViewById(R.id.tv_dialog_content);
            btn_cancel = (Button) findViewById(R.id.btn_dialog_cancel);
            btn_ok = (Button) findViewById(R.id.btn_dialog_ok);
            tv_line = findViewById(R.id.tv_line);
        }

        public static class Builder {
            private AlterDialog alterDialog;

            public Builder(Context context) {
                alterDialog = new AlterDialog(context, R.style.alert_dialog,
                        updatMessageLayoutId);
            }

            public Builder setOKButton(String messgae,
                                       View.OnClickListener clickListener) {
                if (btn_ok.getVisibility() == View.GONE) {
                    btn_ok.setVisibility(View.VISIBLE);
                }
                btn_ok.setText(messgae);
                btn_ok.setOnClickListener(clickListener);
                return this;
            }

            public Builder setNoButton(String messgae,
                                       View.OnClickListener clickListener) {
                if (btn_cancel.getVisibility() == View.GONE) {
                    btn_cancel.setVisibility(View.VISIBLE);
                    tv_line.setVisibility(View.VISIBLE);
                }
                btn_cancel.setText(messgae);
                btn_cancel.setOnClickListener(clickListener);
                return this;
            }

            public Builder setContentText(String messgae) {
                if (!TextUtils.isEmpty(messgae)) {
                    tv_content.setText(messgae);
                }
                return this;
            }

            public AlterDialog show() {
                if (alterDialog != null) {
                    alterDialog.getWindow().setType(
                            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    alterDialog.setCanceledOnTouchOutside(false);
                    alterDialog.show();
                }
                return alterDialog;
            }

            @SuppressWarnings("unused")
            public AlterDialog getAlterDialog() {
                return alterDialog;
            }

        }
    }

}
