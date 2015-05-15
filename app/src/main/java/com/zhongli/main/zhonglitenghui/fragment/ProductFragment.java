package com.zhongli.main.zhonglitenghui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zhongli.main.zhonglitenghui.R;
import com.zhongli.main.zhonglitenghui.adapter.ProductViewAdapter;
import com.zhongli.main.zhonglitenghui.bean.Type;
import com.zhongli.main.zhonglitenghui.custom.PagerSlidingTabStrip;
import com.zhongli.main.zhonglitenghui.volley.VolleyManager;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 278877385 on 2015/3/14.
 */
public class ProductFragment extends BaseFragment {
    private View baseView;
    private PagerSlidingTabStrip pager_product_tab;
    private ViewPager vp_product_view;
    private String[] titles = {"组件","电池片","支架"};
    private List<Type> tList;
    private ProductViewAdapter productViewAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_product,null);
        initView(baseView);
        return baseView;
    }


    @Override
    protected void initView(View view) {
        pager_product_tab = (PagerSlidingTabStrip) view.findViewById(R.id.pager_product_tab);
        vp_product_view = (ViewPager) view.findViewById(R.id.vp_product_view);
        loadType();
    }


    /**
     * 加载网络数据
     */
    protected  void loadType(){
        String url = getRes().getString(R.string.url_root) + getRes().getString(R.string.url_product_type);
        VolleyManager.getNoCacheJson(url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if(!TextUtils.isEmpty(jsonObject.toString())){
                    Map<String,String> strMap = JSON.parseObject(jsonObject.toString(), new TypeReference<LinkedHashMap<String, String>>() {
                    });
                    tList = JSON.parseArray(strMap.get("list"),Type.class);
                    for(int i=0;i<tList.size();i++){
                        Log.i("KIMI",tList.get(i).getName()+"    id:"+tList.get(i).getId());
                    }
                    //加载具体的内容
                    initAdapter();
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }
    @Override
    protected void initListener() {

    }

    @Override
    protected void initAdapter() {
        titles[0] = tList.get(0).getName();
        titles[1] = tList.get(1).getName();
        titles[2] = tList.get(2).getName();
        productViewAdapter = new ProductViewAdapter(getActivity().getSupportFragmentManager(), titles, tList);
        vp_product_view.setAdapter(productViewAdapter);

        pager_product_tab.setViewPager(vp_product_view);

    }

    @Override
    protected void initListenerData() {

    }



}
