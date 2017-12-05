package com.modim.lan.lanandroid;
import android.app.Activity;
import android.content.Context;

import com.modim.lan.lanandroid.LANGLSurfaceView;

public class IGLSurface {
    private static LANGLSurfaceView mImple = null;

    public static LANGLSurfaceView getInstance(Context context){
        if(mImple == null) {
            mImple = new LANGLSurfaceView(context);
        }

        return mImple;
    }
}
