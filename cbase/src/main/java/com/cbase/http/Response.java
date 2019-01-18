package com.cbase.http;

/**
 * @author : zhouyx
 * @date : 2017/9/15
 * @description : 返回数据体封装
 */
public interface Response<T> {

    /**
     * 返回数据体内容主体
     *
     * @return
     */
    T getBody();

    /**
     * 返回数据体状态码
     *
     * @return
     */
    int getStatus();

    /**
     * 返回数据体信息
     *
     * @return
     */
    String getMessage();

    /**
     * 返回数据体成功状态码，如status==200
     *
     * @return
     */
    boolean isOk();

}
