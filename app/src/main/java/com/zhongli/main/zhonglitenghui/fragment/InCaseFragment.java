package com.zhongli.main.zhonglitenghui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.zhongli.main.zhonglitenghui.adapter.CaseGvAdapter;
import com.zhongli.main.zhonglitenghui.app.InitApplication;
import com.zhongli.main.zhonglitenghui.bean.Case;
import com.zhongli.main.zhonglitenghui.custom.VRefresh;
import com.zhongli.main.zhonglitenghui.until.CreateMap;
import com.zhongli.main.zhonglitenghui.until.Tools;
import com.zhongli.main.zhonglitenghui.volley.VolleyManager;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 278877385 on 2015/3/16.
 */
@SuppressLint("ValidFragment")
public class InCaseFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private View baseView;
    private VRefresh sw_in_ref;
    private RecyclerView rl_incase_view;
    private int typeId;
    private CaseGvAdapter caseGvAdapter;
    private List<Case> caseList;
    private int page = 1;
    private int size = Integer.MAX_VALUE;
    private int width;
    private GridLayoutManager gridLayoutManager;

    public InCaseFragment() {
    }

    public InCaseFragment(int typeId) {
        this.typeId = typeId;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_incase, null);
        initView(baseView);
        return baseView;
    }

    @Override
    protected void initView(View view) {
        sw_in_ref = (VRefresh) view.findViewById(R.id.sw_in_ref);
        sw_in_ref.setColorSchemeResources(R.color.actionbar_txt_color, R.color.actionbar_txt_color);
        sw_in_ref.setOnRefreshListener(this);
        rl_incase_view = (RecyclerView) view.findViewById(R.id.rl_incase_view);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rl_incase_view.setHasFixedSize(true);
        rl_incase_view.setLayoutManager(gridLayoutManager);
        //计算图片比例
        sizeViewWidth();
        //显示进度条
        showLoadView(baseView);
        //加载网络数据
        loadView(page);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initAdapter() {
        caseGvAdapter = new CaseGvAdapter(getActivity(), caseList, width, 0);
        rl_incase_view.setAdapter(caseGvAdapter);
        //加载上拉加载的设置
        //sw_in_ref.setView(getActivity(), rl_incase_view, gridLayoutManager, size);
    }

    @Override
    protected void initListenerData() {
        sw_in_ref.setOnLoadListener(new VRefresh.OnLoadListener() {
            @Override
            public void onLoadMore() {
                page++;
                loadView(page);
            }
        });
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        loadView(page);
    }

    protected void loadView(final int page) {
        //开启进度条

        String url = getRes().getString(R.string.url_root) + getRes().getString(R.string.url_case_info);
        String[] keys = {"typeId", "page", "size", "deviceType"};
        String[] values = {typeId + "", page + "", size + "", 2 + ""};
        Map<String, String> params = CreateMap.createMap(keys, values);
        VolleyManager.getJson(url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                //获取的JSON不为空的话
                if (!TextUtils.isEmpty(jsonObject.toString())) {
                    Map<String, String> strMap = JSON.parseObject(jsonObject.toString(),
                            new TypeReference<LinkedHashMap<String, String>>() {
                            });
                    if (caseList == null || caseList.isEmpty()) {
                        caseList = JSON.parseArray(strMap.get("list"), Case.class);
                        //加载具体的内容
                        initAdapter();
                        initListenerData();
                    } else {
                        if (page == 1) {
                            caseList.clear();
                        }
                        caseList.addAll(JSON.parseArray(strMap.get("list"), Case.class));
                        caseGvAdapter.notifyDataSetChanged();
                    }

                }
                //关闭进度条
                dissMissLoadView();
                //完成刷新
                sw_in_ref.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dissMissLoadView();
                //Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                Tools.showToast(getActivity(),"网络错误",Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        Tools.closeToast();
    }



    /**
     * 计算图片的宽高
     */
    protected void sizeViewWidth() {
        width = (InitApplication.screenWidth - 3 * getRes().getDimensionPixelSize(R.dimen.marginpadding)) / 2;
    }
}

