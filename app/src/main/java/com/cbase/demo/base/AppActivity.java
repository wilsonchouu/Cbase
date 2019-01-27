package com.cbase.demo.base;

import com.cbase.base.BaseMvpActivity;
import com.cbase.mvp.BaseMvpPresenter;
import com.cbase.mvp.BaseMvpView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author : zhouyx
 * @date : 2019/1/27
 * @description :
 */
public abstract class AppActivity<V extends BaseMvpView, P extends BaseMvpPresenter> extends BaseMvpActivity<V, P> {

    private Unbinder mUnbinder;

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

}
