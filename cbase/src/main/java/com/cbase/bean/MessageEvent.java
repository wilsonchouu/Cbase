package com.cbase.bean;

/**
 * @author : zhouyx
 * @date : 2017/9/15
 * @description : EventBus统一处理对象
 */
public class MessageEvent {

    private int eventCode = -1;
    private Object eventObj;

    public MessageEvent(int eventCode) {
        this(eventCode, null);
    }

    public MessageEvent(int eventCode, Object eventObj) {
        this.eventCode = eventCode;
        this.eventObj = eventObj;
    }

    public int getEventCode() {
        return eventCode;
    }

    public Object getEventObj() {
        return eventObj;
    }

}
