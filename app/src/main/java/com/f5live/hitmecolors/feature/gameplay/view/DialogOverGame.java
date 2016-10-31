package com.f5live.hitmecolors.feature.gameplay.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.f5live.hitmecolors.R;
import com.f5live.hitmecolors.common.util.Constant;
import com.f5live.hitmecolors.common.util.FontUtil;
import com.f5live.hitmecolors.common.util.PreUtil;


/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */
public class DialogOverGame extends Dialog {

    private int countLoading = 0;

    private OnCancel onCancel;
    private OnDiscard onDiscard;

    private boolean hasSWapButton = false;

    public DialogOverGame(Context context) {
        super(context);
        initLoadingProgress();
    }

    private DialogOverGame initView() {
        ((TextView) findViewById(R.id.txtScore)).setText(String.valueOf("" + PreUtil.getInt(Constant.SCORE)));
        ((TextView) findViewById(R.id.txtScore)).setTypeface(FontUtil.getFontType(getContext()));

        ((TextView) findViewById(R.id.txtBest)).setText(String.valueOf("" + PreUtil.getBest()));
        ((TextView) findViewById(R.id.txtBest)).setTypeface(FontUtil.getFontType(getContext()));

        return this;
    }


    @SuppressWarnings("ConstantConditions")
    private void initLoadingProgress() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_over_game);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(this.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        this.getWindow().setAttributes(lp);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        initView();

        findViewById(R.id.btnOk).setOnClickListener(view -> {
            dismiss();
            if (onCancel != null) {
                if (hasSWapButton) {
                    onDiscard.onClick();
                } else {
                    onCancel.onClick();
                }
            }
        });
        findViewById(R.id.btnCancel).setOnClickListener(view -> {
            dismiss();
            if (onDiscard != null) {
                if (hasSWapButton) {
                    onCancel.onClick();
                } else {
                    onDiscard.onClick();
                }
            }
        });
    }

    public DialogOverGame showMsg() {
        if (countLoading == 0) {
            super.show();
        }
        countLoading++;
        return this;
    }

    public void dismissMsg() {
        countLoading--;
        if (countLoading > 0) return;
        super.dismiss();
    }

    public void forceDismissMsg() {
        countLoading = 0;
        super.dismiss();
    }

    public DialogOverGame setOnCancel(OnCancel onCancel) {
        this.onCancel = onCancel;
        return this;
    }

    public DialogOverGame setOnDiscard(OnDiscard onDiscard) {
        this.onDiscard = onDiscard;
        return this;
    }

    public interface OnCancel {
        void onClick();
    }

    public interface OnDiscard {
        void onClick();
    }
}
