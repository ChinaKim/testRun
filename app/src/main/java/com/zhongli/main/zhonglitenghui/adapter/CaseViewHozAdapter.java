package com.zhongli.main.zhonglitenghui.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhongli.main.zhonglitenghui.R;
import com.zhongli.main.zhonglitenghui.custom.TouchNoNetImageView;
import com.zhongli.main.zhonglitenghui.volley.BitmapCache;
import com.zhongli.main.zhonglitenghui.volley.VolleyManager;

import java.util.List;

/**
 * Created by 278877385 on 2015/3/18.
 */
public class CaseViewHozAdapter extends PagerAdapter {
    private List<String> details;
    private Context context;
    private int size;
    private Handler handler;
    private int type;

    public CaseViewHozAdapter(Context context, List<String> details, Handler handler, int type) {
        this.details = details;
        this.context = context;
        this.size = details.size();
        this.handler = handler;
        this.type = type;
    }

    @Override
    public int getCount() {
        if (size == 1)
            return size;
        else
            return Integer.MAX_VALUE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //图片网络地址
        final String imgUrl = context.getResources().getString(R.string.url_root) + details.get(position % size);
        final TouchNoNetImageView touchImageView = new TouchNoNetImageView(context);
        if (type == 0) {
            touchImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            touchImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        touchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
            }
        });
        //加载默认图片
        touchImageView.setImageResource(R.color.view_bg_color);
        boolean flag = BitmapCache.getInstern().getSDCardBitmap(imgUrl, touchImageView, new BitmapCache.CallBackSDcardImg() {
            @Override
            public void setImgToView(Bitmap bitmap, ImageView imgView) {
                //将本地保存的图片加载进来
                //imgView.setImageBitmap(bitmap);
                new ObjectAnimator().ofFloat(imgView, "alpha", 0.3f, 1.0f).setDuration(500).start();
                imgView.setImageBitmap(bitmap);
            }
        });
        if (!flag) {
            VolleyManager.loadImage(touchImageView, imgUrl, R.drawable.img_def_url);
        }
        //touchImageView.setImageUrl(imgUrl, VolleyManager.getImageLoader());
        container.addView(touchImageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return touchImageView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
