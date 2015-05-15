package com.zhongli.main.zhonglitenghui.custom;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by 278877385 on 2015/3/17.
 */
public class VRefresh extends SwipeRefreshLayout {
    /**
     * RecyclerView 实例
     */
    private RecyclerView recyclerView;
    /**
     * 滑动到最下面时的上拉操作
     */
    private int mTouchSlop;
    /**
     * 按下时的y坐标
     */
    private int mYDown;
    /**
     * 是否在加载中 ( 上拉加载更多 )
     */
    private boolean isLoading = false;

    private float mLastY = -1;
    /**
     * 最后一行
     */
    boolean isLastRow = false;
    /**
     * 是否还有数据可以加载
     */
    private boolean moreData = true;

    /**
     * 可见多少项
     */
    private int visibleItemCount;

    /**
     * 总共多少项目
     */
    private int totalItemCount;

    /**
     * 第一个可见项目
     */
    private int firstVisibleItem;

    /**
     * 上次的总条数
     */
    private int previousTotal;

    /**
     * 加载每页的数据量
     */
    private int visibleThreshold;

    /**
     * ListView的布局器
     */
    private GridLayoutManager mLayoutManager;

    /**
     * 加载更多的判断
     */
    private boolean loading;

    /**
     * 监听器对象
     */
    private OnLoadListener mOnLoadListener;

    public VRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void setView(final Context context, View childView, GridLayoutManager layoutManager, int size) {
        if (childView instanceof RecyclerView) {
            recyclerView = (RecyclerView) childView;
            mLayoutManager = layoutManager;
            visibleThreshold = size;
            // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    //初始化参数
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                    if (loading) {
                        //加载完成
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading &&
                            (totalItemCount - visibleItemCount)
                                    <= (firstVisibleItem + visibleThreshold)) {
                        //Toast.makeText(context, "正在加载更多", Toast.LENGTH_SHORT).show();
                        //Tools.showToast(context,"正在加载更多",Toast.LENGTH_LONG);
                        loading = true;
                        loadData();
                    }
                }
            });
        }
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        mOnLoadListener = onLoadListener;
    }

    /**
     * 加载数据
     */
    protected void loadData() {
        if(mOnLoadListener != null)
        mOnLoadListener.onLoadMore();
    }

    public static interface OnLoadListener {
        public void onLoadMore();
    }

}
