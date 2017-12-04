package cn.zinus.warehouse.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.zinus.warehouse.Fragment.KeyDownFragment;

/**
 * Created by Spring on 2017/3/3.
 */


public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<KeyDownFragment> lstFrg = new ArrayList<KeyDownFragment>();
    private List<String> lstTitles = new ArrayList<String>();

    public MyFragmentPagerAdapter(FragmentManager fm, List<KeyDownFragment> fragments, List<String> titles) {
        super(fm);
        lstFrg = fragments;
        lstTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        if (lstFrg.size() > 0) {
            return lstFrg.get(position);
        }
        throw new IllegalStateException("No fragment at position " + position);
    }

    @Override
    public int getCount() {
        return lstFrg.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (lstTitles.size() > 0) {
            return lstTitles.get(position);
        }
        return null;
    }
}
