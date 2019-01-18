package com.cbase.http;

import com.cbase.utils.CLogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * @author : zhouyx
 * @date : 2017/9/14
 * @description : Log拦截器
 */
public class LogInterceptor implements Interceptor {
    private static final String TAG = LogInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        // 请求log
        Request requestCopy = request.newBuilder().build();
        StringBuilder builder = new StringBuilder();
        builder.append(requestCopy.method());
        builder.append(" ");
        builder.append(requestCopy.url());
        builder.append(" ");
        RequestBody requestBody = requestCopy.body();
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            builder.append(buffer.readUtf8());
        }
        CLogUtils.i(TAG, builder.toString());

        // 返回log
        builder = new StringBuilder();
        long startTime = System.nanoTime();
        Response response = chain.proceed(request);
        long totalTime = (long) ((System.nanoTime() - startTime) / 1e6d);
        builder.append(requestCopy.method());
        builder.append(" ");
        builder.append(requestCopy.url());
        builder.append(" ");
        builder.append(totalTime);
        builder.append("ms ");
        String responseBodyString;
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            responseBodyString = responseBody.string();
            builder.append(responseBodyString);
            CLogUtils.i(TAG, builder.toString());
            return response.newBuilder().body(ResponseBody.create(responseBody.contentType(), responseBodyString.getBytes())).build();
        }
        CLogUtils.i(TAG, builder.toString());
        return response;
    }

}
