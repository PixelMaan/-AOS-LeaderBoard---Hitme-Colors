package com.f5live.hitmecolors.common.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.f5live.hitmecolors.R;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */

public class BaseDialog extends Dialog {
    private Toast mToast;
    /**
     * A dialog showing a progress indicator and an optional text message or
     * view.
     */
    private ProgressDialog mProgressDialog;

    /** Interface to global information about an application environment. */
    protected Context mContext;

    /**
     *
     * @param context
     */
    public BaseDialog(Context context) {
        super(context, R.style.full_screen_dialog);
        mContext = context;
        setCanceledOnTouchOutside(false);
    }

    /**
     * show progress dialog.
     *
     * @param msgResId
     */
    protected void showProgress(int msgResId) {
        try {

            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                return;
            }
            mProgressDialog = new ProgressDialog(mContext);

            mProgressDialog.setIndeterminate(true);
            if (msgResId != 0) {
                mProgressDialog.setMessage(mContext.getString(msgResId));
            }

            mProgressDialog.setCancelable(false);

            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * cancel progress dialog.
     */
    protected void dismissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     * Show Error Dialog
     *
     * @param msgResId
     *            the string resource's id
     */
    protected void showErrorDialog(int msgResId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(mContext.getString(msgResId));
        builder.setPositiveButton(mContext.getString(R.string.OK), null);
        builder.show();
    }

    /**
     * Show Error Dialog
     *
     * @param msg
     *            the message to show
     */
    protected void showErrorDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(msg);
        builder.setPositiveButton(mContext.getString(R.string.OK), null);
        builder.show();
    }

    protected void showToastMessage(int re) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, "", Toast.LENGTH_LONG);
        }
        if (mToast != null) {
            if (mToast.getView().isShown()) {
                mToast.cancel();
            }
            mToast.setText(re);
            mToast.show();
        }
    }
}
