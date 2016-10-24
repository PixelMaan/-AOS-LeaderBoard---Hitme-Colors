package com.f5live.projtemplate.feature.login.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.f5live.projtemplate.R;
import com.f5live.projtemplate.api.APIUrl;
import com.f5live.projtemplate.api.RestAPI;
import com.f5live.projtemplate.feature.login.model.User;
import com.f5live.projtemplate.feature.login.view.LoginView;
import com.google.gson.Gson;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */

public class LoginPresenterImpl implements LoginPresenter {
    private Context mContext;
    private LoginView mView;

    public LoginPresenterImpl(Context context, LoginView view) {
        this.mContext = context;
        this.mView = view;
    }

    @Override
    public void onRequestLogin(String username, String password) {
        if (!onVerifyField(username, password)) {
            return;
        }
        this.mView.onShowLoading();
        String parameter = "{\"email\":\"" + username
                + "\", \"password\":\"" + password + "\"}";
        RestAPI.Post(mContext, APIUrl.API_LOGIN, parameter,
                json -> {
                    this.mView.onDismissLoading();
                    User user = new Gson().fromJson(json.optJSONObject("data").toString(), User.class);
                    this.mView.onLoggedIn(user);
                },
                error -> {
                    this.mView.onDismissLoading();
                    // this.mView.onShowMsgError(error);
                    this.mView.onShowMsgError(this.mContext.getResources()
                            .getString(R.string.IMPLEMENT_YOURS_API));
                });

    }

    private boolean onVerifyField(String user, String pass) {
        if (TextUtils.isEmpty(user)) {
            this.mView.onShowMsgError(this.mContext.getResources()
                    .getString(R.string.error_email_empty));
            return false;
        }

        if (TextUtils.isEmpty(pass)) {
            this.mView.onShowMsgError(this.mContext.getResources()
                    .getString(R.string.error_password_empty));
            return false;
        }
        return true;
    }
}
