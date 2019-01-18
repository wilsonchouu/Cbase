package com.cbase.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cbase.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author : zhouyx
 * @date : 2017/9/17
 * @description : 多状态布局 fork from https://github.com/bubula/StatusView
 */
public class StatusLayout extends FrameLayout {
    /**
     * 正常页
     */
    public static final int TYPE_CONTENT = 10000;
    /**
     * 加载页
     */
    public static final int TYPE_LOADING = 10001;
    /**
     * 重新加载页
     */
    public static final int TYPE_RETRY = 10002;
    /**
     * 空数据页
     */
    public static final int TYPE_EMPTY = 10003;
    /**
     * 设置页（一般是无网络状态,需要去设置）
     */
    public static final int TYPE_SETTING = 10004;

    @IntDef({TYPE_CONTENT, TYPE_LOADING, TYPE_RETRY, TYPE_EMPTY, TYPE_SETTING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StatusMode {
    }

    private BaseStatusView mStatusView;
    private View mContentView;
    private View mLoadingView;
    private View mRetryView;
    private View mEmptyView;
    private View mSettingView;

    /**
     * 当前展示状态
     */
    @StatusMode
    private int mStatusMode;

    public static StatusLayout attach(Activity activity, BaseStatusView statusView) {
        return new StatusLayout.Builder()
                .setContentView(activity)
                .setStatusView(statusView)
                .build();
    }

    public static StatusLayout attach(Fragment fragment, BaseStatusView statusView) {
        return new StatusLayout.Builder()
                .setContentView(fragment)
                .setStatusView(statusView)
                .build();
    }

    public static StatusLayout attach(View view, BaseStatusView statusView) {
        return new StatusLayout.Builder()
                .setContentView(view)
                .setStatusView(statusView)
                .build();
    }

    public StatusLayout(Context context) {
        super(context);
    }

    public StatusLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (mContentView == null) {
            mContentView = getChildAt(0);
        }
    }

    private StatusLayout setStatusView(BaseStatusView statusView) {
        mStatusView = statusView;
        if (mStatusView == null) {
            mStatusView = new DefaultStatusView(getContext());
        }
        setLoadingView();
        setRetryView();
        setEmptyView();
        setSettingView();
        showContent();
        return this;
    }

    /**
     * 设置加载页面
     */
    private void setLoadingView() {
        mLoadingView = mStatusView.getLoadingView();
        if (mLoadingView != null) {
            addView(mLoadingView, new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }
    }

    /**
     * 设置重试界面
     */
    private void setRetryView() {
        mRetryView = mStatusView.getRetryView();
        if (mRetryView != null) {
            addView(mRetryView, new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }
    }

    /**
     * 设置空的页面
     */
    private void setEmptyView() {
        mEmptyView = mStatusView.getEmptyView();
        if (mEmptyView != null) {
            addView(mEmptyView, new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }
    }

    /**
     * 设置设置网络界面
     */
    private void setSettingView() {
        mSettingView = mStatusView.getSettingView();
        if (mSettingView != null) {
            addView(mSettingView, new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }
    }

    private void setVisibility(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    /**
     * 重置所有页面
     */
    private void reset() {
        setVisibility(mContentView, View.GONE);
        setVisibility(mLoadingView, View.GONE);
        setVisibility(mRetryView, View.GONE);
        setVisibility(mEmptyView, View.GONE);
        setVisibility(mSettingView, View.GONE);
    }

    /**
     * 展示正常页
     */
    public void showContent() {
        reset();
        setVisibility(mContentView, View.VISIBLE);
        mStatusMode = TYPE_CONTENT;
    }

    /**
     * 展示加载页
     */
    public void showLoading() {
        reset();
        setVisibility(mLoadingView, View.VISIBLE);
        mStatusMode = TYPE_LOADING;
    }

    /**
     * 展示重新加载页
     */
    public void showRetry() {
        reset();
        setVisibility(mRetryView, View.VISIBLE);
        mStatusMode = TYPE_RETRY;
    }

    /**
     * 展示空数据页
     */
    public void showEmpty() {
        reset();
        setVisibility(mEmptyView, View.VISIBLE);
        mStatusMode = TYPE_EMPTY;
    }

    /**
     * 展示设置页
     */
    public void showSetting() {
        reset();
        setVisibility(mSettingView, View.VISIBLE);
        mStatusMode = TYPE_SETTING;
    }

    /**
     * 获取当前状态
     */
    @StatusMode
    public int getCurrentStatusMode() {
        return mStatusMode;
    }

    /**
     * 获取状态页对象
     */
    public BaseStatusView getStatusView() {
        return mStatusView;
    }

    public static final class Builder {
        private ViewGroup parent;
        private int index;
        private BaseStatusView statusView;
        private StatusLayout statusLayout;
        /**
         * 原内容页面
         */
        private View contentView;

        public Builder setContentView(Activity activity) {
            parent = activity.findViewById(android.R.id.content);
            index = 0;
            return this;
        }

        public Builder setContentView(Fragment fragment) {
            View view = fragment.getView();
            if (view != null) {
                parent = (ViewGroup) view.getParent();
            }
            index = 0;
            return this;
        }

        public Builder setContentView(View view) {
            parent = (ViewGroup) view.getParent();
            for (int i = 0; i < parent.getChildCount(); i++) {
                if (parent.getChildAt(i) == view) {
                    index = i;
                    break;
                }
            }
            return this;
        }

        public Builder setStatusView(BaseStatusView statusView) {
            this.statusView = statusView;
            return this;
        }

        public StatusLayout build() {
            View view = parent.getChildAt(index);
            // 父类本身是StatusLayout
            if (parent != null && parent instanceof StatusLayout) {
                statusLayout = (StatusLayout) parent;
            }
            // 内容本身是StatusLayout
            else if (view != null && view instanceof StatusLayout) {
                statusLayout = (StatusLayout) view;
            }
            // 其他情况
            else {
                contentView = view;
                statusLayout = new StatusLayout(parent.getContext());
                replaceContentView();
            }
            statusLayout.setStatusView(statusView);
            return statusLayout;
        }

        /**
         * 替换原始布局
         */
        private void replaceContentView() {
            if (parent == null || contentView == null || statusLayout == null) {
                return;
            }
            parent.removeView(contentView);
            ViewGroup.LayoutParams params = contentView.getLayoutParams();
            statusLayout.addView(contentView);
            parent.addView(statusLayout, index, params);
        }
    }

    /**
     * 状态View接口
     * fork from https://github.com/bubula/StatusView
     */
    public interface BaseStatusView {
        /**
         * 加载页
         *
         * @return
         */
        View getLoadingView();

        /**
         * 重新加载页
         *
         * @return
         */
        View getRetryView();

        /**
         * 空数据页
         *
         * @return
         */
        View getEmptyView();

        /**
         * 设置页（一般是无网络状态,需要去设置）
         *
         * @return
         */
        View getSettingView();
    }

    /**
     * 缺省状态View
     * fork from https://github.com/bubula/StatusView
     */
    public static class DefaultStatusView implements BaseStatusView {
        private LayoutInflater mInflater;

        DefaultStatusView(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getLoadingView() {
            View view = mInflater.inflate(R.layout.cbase_default_status_loading, null);
            AppCompatTextView tvTips = view.findViewById(R.id.tv_tips);
            tvTips.setText("加载中...");
            return view;
        }

        @Override
        public View getRetryView() {
            View view = mInflater.inflate(R.layout.cbase_default_status_loading, null);
            AppCompatTextView tvTips = view.findViewById(R.id.tv_tips);
            ContentLoadingProgressBar pbProgress = view.findViewById(R.id.pb_progress);
            pbProgress.setVisibility(View.GONE);
            tvTips.setText("加载数据错误，请重试");
            return view;
        }

        @Override
        public View getEmptyView() {
            View view = mInflater.inflate(R.layout.cbase_default_status_loading, null);
            AppCompatTextView tvTips = view.findViewById(R.id.tv_tips);
            ContentLoadingProgressBar pbProgress = view.findViewById(R.id.pb_progress);
            pbProgress.setVisibility(View.GONE);
            tvTips.setText("返回数据为空");
            return view;
        }

        @Override
        public View getSettingView() {
            View view = mInflater.inflate(R.layout.cbase_default_status_loading, null);
            AppCompatTextView tvTips = view.findViewById(R.id.tv_tips);
            ContentLoadingProgressBar pbProgress = view.findViewById(R.id.pb_progress);
            pbProgress.setVisibility(View.GONE);
            tvTips.setText("网络错误，设置网络");
            return view;
        }

    }

}
