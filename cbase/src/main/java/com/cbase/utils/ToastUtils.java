package com.cbase.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * @author : zhouyx
 * @date : 2017/9/14
 * @description : Toast工具
 */
public class ToastUtils {

    private static Handler mMainHandler = new Handler(Looper.getMainLooper());
    private static Toast mToast;

    private ToastUtils() {
    }

    public static void showToast(final Context context, final String message, final int duration) {
        mMainHandler.post(new Runnable() {
            @SuppressLint("ShowToast")
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(context, message, duration);
                } else {
                    mToast.setText(message);
                    mToast.setDuration(duration);
                }
                mToast.show();
            }
        });
    }

    public static void showToast(Context context, int resourceId, int duration) {
        showToast(context, context.getResources().getString(resourceId), duration);
    }

    public static void showShort(Context context, int resourceId) {
        showToast(context, resourceId, Toast.LENGTH_SHORT);
    }

    public static void showShort(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void showLong(Context context, int resourceId) {
        showToast(context, resourceId, Toast.LENGTH_LONG);
    }

    public static void showLong(Context context, String message) {
        showToast(context, message, Toast.LENGTH_LONG);
    }

}
