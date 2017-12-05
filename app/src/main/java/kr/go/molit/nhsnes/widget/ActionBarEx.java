package kr.go.molit.nhsnes.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import kr.go.molit.nhsnes.R;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class ActionBarEx extends LinearLayoutCompat implements View.OnClickListener {

    private TextViewEx mTextViewTitle;
    private ImageView mImageViewIcon;
    private Button mButtonBack;

    public ActionBarEx(Context context) {
        this(context, null);
    }

    public ActionBarEx(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionBarEx(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayout();
        getAttrs(attrs, defStyleAttr);
    }


    public void setLayout() {
        Context context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.view_action_bar, this, false);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(view);

        mTextViewTitle = (TextViewEx) view.findViewById(R.id.tv_title);
        mButtonBack = (Button) view.findViewById(R.id.btn_back);
        mImageViewIcon = (ImageView) view.findViewById(R.id.img_icon);
        setOnBackClickListener(null);
    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ActionBarEx, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {

        String text = typedArray.getString(R.styleable.ActionBarEx_text);
        int textColor = typedArray.getColor(R.styleable.ActionBarEx_textColor, Color.WHITE);
//        int icon = typedArray.getResourceId(R.styleable.ActionBarEx_titleIcon, R.drawable.icon_appinfo);
        int icon = typedArray.getResourceId(R.styleable.ActionBarEx_titleIcon, 0);
        float textSize = typedArray.getDimension(R.styleable.ActionBarEx_textSize, mTextViewTitle.getTextSize());
        setTitleText(text);
        setTitleTextColor(textColor);
        setIcon(icon);
        setTitleTextSie(TypedValue.COMPLEX_UNIT_PX, textSize);
        typedArray.recycle();
    }

    public void setTitleText(String title) {
        mTextViewTitle.setText(title);
    }

    public String getTitleText() {
        return mTextViewTitle.getText().toString();
    }

    public void setTitleTextColor(int color) {
        mTextViewTitle.setTextColor(color);
    }

    public void setTitleTextSie(float size) {
        mTextViewTitle.setTextSize(size);
    }

    public void setTitleTextSie(int unit, float size) {
        mTextViewTitle.setTextSize(unit, size);
    }


    public void setIcon(int res) {
        mImageViewIcon.setImageResource(res);
    }

    public void setIcon(Bitmap bitmap) {
        mImageViewIcon.setImageBitmap(bitmap);
    }

    public void setOnBackClickListener(OnClickListener listener) {
        if (listener != null) {
            mButtonBack.setOnClickListener(listener);
        } else {
            mButtonBack.setOnClickListener(this);
        }
    }

    public void setOnTitleClickListener(OnClickListener listener) {
        mTextViewTitle.setOnClickListener(listener);
    }


    @Override
    public void onClick(View v) {
        Context context = getContext();
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }
}
