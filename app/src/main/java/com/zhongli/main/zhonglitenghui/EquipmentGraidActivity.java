package com.zhongli.main.zhonglitenghui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zhongli.main.zhonglitenghui.adapter.EquipmentGvAdapter;
import com.zhongli.main.zhonglitenghui.app.InitApplication;
import com.zhongli.main.zhonglitenghui.bean.EquipmentInfo;
import com.zhongli.main.zhonglitenghui.custom.VRefresh;
import com.zhongli.main.zhonglitenghui.until.Tools;
import com.zhongli.main.zhonglitenghui.volley.VolleyManager;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KIM on 2015/4/8 0008.
 */
public class EquipmentGraidActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private VRefresh sw_equipment_ref;
    private RecyclerView rl_equitment_view;
    private GridLayoutManager gridLayoutManager;
    /**
     * gridview的宽
     */
    private int width;

    private  static List<EquipmentInfo> tlist;
    private EquipmentGvAdapter equipmentGvAdapter;
    private int size = 8;
    private Toolbar tool_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_equipment);
        //getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        initView();
    }

    @Override
    protected void initView() {
        tool_bar = (Toolbar)findViewById(R.id.tool_bar);
        tool_bar.setBackgroundColor(getResources().getColor(R.color.text_white_color));
        //设置title
        if (tool_bar != null) {
            tool_bar.setTitle("设备工艺");
            setSupportActionBar(tool_bar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sw_equipment_ref = (VRefresh)findViewById(R.id.sw_equipment_ref);
        //设置刷新 加载ICON的颜色
        sw_equipment_ref.setColorSchemeResources(R.color.actionbar_txt_color, R.color.actionbar_txt_color);
        sw_equipment_ref.setOnRefreshListener(this);
        rl_equitment_view = (RecyclerView)findViewById(R.id.rl_equitment_view);
        //设置2列显示
        gridLayoutManager = new GridLayoutManager(this,2);
        rl_equitment_view.setHasFixedSize(true);
        rl_equitment_view.setLayoutManager(gridLayoutManager);
        //计算图片比例
        sizeViewWidth();

        loadType();

    }

    private void sizeViewWidth() {
        width = (InitApplication.screenWidth - 3 * getRes().getDimensionPixelSize(R.dimen.marginpadding)) / 2;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initAdapter(){
        equipmentGvAdapter = new EquipmentGvAdapter(this, tlist, width, 0);
        rl_equitment_view.setAdapter(equipmentGvAdapter);
        //加载上拉加载的设置
        sw_equipment_ref.setView(this, rl_equitment_view, gridLayoutManager, size);
    }

    @Override
    protected void initListenerData() {

    }

    @Override
    public void onRefresh() {
        //loadType();
        //完成刷新
        sw_equipment_ref.setRefreshing(false);
    }

    protected void loadType() {
        //显示进度条
        showLoadView();
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
                    if (tlist != null) {
                        dissMissLoadView();
                        //加载具体的内容
                        initAdapter();
                    }
                }
                dissMissLoadView();
                //完成刷新
                sw_equipment_ref.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Tools.showToast(getApplication(), "网络错误", Toast.LENGTH_LONG);
            }
        });
    }
    public static List<EquipmentInfo> getList(){
        return tlist;
    }
}
