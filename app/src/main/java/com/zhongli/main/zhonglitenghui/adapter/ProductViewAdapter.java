package com.zhongli.main.zhonglitenghui.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.zhongli.main.zhonglitenghui.bean.Type;
import com.zhongli.main.zhonglitenghui.fragment.InproductFragment;

import java.util.List;

/**
 * Created by KIM on 2015/3/23 0023.
 */
public  class ProductViewAdapter extends FragmentStatePagerAdapter {

    /**
     * 标题
     */
    private String titles[];
    private List<Type> tList;
    public ProductViewAdapter(FragmentManager fm,String[] titles, List<Type> tList) {
        super(fm);
        this.titles = titles;
        this.tList = tList;
    }

    @Override
    public Fragment getItem(int position) {

        Log.i("KIMI","positin:"+position +"id:"+tList.get(position).getId());

        switch (position){
            case 0:
                return new InproductFragment(tList.get(0).getId());

            case 1:
                return new InproductFragment(tList.get(1).getId());

            case 2:
                return new InproductFragment(tList.get(2).getId());
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return titles.length;
    }

    public CharSequence getPageTitle(int position){
        return titles[position];
    }

}
