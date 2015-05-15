package com.zhongli.main.zhonglitenghui.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongli.main.zhonglitenghui.R;
import com.zhongli.main.zhonglitenghui.bean.EquipmentInfo;
import com.zhongli.main.zhonglitenghui.custom.TouchNoNetImageView;
import com.zhongli.main.zhonglitenghui.volley.BitmapCache;
import com.zhongli.main.zhonglitenghui.volley.VolleyManager;

import java.util.List;


public class AdvertisementAdapter extends PagerAdapter {
    /**
     * EquipmentInfo集合
     */
    private List<EquipmentInfo> tlist;
    private Context context;
    private LinearLayout.LayoutParams params;
    private int size;


    public AdvertisementAdapter(Context context,List<EquipmentInfo> tlist){
        this.tlist = tlist;
        this.context = context;
        this.size = tlist.size();
    }
        @Override
    public int getCount() {

      return tlist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        /*int s = (position%views.size());
        View iv = views.get(s);
        ((ViewPager) container).removeView(iv);*/

        //container.removeView(views.get(position));
        container.removeView((View) object);


    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

      /* View view = View.inflate(context,R.layout.activity_equipment,null);
        LinearLayout ll = (LinearLayout)view.findViewById(R.id.ll);
        addLinDots(ll, tlist);*/


        String imgrul = context.getResources().getString(R.string.url_root)+tlist.get(position).getImg();
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        //网络加载
         TouchNoNetImageView iv = new TouchNoNetImageView(context);
        iv.setImageResource(R.color.view_bg_color);
        boolean flag = BitmapCache.getInstern().getSDCardBitmap(imgrul,iv,new BitmapCache.CallBackSDcardImg() {
            @Override
            public void setImgToView(Bitmap bitmap, ImageView imgView) {
                new ObjectAnimator().ofFloat(imgView,"alpha",0.3f,1.0f).setDuration(500).start();
                imgView.setImageBitmap(bitmap);
            }
        });
        if(!flag){
            VolleyManager.loadImage(iv,imgrul,R.drawable.img_def_url);
        }
        //iv.setImageUrl(imgrul, VolleyManager.getImageLoader());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setLayoutParams(mParams);

        container.addView(iv);
        return iv;
    }


}
