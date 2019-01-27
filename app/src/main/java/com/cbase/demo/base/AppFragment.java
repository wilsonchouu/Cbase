package com.cbase.demo.base;

import android.view.View;

import com.cbase.base.BaseMvpFragment;
import com.cbase.mvp.BaseMvpPresenter;
import com.cbase.mvp.BaseMvpView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author : zhouyx
 * @date : 2019/1/27
 * @description :
 */
public abstract class AppFragment<V extends BaseMvpView, P extends BaseMvpPresenter> extends BaseMvpFragment<V, P> {

    private Unbinder mUnbinder;

    @Override
    protected void initView(View view) {
        mUnbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}
