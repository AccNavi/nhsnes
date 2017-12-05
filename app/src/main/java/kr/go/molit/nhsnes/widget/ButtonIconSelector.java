package kr.go.molit.nhsnes.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import kr.go.molit.nhsnes.R;

/**
 * 클릭 시 효과를 주기 위한 커스텀 버튼
 * @author 정제영
 * @version 1.0, 2017.03.09 최초 작성
 **/

public class ButtonIconSelector extends LinearLayout {

    Context mContext;

    LayoutInflater mInflater;

    //배경 이미지
    View iv_back;
    //타이틀 Text
    TextViewEx tv_title;
    //이미지 아이콘
    ImageView iv_icon;

    OnClickListener mClickListener = null;

    public ButtonIconSelector(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ButtonIconSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mInflater = LayoutInflater.from(mContext);

        initView();
        setAttributeData(attrs);
    }

    protected void initView() {
        if(mInflater != null) {
            mInflater.inflate(R.layout.view_btn_icon_selector, this);
            iv_back = (View)findViewById(R.id.iv_back);
            tv_title = (TextViewEx)findViewById(R.id.tv_title);
            iv_icon = (ImageView)findViewById(R.id.iv_icon);
        }
    }

    protected void setAttributeData(AttributeSet attrs) {
        TypedArray arrType = mContext.obtainStyledAttributes(attrs, R.styleable.BtnIconIds);

        String strText = arrType.getString(R.styleable.BtnIconIds_btn_text);
        //이미지를 가져올때 getInt로 하면 에러 남. 반드시 resourceID로 가져와야 함
        //배경 이미지
        int imgBack = arrType.getResourceId(R.styleable.BtnIconIds_btn_back, R.drawable.btn_type1_dis);
        //텍스트 색상
        int textColor = arrType.getResourceId(R.styleable.BtnIconIds_btn_text_color, R.color.white);
        //아이콘 이미지
        int imgIcon = arrType.getResourceId(R.styleable.BtnIconIds_btn_icon, R.drawable.icon_memory_dis);

        iv_back.setBackgroundResource(imgBack);
        tv_title.setTextColor(textColor);
        iv_icon.setBackgroundResource(imgIcon);

        if(strText != null) {
            tv_title.setText(strText);
        }
    }
}
