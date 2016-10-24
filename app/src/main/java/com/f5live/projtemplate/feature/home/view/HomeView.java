package com.f5live.projtemplate.feature.home.view;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */

public interface HomeView {
    void onShowLoading();

    void onDismissLoading();

    void onShowMsgError(String msg);
}
