package com.zhongli.main.zhonglitenghui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhongli.main.zhonglitenghui.R;
import com.zhongli.main.zhonglitenghui.bean.Img;
import com.zhongli.main.zhonglitenghui.custom.TouchNoNetImageView;
import com.zhongli.main.zhonglitenghui.custom.ZoomTutorial;
import com.zhongli.main.zhonglitenghui.volley.BitmapCache;
import com.zhongli.main.zhonglitenghui.volley.VolleyManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 278877385 on 2015/3/19.
 */
public class CerViewPage extends PagerAdapter {
    private List<Img> imgList;
    private Context context;
    private int size;
    private List<ViewGroup> phImgList = new ArrayList<ViewGroup>();
    private ZoomTutorial mZoomTutorial;
    private RecyclerView recyclerView;

    public CerViewPage(Context context, List<Img> imgList, ZoomTutorial mZoomTutorial, RecyclerView recyclerView) {
        this.imgList = imgList;
        this.context = context;
        size = imgList.size();
        this.mZoomTutorial = mZoomTutorial;
        this.recyclerView = recyclerView;
    }

    @Override
    public int getCount() {
        if (size == 1)
            return size;
        else
            return Integer.MAX_VALUE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        //图片网络地址
        String imgUrl = context.getResources().getString(R.string.url_root) + imgList.get(position % size).getSrc();
        // ViewGroup viewGroup = phImgList.get(position % size);
        TouchNoNetImageView view = new TouchNoNetImageView(context);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mZoomTutorial.closeZoomAnim(position % size);
            }
        });
        //加载默认小图
//        ImageView img = (ImageView) recyclerView.getChildAt(position % size);
//        if (img != null && img.getDrawable() != null) {
//            view.setImageDrawable(img.getDrawable());
//        } else {
//            view.setImageResource(R.drawable.img_def_url);
//        }
        boolean flag = BitmapCache.getInstern().getSDCardBitmap(imgUrl, view, new BitmapCache.CallBackSDcardImg() {
            @Override
            public void setImgToView(Bitmap bitmap, ImageView imgView) {
                //new ObjectAnimator().ofFloat(imgView, "alpha", 0.3f, 1.0f).setDuration(500).start();
                imgView.setImageBitmap(bitmap);
            }
        });
        if (!flag) {
            VolleyManager.loadImage(view, imgUrl, R.drawable.img_def_url);
        }
        //加载图片
        //view.setImageUrl(imgUrl, VolleyManager.getImageLoader());

        container.addView(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return view;
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
