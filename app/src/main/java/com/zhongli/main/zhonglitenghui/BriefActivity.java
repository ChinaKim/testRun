package com.zhongli.main.zhonglitenghui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zhongli.main.zhonglitenghui.app.InitApplication;
import com.zhongli.main.zhonglitenghui.until.Tools;
import com.zhongli.main.zhonglitenghui.volley.VolleyManager;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by KIM on 2015/3/17 0017.
 */
public class BriefActivity extends BaseActivity{
    /**
     * 公司简介文本
     */
    protected String protext;

    /**
     * 显示文本控件
     */
    private TextView tv_about_brief;

    /**
     * 背景图ImageView控件
     */
    private ImageView iv_background;

    /**
     * LayoutParams控件
     */
    private LinearLayout.LayoutParams params;

    /**
     * 关闭按钮
     */
    private ImageButton IB_click;
    /**
     * 标题
     */
    private Toolbar tool_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //去掉标题栏
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_brief);
        //全屏
        //getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        initView();
        initListener();
    }


    @Override
    protected void initView() {
        tool_bar = (Toolbar)findViewById(R.id.tool_bar);
        //设置title
        if (tool_bar != null) {
            tool_bar.setTitle("公司简介");
            setSupportActionBar(tool_bar);
            //添加返回按钮
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tv_about_brief = (TextView)findViewById(R.id.iv_about_brief);
        //IB_click = (ImageButton)findViewById(R.id.click_iv);
        iv_background = (ImageView)findViewById(R.id.iv_background);
        //sizeImg(iv_background,params,768f,1024f);
        iv_background.setImageDrawable(setResId(R.drawable.brief_bg));
        //iv_background.setImageResource(R.drawable.brief_bg);

        //公司简介文本若不存在于application中，则从网上获取
        if(InitApplication.protext ==null){
            loadBrief();
        }else{
            initAdapter();
        }

    }



    @Override
    protected void initListener() {
       /* IB_click.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BriefActivity.this.finish();
            }
        });*/
    }

    @Override
    protected void initAdapter() {
        tv_about_brief.setText(InitApplication.protext);
    }

    @Override
    protected void initListenerData() {

    }


    /**
     * 从网络获取公司简介文本信息
     */
    private void loadBrief() {

        String url = getRes().getString(R.string.url_root) + getRes().getString(R.string.url_brief);

        VolleyManager.getJson(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                //获取的JSON不为空的话
                if (!TextUtils.isEmpty(jsonObject.toString())) {
                    Map<String, String> strMap = JSON.parseObject(jsonObject.toString(),
                            new TypeReference<LinkedHashMap<String, String>>() {
                            });
                    try {
                        String status = strMap.get("status");
                        if ("success".equals(status)) {
                            JSONObject jsonob = new JSONObject(strMap.get("companyInfo"));
                            InitApplication.protext = jsonob.getString("protext");
                            //加载具体的内容
                            initAdapter();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
     * 按返回键关闭Activity
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BriefActivity.this.finish();
    }
}
