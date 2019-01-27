package com.cbase.demo;

import android.app.Application;

import com.cbase.http.SimpleHttp;
import com.cbase.utils.CLogUtils;
import com.cbase.utils.NetworkStateHelper;

import retrofit2.Retrofit;

/**
 * @author : zhouyx
 * @date : 2017/9/19
 * @description :
 */
public class SampleApp extends Application {

    private static Retrofit sRetrofit;

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkStateHelper.getInstance().start(this);
        NetworkStateHelper.getInstance().addOnNetworkStateListener(new NetworkStateHelper.OnNetworkStateListener() {
            @Override
            public void onWifiEnableChange(boolean isEnable) {
                CLogUtils.i("isEnable: " + isEnable);
            }

            @Override
            public void onWifiConnectChange(boolean isConnected) {
                CLogUtils.i("isConnected: " + isConnected);
            }

            @Override
            public void onConnectChange(int connectType) {
                CLogUtils.i("connectType: " + connectType);
            }
        });
        sRetrofit = SimpleHttp.createRetrofit(this, "http://t.weather.sojson.com/");
    }

    @Override
    public void onTerminate() {
        NetworkStateHelper.getInstance().stop(this);
        super.onTerminate();
    }

    public static Retrofit getRetrofit() {
        return sRetrofit;
    }

}
