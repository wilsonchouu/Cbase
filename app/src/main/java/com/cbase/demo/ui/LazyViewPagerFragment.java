package com.cbase.demo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.cbase.adapter.NotifyFragmentPagerAdapter;
import com.cbase.demo.R;
import com.cbase.demo.base.AppFragment;
import com.cbase.utils.CLogUtils;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

/**
 * @author : zhouyx
 * @date : 2017/10/15
 * @description :
 */
public class LazyViewPagerFragment extends AppFragment {

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    public static LazyViewPagerFragment getInstance() {
        return new LazyViewPagerFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_lazy_view_pager;
    }

    @Override
    protected void initFragment(Bundle savedInstanceState, Bundle inputBundle) {
        List<Fragment> fragments = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            fragments.add(LazyFragment.getInstance(i));
        }
        NotifyFragmentPagerAdapter adapter = new NotifyFragmentPagerAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }

    public static class LazyFragment extends AppFragment {

        private int mPosition;

        static LazyFragment getInstance(int position) {
            LazyFragment fragment = new LazyFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        protected int getLayoutId() {
            return R.layout.fragment_status;
        }

        @Override
        protected void initFragment(Bundle savedInstanceState, Bundle inputBundle) {
            if (inputBundle == null) {
                return;
            }
            mPosition = inputBundle.getInt("position");
            CLogUtils.i(TAG, "initFragment position : " + mPosition);
        }

        @Override
        protected void doLazyLoad() {
            super.doLazyLoad();
            CLogUtils.i(TAG, "onLazyLoadData position : " + mPosition);
        }
    }

}
