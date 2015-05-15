package com.zhongli.main.zhonglitenghui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;

import com.zhongli.main.zhonglitenghui.fragment.AboutFragment;
import com.zhongli.main.zhonglitenghui.fragment.CaseFragment;
import com.zhongli.main.zhonglitenghui.fragment.ProductFragment;
import com.zhongli.main.zhonglitenghui.fragment.VideoFragment;


/**
 *
 */
public class MainActivity extends BaseActivity {

    /**
     * 左滑动菜单的布局
     */
    private DrawerLayout drawer_layout;

    /**
     * actionbar控件
     */
    private Toolbar tool_bar;

    /**
     * 左滑动菜单的填充器
     */
    private ActionBarDrawerToggle drawerToggle;

    /**
     * 左滑动菜单的按钮容器
     */
    private RadioGroup rg_left_menu;

    /**
     * 初始化Fragment
     */
    private AboutFragment aboutFragment;
    private CaseFragment caseFragment;
    private ProductFragment productFragment;
    private VideoFragment videoFragment;

    /**
     * Fragment管理者
     */
    private FragmentManager fragmentManager;

    /**
     * 提交Fragment
     */
    private FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        initListener();
        initAdapter();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initView() {
        //actionbar
        tool_bar = (Toolbar) findViewById(R.id.tool_bar);
        //加载Fragment管理器
        fragmentManager = getSupportFragmentManager();
        //加载Actionbar设置标题
        int flag = getIntent().getExtras().getInt("CHECKEDID");
        if (tool_bar != null) {
            changeActTitle(flag);
            setSupportActionBar(tool_bar);
        }
        //左滑动菜单
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        rg_left_menu = (RadioGroup) findViewById(R.id.rg_left_menu);
        //加载左侧滑动框
        drawerToggle = new ActionBarDrawerToggle(this, drawer_layout, tool_bar, R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();
        drawer_layout.setDrawerListener(drawerToggle);
        //改变状态栏的颜色
        //BaseActivity.setStatusBarTint(this, getRes().getColor(R.color.title_color));
        rg_left_menu.check(flag);
        transaction = fragmentManager.beginTransaction();
        skipToFragment(flag);
        transaction.commit();
    }

    @Override
    protected void initListener() {
        rg_left_menu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                transaction = fragmentManager.beginTransaction();
                //改变actionbar的标题
                changeActTitle(checkedId);
                hideFragments();
                switch (checkedId) {
                    case R.id.rd_left_home:
                        //跳转到首页
                        MainActivity.this.finish();
                        break;
                    default:
                        //跳转到关于
                        skipToFragment(checkedId);
                        break;
                }
                //关闭左侧滑动菜单
                drawer_layout.closeDrawers();
                //提交事物
                transaction.commit();
            }
        });
    }

    @Override
    protected void initAdapter() {

    }

    @Override
    protected void initListenerData() {

    }

    protected void changeActTitle(int checkedId){
        switch (checkedId) {
            case R.id.rd_left_about:
                tool_bar.setTitle("关于腾晖");
                break;
            case R.id.rd_left_case:
                tool_bar.setTitle("项目案例");
                break;
            case R.id.rd_left_pdu:
                tool_bar.setTitle("产品系列");
                break;
            case R.id.rd_left_video:
                tool_bar.setTitle("视频走廊");
                break;
        }
    }

    /**
     * 根据FirstActivity传过来的状态值确认选中状态
     */
    protected void checkRb(int checkedId) {
        transaction = fragmentManager.beginTransaction();
        rg_left_menu.check(checkedId);
        changeActTitle(checkedId);
        skipToFragment(checkedId);
        transaction.commit();
    }

    /**
     * 根据选中的值确定跳转关系
     * @param checkedId
     */
    protected void skipToFragment(int checkedId) {
        if (transaction != null) {
            switch (checkedId) {
                case R.id.rd_left_about:
                    if (aboutFragment != null) {
                        transaction.show(aboutFragment);
                    } else {
                        aboutFragment = new AboutFragment();
                        transaction.add(R.id.fra_main_view, aboutFragment);
                    }
                    break;
                case R.id.rd_left_case:
                    if (caseFragment != null) {
                        transaction.show(caseFragment);
                    } else {
                        caseFragment = new CaseFragment();
                        transaction.add(R.id.fra_main_view, caseFragment);
                    }
                    break;
                case R.id.rd_left_pdu:
                    if (productFragment != null) {
                        transaction.show(productFragment);
                    } else {
                        productFragment = new ProductFragment();
                        transaction.add(R.id.fra_main_view, productFragment);
                    }
                    break;
                case R.id.rd_left_video:
                    if (videoFragment != null) {
                        transaction.show(videoFragment);
                    } else {
                        videoFragment = new VideoFragment();
                        transaction.add(R.id.fra_main_view, videoFragment);
                    }
                    break;
            }
        }
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     */
    protected void hideFragments() {
        if (aboutFragment != null) {
            transaction.hide(aboutFragment);
        }
        if (caseFragment != null) {
            transaction.hide(caseFragment);
        }
        if (productFragment != null) {
            transaction.hide(productFragment);
        }
        if (videoFragment != null) {
            transaction.hide(videoFragment);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
