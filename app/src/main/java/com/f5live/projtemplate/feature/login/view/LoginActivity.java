package com.f5live.projtemplate.feature.login.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.f5live.projtemplate.BuildConfig;
import com.f5live.projtemplate.R;
import com.f5live.projtemplate.common.util.CommonUtil;
import com.f5live.projtemplate.common.util.NetworkUtil;
import com.f5live.projtemplate.common.util.StringUtil;
import com.f5live.projtemplate.common.base.BaseActivity;
import com.f5live.projtemplate.databinding.AActivityLoginBinding;
import com.f5live.projtemplate.feature.login.model.User;
import com.f5live.projtemplate.feature.login.presenter.LoginPresenter;
import com.f5live.projtemplate.feature.login.presenter.LoginPresenterImpl;

public class LoginActivity extends BaseActivity implements LoginView {

    private AActivityLoginBinding mRootView;
    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mRootView = DataBindingUtil.setContentView(this, R.layout.a_activity_login);
        this.mPresenter = new LoginPresenterImpl(this, this);
        this.initViews();
    }

    private void initViews() {
        CommonUtil.setupDismissKeyboardListener(this, this.mRootView.getRoot());
        this.mRootView.toolbar.btnCenter.setImageResource(R.mipmap.ic_launcher);
        this.mRootView.btnlogin.setOnClickListener(v -> this.onLogin());
        this.mRootView.txtVersion.setText(String.format(getString(R.string.appVersion)
                , BuildConfig.VERSION_NAME));
        this.mRootView.tvResetPassword.setOnClickListener(v -> {
                    // TODO
                    showDialogError("Should add yours business.");
                }
        );
        this.mRootView.edEmail.addTextChangedListener(textWatcher);
        this.mRootView.edPsw.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            setBtnLoginStatus(!(TextUtils.isEmpty(mRootView.edEmail.getText())
                    || TextUtils.isEmpty(mRootView.edPsw.getText())));
        }
    };

    private void setBtnLoginStatus(boolean isCan) {
        this.mRootView.btnlogin.setClickable(isCan);
        this.mRootView.btnlogin.setEnabled(isCan);
        if (isCan) {
            this.mRootView.btnlogin.setBackgroundResource(R.drawable.btn_yellow);
        } else {
            this.mRootView.btnlogin.setBackgroundResource(R.drawable.bg_btn_hidden);
        }
    }

    private void onLogin() {
        if (!NetworkUtil.isConnected(this)) {
            this.showDialogError(getResources()
                    .getString(R.string.error_can_not_connect_to_network));
            return;
        }

        String email = this.mRootView.edEmail.getText().toString().trim();
        String password = this.mRootView.edPsw.getText().toString().trim();
        if (!StringUtil.isValidEmail(email)) {
            this.showDialogError(getResources().getString(R.string.error_email_format));
            return;
        }

        this.mPresenter.onRequestLogin(email, password);
    }

    @Override
    public void onShowLoading() {
        super.showLoading();
    }

    @Override
    public void onDismissLoading() {
        super.dismissLoading();
    }

    @Override
    public void onShowMsgError(String msg) {
        super.showDialogError(msg);
    }

    @Override
    public void onLoggedIn(User user) {
        // TODO - Somethings
    }
}
