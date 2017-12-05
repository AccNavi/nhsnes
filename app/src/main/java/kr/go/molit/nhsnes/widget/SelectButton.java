package kr.go.molit.nhsnes.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import kr.go.molit.nhsnes.R;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class SelectButton extends FrameLayout implements View.OnClickListener {

    private TextViewEx mButtonExSelect0;
    private TextViewEx mButtonExSelect1;
    private TextViewEx mButtonExSelect2;

    private OnSelectListener listener;

    private String select0 = "0.5";
    private String select1 = "1.0";
    private String select2 = "1.5";
    private String unit = "MM";

    private int unselectedColor;
    private int selectedColor;

    private int selectedIndex = 0;

    public SelectButton(@NonNull Context context) {
        this(context, null);
    }

    public SelectButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectButton(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayout();
    }

    private void setLayout() {
        Context context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.view_select_button, this, false);
        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(view);

        unselectedColor = getResources().getColor(R.color.textColorTransparentBlack);
        selectedColor = Color.WHITE;

        mButtonExSelect0 = (TextViewEx) view.findViewById(R.id.btn_select_0);
        mButtonExSelect0.setOnClickListener(this);
        mButtonExSelect0.setBackgroundResource(R.drawable.btn_type6_nor);
        mButtonExSelect1 = (TextViewEx) view.findViewById(R.id.btn_select_1);
        mButtonExSelect1.setOnClickListener(this);
        mButtonExSelect2 = (TextViewEx) view.findViewById(R.id.btn_select_2);
        mButtonExSelect2.setOnClickListener(this);

        mButtonExSelect0.setTextColor(selectedColor);
        mButtonExSelect1.setTextColor(unselectedColor);
        mButtonExSelect2.setTextColor(unselectedColor);

        int selectSize = getResources().getDimensionPixelSize(R.dimen.select_button_number_size);
        int unitSize = getResources().getDimensionPixelSize(R.dimen.select_button_unit_size);

        SpannableString mSpannableString0 = new SpannableString(select0);
        mSpannableString0.setSpan(new AbsoluteSizeSpan(selectSize), 0, select0.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        SpannableString mSpannableString1 = new SpannableString(select1);
        mSpannableString1.setSpan(new AbsoluteSizeSpan(selectSize), 0, select1.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        SpannableString mSpannableString2 = new SpannableString(select2);
        mSpannableString2.setSpan(new AbsoluteSizeSpan(selectSize), 0, select2.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString mSpannableStringUnit = new SpannableString(unit);
        mSpannableStringUnit.setSpan(new AbsoluteSizeSpan(unitSize), 0, unit.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        mButtonExSelect0.setText(TextUtils.concat(mSpannableString0, mSpannableStringUnit));
        mButtonExSelect1.setText(TextUtils.concat(mSpannableString1, mSpannableStringUnit));
        mButtonExSelect2.setText(TextUtils.concat(mSpannableString2, mSpannableStringUnit));
    }

    @Override
    public void onClick(View v) {

        String selectedData = "";

        switch (v.getId()) {
            case R.id.btn_select_0:
                mButtonExSelect0.setBackgroundResource(R.drawable.btn_type6_nor);
                mButtonExSelect1.setBackgroundColor(getResources().getColor(R.color.transparent));
                mButtonExSelect2.setBackgroundColor(getResources().getColor(R.color.transparent));
                mButtonExSelect0.setTextColor(selectedColor);
                mButtonExSelect1.setTextColor(unselectedColor);
                mButtonExSelect2.setTextColor(unselectedColor);
                selectedData = select0;
                selectedIndex = 0;
                break;
            case R.id.btn_select_1:
                mButtonExSelect0.setBackgroundColor(getResources().getColor(R.color.transparent));
                mButtonExSelect1.setBackgroundResource(R.drawable.btn_type6_nor);
                mButtonExSelect2.setBackgroundColor(getResources().getColor(R.color.transparent));
                mButtonExSelect0.setTextColor(unselectedColor);
                mButtonExSelect1.setTextColor(selectedColor);
                mButtonExSelect2.setTextColor(unselectedColor);
                selectedData = select1;
                selectedIndex = 1;
                break;
            case R.id.btn_select_2:
                mButtonExSelect0.setBackgroundColor(getResources().getColor(R.color.transparent));
                mButtonExSelect1.setBackgroundColor(getResources().getColor(R.color.transparent));
                mButtonExSelect2.setBackgroundResource(R.drawable.btn_type6_nor);
                mButtonExSelect0.setTextColor(unselectedColor);
                mButtonExSelect1.setTextColor(unselectedColor);
                mButtonExSelect2.setTextColor(selectedColor);
                selectedData = select2;
                selectedIndex = 2;
                break;
        }

        if (listener != null) {
            listener.onSelected(this, selectedIndex, selectedData);
        }
    }


    public String getSelectedData() {

        String selectedData = "";

        switch (this.selectedIndex) {

            case 0:
                return select0;

            case 1:
                return select1;

            case 2:
                return select2;


        }

        return select0;
    }

    public void setOnSelectListener(OnSelectListener listener) {
        this.listener = listener;
    }

    public void setSelectIndex(int index) {
        switch (index) {
            case 0:
                mButtonExSelect0.callOnClick();
                break;
            case 1:
                mButtonExSelect1.callOnClick();
                break;
            case 2:
                mButtonExSelect2.callOnClick();
                break;
        }
    }

    public void setSelect0(String select0) {
        this.select0 = select0;

        int selectSize = getResources().getDimensionPixelSize(R.dimen.select_button_number_size);
        int unitSize = getResources().getDimensionPixelSize(R.dimen.select_button_unit_size);

        if (mButtonExSelect0 != null) {

            SpannableString mSpannableString0 = new SpannableString(select0);
            mSpannableString0.setSpan(new AbsoluteSizeSpan(selectSize), 0, select0.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            SpannableString mSpannableStringUnit = new SpannableString(unit);
            mSpannableStringUnit.setSpan(new AbsoluteSizeSpan(unitSize), 0, unit.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            mButtonExSelect0.setText(TextUtils.concat(mSpannableString0, mSpannableStringUnit));

        }
    }

    public void setSelect1(String select1) {
        this.select1 = select1;

        int selectSize = getResources().getDimensionPixelSize(R.dimen.select_button_number_size);
        int unitSize = getResources().getDimensionPixelSize(R.dimen.select_button_unit_size);

        if (mButtonExSelect1 != null) {

            SpannableString mSpannableString1 = new SpannableString(select1);
            mSpannableString1.setSpan(new AbsoluteSizeSpan(selectSize), 0, select1.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            SpannableString mSpannableStringUnit = new SpannableString(unit);
            mSpannableStringUnit.setSpan(new AbsoluteSizeSpan(unitSize), 0, unit.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            mButtonExSelect1.setText(TextUtils.concat(mSpannableString1, mSpannableStringUnit));

        }
    }

    public void setSelect2(String select2) {
        this.select2 = select2;

        int selectSize = getResources().getDimensionPixelSize(R.dimen.select_button_number_size);
        int unitSize = getResources().getDimensionPixelSize(R.dimen.select_button_unit_size);

        if (mButtonExSelect2 != null) {

            SpannableString mSpannableString2 = new SpannableString(select2);
            mSpannableString2.setSpan(new AbsoluteSizeSpan(selectSize), 0, select2.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            SpannableString mSpannableStringUnit = new SpannableString(unit);
            mSpannableStringUnit.setSpan(new AbsoluteSizeSpan(unitSize), 0, unit.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            mButtonExSelect2.setText(TextUtils.concat(mSpannableString2, mSpannableStringUnit));
        }

    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public interface OnSelectListener {
        public void onSelected(View v, int index, String data);
    }
}
