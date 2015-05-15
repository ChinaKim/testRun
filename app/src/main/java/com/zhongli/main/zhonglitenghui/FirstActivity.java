package com.zhongli.main.zhonglitenghui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongli.main.zhonglitenghui.service.UpdateService;

import java.util.List;

/**
 * Created by 278877385 on 2015/3/14.
 */
public class FirstActivity extends BaseActivity {

    /**
     * 首页4个按钮的父容器
     */
    private LinearLayout view_01;
    private LinearLayout view_02;
    private LinearLayout view_03;
    private LinearLayout view_04;
    /**
     * 关于
     */
    private ImageView img_first_about;
    /**
     * 项目案例
     */
    private ImageView img_first_case;
    /**
     * 产品
     */
    private ImageView img_first_pud;
    /**
     * 视频
     */
    private ImageView img_first_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        initView();
        initListener();
    }

    @Override
    protected void initView() {
        view_01 = (LinearLayout) findViewById(R.id.view_01);
        view_02 = (LinearLayout) findViewById(R.id.view_02);
        view_03 = (LinearLayout) findViewById(R.id.view_03);
        view_04 = (LinearLayout) findViewById(R.id.view_04);
        img_first_about = (ImageView) view_01.findViewById(R.id.img_first_view);
        img_first_case = (ImageView) view_02.findViewById(R.id.img_first_view);
        img_first_pud = (ImageView) view_03.findViewById(R.id.img_first_view);
        img_first_video = (ImageView) view_04.findViewById(R.id.img_first_view);
        img_first_case.setImageResource(R.drawable.project);
        img_first_pud.setImageResource(R.drawable.product);
        img_first_video.setImageResource(R.drawable.video);
        //开启自动更新
        updateService();
    }

    @Override
    protected void initListener() {
        final Intent intent = new Intent(this, MainActivity.class);
        final Bundle bundle = new Bundle();
        img_first_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到关于我们
                bundle.putInt("CHECKEDID", R.id.rd_left_about);
                intent.putExtras(bundle);
                FirstActivity.this.startActivity(intent);
            }
        });
        img_first_case.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到项目案例
                bundle.putInt("CHECKEDID", R.id.rd_left_case);
                intent.putExtras(bundle);
                FirstActivity.this.startActivity(intent);
            }
        });
        img_first_pud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到产品
                bundle.putInt("CHECKEDID", R.id.rd_left_pdu);
                intent.putExtras(bundle);
                FirstActivity.this.startActivity(intent);
            }
        });
        img_first_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到视频
                bundle.putInt("CHECKEDID", R.id.rd_left_video);
                intent.putExtras(bundle);
                FirstActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void initAdapter() {

    }

    @Override
    protected void initListenerData() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    //自动更新服务
    protected void updateService() {
        if (!isServiceRunning(this, UpdateService.class.getName())) {
            Intent intent = new Intent(this, UpdateService.class);
            intent.putExtra("XML_PATH", "http://192.168.1.133:8080/"
                    + "version.xml");
            startService(intent);
        }
    }

    /**
     * 判断自动更新服务是否在运行
     *
     * @param mContext
     * @param className
     * @return
     */
    protected boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
