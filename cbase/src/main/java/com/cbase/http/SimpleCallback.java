package com.cbase.http;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author : zhouyx
 * @date : 2017/9/15
 * @description : 简单请求回调
 */
public abstract class SimpleCallback<T> implements Observer<Response<T>> {

    /**
     * 网络请求失败
     */
    public static final int CODE_REQUEST_FAIL = -1;

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Response<T> tResponse) {
        try {
            if (tResponse.isOk()) {
                success(tResponse.getBody());
            } else {
                fail(tResponse.getStatus(), tResponse.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable t) {
        try {
            fail(CODE_REQUEST_FAIL, t.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete() {

    }

    /**
     * 请求成功回调
     *
     * @param response
     */
    public abstract void success(T response);

    /**
     * 请求失败回调
     *
     * @param errorCode
     * @param errorMessage
     */
    public abstract void fail(int errorCode, String errorMessage);

}
