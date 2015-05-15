package com.zhongli.main.zhonglitenghui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongli.main.zhonglitenghui.adapter.CaseViewVerAdapter;
import com.zhongli.main.zhonglitenghui.adapter.LCaseViewAdapter;
import com.zhongli.main.zhonglitenghui.app.InitApplication;
import com.zhongli.main.zhonglitenghui.bean.Case;
import com.zhongli.main.zhonglitenghui.custom.ExtendedVerViewPager;
import com.zhongli.main.zhonglitenghui.custom.VerticalViewPager;
import com.zhongli.main.zhonglitenghui.until.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 278877385 on 2015/3/16.
 */
public class CaseViewActivity extends BaseActivity {
    private ExtendedVerViewPager vp_ver_view;
    private ListView lv_ver_view;
    private ImageView img_caseview_click;
    private LinearLayout lin_caseview_content;
    private TextView tv_caseview_title;
    private TextView tv_caseview_description;
    private List<View> hList = new ArrayList<View>();
    private List<Case> cList;
    /**
     * 总图片的尺寸
     */
    private int size;

    /**
     * 上一个图片的序号
     */
    private int verOldPosition;

    /**
     * 当前的图片的序号
     */
    private int verCurrent;
    private Bundle bundle;
    /**
     * 适配器
     */
    private LCaseViewAdapter lCaseViewAdapter;
    private CaseViewVerAdapter caseViewVerAdapter;

    /**
     * 关闭打开描述的判断
     */
    private boolean showFlag = true;

    /**
     * 类型（0：案例；1：产品）
     */
    private int type;
    private boolean showList;
    private ObjectAnimator moveInTxt;
    private ObjectAnimator moveOutTxt;
    private ObjectAnimator moveInList;
    private ObjectAnimator moveOutList;
    private Handler handler;

    private ImageButton click_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caseview);
        initView();
        initListener();
        initAdapter();
    }

    @Override
    protected void initView() {
        vp_ver_view = (ExtendedVerViewPager) findViewById(R.id.vp_ver_view);
        lv_ver_view = (ListView) findViewById(R.id.lv_ver_view);
        img_caseview_click = (ImageView) findViewById(R.id.img_caseview_click);
        lin_caseview_content = (LinearLayout) findViewById(R.id.lin_caseview_content);
        tv_caseview_title = (TextView) findViewById(R.id.tv_caseview_title);
        tv_caseview_description = (TextView) findViewById(R.id.tv_caseview_description);
        click_iv = (ImageButton) findViewById(R.id.click_iv);
        //从上一个Activity获取数据
        bundle = getIntent().getExtras();
        type = bundle.getInt("TYPE");
        //案例
        if(type == 0){
            img_caseview_click.setVisibility(View.VISIBLE);
            lin_caseview_content.setVisibility(View.VISIBLE);
        }
        //产品
        if(type == 1){
            int color = Color.argb(127, 96, 96, 96);
            lv_ver_view.setBackgroundColor(color);
        }
        cList = (List<Case>) bundle.getSerializable(Constant.CASE_LIST);
        size = cList.size();
        LayoutInflater inflater = this.getLayoutInflater();
        for (int i = 0; i < size; i++) {
            hList.add(inflater.inflate(R.layout.view_case, null, false));
        }
        //初始化动画
        float moveList = getRes().getDimensionPixelSize(R.dimen.list_ver);
        moveInTxt = new ObjectAnimator().ofFloat(lin_caseview_content, "scaleX", 0.0f, 1.0f).setDuration(500);
        moveOutTxt = new ObjectAnimator().ofFloat(lin_caseview_content, "scaleX", 1.0f, 0.0f).setDuration(500);
        moveInList = new ObjectAnimator().ofFloat(lv_ver_view, "x", InitApplication.screenHeight, InitApplication.screenHeight - moveList).setDuration(500);
        moveOutList = new ObjectAnimator().ofFloat(lv_ver_view, "x", InitApplication.screenHeight - moveList, InitApplication.screenHeight).setDuration(500);
    }

    @Override
    protected void initListener() {
        ViewTreeObserver vtoOutdoor = lin_caseview_content.getViewTreeObserver();
        vtoOutdoor.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                int height = lin_caseview_content.getMeasuredHeight();
                lin_caseview_content.setPivotX(0f);
                lin_caseview_content.setPivotY(height);

            }
        });
        lv_ver_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != verCurrent % size) {
                    //计算相对位置
                    int selectP = verCurrent + (position - verCurrent % size);
                    vp_ver_view.setCurrentItem(selectP);
                }
            }
        });
        vp_ver_view.setOnPageChangeListener(new VerticalViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                verCurrent = position;
                //页面滑动时改变标题
                setTilteDes(verCurrent % size);
                //游标选择
                cList.get(verCurrent % size).setSelFag(true);
                if (verOldPosition % size != verCurrent % size) {
                    cList.get(verOldPosition % size).setSelFag(false);
                }
                lCaseViewAdapter.notifyDataSetChanged();
                lv_ver_view.smoothScrollToPositionFromTop(verCurrent % size, 0);
                //lv_ver_view.setSelection(verCurrent % size);
                verOldPosition = verCurrent;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        img_caseview_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (!showList) {
                    moveInList();
                } else {
                    moveOutList();
                }
            }
        };

        click_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaseViewActivity.this.finish();
            }
        });
    }

    @Override
    protected void initAdapter() {
        //获取类型
        int type = bundle.getInt("TYPE");
        //加载导航页面
        lCaseViewAdapter = new LCaseViewAdapter(this, cList);
        caseViewVerAdapter = new CaseViewVerAdapter(this, hList, cList, handler, type);
        lv_ver_view.setAdapter(lCaseViewAdapter);
        //加载适配器
        vp_ver_view.setAdapter(caseViewVerAdapter);
        //开始选择的位置
        verCurrent = bundle.getInt("POSITION");
        vp_ver_view.setCurrentItem(verCurrent);
        //复选文件框
        cList.get(verCurrent % size).setSelFag(true);
        lCaseViewAdapter.notifyDataSetChanged();
        lv_ver_view.smoothScrollToPositionFromTop(verCurrent % size, 0);
        verOldPosition = verCurrent;
        setTilteDes(verCurrent % size);
    }

    @Override
    protected void initListenerData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 添加文字和标题
     */
    protected void setTilteDes(int position) {
        tv_caseview_title.setText(cList.get(position).getName());
        tv_caseview_description.setText(cList.get(position).getDescription());
    }

    protected void moveInTxt() {
        moveInTxt.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                lin_caseview_content.setVisibility(View.VISIBLE);
                showFlag = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        moveInTxt.start();
    }

    protected void moveOutTxt() {
        moveOutTxt.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                lin_caseview_content.setVisibility(View.GONE);
                showFlag = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        moveOutTxt.start();
    }

    protected void moveInList() {

        moveInList.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                click_iv.setVisibility(View.GONE);
                lv_ver_view.setVisibility(View.VISIBLE);
                showList = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        moveInList.start();
    }

    protected void moveOutList() {
        moveOutList.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                click_iv.setVisibility(View.VISIBLE);
                lv_ver_view.setVisibility(View.GONE);
                showList = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        moveOutList.start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //关闭Activity
        this.finish();
    }
}
