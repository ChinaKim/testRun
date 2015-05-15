package com.zhongli.main.zhonglitenghui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zhongli.main.zhonglitenghui.bean.Type;
import com.zhongli.main.zhonglitenghui.fragment.InCaseFragment;

import java.util.List;

/**
 * Created by 278877385 on 2015/3/16.
 */
public class CaseViewAdapter extends FragmentPagerAdapter {

    /**
     * 标题
     */
    private String titles[];
    private List<Type> tList;

    public CaseViewAdapter(FragmentManager fm, String[] titles, List<Type> tList) {
        super(fm);
        this.titles = titles;
        this.tList = tList;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new InCaseFragment(tList.get(0).getId());
        } else {
            return new InCaseFragment(tList.get(1).getId());
        }
    }
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
