package com.zhongli.main.zhonglitenghui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongli.main.zhonglitenghui.custom.TouchNoNetImageView;

/**
 * Created by KIM on 2015/3/27 0027.
 */
public class CultureAdapter extends PagerAdapter {
    private Context context;
    /**
     * 图片数组
     */
    private Drawable[] cultureArray;

    public CultureAdapter(Context context, Drawable[] cultureArray) {
        this.context = context;
        this.cultureArray = cultureArray;
    }

    @Override
    public int getCount() {
        return cultureArray.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);

        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        //本地图片可缩放装载控件
        TouchNoNetImageView touchNoNetImageView = new TouchNoNetImageView(context);
        //设置TouchNoNetImageView适配图片
        //BaseActivity.sizeImg(touchNoNetImageView,mParams,1024f,576f);
        //iv.setImageResource(cultureArray[position]);
        touchNoNetImageView.setImageDrawable(cultureArray[position]);
        touchNoNetImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        touchNoNetImageView.setLayoutParams(mParams);
        container.addView(touchNoNetImageView);
        return touchNoNetImageView;
    }
}
