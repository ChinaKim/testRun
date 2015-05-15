package com.zhongli.main.zhonglitenghui.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhongli.main.zhonglitenghui.R;
import com.zhongli.main.zhonglitenghui.app.InitApplication;
import com.zhongli.main.zhonglitenghui.bean.Case;
import com.zhongli.main.zhonglitenghui.custom.ExtendedViewPager;
import com.zhongli.main.zhonglitenghui.custom.VerticalPagerAdapter;

import java.util.List;

/**
 * Created by 278877385 on 2015/3/18.
 */
public class CaseViewVerAdapter extends VerticalPagerAdapter {
    public List<View> hList;
    public int size;
    private Context context;
    private List<Case> cList;
    private Handler handler;
    private int type;
    private LinearLayout.LayoutParams params;
    private int[] currentIds;
    private int[] oldIds;

    public CaseViewVerAdapter(Context context, List<View> hList, List<Case> cList, Handler handler, int type) {
        super(context);
        this.hList = hList;
        this.size = hList.size();
        this.cList = cList;
        this.context = context;
        this.handler = handler;
        this.type = type;
        currentIds = new int[size];
        oldIds = new int[size];
    }

    @Override
    public int getCount() {
        //无限滑动
        if (size == 1)
            return size;
        else
            return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        //将View添加到适配器上
        View view = View.inflate(context, R.layout.casehor_view, null);
        ExtendedViewPager viewPager = (ExtendedViewPager) view.findViewById(R.id.case_ex_view);
        final LinearLayout lin_case_dots = (LinearLayout) view.findViewById(R.id.lin_case_dots);
        viewPager.setAdapter(new CaseViewHozAdapter(context, cList.get(position % size).getDetails(), handler, type));
        final int hSize = cList.get(position % size).getDetails().size();
        if (hSize != 1) {
            addLinDots(lin_case_dots, cList.get(position % size));
            currentIds[position % size] = viewPager.getCurrentItem() % hSize;
            oldIds[position % size] = currentIds[position % size];
            viewPager.setOnPageChangeListener(new OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int positions) {

                    currentIds[position % size] = positions % hSize;
                    InitApplication.appLog.i("当前选中的为:" + currentIds[position % size]);
                    InitApplication.appLog.i("上一个为:" + oldIds[position % size]);
                    if (oldIds[position % size] != currentIds[position % size]) {
                        lin_case_dots.getChildAt(currentIds[position % size]).setBackgroundResource(R.drawable.shape_dots_down);
                        lin_case_dots.getChildAt(oldIds[position % size]).setBackgroundResource(R.drawable.shape_dots_up);
                    }

                    oldIds[position % size] = currentIds[position % size];
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            lin_case_dots.getChildAt(currentIds[position % size]).setBackgroundResource(R.drawable.shape_dots_down);
        }
        container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    protected void addLinDots(LinearLayout lin_case_dots, Case aCase) {
        int size = context.getResources().getDimensionPixelSize(R.dimen.dots_size);
        int dsize = context.getResources().getDimensionPixelSize(R.dimen.marginTopNext);
        for (int i = 0; i < aCase.getDetails().size(); i++) {
            View dotView = new View(context);
            params = new LinearLayout.LayoutParams(size, size);
            params.gravity = Gravity.CENTER_VERTICAL;
            params.setMargins(0, 0,
                    dsize, 0);
            dotView.setLayoutParams(params);
            dotView.setBackgroundResource(R.drawable.shape_dots_up);
            lin_case_dots.addView(dotView);
        }
    }
}
