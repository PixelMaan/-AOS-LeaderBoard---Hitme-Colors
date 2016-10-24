package com.f5live.projtemplate.feature.login.view;

import com.f5live.projtemplate.feature.login.model.User;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */

public interface LoginView {
    void onShowLoading();

    void onDismissLoading();

    void onShowMsgError(String msg);

    void onLoggedIn(User user);
}
