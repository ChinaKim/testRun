package com.zhongli.main.zhonglitenghui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zhongli.main.zhonglitenghui.R;
import com.zhongli.main.zhonglitenghui.adapter.LVideoAdapter;
import com.zhongli.main.zhonglitenghui.app.InitApplication;
import com.zhongli.main.zhonglitenghui.bean.Video;
import com.zhongli.main.zhonglitenghui.volley.VolleyManager;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 278877385 on 2015/3/14.
 */
public class VideoFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private View baseView;
    private ListView lv_video_view;
    private SwipeRefreshLayout rf_video_list;
    private List<Video> vList;
    private LVideoAdapter lVideoAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_video, null);
        initView(baseView);
        return baseView;
    }

    @Override
    protected void initView(View view) {
        lv_video_view = (ListView) view.findViewById(R.id.lv_video_view);
        rf_video_list = (SwipeRefreshLayout) view.findViewById(R.id.rf_video_list);
        rf_video_list.setColorSchemeResources(R.color.actionbar_txt_color, R.color.actionbar_txt_color);
        rf_video_list.setOnRefreshListener(this);
        showLoadView(baseView);
        loadData();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initAdapter() {
        lVideoAdapter = new LVideoAdapter(getActivity(), vList);
        lv_video_view.setAdapter(lVideoAdapter);
    }

    @Override
    protected void initListenerData() {

    }

    /**
     * 加载网络数据
     */
    protected void loadData() {
        String url = getRes().getString(R.string.url_root) + getRes().getString(R.string.url_video_list);
        VolleyManager.getJson(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (!TextUtils.isEmpty(jsonObject.toString())) {
                    Map<String, String> strMap = JSON.parseObject(jsonObject.toString(),
                            new TypeReference<LinkedHashMap<String, String>>() {
                            });
                    if ("success".equals(strMap.get("status"))) {
                        //加载数据
                        if (vList == null || vList.isEmpty()) {
                            vList = JSON.parseArray(strMap.get("videos"), Video.class);
                            initAdapter();
                        } else {
                            vList.clear();
                            vList.addAll(JSON.parseArray(strMap.get("videos"), Video.class));
                            lVideoAdapter.notifyDataSetChanged();
                        }
                    }
                }
                dissMissLoadView();
                rf_video_list.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dissMissLoadView();
            }
        });
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭所有标志位
        if (vList != null && !vList.isEmpty()) {
            for (Video video : vList) {
                video.setDown(false);
            }
        }
        InitApplication.downCount = 0;
        InitApplication.appLog.i("返回了");
    }
}
