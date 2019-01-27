package com.cbase.demo.model;

import com.cbase.http.Response;

/**
 * @author : zhouyx
 * @date : 2017/10/22
 * @description :
 */
public class AppResponse<T> implements Response<T> {

    private String time;
    private String date;
    private int status;
    private String message;
    private T data;

    @Override
    public T getBody() {
        return data;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public boolean isOk() {
        return status == 200;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
