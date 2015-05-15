package com.zhongli.main.zhonglitenghui.app;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.zhongli.main.zhonglitenghui.bean.AboutUsInfo;
import com.zhongli.main.zhonglitenghui.until.Log4j;
import com.zhongli.main.zhonglitenghui.volley.VolleyManager;

import java.util.List;

public class InitApplication extends Application {
    public static int screenWidth;
    public static int screenHeight;
    public static String filePath;
    public static String cookieStr;
    public static Log4j appLog;
    public static int mHeaderHeight ;
    public static int position;
    public static int downCount = 0;
    public static List<AboutUsInfo> aboutUsList;
    public static String protext;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        appLog = Log4j.Log();
        // 开启Log调试信息
        Log4j.flag = true;
        // 计算屏幕的像素
        figureScreen(getApplicationContext());
        // 获取程序的缓存路径
        filePath = getApplicationContext().getCacheDir().getPath();
        // 初始化访问类
        VolleyManager.init(getApplicationContext());
        //mHeaderHeight = getApplicationContext().getResources().getDimensionPixelSize(R.dimen.move_top);

    }

    public static void figureScreen(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }
}
