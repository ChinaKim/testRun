package com.zhongli.main.zhonglitenghui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongli.main.zhonglitenghui.BriefActivity;
import com.zhongli.main.zhonglitenghui.CerActivity;
import com.zhongli.main.zhonglitenghui.CultureActivity;
import com.zhongli.main.zhonglitenghui.EquipmentGraidActivity;
import com.zhongli.main.zhonglitenghui.NetActivity;
import com.zhongli.main.zhonglitenghui.R;
import com.zhongli.main.zhonglitenghui.US2Activity;
import com.zhongli.main.zhonglitenghui.until.Tools;
import com.zhongli.main.zhonglitenghui.volley.BitmapCache;

/**
 * Created by 278877385 on 2015/3/14.
 */
public class AboutFragment extends BaseFragment implements View.OnClickListener {
    private View baseView;
    private LinearLayout lin_about_brief;//公司简介
    private LinearLayout lin_about_culture;//企业文化
    private LinearLayout lin_about_net;  //销售网络
    private LinearLayout lin_about_certificate;//认证证书
    private LinearLayout lin_about_craft;  //设备工艺
    private LinearLayout lin_about_us;   //关于我们
    private LinearLayout lin_about_email; //邮件反馈
    private TextView tv_about_brief;
    private TextView tv_about_culture;
    private TextView tv_about_net;
    private TextView tv_about_certificate;
    private TextView tv_about_craft;
    private TextView tv_about_us;
    private TextView tv_about_email;

    private ImageView iv_about_bg;
    private LinearLayout.LayoutParams params;
    private BitmapCache instern = BitmapCache.getInstern();
    private Bitmap background;
    private Handler  handler = new Handler();



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_about, null);
        initView(baseView);
        initListener();
        return baseView;
    }

    @Override
    protected void initView(View view) {
        lin_about_brief = (LinearLayout) view.findViewById(R.id.lin_about_brief);
        tv_about_brief = (TextView) lin_about_brief.findViewById(R.id.tv_about_view);
        lin_about_culture = (LinearLayout) view.findViewById(R.id.lin_about_culture);
        tv_about_culture = (TextView) lin_about_culture.findViewById(R.id.tv_about_view);
        lin_about_net = (LinearLayout) view.findViewById(R.id.lin_about_net);
        tv_about_net = (TextView) lin_about_net.findViewById(R.id.tv_about_view);
        lin_about_certificate = (LinearLayout) view.findViewById(R.id.lin_about_certificate);
        tv_about_certificate = (TextView) lin_about_certificate.findViewById(R.id.tv_about_view);
        lin_about_craft = (LinearLayout) view.findViewById(R.id.lin_about_craft);
        tv_about_craft = (TextView) lin_about_craft.findViewById(R.id.tv_about_view);
        lin_about_us = (LinearLayout) view.findViewById(R.id.lin_about_us);
        tv_about_us = (TextView) lin_about_us.findViewById(R.id.tv_about_view);
        lin_about_email = (LinearLayout) view.findViewById(R.id.lin_about_email);
        tv_about_email = (TextView)lin_about_email.findViewById(R.id.tv_about_view);
        tv_about_brief.setText(R.string.about_brief);
        tv_about_culture.setText(R.string.about_culture);
        tv_about_net.setText(R.string.about_net);
        tv_about_certificate.setText(R.string.about_certificate);
        tv_about_craft.setText(R.string.about_craft);
        tv_about_us.setText(R.string.about_us);
        tv_about_email.setText(R.string.about_email);

        iv_about_bg = (ImageView)view.findViewById(R.id.iv_about_bg);
       /* BaseActivity.sizeImg(iv_about_bg,params,750f,1334f);
        iv_about_bg.setImageDrawable(BaseActivity.);*/
        background = instern.getBitmap("backgound");
        if(background == null){
            background = BitmapFactory.decodeResource(getRes(),R.drawable.about_bg);
            instern.putBitmap("backgound",background);
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_about_bg.setImageBitmap(background);
            }
        },40);

    }

    protected void initListener() {
        lin_about_brief.setOnClickListener(this);
        lin_about_culture.setOnClickListener(this);
        lin_about_net.setOnClickListener(this);
        lin_about_certificate.setOnClickListener(this);
        lin_about_craft.setOnClickListener(this);
        lin_about_us.setOnClickListener(this);
        lin_about_email.setOnClickListener(this);
    }

    @Override
    protected void initAdapter() {

    }

    @Override
    protected void initListenerData() {

    }


    @Override
    public void onClick(View v) {
        //页面跳转
        switch (v.getId()) {
            case R.id.lin_about_brief:
                //公司简介
                Intent intentBrief = new Intent(getActivity(), BriefActivity.class);
                startActivity(intentBrief);
                break;
            case R.id.lin_about_culture:
                //企业文化
                Intent intentCulture = new Intent(getActivity(), CultureActivity.class);
                startActivity(intentCulture);
                break;
            case R.id.lin_about_certificate:
                //认证证书
                Intent intent = new Intent(getActivity(), CerActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.lin_about_net:
                //销售网络
                Intent intentNet = new Intent(getActivity(), NetActivity.class);
                startActivity(intentNet);
                break;
            case R.id.lin_about_craft:
                //设备工艺
                //Intent intentCraft = new Intent(getActivity(), EquipmentActivity.class);
                Intent intentCraft = new Intent(getActivity(), EquipmentGraidActivity.class);
                startActivity(intentCraft);
                break;
            case R.id.lin_about_us:
                //关于我们
                Intent intentUs = new Intent(getActivity(), US2Activity.class);
                startActivity(intentUs);
                break;
            case R.id.lin_about_email:
                String email = "president.office@talesun.com";
                try{
                    Intent intetEmail = new Intent(Intent.ACTION_SENDTO);
                    intetEmail.setData(Uri.parse("mailto:"+email));
                    intetEmail.putExtra(Intent.EXTRA_SUBJECT,"信息咨询");
                    intetEmail.putExtra(Intent.EXTRA_TEXT,R.string.email_context);
                    startActivity(intetEmail);
                }catch (Exception e){
                    Tools.showToast(getActivity(),"请先安装邮箱", Toast.LENGTH_LONG);
                }
                break;

        }
    }
}
