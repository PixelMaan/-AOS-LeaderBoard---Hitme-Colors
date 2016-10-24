package com.f5live.hitmecolors.common.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */

public class FontUtil {
    public static Typeface getFontType(Context context) {
        return Typeface.createFromAsset(context.getAssets(),"fonts/FredokaOne.ttf");
    }
}
