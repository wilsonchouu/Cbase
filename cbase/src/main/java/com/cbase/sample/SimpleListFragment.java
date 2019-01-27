package com.cbase.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cbase.R;
import com.cbase.base.BaseFragment;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author : zhouyx
 * @date : 2017/9/17
 * @description : 列表页Fragment
 */
public abstract class SimpleListFragment extends BaseFragment {

    protected BGARefreshLayout mBGARefreshLayout;
    protected RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.cbase_fragment_list_view;
    }

    @Override
    protected void initView(View view) {
        mBGARefreshLayout = findViewById(R.id.refresh_layout);
        mRecyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected void initFragment(@Nullable Bundle savedInstanceState, @Nullable Bundle inputBundle) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(getAdapter());
        mBGARefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                onPullDown();
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                return onPullUp();
            }
        });
    }

    /**
     * 获取适配器
     *
     * @return
     */
    protected abstract RecyclerView.Adapter getAdapter();

    /**
     * 下拉加载
     */
    protected abstract void onPullDown();

    /**
     * 上拉加载更多
     *
     * @return 返回true，显示正在加载更多；返回false，不显示正在加载更多
     */
    protected abstract boolean onPullUp();

}
