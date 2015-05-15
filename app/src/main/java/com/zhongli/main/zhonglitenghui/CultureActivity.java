package com.zhongli.main.zhonglitenghui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongli.main.zhonglitenghui.adapter.CultureAdapter;
import com.zhongli.main.zhonglitenghui.app.InitApplication;

/**
 * Created by KIM on 2015/3/18 0018.
 */
public class CultureActivity extends BaseActivity {
    /**
     * actionbar
     */
    private Toolbar toolbar;


    /**
     * ViewPager控件
     */
    private ViewPager vp_culture;

    /**
     * Adapter控件
     */
    private CultureAdapter cultureAdapter;

    /**
     * 图片数组
     */
    private int[] cultureArray = {R.drawable.culture1, R.drawable.culture2};

    private Drawable[] drawable;

    /**
     * 企业文化ImageView控件
     */
    private ImageView iv_culture1;
    private ImageView iv_culture2;
    private LinearLayout.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about_culture);
        //去掉Activity上面的状态栏
        //getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        initView();
        initListener();
    }



    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        //设置title
        if (toolbar != null) {
            toolbar.setTitle("企业文化");
            setSupportActionBar(toolbar);
            //添加返回按钮
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //改变状态栏的颜色
        //BaseActivity.setStatusBarTint(this, getRes().getColor(R.color.title_color));

        /*vp_culture = (ViewPager) findViewById(R.id.vp_culture);
         drawable = new Drawable[2];
        for (int i = 0; i < cultureArray.length; i++) {
            drawable[i] = setResId(cultureArray[i]);
        }

       cultureAdapter = new CultureAdapter(this, drawable);
        vp_culture.setAdapter(cultureAdapter);*/

        iv_culture1 = (ImageView)findViewById(R.id.iv_culture1);
        iv_culture2 = (ImageView)findViewById(R.id.iv_culture2);
        //sizeImg(iv_culture1,params,720f,1280f);
        // sizeImg(iv_culture2,params,720f,1280f);

        params = new LinearLayout.LayoutParams(InitApplication.screenWidth,InitApplication.screenWidth*1280/720);
        iv_culture1.setLayoutParams(params);
        iv_culture2.setLayoutParams(params);
        iv_culture1.setImageDrawable(setResId(R.drawable.culture1));
        iv_culture2.setImageDrawable(setResId(R.drawable.culture2));
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initAdapter() {

    }

    @Override
    protected void initListenerData() {

    }



    /*protected Drawable setResId(int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        InputStream is = getRes().openRawResource(resId);

        Bitmap bitmap = BitmapFactory.decodeStream(is);

        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BitmapDrawable(getRes(), bitmap);
    }*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CultureActivity.this.finish();
    }
}
