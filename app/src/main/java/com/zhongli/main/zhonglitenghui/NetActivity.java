package com.zhongli.main.zhonglitenghui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.zhongli.main.zhonglitenghui.custom.TouchNoNetImageView;

/**
 * Created by KIM on 2015/3/18 0018.
 */
public class NetActivity extends BaseActivity {
    /**
     * 显示销售网络图控件TouchNoNetImageView
     */
    private TouchNoNetImageView iv_about_net;

    /**
     * 关闭按钮
     */
    private ImageButton IB_click;
    private LinearLayout.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_net);
        initView();
        initListener();
    }

    @Override
    protected void initView() {
        iv_about_net = (TouchNoNetImageView) findViewById(R.id.iv_about_net);
        //sizeImg(iv_about_net,params,530f,998f);
        iv_about_net.setImageDrawable(setResId(R.drawable.net_bg));
        IB_click = (ImageButton) findViewById(R.id.click_iv);
    }

    @Override
    protected void initListener() {
        //关闭Activity
        IB_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetActivity.this.finish();
            }
        });
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

    /*protected void sizeImg(ImageView img) {
        int width = (int) (InitApplication.screenWidth / 530f * 998);
        params = new LinearLayout.LayoutParams(width, InitApplication.screenWidth);
        img.setLayoutParams(params);
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
