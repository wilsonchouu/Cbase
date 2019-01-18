package com.cbase.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author : zhouyx
 * @date : 2016/4/15
 * @description : 网络状态监听
 * 需要声明权限：
 * <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 */
public class NetworkStateHelper {

    private static final String TAG = NetworkStateHelper.class.getSimpleName();
    private static final NetworkStateHelper ourInstance = new NetworkStateHelper();

    public static NetworkStateHelper getInstance() {
        return ourInstance;
    }

    private NetworkStateHelper() {
    }

    private NetworkStateBroadcast networkStateBroadcast = new NetworkStateBroadcast();
    private final List<OnNetworkStateListener> listeners = Collections.synchronizedList(new LinkedList<OnNetworkStateListener>());
    private boolean isWifiEnable;
    private boolean isWifiConnected;
    @NetworkType
    private int connectType;

    /**
     * 开始监听网络状态广播
     *
     * @param context
     */
    public void start(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        context.getApplicationContext().registerReceiver(networkStateBroadcast, filter);
    }

    /**
     * 停止监听网络状态广播
     *
     * @param context
     */
    public void stop(Context context) {
        context.getApplicationContext().unregisterReceiver(networkStateBroadcast);
    }

    /**
     * 设置监听回调
     *
     * @param onNetworkStateListener
     */
    public void addOnNetworkStateListener(OnNetworkStateListener onNetworkStateListener) {
        if (onNetworkStateListener != null && !listeners.contains(onNetworkStateListener)) {
            listeners.add(onNetworkStateListener);
        }
    }

    /**
     * 移除监听回调
     *
     * @param onNetworkStateListener
     */
    public void removeOnNetworkStateListener(OnNetworkStateListener onNetworkStateListener) {
        if (onNetworkStateListener != null && listeners.contains(onNetworkStateListener)) {
            listeners.remove(onNetworkStateListener);
        }
    }

    /**
     * 获取当前Wifi状态
     *
     * @return true：wifi打开；false：wifi关闭；
     */
    public boolean isWifiEnable() {
        return isWifiEnable;
    }

    /**
     * 获取当前Wifi连接状态
     *
     * @return true：连接上路由；false：未连接上路由；
     */
    public boolean isWifiConnected() {
        return isWifiConnected;
    }

    /**
     * 获取当前网络连接类型
     *
     * @return </p>
     * TYPE_WIFI      : WIFI
     * TYPE_MOBILE    : 移动网络
     * TYPE_NONE      : 无网络
     */
    @NetworkType
    public int getConnectType() {
        return connectType;
    }

    /**
     * 网络状态监听
     */
    private class NetworkStateBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                inConnectivityAction(context);
                return;
            }
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                inWifiStateChangedAction(intent);
                return;
            }
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
                inNetworkStateChangedAction(intent);
            }
        }

        /**
         * 监听WIFI的连接状态，即是否连上了一个有效无线路由，当WifiManager.WIFI_STATE_CHANGED_ACTION广播
         * 的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，不会接到这个广播。
         * 当WifiManager.WIFI_STATE_CHANGED_ACTION广播接到的状态是WifiManager.WIFI_STATE_ENABLED的同时也
         * 会接到这个广播，当然刚打开WIFI肯定还没有连接到有效的无线
         */
        private void inNetworkStateChangedAction(Intent intent) {
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (parcelableExtra == null) {
                return;
            }
            NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
            NetworkInfo.State state = networkInfo.getState();
            // 这边可以更精确的确定WIFI连接上路由的状态
            boolean isConnected = state == NetworkInfo.State.CONNECTED;
            CLogUtils.i(TAG, "isConnected : " + isConnected);
            isWifiConnected = isConnected;
            synchronized (listeners) {
                try {
                    for (OnNetworkStateListener listener : listeners) {
                        if (listener == null) {
                            continue;
                        }
                        listener.onWifiConnectChange(isConnected);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 通知网络状态
         *
         * @param newConnectType
         */
        private void notifyConnectChange(@NetworkType int newConnectType) {
            if (newConnectType == TYPE_NONE) {
                CLogUtils.i(TAG, "当前没有网络连接，请确保你已经打开网络");
            } else if (newConnectType == TYPE_WIFI) {
                CLogUtils.i(TAG, "当前WiFi连接可用");
            } else if (newConnectType == TYPE_MOBILE) {
                CLogUtils.i(TAG, "当前移动网络连接可用");
            }
            connectType = newConnectType;
            synchronized (listeners) {
                try {
                    for (OnNetworkStateListener listener : listeners) {
                        if (listener == null) {
                            continue;
                        }
                        listener.onConnectChange(connectType);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 监听网络连接，包括wifi和移动数据的打开和关闭。
         * 这个广播的最大弊端是比另外两个广播的反应要慢，如果是只监听wifi，用另外两个配合比较合适
         */
        @SuppressLint("MissingPermission")
        private void inConnectivityAction(Context context) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager == null) {
                notifyConnectChange(TYPE_NONE);
                return;
            }
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo == null) {
                notifyConnectChange(TYPE_NONE);
                return;
            }
            CLogUtils.i(TAG, "info.getTypeName()" + networkInfo.getTypeName());
            CLogUtils.i(TAG, "getSubtypeName()" + networkInfo.getSubtypeName());
            CLogUtils.i(TAG, "getState()" + networkInfo.getState());
            CLogUtils.i(TAG, "getDetailedState()" + networkInfo.getDetailedState().name());
            CLogUtils.i(TAG, "getDetailedState()" + networkInfo.getExtraInfo());
            CLogUtils.i(TAG, "getType()" + networkInfo.getType());
            if (!manager.getActiveNetworkInfo().isConnected()) {
                notifyConnectChange(TYPE_NONE);
                return;
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                notifyConnectChange(TYPE_WIFI);
                return;
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                notifyConnectChange(TYPE_MOBILE);
            }
        }

        /**
         * 通知WiFi开关
         *
         * @param enable
         */
        private void notifyWifiEnableChange(boolean enable) {
            synchronized (listeners) {
                try {
                    for (OnNetworkStateListener listener : listeners) {
                        if (listener == null) {
                            continue;
                        }
                        listener.onWifiEnableChange(enable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 监听WIFI关闭、打开状态，与WIFI连接无关
         */
        private void inWifiStateChangedAction(Intent intent) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            CLogUtils.i(TAG, "wifiState : " + wifiState);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    isWifiEnable = false;
                    notifyWifiEnableChange(false);
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    isWifiEnable = true;
                    notifyWifiEnableChange(true);
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    break;
                default:
                    break;
            }
        }

    }

    public interface OnNetworkStateListener {
        /**
         * 回调WIFI关闭、打开状态，与WIFI连接无关
         *
         * @param isEnable true：wifi打开；false：wifi关闭；
         */
        void onWifiEnableChange(boolean isEnable);

        /**
         * 回调WIFI连接路由状态
         *
         * @param isConnected true：连接上路由；false：未连接上路由；
         */
        void onWifiConnectChange(boolean isConnected);

        /**
         * 回调连接类型
         *
         * @param connectType </p>
         *                    TYPE_WIFI      : WIFI
         *                    TYPE_MOBILE    : 移动网络
         *                    TYPE_NONE      : 无网络
         */
        void onConnectChange(@NetworkType int connectType);
    }

    /**
     * 无网络
     */
    public static final int TYPE_NONE = 0;
    /**
     * 移动网络
     */
    public static final int TYPE_MOBILE = 1;
    /**
     * WiFi网络
     */
    public static final int TYPE_WIFI = 2;

    @IntDef({TYPE_NONE, TYPE_MOBILE, TYPE_WIFI})
    @Retention(RetentionPolicy.SOURCE)
    @interface NetworkType {
    }

}
