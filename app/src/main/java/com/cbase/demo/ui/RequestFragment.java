package com.cbase.demo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cbase.demo.R;
import com.cbase.demo.base.AppFragment;
import com.cbase.demo.mvp.RequestPresenter;
import com.cbase.demo.mvp.RequestView;
import com.cbase.widget.StatusLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author : zhouyx
 * @date : 2017/9/17
 * @description :
 */
public class RequestFragment extends AppFragment<RequestView, RequestPresenter> implements RequestView {

    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.content)
    TextView content;

    private StatusLayout mStatusLayout;

    public static RequestFragment getInstance() {
        return new RequestFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_request;
    }

    @Override
    protected void initFragment(Bundle savedInstanceState, Bundle inputBundle) {
        mStatusLayout = StatusLayout.attach(content, null);
    }

    @OnClick({R.id.request, R.id.multi_request})
    public void onViewClicked(View view) {
        String city = etAddress.getText().toString().trim();
        int cityCode;
        try {
            cityCode = Integer.valueOf(city);
        } catch (NumberFormatException e) {
            cityCode = 101010100;
        }
        switch (view.getId()) {
            case R.id.request:
                getMvpPresenter().single(cityCode);
                break;
            case R.id.multi_request:
                getMvpPresenter().multi(cityCode);
                break;
            default:
                break;
        }
    }

    @Override
    public void showLoading() {
        mStatusLayout.showLoading();
    }

    @Override
    public void onSuccess(String result) {
        mStatusLayout.showContent();
        content.setText(result);
    }

    @Override
    public void showRetry() {
        mStatusLayout.showRetry();
    }

    @Override
    public RequestView bindMvpView() {
        return this;
    }

    @Override
    public RequestPresenter bindMvpPresenter() {
        return new RequestPresenter();
    }

}
