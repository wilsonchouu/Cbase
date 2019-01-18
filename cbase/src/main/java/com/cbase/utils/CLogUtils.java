package com.cbase.utils;

import android.util.Log;

/**
 * @author : zhouyx
 * @date : 2019/1/12
 * @description : Log工具类
 */
public class CLogUtils {
    private static final String DEFAULT_TAG = CLogUtils.class.getSimpleName();
    private static final String DEFAULT_MSG = "EMPTY!";
    private static boolean sDebug = true;

    public static void setDebug(boolean debug) {
        sDebug = debug;
    }

    private static String checkTag(String tag) {
        if (tag == null || "".equals(tag) || "null".equals(tag)) {
            return DEFAULT_TAG;
        }
        return tag;
    }

    private static String checkMsg(String msg) {
        if (msg == null || "".equals(msg) || "null".equals(msg)) {
            return DEFAULT_MSG;
        }
        return msg;
    }

    public static void e(String tag, String msg) {
        if (!sDebug) {
            return;
        }
        tag = checkTag(tag);
        msg = checkMsg(msg);
        Log.e(tag, msg);
    }

    public static void e(String msg) {
        e("", msg);
    }

    public static void d(String tag, String msg) {
        if (!sDebug) {
            return;
        }
        tag = checkTag(tag);
        msg = checkMsg(msg);
        Log.d(tag, msg);
    }

    public static void d(String msg) {
        d("", msg);
    }

    public static void i(String tag, String msg) {
        if (!sDebug) {
            return;
        }
        tag = checkTag(tag);
        msg = checkMsg(msg);
        Log.i(tag, msg);
    }

    public static void i(String msg) {
        i("", msg);
    }

    public static void w(String tag, String msg) {
        if (!sDebug) {
            return;
        }
        tag = checkTag(tag);
        msg = checkMsg(msg);
        Log.w(tag, msg);
    }

    public static void w(String msg) {
        w("", msg);
    }

    public static void v(String tag, String msg) {
        if (!sDebug) {
            return;
        }
        tag = checkTag(tag);
        msg = checkMsg(msg);
        Log.v(tag, msg);
    }

    public static void v(String msg) {
        v("", msg);
    }

}
