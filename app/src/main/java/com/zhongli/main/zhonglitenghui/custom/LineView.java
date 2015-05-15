package com.zhongli.main.zhonglitenghui.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.zhongli.main.zhonglitenghui.R;

/**
 * Created by KIM on 2015/3/27 0027.
 */
public class LineView extends LinearLayout{
    public LineView(Context context) {
        super(context);
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.line_view,this);
    }
}
