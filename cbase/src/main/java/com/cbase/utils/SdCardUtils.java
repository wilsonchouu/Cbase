package com.cbase.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;

/**
 * @author : zhouyx
 * @date : 2016/04/13
 * @description : SD卡工具
 */
public class SdCardUtils {

    /**
     * 检查是否有挂载sd卡
     *
     * @return
     */
    public static boolean checkSdCardExists() {
        String status = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(status);
    }

    /**
     * 获取sd卡路径
     *
     * @return
     */
    public static String getSdCardPath() {
        if (!checkSdCardExists()) {
            return null;
        }
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获取系统相册路径
     *
     * @return
     */
    public static String getDCIMPath() {
        if (!checkSdCardExists()) {
            return null;
        }
        String path = getSdCardPath() + File.separator + "dcim" + File.separator;
        if (new File(path).exists()) {
            return path;
        }
        path = getSdCardPath() + File.separator + "DCIM" + File.separator;
        File file = new File(path);
        if (!file.exists() && !file.mkdir()) {
            return null;
        }
        return path;
    }

    /**
     * 获取DCIM目录下的Camera目录
     *
     * @return
     */
    public static String getDCIMCameraPath() {
        if (!checkSdCardExists()) {
            return null;
        }
        String path = getDCIMPath() + "Camera" + File.separator;
        File file = new File(path);
        if (!file.exists() && !file.mkdir()) {
            return null;
        }
        return path;
    }

    /**
     * 获取app缓存系统目录
     *
     * @param context
     * @return
     */
    public static String getExternalCachePath(Context context) {
        if (!checkSdCardExists()) {
            return null;
        }
        File file = context.getExternalCacheDir();
        if (file == null) {
            return getSdCardPath() + File.separator + "Android" + File.separator + "data" + File.separator
                    + context.getPackageName() + File.separator + "cache" + File.separator;
        } else {
            return file.getAbsolutePath() + File.separator;
        }
    }

    /**
     * 获取应用名称
     */
    private static String getApplicationName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            ApplicationInfo applicationInfo = manager.getApplicationInfo(context.getPackageName(), 0);
            return manager.getApplicationLabel(applicationInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取app sd卡目录
     *
     * @param context
     * @return
     */
    public static String getApplicationSdCachePath(Context context) {
        if (!checkSdCardExists()) {
            return null;
        }
        String path = getSdCardPath() + File.separator + getApplicationName(context) + File.separator;
        File file = new File(path);
        if (!file.exists() && !file.mkdir()) {
            return null;
        }
        return path;
    }

    /**
     * 生成一个存放在相册目录下的图片路径
     *
     * @param suffix .jpg or .png
     * @return
     */
    public static String getDCIMCameraImagePath(String suffix) {
        String path = getDCIMCameraPath();
        if (path == null) {
            return null;
        }
        return path + "IMG_" + System.currentTimeMillis() + suffix;
    }

    /**
     * 生成一个存放在sd卡目录下的图片路径
     *
     * @param context
     * @param suffix  .jpg or .png
     * @return
     */
    public static String getApplicationSdCacheImagePath(Context context, String suffix) {
        String path = getApplicationSdCachePath(context);
        if (path == null) {
            return null;
        }
        return path + "IMG_" + System.currentTimeMillis() + suffix;
    }

}