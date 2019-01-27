package com.cbase.demo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.cbase.demo.R;
import com.cbase.demo.base.AppFragment;
import com.cbase.widget.StatusLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author : zhouyx
 * @date : 2017/9/17
 * @description :
 */
public class StatusFragment extends AppFragment {

    @BindView(R.id.content_layout)
    LinearLayout contentLayout;

    private StatusLayout mStatusLayout;

    public static StatusFragment getInstance() {
        return new StatusFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_status;
    }

    @Override
    protected void initFragment(Bundle savedInstanceState, Bundle inputBundle) {
        mStatusLayout = StatusLayout.attach(contentLayout, null);
    }

    @OnClick({R.id.content, R.id.empty, R.id.load, R.id.retry, R.id.setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.content:
                mStatusLayout.showContent();
                break;
            case R.id.empty:
                mStatusLayout.showEmpty();
                break;
            case R.id.load:
                mStatusLayout.showLoading();
                break;
            case R.id.retry:
                mStatusLayout.showRetry();
                break;
            case R.id.setting:
                mStatusLayout.showSetting();
                break;
            default:
                break;
        }
    }

}
