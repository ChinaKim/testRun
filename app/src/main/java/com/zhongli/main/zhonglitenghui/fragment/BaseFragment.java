package com.zhongli.main.zhonglitenghui.fragment;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zhongli.main.zhonglitenghui.R;

/**
 * Created by 278877385 on 2015/3/2.
 */
public abstract class BaseFragment extends Fragment {
    private View loadView;

    abstract protected void initView(View view);

    abstract protected void initListener();

    abstract protected void initAdapter();

    abstract protected void initListenerData();

    protected Resources getRes() {
        return getActivity().getResources();
    }



    protected void showLoadView(View view) {
        loadView = view.findViewById(R.id.loadview);
        loadView.setVisibility(View.VISIBLE);
    }

    protected void dissMissLoadView() {
        if (loadView != null) {
            loadView.setVisibility(View.GONE);
        }
    }
}
