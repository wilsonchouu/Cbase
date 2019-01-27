package com.cbase.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cbase.R;
import com.cbase.bean.MessageEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author : zhouyx
 * @date : 2017/9/15
 * @description : Fragment基类（需配合BaseActivity使用，建议子类再继承一层进行业务拓展）
 */
public abstract class BaseFragment extends RxFragment {
    protected final String TAG = getClass().getSimpleName();

    /**
     * 视图准备完毕
     */
    private boolean mPrepared;
    /**
     * 视图到前台显示
     */
    private boolean mVisibleToUser;

    protected BaseActivity mActivity;
    private View mRootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mActivity == null && context instanceof BaseActivity) {
            mActivity = (BaseActivity) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = getLayoutId();
        if (layoutId > 0) {
            return setContentView(inflater, layoutId);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 调用该办法可避免重复加载UI
     */
    private View setContentView(LayoutInflater inflater, int resId) {
        if (mRootView == null) {
            mRootView = inflater.inflate(resId, null);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initFragment(savedInstanceState, getArguments());
        if (isRegisterEventBusHere()) {
            EventBus.getDefault().register(this);
        }
        mPrepared = true;
        lazyLoad();
    }

    /**
     * 销毁视图
     */
    @Override
    public void onDestroyView() {
        if (isRegisterEventBusHere()) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyView();
        mRootView = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mVisibleToUser = isVisibleToUser;
        if (mVisibleToUser) {
            lazyLoad();
        }
    }

    /**
     * 判断是否执行懒加载方法
     */
    protected void lazyLoad() {
        if (mPrepared && mVisibleToUser) {
            doLazyLoad();
            mPrepared = false;
            mVisibleToUser = false;
        }
    }

    /**
     * 需要使用懒加载时重写该方法
     */
    protected void doLazyLoad() {

    }

    /**
     * 关闭页面
     */
    public void finish() {
        mActivity.finish();
    }

    /**
     * 带结果及参数内容关闭页面
     *
     * @param intent 传递参数内容
     */
    public void finishResult(Intent intent) {
        mActivity.finishResult(intent);
    }

    /**
     * 带结果关闭页面
     */
    public void finishResult() {
        mActivity.finishResult();
    }

    /**
     * 无动画关闭页面
     */
    public void finishSimple() {
        mActivity.finishSimple();
    }

    /**
     * 打开页面
     *
     * @param bundle 传递参数内容
     * @param target Activity.class
     */
    public void startActivity(Bundle bundle, Class<? extends Activity> target) {
        Intent intent = new Intent(mActivity, target);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        mActivity.overridePendingTransition(R.anim.cbase_slide_left_in, R.anim.cbase_slide_left_out);
    }

    /**
     * 请求结果打开页面
     *
     * @param bundle      传递参数内容
     * @param requestCode 请求码
     * @param target      Activity.class
     */
    public void startActivityForResult(Bundle bundle, int requestCode, Class<? extends Activity> target) {
        Intent intent = new Intent(mActivity, target);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        mActivity.overridePendingTransition(R.anim.cbase_slide_left_in, R.anim.cbase_slide_left_out);
    }

    /**
     * 查找View
     *
     * @param id  View id
     * @param <T> View
     * @return
     */
    protected <T extends View> T findViewById(@IdRes int id) {
        return mRootView.findViewById(id);
    }

    /**
     * RxJava
     * 1. 订阅者与Fragment生命周期绑定
     * 2 .线程调度切换
     *
     * @param <T>
     * @return
     */
    public <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {
            @SuppressWarnings("unchecked")
            @Override
            public ObservableSource<T> apply(@io.reactivex.annotations.NonNull Observable<T> upstream) {
                return (ObservableSource<T>) upstream
                        .compose(bindToLifecycle())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * EventBus 事件接收处理，子类重写该方法 (主线程运行)
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

    }

    /**
     * EventBus 重写该方法并返回true开启EventBus
     */
    protected boolean isRegisterEventBusHere() {
        return false;
    }

    /**
     * 获取布局id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 控件绑定操作（如findViewById 或 ButterKnife注册）
     *
     * @param view 视图
     */
    protected abstract void initView(View view);

    /**
     * 初始化Fragment
     *
     * @param savedInstanceState 保存的参数，可为空
     * @param inputBundle        传递的参数，可为空
     */
    protected abstract void initFragment(@Nullable Bundle savedInstanceState, @Nullable Bundle inputBundle);

}
