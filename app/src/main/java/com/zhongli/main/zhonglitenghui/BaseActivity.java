package com.zhongli.main.zhonglitenghui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zhongli.main.zhonglitenghui.app.InitApplication;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 278877385 on 2015/2/2.
 */
public abstract class BaseActivity extends ActionBarActivity {

    /**
     * 加载进度条
     */
    private View loadView;

    /**
     * 控件初始化
     */
    abstract protected void initView();

    /**
     * 各种触发事件的监听
     */
    abstract protected void initListener();

    /**
     * 控件适配器
     */
    abstract protected void initAdapter();

    /**
     * 网络数据加载后的监听
     */
    abstract protected void initListenerData();

    /**
     * 获得资源res实例
     *
     * @return
     */
    protected Resources getRes() {
        return getApplicationContext().getResources();
    }

    /**
     * 设置一体化状态栏颜色
     *
     * @param activity
     * @param color    状态栏颜色
     */
    public static void setStatusBarTint(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= 19) {
            setTranslucentStatus(activity, true);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(color);
        }
    }

    /**
     * 返回上一层activity
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            //返回
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(19)
    public static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    protected void showLoadView() {
        loadView = findViewById(R.id.loadview);
        loadView.setVisibility(View.VISIBLE);
    }

    protected void dissMissLoadView() {
        if (loadView != null) {
            loadView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    /**
     * 设置ImageView适配屏幕
     *
     * @param img    装载图片ImageView
     * @param params
     * @param width  图片宽
     * @param height 图片高
     */
    public static void sizeImg(ImageView img, LinearLayout.LayoutParams params, float width, float height) {
        int heightNew = (int) (InitApplication.screenHeight / height * width);
        params = new LinearLayout.LayoutParams(InitApplication.screenHeight, heightNew);
        img.setLayoutParams(params);
    }

    public static void sizeImgAboutUs(ImageView img, LinearLayout.LayoutParams params, float width, float height) {
        int heightNew = (int) ((InitApplication.screenHeight) / height * width);
        params = new LinearLayout.LayoutParams(InitApplication.screenHeight, heightNew);
        params.setMargins(35, 20, 35, 0);
        img.setLayoutParams(params);
    }


    /**
     * 图片流读取
     *
     * @param resId
     * @return
     */
    public Drawable setResId(int resId) {
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
    }
}
