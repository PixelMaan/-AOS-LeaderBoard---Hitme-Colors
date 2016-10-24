package com.f5live.projtemplate.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */

public class PermissionUtil {
    public static final int PERMISSION_REQUEST_CODE = 10001;

    public static boolean isPermissionGranted(Context context, String permissionName) {
        return ContextCompat.checkSelfPermission(context, permissionName) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(Context context, String permissionName) {
        requestPermission(context, permissionName, PERMISSION_REQUEST_CODE);
    }

    public static void requestPermission(Context context, String permissionName, int requestCode) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{permissionName}, requestCode);
    }
}
