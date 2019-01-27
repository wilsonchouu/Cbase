package com.cbase.demo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : zhouyx
 * @date : 2017/10/15
 * @description :
 */
public class ItemBean implements Serializable {

    public static final int TYPE_LIST = 1000;
    public static final int TYPE_STATUS = 1001;
    public static final int TYPE_REQUEST = 1002;
    public static final int TYPE_WEB_VIEW = 1003;
    public static final int TYPE_IMAGE_GALLERY = 1004;
    public static final int TYPE_LAZY = 1005;

    private String name;
    private int type;

    private ItemBean(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public static List<ItemBean> getList() {
        List<ItemBean> data = new ArrayList<>();
        data.add(new ItemBean("状态", TYPE_STATUS));
        data.add(new ItemBean("列表", TYPE_LIST));
        data.add(new ItemBean("网络请求", TYPE_REQUEST));
        data.add(new ItemBean("网页加载", TYPE_WEB_VIEW));
        data.add(new ItemBean("图片墙", TYPE_IMAGE_GALLERY));
        data.add(new ItemBean("懒加载Fragment", TYPE_LAZY));
        return data;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

}
