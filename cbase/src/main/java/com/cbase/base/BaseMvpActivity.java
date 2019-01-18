package com.cbase.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cbase.mvp.BaseMvpPresenter;
import com.cbase.mvp.BaseMvpView;

/**
 * @author : zhouyx
 * @date : 2018/7/2
 * @description : MVP Activity基类（建议子类再继承一层进行业务拓展）
 */
public abstract class BaseMvpActivity<V extends BaseMvpView, P extends BaseMvpPresenter> extends BaseActivity {

    private P mMvpPresenter;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        V mvpView = bindMvpView();
        if (mMvpPresenter == null) {
            mMvpPresenter = bindMvpPresenter();
        }
        if (mMvpPresenter != null && mvpView != null) {
            mMvpPresenter.attachView(mvpView);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMvpPresenter != null) {
            mMvpPresenter.detachView();
            mMvpPresenter = null;
        }
    }

    /**
     * 绑定View
     * （需要使用MVP模式则重写该方法及{@link BaseMvpActivity#bindMvpPresenter}方法）
     *
     * @return
     */
    public V bindMvpView() {
        return null;
    }

    /**
     * 绑定Presenter
     * （需要使用MVP模式则重写该方法及{@link BaseMvpActivity#bindMvpView}方法）
     *
     * @return
     */
    public P bindMvpPresenter() {
        return null;
    }

    /**
     * @return 获取Presenter
     */
    public P getMvpPresenter() {
        return mMvpPresenter;
    }

}
