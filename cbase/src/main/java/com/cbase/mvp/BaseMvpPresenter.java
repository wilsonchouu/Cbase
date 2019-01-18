package com.cbase.mvp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import io.reactivex.ObservableTransformer;

/**
 * @author : zhouyx
 * @date : 2017/10/22
 * @description : Base MVP Presenter
 */
public class BaseMvpPresenter<V extends BaseMvpView> {
    /**
     * 连接P和V的接口
     */
    private V mView;
    private V mProxyView;

    /**
     * 关联V层和P层
     *
     * @param view
     */
    @SuppressWarnings("unchecked")
    public void attachView(V view) {
        mView = view;
        MvpHandler handler = new MvpHandler(view);
        mProxyView = (V) Proxy.newProxyInstance(view.getClass().getClassLoader(), view.getClass().getInterfaces(), handler);
    }

    /**
     * @return 获取V层
     */
    public V getView() {
        return mProxyView;
    }

    /**
     * 非反射模式关联V层和P层
     * 用于页面多V层和P层引用，该情况十分少概率发生，尽量不使用该方法，关联V层和P层应使用{@link BaseMvpPresenter#attachView}
     *
     * @param view
     */
    public void attachViewNoReflect(V view) {
        mView = view;
    }

    /**
     * @return 非反射模式获取V层
     * 用于页面多V层和P层引用，该情况十分少概率发生，尽量不使用该方法，获取V层应使用{@link BaseMvpPresenter#getView}
     */
    public V getViewNoReflect() {
        return mView;
    }

    /**
     * 断开V层和P层
     */
    public void detachView() {
        mView = null;
    }

    /**
     * @return V层和P层是否关联
     */
    public boolean isViewAttached() {
        return mView != null;
    }

    /**
     * V层和P层反射关联
     */
    private class MvpHandler implements InvocationHandler {
        private BaseMvpView mMvpView;

        MvpHandler(BaseMvpView mvpView) {
            this.mMvpView = mvpView;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 如果V层没被销毁, 执行V层的方法.
            if (isViewAttached()) {
                try {
                    return method.invoke(mMvpView, args);
                } catch (Exception e) {
                    throw e.getCause();
                }
            }
            // P层不需要关注V层的返回值
            return null;
        }
    }

    protected <T> ObservableTransformer<T, T> applySchedulers() {
        return getView().applySchedulers();
    }

}
