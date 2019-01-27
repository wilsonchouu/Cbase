package com.cbase.sample;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cbase.R;
import com.cbase.base.BaseFragment;
import com.cbase.widget.ProgressWebView;

import java.util.regex.Pattern;

/**
 * @author : zhouyx
 * @date : 2017/9/15
 * @description : 加载网页Fragment
 */
public class WebViewFragment extends BaseFragment {

    private static final String WEB_CACHE_DIR = "/webCache";

    private static final int TYPE_LOAD_URL = 1000;
    private static final int TYPE_LOAD_HTML = 2000;

    protected ProgressWebView mWebView;
    protected int mLoadType;
    protected String mContent;

    /**
     * 加载url
     *
     * @param url
     * @return
     */
    public static WebViewFragment getUrlInstance(String url) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("content", url);
        bundle.putInt("loadType", TYPE_LOAD_URL);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 加载html
     *
     * @param html
     * @return
     */
    public static WebViewFragment getHtmlInstance(String html) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("content", html);
        bundle.putInt("loadType", TYPE_LOAD_HTML);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.cbase_fragment_web_view;
    }

    @Override
    protected void initView(View view) {
        mWebView = findViewById(R.id.web_view);
    }

    @Override
    protected void initFragment(Bundle savedInstanceState, Bundle inputBundle) {
        if (inputBundle == null) {
            return;
        }
        mContent = inputBundle.getString("content", "");
        mLoadType = inputBundle.getInt("loadType", TYPE_LOAD_URL);

        initWebView();
        loadWebView();
    }

    /**
     * 初始化WebView
     */
    @SuppressLint("SetJavaScriptEnabled")
    protected void initWebView() {
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // 禁用选择，复制功能
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View arg0) {
                if (mWebView != null) {
                    WebView.HitTestResult hit = mWebView.getHitTestResult();
                    // 只启用输入框的长按功能
                    if (hit != null && hit.getType() == WebView.HitTestResult.EDIT_TEXT_TYPE) {
                        return false;
                    }
                }
                return true;
            }
        });

        WebSettings settings = mWebView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        try {
            // fixed: 联想手机会初始化语音，NullPointerException
            settings.setJavaScriptEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        settings.setBuiltInZoomControls(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setAllowFileAccess(false);
        settings.setSupportMultipleWindows(true);
        settings.setMinimumFontSize(settings.getMinimumFontSize() + 8);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setSavePassword(false);
        // 缓存模式
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        // 开启 database storage API 功能
        settings.setDatabaseEnabled(true);
        //设置5.0以上系统 web view 的 MixedContentMode，支持https中的http
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        String cacheDirPath = mActivity.getFilesDir().getAbsolutePath() + WEB_CACHE_DIR;
        // 设置数据库缓存路径
        settings.setDatabasePath(cacheDirPath);
        // 设置 Application Caches 缓存目录
        settings.setAppCachePath(cacheDirPath);
        // 开启 Application Caches 功能
        settings.setAppCacheEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (getActivity() == null) {
                    return;
                }
                if (getActivity().isFinishing()) {
                    return;
                }
                // android 4.4 不使用图片延迟加载
                if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) {
                    view.getSettings().setBlockNetworkImage(true);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (getActivity() == null) {
                    return;
                }
                if (getActivity().isFinishing()) {
                    return;
                }
                // android 4.4 不使用图片延迟加载
                if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) {
                    view.getSettings().setBlockNetworkImage(false);
                }
            }
        });
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                        mWebView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * 加载需要显示的网页
     */
    protected void loadWebView() {
        switch (mLoadType) {
            case TYPE_LOAD_URL:
                if (!checkUrl(mContent)) {
                    mContent = "http://" + mContent;
                }
                mWebView.loadUrl(mContent);
                break;
            case TYPE_LOAD_HTML:
                mWebView.loadDataWithBaseURL(null, mContent, "text/html", "UTF-8", null);
                break;
            default:
                break;
        }
    }

    /**
     * 检查是否url
     *
     * @param url
     * @return
     */
    private boolean checkUrl(String url) {
        String regular = "(http|https)://[\\S]*";
        return Pattern.matches(regular, url);
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearFormData();
            mWebView.setTag(null);
            mWebView.clearHistory();
            mWebView.clearView();
            // in android 5.1(sdk:21) we should invoke this to avoid memory leak
            // see (https://coolpers.github.io/webview/memory/leak/2015/07/16/
            // android-5.1-webview-memory-leak.html)
            // ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

}
