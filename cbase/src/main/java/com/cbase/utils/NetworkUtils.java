package com.cbase.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author : zhouyx
 * @date : 2016/4/14
 * @description : 网络状态工具
 * * 需要声明权限：
 * <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 */
public class NetworkUtils {

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static boolean checkNetworkConnected(Context context) {
        // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        // 获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        // 判断NetworkInfo对象是否为空
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    /**
     * 判断MOBILE网络是否可用
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static boolean checkMobileConnected(Context context) {
        //获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        //获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        // 判断NetworkInfo对象是否为空 并且类型是否为MOBILE
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && networkInfo.isAvailable();
    }

    /**
     * 判断WIFI网络是否可用
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static boolean checkWifiConnected(Context context) {
        // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        // 获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        // 判断NetworkInfo对象是否为空 并且类型是否为WIFI
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isAvailable();
    }

    /**
     * 判断定位服务是否可用
     *
     * @param context
     * @return
     */
    public static boolean checkLocationConnected(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (manager == null) {
            return false;
        }
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;
    }

    /**
     * 获取当前网络连接的类型信息
     *
     * @param context
     * @return {@link ConnectivityManager#TYPE_WIFI} {@link ConnectivityManager#TYPE_MOBILE} etc.
     */
    @SuppressLint("MissingPermission")
    public static int getConnectedType(Context context) {
        //获取手机所有连接管理对象
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return -1;
        }
        //获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            //返回NetworkInfo的类型
            return networkInfo.getType();
        }
        return -1;
    }

    /**
     * 获取当前的网络状态 ：没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static int getAPNType(Context context) {
        //结果返回值
        int netType = 0;
        //获取手机所有连接管理对象
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return netType;
        }
        //获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //NetworkInfo对象为空 则代表没有网络
        if (networkInfo == null) {
            return netType;
        }
        //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            //WIFI
            netType = 1;
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            // 4G
            if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                    && telephonyManager != null
                    && !telephonyManager.isNetworkRoaming()) {
                netType = 4;
            }
            // 3G  联通的3G为UMTS或HSDPA 电信的3G为EVDO
            else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_A
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSUPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_B
                    || nSubType == TelephonyManager.NETWORK_TYPE_EHRPD
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSPAP
                    && telephonyManager != null
                    && !telephonyManager.isNetworkRoaming()) {
                netType = 3;
            }
            // 2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
            else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                    || nSubType == TelephonyManager.NETWORK_TYPE_1xRTT
                    || nSubType == TelephonyManager.NETWORK_TYPE_IDEN
                    && telephonyManager != null
                    && !telephonyManager.isNetworkRoaming()) {
                netType = 2;
            } else {
                String subTypeName = networkInfo.getSubtypeName();
                //中国移动、联通、电信三种3G制式
                if ("TD-SCDMA".equalsIgnoreCase(subTypeName)
                        || "WCDMA".equalsIgnoreCase(subTypeName)
                        || "CDMA2000".equalsIgnoreCase(subTypeName)) {
                    netType = 3;
                } else if ("FDD-LTE".equalsIgnoreCase(subTypeName)) {
                    netType = 4;
                } else {
                    netType = 2;
                }
            }
        }
        return netType;
    }

    /**
     * 获得本机ip地址
     *
     * @return
     */
    public static String getHostIp() {
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                NetworkInterface element = enumeration.nextElement();
                Enumeration<InetAddress> ipAddress = element.getInetAddresses();
                while (ipAddress.hasMoreElements()) {
                    InetAddress inetAddress = ipAddress.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断是否有外网连接
     *
     * @return
     */
    public static boolean ping() {
        try {
            String ip = "www.qq.com";
            // ping网址3次
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);
            // 读取ping的内容，可以去除
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuilder builder = new StringBuilder();
            String content;
            while ((content = in.readLine()) != null) {
                builder.append(content);
            }
            CLogUtils.i("---ping---", "result content : " + builder.toString());
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                return true;
            }
        } catch (Exception e) {
            CLogUtils.i("---ping---", "exception : " + e.toString());
        }
        return false;
    }

}
