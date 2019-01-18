package com.cbase.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * @author : zhouyx
 * @date : 2017/9/11
 * @description : Activity管理栈
 */
public class AppManager {

    private static final AppManager ourInstance = new AppManager();

    public static AppManager getInstance() {
        return ourInstance;
    }

    private AppManager() {
    }

    private Stack<Activity> activityStack = new Stack<>();

    /**
     * 添加Activity到栈
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        activityStack.add(activity);
    }

    /**
     * 从栈中移除Activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        activityStack.remove(activity);
    }

    /**
     * 检查栈里是否包含目标Activity
     *
     * @param activityCls
     * @return
     */
    public boolean containActivity(Class<? extends Activity> activityCls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(activityCls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 结束指定Activity
     *
     * @param activityCls
     */
    public void finishActivity(Class<? extends Activity> activityCls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(activityCls)) {
                activity.finish();
            }
        }
    }

}
