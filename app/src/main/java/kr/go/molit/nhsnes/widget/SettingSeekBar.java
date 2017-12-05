package kr.go.molit.nhsnes.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import kr.go.molit.nhsnes.R;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class SettingSeekBar extends FrameLayout implements SeekBar.OnSeekBarChangeListener {

    private ImageView mImageViewIcon;
    private SeekBar mSeekBarSetting;
    private TextViewEx mTextViewExPercent;

    private SeekBar.OnSeekBarChangeListener listener;

    public SettingSeekBar(@NonNull Context context) {
        this(context, null);
    }

    public SettingSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayout();
        getAttrs(attrs, defStyleAttr);
    }

    private void setLayout() {
        Context context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.view_setting_seek_bar, this, false);
        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(view);

        mImageViewIcon = (ImageView) view.findViewById(R.id.iv_icon);
        mSeekBarSetting = (SeekBar) view.findViewById(R.id.sb_setting);
        mSeekBarSetting.setOnSeekBarChangeListener(this);
        mTextViewExPercent = (TextViewEx) view.findViewById(R.id.tv_percent);
    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SettingSeekBar, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        int textColor = typedArray.getColor(R.styleable.SettingSeekBar_textColor, Color.WHITE);
        float textSize = typedArray.getDimension(R.styleable.SettingSeekBar_textSize, mTextViewExPercent.getTextSize());
        int progress = typedArray.getInt(R.styleable.SettingSeekBar_progress, 0);
        int progressIcon = typedArray.getResourceId(R.styleable.SettingSeekBar_progressIcon, R.drawable.icon_vol);
        setProgressTextColor(textColor);
        setProgressTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        setProgressIcon(progressIcon);
        setProgress(progress);

        typedArray.recycle();
    }

    public void setProgressIcon(int res) {
        mImageViewIcon.setImageResource(res);
    }

    public void setProgressIcon(Bitmap bitmap) {
        mImageViewIcon.setImageBitmap(bitmap);
    }

    public String getTitleText() {
        return mTextViewExPercent.getText().toString();
    }

    public void setProgressTextColor(int color) {
        mTextViewExPercent.setTextColor(color);
    }

    public void setProgressTextSize(int unit, float size) {
        mTextViewExPercent.setTextSize(unit, size);
    }

    public void setProgress(int progress) {
        mSeekBarSetting.setProgress(progress);
        mTextViewExPercent.setText(Integer.toString(progress));
    }

    public void setProgressTextSize(float size) {
        mTextViewExPercent.setTextSize(size);
    }


    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mTextViewExPercent.setText(Integer.toString(progress));
        if (listener != null) {
            listener.onProgressChanged(seekBar, progress, fromUser);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (listener != null) {
            listener.onStartTrackingTouch(seekBar);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (listener != null) {
            listener.onStopTrackingTouch(seekBar);
        }
    }

}
