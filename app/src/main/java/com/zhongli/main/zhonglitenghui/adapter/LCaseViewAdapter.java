package com.zhongli.main.zhonglitenghui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongli.main.zhonglitenghui.R;
import com.zhongli.main.zhonglitenghui.bean.Case;
import com.zhongli.main.zhonglitenghui.volley.BitmapCache;
import com.zhongli.main.zhonglitenghui.volley.VolleyManager;

import java.util.List;

/**
 * Created by 278877385 on 2015/3/17.
 */
public class LCaseViewAdapter extends BaseAdapter {
    private List<Case> cList;
    private Context context;
    private int size;

    public LCaseViewAdapter(Context context, List<Case> cList) {
        this.cList = cList;
        this.context = context;
        this.size = cList.size();
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return cList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHorld horld;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_case_list, null);
            horld = new ViewHorld();
            horld.img_case_list = (ImageView) convertView.findViewById(R.id.img_case_list);
            horld.lin_case_list = (LinearLayout) convertView.findViewById(R.id.lin_case_list);
            convertView.setTag(horld);
        } else {
            horld = (ViewHorld) convertView.getTag();
        }
        String imgUrl = context.getResources().getString(R.string.url_root) + cList.get(position % size).getImg();
        boolean flag = BitmapCache.getInstern().getSDCardBitmap(imgUrl, horld.img_case_list, new BitmapCache.CallBackSDcardImg() {
            @Override
            public void setImgToView(Bitmap bitmap, ImageView imgView) {
               //new ObjectAnimator().ofFloat(imgView, "alpha", 0.3f, 1.0f).setDuration(500).start();
                imgView.setImageBitmap(bitmap);
            }
        });
        if (!flag) {
            VolleyManager.loadImage(horld.img_case_list, imgUrl, R.drawable.img_def_url);
        }
//        horld.img_case_list.setDefaultImageResId(R.drawable.img_def_url);
//        horld.img_case_list.setImageUrl(imgUrl, VolleyManager.getImageLoader());
        if (cList.get(position % size).isSelFag()) {
            horld.lin_case_list.setBackgroundResource(R.drawable.shape_list_view);
        } else {
            horld.lin_case_list.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }
        return convertView;
    }

    class ViewHorld {
        public ImageView img_case_list;
        public LinearLayout lin_case_list;
    }
}
