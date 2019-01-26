package com.cbase.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.cbase.R;
import com.cbase.bean.MessageEvent;
import com.cbase.utils.AppManager;
import com.cbase.utils.ScreenUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * @author : zhouyx
 * @date : 2017/9/14
 * @description : Activity基类（建议子类再继承一层进行业务拓展）
 */
public abstract class BaseActivity extends RxAppCompatActivity {
    protected final String TAG = getClass().getSimpleName();

    protected Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        int layoutId = getLayoutId();
        if (layoutId > 0) {
            setContentView(layoutId);
        }
        mUnbinder = ButterKnife.bind(this);
        initActivity(savedInstanceState, getIntent().getExtras());
        if (isRegisterEventBusHere()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (isRegisterEventBusHere()) {
            EventBus.getDefault().unregister(this);
        }
        AppManager.getInstance().removeActivity(this);
        super.onDestroy();
        mUnbinder.unbind();
    }

    /**
     * 关闭页面
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.cbase_slide_right_in, R.anim.cbase_slide_right_out);
    }

    /**
     * 带结果及参数内容关闭页面
     *
     * @param intent 传递参数内容
     */
    public void finishResult(Intent intent) {
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }

    /**
     * 带结果关闭页面
     */
    public void finishResult() {
        setResult(Activity.RESULT_OK);
        this.finish();
    }

    /**
     * 无动画关闭页面
     */
    public void finishSimple() {
        super.finish();
    }

    /**
     * 打开页面
     *
     * @param bundle 传递参数内容
     * @param target Activity.class
     */
    public void startActivity(Bundle bundle, Class<? extends Activity> target) {
        Intent intent = new Intent(this, target);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.cbase_slide_left_in, R.anim.cbase_slide_left_out);
    }

    /**
     * 请求结果打开页面
     *
     * @param bundle      传递参数内容
     * @param requestCode 请求码
     * @param target      Activity.class
     */
    public void startActivityForResult(Bundle bundle, int requestCode, Class<? extends Activity> target) {
        Intent intent = new Intent(this, target);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.cbase_slide_left_in, R.anim.cbase_slide_left_out);
    }

    /**
     * 初始化Toolbar
     *
     * @param toolbar        toolbar
     * @param title          标题
     * @param navigationIcon 回退键图标
     */
    protected void setToolbar(Toolbar toolbar, String title, int navigationIcon) {
        if (toolbar == null) {
            return;
        }
        setSupportActionBar(toolbar);
        if (getSupportActionBar() == null) {
            return;
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (navigationIcon > 0) {
            toolbar.setNavigationIcon(navigationIcon);
        }
        AppCompatTextView titleTv = toolbar.findViewById(R.id.tool_bar_title);
        if (titleTv != null) {
            titleTv.setText(title);
        }
        toolbar.setMinimumWidth(ScreenUtils.getScreenWidth(this));
    }

    /**
     * 初始化Toolbar
     *
     * @param toolbar toolbar
     * @param title   标题
     */
    protected void setToolbar(Toolbar toolbar, String title) {
        setToolbar(toolbar, title, 0);
    }

    /**
     * 初始化无回退Toolbar
     *
     * @param toolbar toolbar
     * @param title   标题
     */
    protected void setUnBackToolBar(Toolbar toolbar, String title) {
        if (toolbar == null) {
            return;
        }
        setSupportActionBar(toolbar);
        if (getSupportActionBar() == null) {
            return;
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        AppCompatTextView titleTv = toolbar.findViewById(R.id.tool_bar_title);
        if (titleTv != null) {
            titleTv.setText(title);
        }
        toolbar.setMinimumWidth(ScreenUtils.getScreenWidth(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * RxJava
     * 1. 订阅者与Activity生命周期绑定
     * 2 .线程调度切换
     *
     * @param <T>
     * @return
     */
    public <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {
            @SuppressWarnings("unchecked")
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
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
     * EventBus 重写该方法并返回true注册EventBus
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
     * 初始化Activity
     *
     * @param savedInstanceState 保存的参数，可为空
     * @param inputBundle        传递的参数，可为空
     */
    protected abstract void initActivity(@Nullable Bundle savedInstanceState, @Nullable Bundle inputBundle);

}
