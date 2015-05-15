package com.zhongli.main.zhonglitenghui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 278877385 on 2015/3/24.
 */
public class ExtendedVerViewPager extends VerticalViewPager {
    public ExtendedVerViewPager(Context context) {
        super(context);
    }

    public ExtendedVerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dy, int x, int y) {
        if (v instanceof TouchImageView) {
            //
            // canScrollHorizontally is not supported for Api < 14. To get around this issue,
            // ViewPager is extended and canScrollHorizontallyFroyo, a wrapper around
            // canScrollHorizontally supporting Api >= 8, is called.
            //
            return ((TouchImageView) v).canScrollHorizontallyFroyo(-dy);

        } else {
            return super.canScroll(v, checkV, dy, x, y);
        }
    }
}
