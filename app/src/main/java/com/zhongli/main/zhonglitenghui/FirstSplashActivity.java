package com.zhongli.main.zhonglitenghui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongli.main.zhonglitenghui.app.InitApplication;
import com.zhongli.main.zhonglitenghui.custom.DepthPageTransformer;

/**
 * Created by KIM on 2015/4/22 0022.
 */
public class FirstSplashActivity extends BaseActivity {
    /**
     * 偏好设置，用于保存是否是第一次开启程序
     */
    private SharedPreferences sp;

    /**
     * viewPager
     */
    private ViewPager viewPager;
    private ViewpagerAdpter viewpagerAdpter;

    /**
     * 开机动画数组
     */
    private int[] imgs = {R.drawable.first_show,R.drawable.second_show,R.drawable.third_show};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_splash);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);

        sp = this.getSharedPreferences("user.xml",MODE_PRIVATE);

        if(sp.getBoolean("isFirst",true)){//第一次开启程序
            setContentView(R.layout.activity_first_splash);
            initView();
            initAdapter();
        }else{//直接跳到主页面
            setContentView(R.layout.activity_first);
            skipToActivityFinish(this);
        }

    }



    /**
     * 跳转到FirstActivity
     * @param context
     */
    private void skipToActivityFinish(Context context) {
        Intent intent = new Intent(context,FirstActivity.class);
        startActivity(intent);
        ((Activity)context).finish();
    }

    @Override
    protected void initView() {
        viewPager = (ViewPager)findViewById(R.id.vp_splash);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initAdapter() {
            viewpagerAdpter = new ViewpagerAdpter(this);
            //viewpagerAdpter = new FragmentViewpagerAdpter(getSupportFragmentManager());
            viewPager.setAdapter(viewpagerAdpter);
            viewPager.setPageTransformer(true,new DepthPageTransformer());


    }


    @Override
    protected void initListenerData() {

    }

    /**
     * Adapter
     */
    class ViewpagerAdpter extends PagerAdapter {
        private Context context;
        private LinearLayout.LayoutParams  params;

        public ViewpagerAdpter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return imgs.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView  = new ImageView(context);
            //new ObjectAnimator().ofFloat(imageView,"alpha",0.3f,1.0f).setDuration(2000).start();
            //imageView.setImageResource(imgs[position]);
            params =new LinearLayout.LayoutParams(InitApplication.screenWidth,InitApplication.screenWidth*1280/720);
            imageView.setLayoutParams(params);
            imageView.setImageDrawable(setResId(imgs[position]));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            /**
             * 最后一幅画点击进入主页
             */
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == imgs.length - 1) {
                        skipToActivityFinish(context);
                        SharedPreferences sp = context.getSharedPreferences("user.xml", MODE_PRIVATE);
                        sp.edit().putBoolean("isFirst", false).commit();
                    }
                }
            });



            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

    }

    /**
     * 一副图片一个Fragment
     */
    /*class FragmentViewpagerAdpter extends FragmentStatePagerAdapter{
        FragmentViewpagerAdpter(FragmentManager  fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return new SplashFragment(position,imgs);
        }

        @Override
        public int getCount() {
            return imgs.length;
        }
    }*/








}
