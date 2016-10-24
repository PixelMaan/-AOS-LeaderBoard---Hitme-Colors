package com.f5live.projtemplate.common.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.f5live.projtemplate.common.view.DialogMessage;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */
@SuppressLint("ValidFragment")
public class BaseFragment extends Fragment {

    public Context mContext;

    public BaseFragment() {
    }

    public BaseFragment(Context context) {
        this.mContext = context;
    }

    public void showLoading() {
        if (mContext != null) {
            ((IBaseActivity) mContext).showLoading();
        }
    }

    public void dismissLoading() {
        if (mContext != null) {
            ((IBaseActivity) mContext).dismissLoading();
        }
    }

    public DialogMessage showDialogError(String msg) {
        if (mContext == null) return null;
        return ((IBaseActivity) mContext).showDialogError(msg);
    }

    public void forceDismissLoading() {
        if (mContext == null) return;
        ((IBaseActivity) mContext).forceDismissLoading();
    }

    public DialogMessage showDialogSuccess(String msg) {
        if (mContext == null) return null;
        return ((IBaseActivity) mContext).showDialogSuccess(msg);
    }
}
