package kr.go.molit.nhsnes.common;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

/**
 * Created by user on 2017-12-17.
 */

public class FontUtil {
    private static Typeface sNormal = null;
    private static Typeface sBold = null;
    private static Typeface sBarunBold = null;

    public static Typeface getRegular(Context context) {
        if (sNormal == null) {
            try {
                Log.d("JeLib","-------sNormal-----------");
                sNormal = Typeface.createFromAsset(context.getAssets(), "font/NanumGothicBold.ttf");
            } catch (Exception e) {
            }
        }
        return sNormal;
    }

    public static Typeface getBold(Context context) {
        if (sBold == null) {
            try {
                Log.d("JeLib","-----------sBold----------");
                sBold = Typeface.createFromAsset(context.getAssets(), "font/NanumGothicExtraBold.ttf");
            } catch (Exception e) {
            }
        }
        return sBold;
    }

    public static Typeface getBarunBold(Context context) {
        if (sBarunBold == null) {
            try {
                Log.d("JeLib","-----------sBarunBold--------");
                sBarunBold = Typeface.createFromAsset(context.getAssets(), "font/NanumBarunGothicBold.ttf");
            } catch (Exception e) {
            }
        }
        return sBarunBold;
    }
}
