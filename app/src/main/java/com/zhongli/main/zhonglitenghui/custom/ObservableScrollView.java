package com.zhongli.main.zhonglitenghui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by 278877385 on 2015/2/6.
 */
public class ObservableScrollView extends ScrollView {
    public CallBack myCallBack;

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        // TODO Auto-generated method stub
        super.onScrollChanged(l, t, oldl, oldt);
        if (myCallBack != null) {
            myCallBack.onScrollChanged(t);
        }
    }

    public void setMyCallBack(CallBack myCallBack) {
        this.myCallBack = myCallBack;
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    public static interface CallBack {
        public void onScrollChanged(int scrollY);
    }
}

