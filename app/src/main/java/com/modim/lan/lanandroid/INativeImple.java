package com.modim.lan.lanandroid;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;

import com.modim.lan.lanandroid.NativeImplement;

public class INativeImple {
    private static NativeImplement mImple = null;

    public static NativeImplement getInstance(Context context){
        if(mImple == null) {

            mImple = new NativeImplement(context);

//            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//            int width = metrics.widthPixels;
//            int height = metrics.heightPixels;
//
//            mImple.lanResize(width, height);

        }

        return mImple;
    }
}