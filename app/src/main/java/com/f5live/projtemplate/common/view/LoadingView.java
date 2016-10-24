package com.f5live.projtemplate.common.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.f5live.projtemplate.R;


/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */
public class LoadingView extends Dialog {

    private int countLoading = 0;

    public LoadingView(Context context) {
        super(context);

        initLoadingProgress(context);
    }

    private void initLoadingProgress(Context context) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.loading_progress);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    public void show() {
        if(countLoading == 0) {
            super.show();
        }
        countLoading++;
    }

    @Override
    public void dismiss() {
        countLoading--;
        if(countLoading > 0 || !super.isShowing()) return;
        super.dismiss();
    }

    public void forceDismiss() {
        countLoading = 0;
        super.dismiss();
    }
}
