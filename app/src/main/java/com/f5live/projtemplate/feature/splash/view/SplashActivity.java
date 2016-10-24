package com.f5live.projtemplate.feature.splash.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.f5live.projtemplate.R;
import com.f5live.projtemplate.common.base.BaseActivity;
import com.f5live.projtemplate.feature.login.view.LoginActivity;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_splash);

        new Handler().postDelayed(() -> {
            this.startActivity(new Intent(this, LoginActivity.class));
            this.finish();
        }, 1500);
    }
}
