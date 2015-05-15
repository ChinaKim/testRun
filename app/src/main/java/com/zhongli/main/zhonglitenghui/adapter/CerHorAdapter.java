package com.zhongli.main.zhonglitenghui.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhongli.main.zhonglitenghui.R;
import com.zhongli.main.zhonglitenghui.bean.Img;
import com.zhongli.main.zhonglitenghui.volley.BitmapCache;
import com.zhongli.main.zhonglitenghui.volley.VolleyManager;

import java.util.List;

/**
 * Created by 278877385 on 2015/3/16.
 */
public class CerHorAdapter extends RecyclerView.Adapter<CerHorAdapter.ViewHorld> {
    private List<Img> imgList;
    private Context context;
    private ViewGroup.LayoutParams ilayoutParams;
    private OnItemClickListener onItemClickListener;

    public CerHorAdapter(Context context, List<Img> imgList, int width, int height) {
        this.context = context;
        this.imgList = imgList;
        this.ilayoutParams = new ViewGroup.LayoutParams(width, height);
    }

    @Override
    public ViewHorld onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cer_view, null);
        ViewHorld viewHorld = new ViewHorld(view);
        return viewHorld;
    }

    @Override
    public void onBindViewHolder(final ViewHorld holder, final int position) {
        String imgUrl = context.getResources().getString(R.string.url_root) + imgList.get(position).getZip();
        boolean flag = BitmapCache.getInstern().getSDCardBitmap(imgUrl, holder.img_cer_view, new BitmapCache.CallBackSDcardImg() {
            @Override
            public void setImgToView(Bitmap bitmap, ImageView imgView) {
                new ObjectAnimator().ofFloat(imgView, "alpha", 0.3f, 1.0f).setDuration(500).start();
                imgView.setImageBitmap(bitmap);
            }
        });
        if (!flag) {
            VolleyManager.loadImage(holder.img_cer_view, imgUrl, R.drawable.img_def_url);
        }
//        holder.img_cer_view.setDefaultImageResId(R.drawable.img_def_url);
//        holder.img_cer_view.setImageUrl(imgUrl, VolleyManager.getImageLoader());
        //添加点击监听
        holder.img_cer_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到具体的展示页面
                onItemClickListener.itemClickListener(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.imgList.size();
    }

    public class ViewHorld extends RecyclerView.ViewHolder {
        public ImageView img_cer_view;

        public ViewHorld(View itemView) {
            super(itemView);
            img_cer_view = (ImageView) itemView.findViewById(R.id.img_cer_view);
            //计算图片的宽高
            img_cer_view.setLayoutParams(ilayoutParams);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void itemClickListener(View view, int position);
    }
}
