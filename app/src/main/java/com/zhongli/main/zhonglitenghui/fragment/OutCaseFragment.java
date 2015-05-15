package com.zhongli.main.zhonglitenghui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongli.main.zhonglitenghui.R;

/**
 * Created by 278877385 on 2015/3/16.
 */
@SuppressLint("ValidFragment")
public class OutCaseFragment extends BaseFragment {
    private View baseView;
    private int typeId;

    public OutCaseFragment() {
    }

    public OutCaseFragment(int typeId) {
        this.typeId = typeId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_outcase, null);
        return baseView;
    }

    @Override
    protected void initView(View view) {
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initAdapter() {

    }

    @Override
    protected void initListenerData() {

    }
}
