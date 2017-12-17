package kr.go.molit.nhsnes.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.FontUtil;

/**
 * 버튼에서 커스텀 폰트를 사용하기 위한 클래스
 * @author 정제영
 * @version 1.0, 2017.03.06 최초 작성
 **/

public class ButtonEx extends android.support.v7.widget.AppCompatButton {
/*
    private static Typeface sNormal = null;
    private static Typeface sBold = null;
    private static Typeface sBarunBold = null;
*/
    public ButtonEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    public ButtonEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public ButtonEx(Context context) {
        super(context);
    }

    public void	setCustomFont(Context context, AttributeSet attrs) {
        TypedArray arrType = context.obtainStyledAttributes(attrs, R.styleable.FontIds);
        int customFont = arrType.getInt(R.styleable.FontIds_font, 0);

        Typeface font = null;

        switch (customFont) {
            case 0:		// REGULAR
                if ( (font = FontUtil.getRegular(context)) != null) {
                    setTypeface(font);
                }
                break;
            case 1:		// BOLD
                if ( (font = FontUtil.getBold(context)) != null) {
                    setTypeface(font);
                }
                break;
            case 2:		// Nanum Barun Bold
                if ( (font = FontUtil.getBarunBold(context)) != null) {
                    setTypeface(font);
                }
                break;
            default:
                break;
        }
        arrType.recycle();
    }
/*
    public Typeface getRegular(Context context) {
        if (sNormal == null) {
            try {
                sNormal = Typeface.createFromAsset(context.getAssets(), "font/NanumBarunGothicBold.ttf");
            } catch (Exception e) {
            }
        }
        return sNormal;
    }

    public Typeface getBold(Context context) {
        if (sBold == null) {
            try {
                sBold = Typeface.createFromAsset(context.getAssets(), "font/NanumGothicExtraBold.ttf");
            } catch (Exception e) {
            }
        }
        return sBold;
    }

    public Typeface getBarunBold(Context context) {
        if (sBarunBold == null) {
            try {
                sBarunBold = Typeface.createFromAsset(context.getAssets(), "font/NanumBarunGothicBold.ttf");
            } catch (Exception e) {
            }
        }
        return sBarunBold;
    }
*/
}
