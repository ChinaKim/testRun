package com.zhongli.main.zhonglitenghui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zhongli.main.zhonglitenghui.R;
import com.zhongli.main.zhonglitenghui.adapter.CaseViewAdapter;
import com.zhongli.main.zhonglitenghui.bean.Type;
import com.zhongli.main.zhonglitenghui.custom.PagerSlidingTabStrip;
import com.zhongli.main.zhonglitenghui.until.Tools;
import com.zhongli.main.zhonglitenghui.volley.VolleyManager;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 278877385 on 2015/3/14.
 */
public class CaseFragment extends BaseFragment {
    private View baseView;
    private PagerSlidingTabStrip pager_case_tab;
    private ViewPager vp_case_view;
    private String[] titles = {"国内电站", "海外电站"};
    private CaseViewAdapter caseViewAdapter;
    private List<Type> tList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_case, null);
        initView(baseView);
        return baseView;
    }

    @Override
    protected void initView(View view) {
        pager_case_tab = (PagerSlidingTabStrip) view.findViewById(R.id.pager_case_tab);
        vp_case_view = (ViewPager) view.findViewById(R.id.vp_case_view);
        loadType();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initAdapter() {
        titles[0] = tList.get(0).getName();
        titles[1] = tList.get(1).getName();
        caseViewAdapter = new CaseViewAdapter(getActivity().getSupportFragmentManager(), titles, tList);
        vp_case_view.setAdapter(caseViewAdapter);
        pager_case_tab.setViewPager(vp_case_view);
    }

    @Override
    protected void initListenerData() {

    }

    /**
     * 加载网络数据
     */
    protected void loadType() {
        String url = getRes().getString(R.string.url_root) + getRes().getString(R.string.url_case_type);
        VolleyManager.getNoCacheJson(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                //获取的JSON不为空的话
                if (!TextUtils.isEmpty(jsonObject.toString())) {
                    Map<String, String> strMap = JSON.parseObject(jsonObject.toString(),
                            new TypeReference<LinkedHashMap<String, String>>() {
                            });
                    tList = JSON.parseArray(strMap.get("list"), Type.class);
                    //加载具体的内容
                    initAdapter();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                Tools.showToast(getActivity(),"网络错误",Toast.LENGTH_LONG);
            }
        });
    }
}
