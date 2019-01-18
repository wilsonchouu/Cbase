package com.cbase.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author : zhouyx
 * @date : 2017/9/11
 * @description : 解决更改数据集后notifyDataSetChanged()无效的问题，亦可用于懒加载适配器
 */
public class NotifyFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<? extends Fragment> mFragments;

    public NotifyFragmentPagerAdapter(FragmentManager fm, List<? extends Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments == null ? null : mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    /**
     * 重写该方法使destroyItem时detach该Fragment，instantiateItem时attach该Fragment
     *
     * @param object
     * @return
     */
    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    /**
     * 可重写该方法，使用setter方法传值给fragment（不能使用setArguments()，会导致 java.lang.IllegalStateException: Fragment already active）
     * 亦可在fragments获取目标fragment进行操作
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        //do setter to fragment
        return fragment;
    }

}
