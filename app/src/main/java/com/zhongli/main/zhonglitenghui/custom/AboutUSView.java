package com.zhongli.main.zhonglitenghui.custom;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongli.main.zhonglitenghui.R;
import com.zhongli.main.zhonglitenghui.bean.AboutUsInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by KIM on 2015/3/27 0027.
 */
public class AboutUSView extends LinearLayout implements View.OnClickListener{
    private AboutUsInfo info;

    /**
     * 公司信息
     */
    private AboutUsInfo companyInfo1;

    /**
     * 公司名
     */
    private TextView tv_companyName1;

    /**
     * 别名
     */
    private TextView tv_aliss_name;

    /**
     * 中文地址
     */
    private TextView tv_ch_address;

    /**
     * 区域地址
     */
    private TextView tv_area_address;

    /**
     * 邮编
     */
    private TextView tv_postcode;

    /**
     * telephone 电话
     */
    private TextView tv_telephone;

    /**
     * fax
     */
    private TextView tv_fax;

    /**
     * phone
     */
    private TextView tv_phone;

    /**
     * email
     */
    private TextView tv_email;

    /**
     * 网址
     */
    private TextView tv_webUrl;
    private Context context;

    public AboutUSView(Context context,AboutUsInfo info) {
        super(context);
        this.context = context;
        this.info = info;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.aboutus_view,this);
        initView();
        initListern();
        initAdapter();

    }



    private void initView() {
        tv_companyName1 = (TextView)findViewById(R.id.tv_companyName1);
        tv_aliss_name = (TextView)findViewById(R.id.tv_aliss_name);
        tv_ch_address = (TextView)findViewById(R.id.tv_ch_address);
        tv_area_address = (TextView)findViewById(R.id.tv_area_address);
        tv_postcode = (TextView)findViewById(R.id.tv_postcode);
        tv_telephone = (TextView)findViewById(R.id.tv_telephone);
        tv_fax = (TextView)findViewById(R.id.tv_fax);
        tv_phone = (TextView)findViewById(R.id.tv_phone);
        tv_email = (TextView)findViewById(R.id.tv_email);
        tv_webUrl = (TextView)findViewById(R.id.tv_webUrl);
    }

    private void initListern() {
        tv_telephone.setOnClickListener(this);
        tv_phone.setOnClickListener(this);
        //tv_email.setOnClickListener(this);
        tv_webUrl.setOnClickListener(this);
        tv_ch_address.setOnClickListener(this);
    }

    private void initAdapter() {
        if(info != null){
            companyInfo1 = info;
            tv_companyName1.setText(companyInfo1.getName());
            if(!"".equals(companyInfo1.getAliss_name())){
                tv_aliss_name.setVisibility(View.VISIBLE);
                tv_aliss_name.setText(companyInfo1.getAliss_name());
            }
            if(!"".equals(companyInfo1.getCh_address())){
                tv_ch_address.setVisibility(View.VISIBLE);
                tv_ch_address.setText(companyInfo1.getCh_address());
                //tv_ch_address.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                //tv_ch_address.getPaint().setAntiAlias(true);//抗锯齿
            }
            if(!"".equals(companyInfo1.getArea_address())){
                tv_area_address.setVisibility(View.VISIBLE);
                tv_area_address.setText(companyInfo1.getArea_address());
            }
            if(!"".equals(companyInfo1.getPostcode()) && (companyInfo1.getPostcode() != null)){
                tv_postcode.setVisibility(View.VISIBLE);
                tv_postcode.setText("邮编："+companyInfo1.getPostcode());
            }
            if(!"".equals(companyInfo1.getTelephone())){
                tv_telephone.setVisibility(View.VISIBLE);
                tv_telephone.setText("Tel:"+companyInfo1.getTelephone());
                //tv_telephone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                //tv_telephone.getPaint().setAntiAlias(true);//抗锯齿
            }
            if(!"".equals(companyInfo1.getFax())){
                tv_fax.setVisibility(View.VISIBLE);
                tv_fax.setText("Fax:"+companyInfo1.getFax());
            }
            if(!"".equals(companyInfo1.getPhone())){
                tv_phone.setVisibility(View.VISIBLE);
                tv_phone.setText("Phone:"+companyInfo1.getPhone());
                //tv_phone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                //tv_phone.getPaint().setAntiAlias(true);//抗锯齿
            }
            if(!"".equals(companyInfo1.getEmail())){
                tv_email.setVisibility(View.VISIBLE);
                tv_email.setText("Email:"+companyInfo1.getEmail());
                /*tv_email.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                tv_email.getPaint().setAntiAlias(true);//抗锯齿*/
            }
            if(!"".equals(companyInfo1.getWebUrl())){
                tv_webUrl.setVisibility(View.VISIBLE);
                tv_webUrl.setText("Web:"+companyInfo1.getWebUrl());
                //tv_webUrl.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                //tv_webUrl.getPaint().setAntiAlias(true);//抗锯齿
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
                //拨号
            case R.id.tv_telephone:
                String tel = onlydigita(companyInfo1.getTelephone());
                Intent intentTel = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
                //intentTel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentTel);
                break;
                //拨号
            case R.id.tv_phone:
                String phone= onlydigita(companyInfo1.getPhone());
                Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                context.startActivity(intentPhone);
                break;
                //打开网页
            case R.id.tv_webUrl:
                String url =companyInfo1.getWebUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
                break;
                //发送邮件
            /*case R.id.tv_email:
                String email = companyInfo1.getEmail();
                Intent data=new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:" + email));
                data.putExtra(Intent.EXTRA_SUBJECT, R.string.email_title);
                data.putExtra(Intent.EXTRA_TEXT, R.string.email_context);
                context.startActivity(data);
                break;*/
                //一键地址导航
            case R.id.tv_ch_address:
                String urlmap ="http://api.map.baidu.com/marker?location=31.561959,120.835779&title=公司位置&content=江苏常熟沙家浜常昆工业园腾晖路1号&output=html";
                Intent intentMap = new Intent(Intent.ACTION_VIEW);
                intentMap.setData(Uri.parse(urlmap));
                context.startActivity(intentMap);
                break;
        }

    }

    //提取数字
    public String onlydigita(String str){
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(str);
        return  m.replaceAll("").trim();
    }
}
