package kr.go.molit.nhsnes.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;

import kr.go.molit.nhsnes.R;

/**
 * 커스텀 폰트를 사용하기 위한 클래스
 * @author 정제영
 * @version 1.0, 2017.03.06 최초 작성
 **/

public class TextViewEx extends android.support.v7.widget.AppCompatTextView {

    private static Typeface sNormal = null;
    private static Typeface sBold = null;
    private static Typeface sBarunBold = null;

    public TextViewEx(Context context) {
        super(context);
    }

    public TextViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public TextViewEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray arrType = ctx.obtainStyledAttributes(attrs, R.styleable.FontIds);
        int customFont = arrType.getInt(R.styleable.FontIds_font, 0);
        int customStyle = arrType.getInt(R.styleable.FontIds_style, 0);
        setCustomFont(ctx, customFont);
        setCustomStyle(customStyle);
        arrType.recycle();
    }

    public void setCustomFont(Context context, int fontType) {


        Typeface font = null;

        switch (fontType) {
            case 0:		// Nanum Bold
                if ( (font = getRegular(context)) != null) {
                    setTypeface(font);
                }
                break;
            case 1:		// Nanum ExtraBold
                if ( (font = getBold(context)) != null) {
                    setTypeface(font);
                }
                break;
            case 2:		// Nanum Barun Bold
                if ( (font = getBarunBold(context)) != null) {
                    setTypeface(font);
                }
                break;
            default:
                break;
        }
    }

    public void setCustomStyle(int fontType) {

        switch (fontType) {
            case 0:
                break;
            case 1:		// UnderLine
                setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                break;
            default:
                break;
        }
    }

    public Typeface getRegular(Context context) {
        if (sNormal == null) {
            try {
                sNormal = Typeface.createFromAsset(context.getAssets(), "font/NanumGothicBold.ttf");
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
}
