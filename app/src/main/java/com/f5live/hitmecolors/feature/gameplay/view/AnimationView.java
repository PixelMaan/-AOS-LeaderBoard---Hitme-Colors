package com.f5live.hitmecolors.feature.gameplay.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import com.f5live.hitmecolors.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */

public class AnimationView extends View {
    private Movie mMovie;
    private long mMovieStart;
    private static final boolean DECODE_STREAM = true;

    private static byte[] streamToBytes(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = is.read(buffer)) >= 0) {
                os.write(buffer, 0, len);
            }
        } catch (java.io.IOException ignored) {
        }
        return os.toByteArray();
    }

    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        java.io.InputStream is;
        // YOUR GIF IMAGE Here
        is = context.getResources().openRawResource(R.raw.spawn);
        if (DECODE_STREAM) {
            mMovie = Movie.decodeStream(is);
        } else {
            byte[] array = streamToBytes(is);
            mMovie = Movie.decodeByteArray(array, 0, array.length);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        long now = android.os.SystemClock.uptimeMillis();
        if (mMovieStart == 0) {
            mMovieStart = now;
        }
        if (mMovie != null) {
            int dur = mMovie.duration();
            if (dur == 0) {
                dur = 3000;
            }
            int relTime = (int) ((now - mMovieStart) % dur);
            mMovie.setTime(relTime);
            mMovie.draw(canvas, getWidth() - 200, getHeight() - 200);
            invalidate();
        }
    }
}