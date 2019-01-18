package com.cbase.mvp;

import io.reactivex.ObservableTransformer;

/**
 * @author : zhouyx
 * @date : 2017/10/22
 * @description : Base MVP View
 */
public interface BaseMvpView {

    /**
     * RxJava线程切换
     *
     * @param <T>
     * @return
     */
    <T> ObservableTransformer<T, T> applySchedulers();

}
