package kr.go.molit.nhsnes.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import kr.go.molit.nhsnes.R;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class TextToggleButton extends FrameLayout implements CompoundButton.OnCheckedChangeListener{

    /**
    * 체크 박스 리스너
    * @author FIESTA
    * @since  오전 12:28
    **/
    public interface OnCheckedChangeListener{

        public void onCheckedChanged(int id, boolean isChecked);

    }

    private ToggleButton mToggleButtonSwitch;
    private TextView mTextViewTitle;
    private OnCheckedChangeListener onCheckedChangeListener;

    public TextToggleButton(@NonNull Context context) {
        this(context, null);
    }

    public TextToggleButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextToggleButton(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayout();
        getAttrs(attrs, defStyleAttr);
    }

    private void setLayout() {
        Context context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.view_text_toggle_button, this, false);
        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(view);

        mTextViewTitle = (TextView) view.findViewById(R.id.tv_title);
        mToggleButtonSwitch = (ToggleButton) view.findViewById(R.id.tb_switch);
        mToggleButtonSwitch.setOnCheckedChangeListener(this);
    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TextToggleButton, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        String text = typedArray.getString(R.styleable.TextToggleButton_text);
        int textColor = typedArray.getColor(R.styleable.TextToggleButton_textColor, Color.WHITE);
        float textSize = typedArray.getDimension(R.styleable.TextToggleButton_textSize, mTextViewTitle.getTextSize());
        setTitleText(text);
        setTitleTextColor(textColor);
        setTitleTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
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

    public void setTitleTextSize(float size) {
        mTextViewTitle.setTextSize(size);
    }

    public void setTitleTextSize(int unit, float size) {
        mTextViewTitle.setTextSize(unit, size);
    }

    /**
    * 체크 박스 활성화/비활성화
    * @author FIESTA
    * @since  오전 12:39
    **/
    public void setCheck(boolean isCheck) {
        this.mToggleButtonSwitch.setChecked(isCheck);
    }

    public boolean isCheck(){
        return this.mToggleButtonSwitch.isChecked();
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener){
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if (this.onCheckedChangeListener != null) {
            this.onCheckedChangeListener.onCheckedChanged(getId(), b);
        }

    }
}
