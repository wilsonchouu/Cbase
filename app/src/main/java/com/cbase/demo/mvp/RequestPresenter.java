package com.cbase.demo.mvp;

import com.cbase.demo.SampleApp;
import com.cbase.demo.model.AppResponse;
import com.cbase.demo.model.WeatherBean;
import com.cbase.http.SimpleCallback;
import com.cbase.mvp.BaseMvpPresenter;
import com.cbase.utils.CLogUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function3;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author : zhouyx
 * @date : 2017/10/22
 * @description : 测试请求
 */
public class RequestPresenter extends BaseMvpPresenter<RequestView> {
    private static final String TAG = RequestPresenter.class.getSimpleName();

    /**
     * 测试单请求
     */
    public void single(int cityCode) {
        getView().showLoading();
        SampleApp.getRetrofit().create(WeatherApi.class).weather(cityCode)
                .compose(this.<AppResponse<WeatherBean>>applySchedulers())
                .subscribe(new SimpleCallback<WeatherBean>() {
                    @Override
                    public void success(WeatherBean response) {
                        getView().onSuccess(new Gson().toJson(response));
                    }

                    @Override
                    public void fail(int errorCode, String errorMessage) {
                        CLogUtils.i(TAG, errorCode + " : " + errorMessage);
                        getView().showRetry();
                    }
                });
    }

    /**
     * 测试多重请求
     */
    public void multi(int cityCode) {
        getView().showLoading();
        WeatherApi api = SampleApp.getRetrofit().create(WeatherApi.class);
        Observable.zip(api.weather(cityCode), api.weather(cityCode), api.weather(cityCode), new Function3<AppResponse<WeatherBean>, AppResponse<WeatherBean>, AppResponse<WeatherBean>, List<AppResponse>>() {
            @Override
            public List<AppResponse> apply(@NonNull AppResponse<WeatherBean> response1, @NonNull AppResponse<WeatherBean> response2, @NonNull AppResponse<WeatherBean> response3) throws Exception {
                List<AppResponse> linkedList = new ArrayList<>();
                linkedList.add(response1);
                linkedList.add(response2);
                linkedList.add(response3);
                return linkedList;
            }
        })
                .compose(this.<List<AppResponse>>applySchedulers())
                .delaySubscription(1, TimeUnit.SECONDS)
                .subscribe(new Observer<List<AppResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<AppResponse> objects) {
                        StringBuilder result = new StringBuilder();
                        for (AppResponse object : objects) {
                            result.append(new Gson().toJson(object));
                        }
                        getView().onSuccess(result.toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().showRetry();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 应该在另外的地方定义
     */
    public interface WeatherApi {
        /**
         * 获取数据
         *
         * @param cityCode
         * @return
         */
        @GET("api/weather/city/{city_code}")
        Observable<AppResponse<WeatherBean>> weather(@Path("city_code") int cityCode);
    }

}
