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

import com.zhongli.main.zhonglitenghui.CaseViewActivity;
import com.zhongli.main.zhonglitenghui.R;
import com.zhongli.main.zhonglitenghui.bean.Case;
import com.zhongli.main.zhonglitenghui.until.Constant;
import com.zhongli.main.zhonglitenghui.volley.BitmapCache;
import com.zhongli.main.zhonglitenghui.volley.VolleyManager;

import java.util.List;

/**
 * Created by 278877385 on 2015/3/16.
 */
public class CaseGvAdapter extends RecyclerView.Adapter<CaseGvAdapter.ViewHorld> {
    private List<Case> caseList;
    private Context context;
    private LinearLayout.LayoutParams ilayoutParams;
    private LinearLayout.LayoutParams tlayoutParams;
    private int type;

    public CaseGvAdapter(Context context, List<Case> caseList, int width, int type) {
        this.context = context;
        this.caseList = caseList;
        this.type = type;
        this.ilayoutParams = new LinearLayout.LayoutParams(width, width);
        this.tlayoutParams = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public ViewHorld onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_case_view, null);
        ViewHorld viewHorld = new ViewHorld(view);
        return viewHorld;
    }

    @Override
    public void onBindViewHolder(ViewHorld holder, final int position) {
        String imgUrl = context.getResources().getString(R.string.url_root) + caseList.get(position).getImg();
        holder.tv_case_title.setText(caseList.get(position).getName());
        holder.tv_case_title.setGravity(Gravity.CENTER);//标题居中
        holder.tv_case_title.setTextSize(12);
        holder.tv_case_title.setTextColor(Color.BLACK);
        //holder.img_case_view.setDefaultImageResId(R.drawable.img_def_url);
        holder.img_case_view.setBackgroundColor(Color.WHITE);
        //holder.img_case_view.setImageUrl(imgUrl, VolleyManager.getImageLoader());
        boolean flag = BitmapCache.getInstern().getSDCardBitmap(imgUrl, holder.img_case_view, new BitmapCache.CallBackSDcardImg() {

            @Override
            public void setImgToView(Bitmap bitmap, ImageView imgView) {
                new ObjectAnimator().ofFloat(imgView, "alpha", 0.3f, 1.0f).setDuration(350).start();

                imgView.setImageBitmap(bitmap);
            }
        });
        if (!flag) {
            VolleyManager.loadImage(holder.img_case_view, imgUrl, R.drawable.img_def_url);
        }
        //添加点击监听
        holder.lin_case_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到具体的展示页面
                Intent intent = new Intent(context, CaseViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.CASE_LIST, (java.io.Serializable) caseList);
                bundle.putInt("POSITION", position);
                bundle.putInt("TYPE", type);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return caseList.size();
    }

    class ViewHorld extends RecyclerView.ViewHolder {
        public ImageView img_case_view;
        public TextView tv_case_title;
        public LinearLayout lin_case_view;

        public ViewHorld(View itemView) {
            super(itemView);
            img_case_view = (ImageView) itemView.findViewById(R.id.img_case_view);
            tv_case_title = (TextView) itemView.findViewById(R.id.tv_case_title);
            lin_case_view = (LinearLayout) itemView.findViewById(R.id.lin_case_view);
            //计算图片的宽高
            img_case_view.setLayoutParams(ilayoutParams);
            tv_case_title.setLayoutParams(tlayoutParams);
        }
    }

}
