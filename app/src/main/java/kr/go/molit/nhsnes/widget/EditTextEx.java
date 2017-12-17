package kr.go.molit.nhsnes.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.FontUtil;

/**
 * 커스텀 폰트를 사용하기 위한 클래스
 * @author 정제영
 * @version 1.0, 2017.03.06 최초 작성
 **/

public class EditTextEx extends android.support.v7.widget.AppCompatEditText {
/*
    private static Typeface sNormal = null;
    private static Typeface sBold = null;
*/
    public EditTextEx(Context context) {
        super(context);
    }

    public EditTextEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public EditTextEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray arrType = ctx.obtainStyledAttributes(attrs, R.styleable.EditIds);
        int customFont = arrType.getInt(R.styleable.EditIds_type, 0);
        setCustomStyle(ctx, customFont);
        arrType.recycle();
    }

    public void	setCustomStyle(Context context, int fontType) {
        setTypeface(FontUtil.getRegular(context));

        switch (fontType) {
            case 0:		// 숫자
                setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case 1:		// 전화번호
                setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case 2:		// 한글
                setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case 3:		// 영어

//                setKeyListener(DigitsKeyListener.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"));
                setPrivateImeOptions("defaultInputmode=english;");
                break;
            case 4:		// 비번
                setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case 5:
                setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            default:
                break;
        }
    }
/*
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
*/
}
