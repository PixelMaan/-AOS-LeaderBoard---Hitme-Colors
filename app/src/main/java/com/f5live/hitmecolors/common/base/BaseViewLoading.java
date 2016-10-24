package com.f5live.hitmecolors.common.base;

import com.f5live.hitmecolors.common.view.DialogMessage;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by Hieu➈ on 29/09/2016.
 */

public interface BaseViewLoading {
    void showLoading();
    void dismissLoading();
    DialogMessage showDialogSuccess(String msg);
    DialogMessage showDialogError(String msg);
}
