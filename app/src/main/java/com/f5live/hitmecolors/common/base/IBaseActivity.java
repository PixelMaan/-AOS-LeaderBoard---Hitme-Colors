package com.f5live.hitmecolors.common.base;

import com.f5live.hitmecolors.common.view.DialogMessage;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */
public interface IBaseActivity {
    DialogMessage showDialogSuccess(String msg);

    void forceDismissLoading();

    DialogMessage showDialogError(String msg);

    void dismissLoading();

    void showLoading();

    void showDialogPermission(String msg);
}
