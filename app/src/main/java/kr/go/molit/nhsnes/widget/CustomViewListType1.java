package kr.go.molit.nhsnes.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.go.molit.nhsnes.R;

/**
 * Created by yeonjukim on 2017. 4. 2..
 */

public class CustomViewListType1 extends LinearLayout {

    private TextView mTextViewText;
    private TextView mTextViewSubText;
    private ImageView mImageViewPre;
    private ImageView mImageViewPost;
    private LinearLayout mBg;

    public CustomViewListType1(Context context) {
        this(context, null);
    }

    public CustomViewListType1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomViewListType1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayout();
        getAttrs(attrs, defStyleAttr);
    }

    public void setOnPreIconClickListener(OnClickListener listener) {
        mImageViewPre.setOnClickListener(listener);
    }

    public void setOnPostIconClickListener(OnClickListener listener) {
        mImageViewPost.setOnClickListener(listener);
    }

    public void setOnLayoutClickListener(OnClickListener listener) {
        mBg.setOnClickListener(listener);
    }

    public void setOnLayoutClickListener(OnClickListener listener, Object tag) {
        mBg.setOnClickListener(listener);
        mBg.setTag(tag);
    }


    public void setLayout() {
        Context context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.custom_listview_type1, this, false);
        view.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT));
        addView(view);
        mTextViewText = (TextView) view.findViewById(R.id.text);
        mTextViewSubText = (TextView) view.findViewById(R.id.subText);
        mImageViewPre = (ImageView) view.findViewById(R.id.icon_pre);
        mImageViewPost = (ImageView) view.findViewById(R.id.icon_post);
        mBg = (LinearLayout) view.findViewById(R.id.bg);

    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomViewListType1, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {

        String text = typedArray.getString(R.styleable.CustomViewListType1_text);
        String subText = typedArray.getString(R.styleable.CustomViewListType1_subText);

        int iconPre = typedArray.getResourceId(R.styleable.CustomViewListType1_preIcon, 0);
        int iconPost = typedArray.getResourceId(R.styleable.CustomViewListType1_postIcon, 0);

        mTextViewText.setText(text);

        if (TextUtils.isEmpty(subText)) {
            mTextViewSubText.setVisibility(GONE);
        } else {
            mTextViewSubText.setText(subText);
        }

        if (iconPre == 0) {
            mImageViewPre.setVisibility(GONE);
        } else {
            mImageViewPre.setImageResource(iconPost);
        }

        if (iconPost == 0) {
            mImageViewPost.setVisibility(GONE);
        } else {
            mImageViewPost.setImageResource(iconPost);
        }

        typedArray.recycle();
    }

    public void setTextViewText(String str) {
        mTextViewText.setText(str);
    }

    public void setTextViewSubText(String str) {
        mTextViewSubText.setText(str);
        mTextViewSubText.setVisibility(VISIBLE);
    }

    public void setImageViewPreVisible(boolean visible) {
        if (visible) {
            mImageViewPre.setVisibility(View.VISIBLE);
        } else {
            mImageViewPre.setVisibility(View.GONE);
        }

    }

    public void setImageViewPostVisible(boolean visible) {
        if (visible) {
            mImageViewPost.setVisibility(View.VISIBLE);
        } else {
            mImageViewPost.setVisibility(View.GONE);
        }

    }


}
