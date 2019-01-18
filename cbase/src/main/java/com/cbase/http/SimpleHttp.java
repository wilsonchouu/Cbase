package com.cbase.http;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author : zhouyx
 * @date : 2017/9/15
 * @description : 简单创建Retrofit
 */
public class SimpleHttp {

    /**
     * 简单创建Retrofit
     *
     * @param context
     * @param baseUrl
     * @return
     */
    public static Retrofit createRetrofit(@NonNull Context context, @NonNull String baseUrl) {
        File cacheDirectory = new File(context.getApplicationContext().getCacheDir(),
                context.getApplicationContext().getPackageName());
        // 10 * 1024 * 1024 = 10485760 = 10 MB
        int cacheSize = 10485760;
        Cache cache = new Cache(cacheDirectory, cacheSize);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new LogInterceptor());

        return new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

}
