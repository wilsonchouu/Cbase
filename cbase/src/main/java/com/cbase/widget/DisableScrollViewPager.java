package com.cbase.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author : zhouyx
 * @date : 2017/5/26
 * @description : 不可滑动的ViewPager
 */
public class DisableScrollViewPager extends ViewPager {

    private boolean mDisableScroll = false;

    public DisableScrollViewPager(Context context) {
        super(context);
    }

    public DisableScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDisableScroll(boolean disableScroll) {
        this.mDisableScroll = disableScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (mDisableScroll) {
            return false;
        } else {
            return super.onTouchEvent(arg0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (mDisableScroll) {
            return false;
        } else {
            return super.onInterceptTouchEvent(arg0);
        }
    }

}
