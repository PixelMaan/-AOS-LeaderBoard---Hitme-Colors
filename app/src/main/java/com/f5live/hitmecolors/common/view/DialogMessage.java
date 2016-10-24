package com.f5live.hitmecolors.common.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.f5live.hitmecolors.R;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */
public class DialogMessage extends Dialog {

    private int countLoading = 0;

    OnButtonClick onButtonClick;

    public DialogMessage(Context context, String msg) {
        super(context);

        initLoadingProgress(context);
        ((TextView) findViewById(R.id.txtMsg)).setText(msg);
    }

    private void initLoadingProgress(Context context) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_message);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(this.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        this.getWindow().setAttributes(lp);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        this.findViewById(R.id.btnOk).setOnClickListener(view -> {
            dismiss();
            if(onButtonClick != null){
                onButtonClick.onClick();
            }
        });
    }

    public DialogMessage showMsg() {
        if(countLoading == 0) {
            super.show();
        }
        countLoading++;
        return this;
    }

    public void dismissMsg() {
        countLoading--;
        if(countLoading > 0) return;
        super.dismiss();
    }

    public void forceDismissMsg() {
        countLoading = 0;
        super.dismiss();
    }

    public void setOnButtonClick(OnButtonClick onButtonClick){
        this.onButtonClick = onButtonClick;
    }

    public interface OnButtonClick {
        void onClick();
    }
}
