package com.f5live.hitmecolors.common.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */

public class MediaUtil {

    private static final String TAG = MediaUtil.class.getSimpleName();

    /**
     * Convenience method to create a MediaPlayer for a given resource id.
     * On success,  will already have been called and must not be called again.
     * <p>When done with the MediaPlayer, you should call  ,
     * to free the resources. If not released, too many MediaPlayer instances will
     * result in an exception.</p>
     *
     * @param context the Context to use
     * @param resid the raw resource id (<var>R.raw.&lt;something></var>) for
     *              the resource to use as the datasource
     * @return a MediaPlayer object, or null if creation failed
     */
    public static MediaPlayer create(Context context, int resid) {
        try {
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(resid);
            if (afd == null) return null;

            MediaPlayer mp = new MediaPlayer();
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp.prepare();
            return mp;
        } catch (IOException | IllegalArgumentException | SecurityException ex) {
            Log.d(TAG, "create failed:", ex);
        }
        return null;
    }
}
