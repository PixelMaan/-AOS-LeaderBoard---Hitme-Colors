package com.f5live.hitmecolors.feature.splash.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.f5live.hitmecolors.R;
import com.f5live.hitmecolors.common.base.BaseActivity;
import com.f5live.hitmecolors.common.util.FontUtil;
import com.f5live.hitmecolors.databinding.AActivitySplashBinding;
import com.f5live.hitmecolors.feature.home.view.HomeActivity;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */

public class SplashActivity extends BaseActivity {

    private AActivitySplashBinding mRootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mRootView = DataBindingUtil.setContentView(this, R.layout.a_activity_splash);
        this.initViews();
        new Handler().postDelayed(() -> {
            this.startActivity(new Intent(this, HomeActivity.class));
            this.finish();
        }, 2000);
    }

    private void initViews() {
        this.mRootView.splashTvAppSlogan.setText(this.getResources().getString(R.string.str_app_slogan));
        this.mRootView.splashTvAppSlogan.setTypeface(FontUtil.getFontType(this));
    }
}
