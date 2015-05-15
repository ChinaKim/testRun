package com.zhongli.main.zhonglitenghui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zhongli.main.zhonglitenghui.adapter.CerHorAdapter;
import com.zhongli.main.zhonglitenghui.adapter.CerViewPage;
import com.zhongli.main.zhonglitenghui.app.InitApplication;
import com.zhongli.main.zhonglitenghui.bean.Certificate;
import com.zhongli.main.zhonglitenghui.bean.Img;
import com.zhongli.main.zhonglitenghui.custom.ExtendedViewPager;
import com.zhongli.main.zhonglitenghui.custom.ZoomTutorial;
import com.zhongli.main.zhonglitenghui.volley.VolleyManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 278877385 on 2015/3/19.
 */
public class CerActivity extends BaseActivity {
    /**
     * actionbar
     */
    private Toolbar toolbar;

    /**
     * 标题控件TextView
     */
    private TextView tv_cer_product;
    private TextView tv_cer_system;

    /**
     * 显示证书控件RecyclerView
     */
    private RecyclerView rl_cer_product;
    private RecyclerView rl_cer_system;
    private List<Certificate> cerList;

    /**
     * 标题
     */
    private String proName;
    private String sysName;

    /**
     * 证书集合
     */
    private List<Img> pImgList;
    private List<Img> sImgList;

    /**
     * 证书宽高
     */
    private int height;
    private int width;
    private CerHorAdapter pCerHorAdapter;
    private CerHorAdapter sCerHorAdapter;
    private LinearLayoutManager plinearLayoutManager;
    private LinearLayoutManager slinearLayoutManager;
    private ExtendedViewPager expandedView;
    private boolean isPro;
    private boolean isSys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);
        //getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        initView();
        initListener();
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        //设置title
        if (toolbar != null) {
            toolbar.setTitle("认证证书");
            setSupportActionBar(toolbar);
            //添加返回按钮
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //改变状态栏的颜色
        //BaseActivity.setStatusBarTint(this, getRes().getColor(R.color.title_color));
        tv_cer_product = (TextView) findViewById(R.id.tv_cer_product);
        tv_cer_system = (TextView) findViewById(R.id.tv_cer_system);
        rl_cer_product = (RecyclerView) findViewById(R.id.rl_cer_product);
        rl_cer_system = (RecyclerView) findViewById(R.id.rl_cer_system);
        expandedView = (ExtendedViewPager) this.findViewById(R.id.vp_cer_img);
        //设置布局管理器
        plinearLayoutManager = new LinearLayoutManager(this);
        plinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        slinearLayoutManager = new LinearLayoutManager(this);
        slinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rl_cer_product.setLayoutManager(plinearLayoutManager);
        rl_cer_system.setLayoutManager(slinearLayoutManager);
        ViewGroup.LayoutParams lp = rl_cer_product.getLayoutParams();
        lp.height = sizeHeight();
        rl_cer_product.setLayoutParams(lp);
        rl_cer_system.setLayoutParams(lp);
        sizeViewWidth();
        //加载数据
        loadView();
    }

    @Override
    protected void initListener() {

    }


    @Override
    protected void initAdapter() {
        pCerHorAdapter = new CerHorAdapter(this, pImgList, width, height);
        sCerHorAdapter = new CerHorAdapter(this, sImgList, width, height);
        rl_cer_product.setAdapter(pCerHorAdapter);
        rl_cer_system.setAdapter(sCerHorAdapter);
    }

    @Override
    protected void initListenerData() {
        pCerHorAdapter.setOnItemClickListener(new CerHorAdapter.OnItemClickListener() {
            @Override
            public void itemClickListener(View view, final int position) {
                //点击事件展开大图
                //滑动到最顶端
                isPro = true;
                isSys = false;
                setViewPagerAndZoom(view, position, pImgList, rl_cer_product);
                rl_cer_product.smoothScrollToPosition(position);
            }
        });
        sCerHorAdapter.setOnItemClickListener(new CerHorAdapter.OnItemClickListener() {
            @Override
            public void itemClickListener(View view, int position) {
                //点击事件展开大图
                //滑动到最顶端
                isPro = false;
                isSys = true;
                setViewPagerAndZoom(view, position, sImgList, rl_cer_system);
                rl_cer_system.smoothScrollToPosition(position);
            }
        });
        expandedView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("777", "expandedView");
                if (isPro) {
                    //滑动产品认证
                    rl_cer_product.smoothScrollToPosition(position % pImgList.size());
                } else if (isSys) {
                    //滑动体系认证
                    rl_cer_system.smoothScrollToPosition(position % sImgList.size());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 加载网络数据
     */
    protected void loadView() {
        showLoadView();
        String url = getRes().getString(R.string.url_root) + getRes().getString(R.string.url_cer_list);
        VolleyManager.getJson(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                //获取的JSON不为空的话
                if (!TextUtils.isEmpty(jsonObject.toString())) {
                    Map<String, String> strMap = JSON.parseObject(jsonObject.toString(),
                            new TypeReference<LinkedHashMap<String, String>>() {
                            });
                    cerList = JSON.parseArray(strMap.get("list"), Certificate.class);
                    //分类
                    pImgList = new ArrayList<Img>();
                    sImgList = new ArrayList<Img>();
                    //服务器怎么传的封装的JSON格式简直浪费时间。。。幸亏数据少！！！
                    for (Certificate cer : cerList) {
                        //产品认证
                        if (cer.getType() == 1) {
                            for (Img img : cer.getImgs())
                                pImgList.add(img);
                        } else if (cer.getType() == 2) {
                            for (Img img : cer.getImgs())
                                sImgList.add(img);
                        }
                    }
                    //加载线性布局
                    initAdapter();
                    initListenerData();
                    proName = "产品认证";
                    sysName = "体系认证";
                    tv_cer_product.setText(proName);
                    tv_cer_system.setText(sysName);
                }
                //关闭进度条
                dissMissLoadView();
                //完成刷新
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dissMissLoadView();
                Toast.makeText(CerActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取ActionBar 高度
     *
     * @param context
     * @return
     */
    protected int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data,
                    getRes().getDisplayMetrics());
        }
        return 0;
    }

    protected int sizeHeight() {
        int height = (InitApplication.screenHeight - 4 * tv_cer_product.getLineHeight() - getActionBarHeight(this) - getRes().getDimensionPixelSize(R.dimen.status_title)) / 2;
        return height;
    }

    /**
     * 计算图片的宽高
     */
    protected void sizeViewWidth() {
        //height = getRes().getDimensionPixelOffset(R.dimen.rl_ver);
        height = sizeHeight();
        width = (int) ((float) height / 257 * 182);
    }

    /**
     * 展开大图
     *
     * @param v
     * @param position
     */
    public void setViewPagerAndZoom(final View v, final int position, List<Img> imgList, final RecyclerView recyclerView) {
        //得到要放大展示的视图界面
        //最外层的容器，用来计算
        View containerView = this.findViewById(R.id.fra_cer_view);
        //实现放大缩小类，传入当前的容器和要放大展示的对象
        ZoomTutorial mZoomTutorial = new ZoomTutorial(containerView, expandedView);
        CerViewPage adapter = new CerViewPage(this,
                imgList, mZoomTutorial, recyclerView);
        expandedView.setAdapter(adapter);
        expandedView.setCurrentItem(position % imgList.size());
        // 通过传入Id来从小图片扩展到大图，开始执行动画
        mZoomTutorial.zoomImageFromThumb(v);
        mZoomTutorial.setOnZoomListener(new ZoomTutorial.OnZoomListener() {

            @Override
            public void onThumbed() {
                // TODO 自动生成的方法存根
                System.out.println("现在是-------------------> 小图状态");
            }

            @Override
            public void onExpanded() {
                // TODO 自动生成的方法存根
                System.out.println("现在是-------------------> 大图状态");

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
