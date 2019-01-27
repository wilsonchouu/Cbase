package com.cbase.demo.mvp;

import com.cbase.mvp.BaseMvpView;

/**
 * @author : zhouyx
 * @date : 2017/10/22
 * @description :
 */
public interface RequestView extends BaseMvpView {

    /**
     * 加载
     */
    void showLoading();

    /**
     * 成功
     *
     * @param result 结果
     */
    void onSuccess(String result);

    /**
     * 重试
     */
    void showRetry();

}
