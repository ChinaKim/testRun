package com.zhongli.main.zhonglitenghui.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongli.main.zhonglitenghui.EquipmentActivity;
import com.zhongli.main.zhonglitenghui.R;
import com.zhongli.main.zhonglitenghui.bean.EquipmentInfo;
import com.zhongli.main.zhonglitenghui.volley.BitmapCache;
import com.zhongli.main.zhonglitenghui.volley.VolleyManager;

import java.util.List;

/**
 * Created by 278877385 on 2015/3/16.
 */
public class EquipmentGvAdapter extends RecyclerView.Adapter<EquipmentGvAdapter.ViewHorld> {
    private List<EquipmentInfo> tList;
    private Context context;
    private LinearLayout.LayoutParams ilayoutParams;
    private LinearLayout.LayoutParams tlayoutParams;
    private int type;

    public EquipmentGvAdapter(Context context, List<EquipmentInfo> tList, int width, int type) {
        this.context = context;
        this.tList = tList;
        this.type = type;
        this.ilayoutParams = new LinearLayout.LayoutParams(width, width);
        this.tlayoutParams = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public ViewHorld onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_equipment_view, null);
        ViewHorld viewHorld = new ViewHorld(view);
        return viewHorld;
    }

    @Override
    public void onBindViewHolder(ViewHorld holder, final int position) {
        String imgUrl = context.getResources().getString(R.string.url_root) + tList.get(position).getImg();
        holder.tv_equitment.setText(tList.get(position).getDescription());
        holder.tv_equitment.setGravity(Gravity.CENTER);//标题居中
        holder.tv_equitment.setTextSize(12);
        holder.tv_equitment.setTextColor(Color.BLACK);
        holder.tv_equitment.setBackgroundColor(Color.WHITE);
        // holder.img_equipment_view.setDefaultImageResId(R.drawable.img_def_url);
        holder.img_equipment_view.setBackgroundColor(Color.WHITE);
        boolean flag = BitmapCache.getInstern().getSDCardBitmap(imgUrl,holder.img_equipment_view,new BitmapCache.CallBackSDcardImg() {
            @Override
            public void setImgToView(Bitmap bitmap, ImageView imgView) {
                new ObjectAnimator().ofFloat(imgView,"alpha",0.3f,1.0f).setDuration(350).start();
                imgView.setImageBitmap(bitmap);
            }
        });
        if(!flag){
            VolleyManager.loadImage(holder.img_equipment_view, imgUrl, R.drawable.img_def_url);
        }
        //holder.img_equipment_view.setImageUrl(imgUrl, VolleyManager.getImageLoader());
        //添加点击监听
        holder.lin_equipment_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到具体的展示页面
                Intent intent = new Intent(context, EquipmentActivity.class);
                Bundle bundle = new Bundle();
                //bundle.putSerializable(Constant.EQUIPMENT_LIST, (java.io.Serializable) tList);
                bundle.putInt("EQPOSITION", position);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tList.size();
    }

    class ViewHorld extends RecyclerView.ViewHolder {
        public ImageView img_equipment_view;
        public LinearLayout lin_equipment_view;
        public  TextView tv_equitment;

        public ViewHorld(View itemView) {
            super(itemView);
            img_equipment_view = (ImageView) itemView.findViewById(R.id.img_equipment_view);
            tv_equitment = (TextView)itemView.findViewById(R.id.tv_equitment);
            lin_equipment_view = (LinearLayout) itemView.findViewById(R.id.lin_equipment_view);
            //计算图片的宽高
            img_equipment_view.setLayoutParams(ilayoutParams);
        }
    }

}
