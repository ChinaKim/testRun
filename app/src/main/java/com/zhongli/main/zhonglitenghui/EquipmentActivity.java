package com.zhongli.main.zhonglitenghui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zhongli.main.zhonglitenghui.adapter.AdvertisementAdapter;
import com.zhongli.main.zhonglitenghui.bean.EquipmentInfo;
import com.zhongli.main.zhonglitenghui.until.Tools;
import com.zhongli.main.zhonglitenghui.volley.VolleyManager;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KIM on 2015/3/20 0020.
 */
public class EquipmentActivity  extends BaseActivity implements ViewPager.OnPageChangeListener{

    private List<EquipmentInfo> tlist;

    /**
     * 描述点击按钮
     */
    private ImageButton ib_click;

    /**
     *  显示描述控件
     */
    private TextView tv_describe;

    private boolean showFlag = true;
    /**
     * 显示、隐藏文本动画
     */
    private ObjectAnimator moveInText;
    private ObjectAnimator moveOutText;

    private LinearLayout ll;
    private LayoutInflater inflater;
    private ViewPager vp;
    private AdvertisementAdapter vpAdapter;
    /**
     * 底部小点图片
     */
    private ImageView[] dots;

    /**
     * 记录当前选中位置
     */
    private int currentIndex;

    /**
     * 关闭按钮
     */
    private ImageButton click_iv;

    private  Bundle bundle;

    private int position;

    private LinearLayout.LayoutParams params;

    //自动滑动handler
    /*private Handler runHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x01:
                    int currentPage = (Integer) msg.obj;
                    setCurrentDot(currentPage);
                    vpAdvertise.setCurrentItem(currentPage);
                    break;
            }
        }
    };*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_equipment);
        initView();
        initListener();
        //loadType();
        initAdapter();
    }





    @Override
    protected void initView(){
       bundle = getIntent().getExtras();
        Log.i("bund",bundle +"");
       tlist = EquipmentGraidActivity.getList();
       position = bundle.getInt("EQPOSITION");
       vp = (ViewPager)findViewById(R.id.vpAdvertise);
       ll = (LinearLayout)findViewById(R.id.ll);
       inflater = LayoutInflater.from(this);
       ib_click = (ImageButton)findViewById(R.id.ib_click);
       tv_describe = (TextView)findViewById(R.id.tv_describe);
       //初始化动画
       moveInText = new ObjectAnimator().ofFloat(tv_describe,"scaleX",0.0f,1.0f).setDuration(500);
       moveOutText = new ObjectAnimator().ofFloat(tv_describe,"scaleX",1.0f,0.0f).setDuration(500);

       click_iv = (ImageButton)findViewById(R.id.click_iv);
    }

    @Override
    protected void initListener() {
        //自动滑动
        /*timer = new Timer();
        task = new TimerTask(){
            @Override
            public void run() {
                int currentPage = count%advertiseArray.length();
                count++;
                Message msg = Message.obtain();
                msg.what = 0x01;
                msg.obj = currentPage;
                runHandler.sendMessage(msg);
            }
        };
        timer.schedule(task, 0, timeDratioin);*/

        ViewTreeObserver vtoOutdoor = tv_describe.getViewTreeObserver();
        vtoOutdoor.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                int height = tv_describe.getMeasuredHeight();
                tv_describe.setPivotX(0f);
                tv_describe.setPivotY(height);
            }
        });

       click_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EquipmentActivity.this.finish();
            }
        });


       ib_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ib_click.setVisibility(ImageView.VISIBLE);
                //点击展示描述
                if (!showFlag) {
                    //加载打开动画
                    moveInTxt();
                } else {
                    //加载关闭动画
                    moveOutTxt();
                }
            }
        });
    }
    @Override
    protected void initAdapter(){
        for(EquipmentInfo i : tlist){
            //网络加载
            /*TouchImageView iv = new TouchImageView(EquipmentActivity.this);
            iv.setImageResource(R.drawable.img_def_url);
            iv.setImageUrl(urlRoot+tlist.get(i).getImg(),VolleyManager.getImageLoader());
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setLayoutParams(mParams);
            views.add(iv);*/
            //添加底部白点

            ll.addView(inflater.inflate(R.layout.advertisement_board_dot, null));
        }

        tv_describe.setText(tlist.get(position).getDescription());


        //初始化底部白点
        initDots(ll);
        vpAdapter = new AdvertisementAdapter(this,tlist);
        vp.setAdapter(vpAdapter);
        vp.setCurrentItem(position);
        vp.setOnPageChangeListener(this);
    }


    @Override
    protected void initListenerData() {

    }

    protected void loadType() {
        String url = getRes().getString(R.string.url_root) + getRes().getString(R.string.url_equipment);
        VolleyManager.getNoCacheJson(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                //获取的JSON不为空的话
                if (!TextUtils.isEmpty(jsonObject.toString())) {
                    Map<String, String> strMap = JSON.parseObject(jsonObject.toString(),
                            new TypeReference<LinkedHashMap<String, String>>() {
                            });
                    tlist = JSON.parseArray(strMap.get("list"), EquipmentInfo.class);
                    if(tlist != null){
                        //加载具体的内容
                        initAdapter();
                    }
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Tools.showToast(getApplication(),"网络错误", Toast.LENGTH_LONG);
            }
        });
    }


    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
          setCurrentDot(i);
          tv_describe.setText(tlist.get(currentIndex).getDescription());
    }


    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private void initDots(LinearLayout ll) {
        dots = new ImageView[tlist.size()];
        // 循环取得小点图片
        for (int i = 0; i < tlist.size(); i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);// 都设为灰色
        }
        currentIndex = position;
        dots[currentIndex].setEnabled(false);
    }

    private void setCurrentDot(int position) {

        if(position < 0 || position >tlist.size() -1 || currentIndex == position){
            return;
        }
        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = position;
    }


    private void moveInTxt(){
        moveInText.addListener(new Animator.AnimatorListener(){
            @Override
            public void onAnimationStart(Animator animation) {
                ViewTreeObserver vtoOutdoor = tv_describe.getViewTreeObserver();
                vtoOutdoor.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        int height = tv_describe.getMeasuredHeight();
                        tv_describe.setPivotX(0f);
                        tv_describe.setPivotY(height);
                    }
                });
                //tv_describe.setVisibility(View.VISIBLE);
                if(tlist != null){
                    tv_describe.setText(tlist.get(currentIndex).getDescription());
                }

                showFlag = true;
            }
            @Override
            public void onAnimationEnd(Animator animation) {}

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        moveInText.start();
    }

    private void moveOutTxt(){
        moveOutText.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //tv_describe.setVisibility(View.GONE);
                ViewTreeObserver observer = tv_describe.getViewTreeObserver();
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        tv_describe.setPivotX(1f);
                        int height = tv_describe.getMeasuredHeight();
                        tv_describe.setPivotY(height);
                    }
                });
                showFlag = false;
            }
            @Override
            public void onAnimationEnd(Animator animation) {}
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        moveOutText.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Tools.closeToast();
        this.finish();
    }
}
