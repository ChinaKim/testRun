package com.zhongli.main.zhonglitenghui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zhongli.main.zhonglitenghui.app.InitApplication;
import com.zhongli.main.zhonglitenghui.bean.AboutUsInfo;
import com.zhongli.main.zhonglitenghui.custom.AboutUSView;
import com.zhongli.main.zhonglitenghui.until.Tools;
import com.zhongli.main.zhonglitenghui.volley.VolleyManager;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KIM on 2015/3/19 0019.
 */
public class US2Activity extends BaseActivity implements View.OnClickListener {
    /**
     * actionbar
     */
    private Toolbar toolbar;

    /**
     * 关于我们信息列表
     */
    private List<AboutUsInfo> aboutUsList;

    /**
     * ImageView控件
     */
    private ImageView iv_about_us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_about_us);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);

        initView();
        initListener();
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) {
            toolbar.setTitle("关于我们");
            setSupportActionBar(toolbar);
            //添加返回按钮
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //改变状态栏的颜色
        //BaseActivity.setStatusBarTint(this, getRes().getColor(R.color.title_color));
        iv_about_us = (ImageView) findViewById(R.id.iv_about_us);
        //sizeImgAboutUs(iv_about_us,params,334f,761f);
        iv_about_us.setImageDrawable(setResId(R.drawable.main_view_1));

        // iv_about_us = (ImageView)findViewById(R.id.iv_about_us);
        if(InitApplication.aboutUsList == null){
            loadAboutUs();
        }else{
            initAdapter();
        }


    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initAdapter() {
        LinearLayout linerlayout_aboutus = (LinearLayout) findViewById(R.id.linerlayout_aboutus);

        if (InitApplication.aboutUsList != null) {
            //根据信息大小动态添加
            for (int i = 0; i < InitApplication.aboutUsList.size(); i++) {
                //添加公司信息
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout layout = new AboutUSView(this, InitApplication.aboutUsList.get(i));
                layout.setLayoutParams(params);
                linerlayout_aboutus.addView(layout);
                //添加下划线
//                LinearLayout.LayoutParams paramsLine = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                LinearLayout layoutLine = new LineView(this);
//                layoutLine.setLayoutParams(paramsLine);
//                linerlayout_aboutus.addView(layoutLine);
            }
        } else {
        }

    }

    @Override
    protected void initListenerData() {

    }


    private void loadAboutUs() {

        String url = getRes().getString(R.string.url_root) + getRes().getString(R.string.url_about_us);

        VolleyManager.getJson(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                //获取的JSON不为空的话

                if (!TextUtils.isEmpty(jsonObject.toString())) {
                    Map<String, String> strMap = JSON.parseObject(jsonObject.toString(),
                            new TypeReference<LinkedHashMap<String, String>>() {
                            });
                    InitApplication.aboutUsList = JSON.parseArray(strMap.get("list"), AboutUsInfo.class);
                    initAdapter();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Tools.showToast(getApplication(), "网络错误", Toast.LENGTH_LONG);
            }
        });
    }


    /**
     * 将公司别名、地址等信息包装成一个字符串
     *
     * @param aboutUSinfo "关于我们" 信息类
     * @return 信息包装后的字符串
     */
    private String createStringDetails(AboutUsInfo aboutUSinfo) {
        String aliss_name = aboutUSinfo.getAliss_name();
        String ch_address = aboutUSinfo.getCh_address();
        String area_address = aboutUSinfo.getArea_address();
        String postcode = aboutUSinfo.getPostcode();
        String telephone = aboutUSinfo.getTelephone();
        String phone = aboutUSinfo.getPhone();
        String fax = aboutUSinfo.getFax();
        String email = aboutUSinfo.getEmail();
        String webUrl = aboutUSinfo.getWebUrl();
        StringBuilder details = new StringBuilder();
        if (!"".equals(aliss_name)) {
            details.append(aliss_name + "\n");
        }
        if (!"".equals(ch_address)) {
            details.append(ch_address + "\n");
        }
        if (!"".equals(area_address)) {
            details.append(area_address + "\n");
        }
        if (!"".equals(postcode) && postcode != null) {
            details.append("邮编:" + postcode + "\n");
        }
        if (!"".equals(telephone)) {
            details.append("T:" + telephone + "\n");
        }
        if (!"".equals(fax)) {
            details.append("F:" + fax + "\n");
        }
        if (!"".equals(phone)) {
            details.append("Phone:" + phone + "\n");
        }
        if (!"".equals(email)) {
            details.append("Email:" + email + "\n");
        }
        if (!"".equals(webUrl)) {
            details.append("Web:" + webUrl);
        }

        return details.toString();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onClick(View v) {

    }


}
