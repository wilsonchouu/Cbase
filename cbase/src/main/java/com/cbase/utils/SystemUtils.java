package com.cbase.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.File;

/**
 * @author : zhouyx
 * @date : 2017/9/13
 * @description : 系统工具
 * * 需要声明权限：
 * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
 */
public class SystemUtils {

    /**
     * 唯一的设备ID GSM手机的IMEI
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getImei(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager == null) {
            return "";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return manager.getImei();
        } else {
            return manager.getDeviceId();
        }
    }

    /**
     * 唯一的设备ID CDMA手机的 MEID.
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getMeid(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager == null) {
            return "";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return manager.getMeid();
        } else {
            return manager.getDeviceId();
        }
    }

    /**
     * 设备的软件版本号
     */
    @SuppressLint("MissingPermission")
    public static String getDeviceSoftwareVersion(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager == null) {
            return "";
        }
        return manager.getDeviceSoftwareVersion();
    }

    /**
     * 获取手机名称
     */
    public static String getDeviceName() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获取应用版本
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取应用版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取应用名称
     */
    public static String getApplicationName(Context context) {
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
     * 安装APK
     *
     * @param apkPath
     */
    public static void installApk(Context context, String apkPath) {
        File file = new File(apkPath);
        if (!file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 跳转到系统的网络设置界面
     */
    @SuppressLint("ObsoleteSdkInt")
    public static void openNetworkSetting(Context context) {
        Intent intent;
        // 先判断当前系统版本
        // 3.0以上
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        } else {
            intent = new Intent();
            intent.setClassName("com.android.settings", Settings.ACTION_WIFI_SETTINGS);
        }
        if (context instanceof Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

}
