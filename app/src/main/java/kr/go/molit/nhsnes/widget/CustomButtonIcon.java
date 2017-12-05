package kr.go.molit.nhsnes.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import kr.go.molit.nhsnes.R;

/**
 * Created by yeonjukim on 2017. 4. 2..
 */

public class CustomButtonIcon extends LinearLayout {

    private TextViewEx mTextViewText;
    private ImageView mImageView;

    public CustomButtonIcon(Context context) {
        this(context, null);
    }

    public CustomButtonIcon(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomButtonIcon(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayout();
        getAttrs(attrs, defStyleAttr);
    }

    public void setLayout() {
        Context context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.custom_button_icon, this, false);
        view.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT));
        addView(view);
        mTextViewText = (TextViewEx) view.findViewById(R.id.text);
        mImageView= (ImageView) view.findViewById(R.id.symbol);

    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomButtonIcon, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {

        String text = typedArray.getString(R.styleable.CustomButtonIcon_text);
        int symbol = typedArray.getResourceId(R.styleable.CustomButtonIcon_symbol, 0);

        mTextViewText.setText(text);
        mImageView.setImageResource(symbol);

        typedArray.recycle();
    }


}
