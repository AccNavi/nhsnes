package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.activity.NhsFlightWriteActivity;
import kr.go.molit.nhsnes.net.realm.FlightPlanInfo;

/**
 * Created by jongrakmoon on 2017. 4. 2..
 */

public class DialogSelectFlightCategory extends DialogBase implements View.OnClickListener {
    private boolean isSuperLight;

    private ViewGroup mLayout1;
    private ViewGroup mLayout2;

    private ImageView mImageView1;
    private ImageView mImageView2;

    private TextView mTextView1;
    private TextView mTextView2;
    public static String SELECTED_PLANE = "SELECTED_PLANE";
    private FlightPlanInfo flightPlanInfo = null;

    public DialogSelectFlightCategory(@NonNull Context context) {
        super(context);
    }

    public DialogSelectFlightCategory(@NonNull Context context, FlightPlanInfo flightPlanInfo) {
        super(context);
        this.flightPlanInfo = flightPlanInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_flight_category);
        setLayout();


        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isPushButton) {
                    DialogSelectFlightCategory.this.onClick(findViewById(R.id.btn_cancel));
                }
            }
        });

    }

    private void setLayout() {
        mLayout1 = (ViewGroup) findViewById(R.id.bt_menu1);
        mLayout2 = (ViewGroup) findViewById(R.id.bt_menu2);

        mLayout1.setOnClickListener(this);
        mLayout2.setOnClickListener(this);

        mImageView1 = (ImageView) findViewById(R.id.iv_icon1);
        mImageView2 = (ImageView) findViewById(R.id.iv_icon2);

        mTextView1 = (TextView) findViewById(R.id.tv_menu1);
        mTextView2 = (TextView) findViewById(R.id.tv_menu2);

        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);

        setVisibilityLayout();
        selectMenu1();
    }

    private void setVisibilityLayout() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_menu1:
                selectMenu1();
                isSuperLight = false;
                break;
            case R.id.bt_menu2:
                isSuperLight = true;
                selectMenu2();
                break;
            case R.id.btn_ok:

                dismiss();

                break;
            case R.id.btn_cancel:

                Intent intent = new Intent(getContext(), NhsFlightWriteActivity.class);
                intent.putExtra(SELECTED_PLANE, isSuperLight);

                if (this.flightPlanInfo != null) {
                    intent.putExtra(DialogSelectFlightPlain.INTENT_PLAN_ID, this.flightPlanInfo.getPlanId());
                    intent.putExtra(DialogSelectFlightPlain.INTENT_PLAN_SN, this.flightPlanInfo.getPlanSn());
                }

                intent.putExtra(DialogSelectFlightPlain.IS_NEW_MODE, true);
                getContext().startActivity(intent);
                dismiss();
                break;
        }
    }

    private void selectMenu1() {
        mLayout1.setBackgroundResource(R.drawable.btn_type1_nor);
        mImageView1.setBackgroundResource(R.drawable.icon_airplane1_nor);
        mTextView1.setTextColor(Color.parseColor("#ffffff"));

        mLayout2.setBackgroundResource(R.drawable.btn_type1_pre);
        mImageView2.setBackgroundResource(R.drawable.icon_airplane2_dis);
        mTextView2.setTextColor(Color.parseColor("#5682c4"));
    }

    private void selectMenu2() {
        mLayout1.setBackgroundResource(R.drawable.btn_type1_dis);
        mImageView1.setBackgroundResource(R.drawable.icon_airplane1_dis);
        mTextView1.setTextColor(Color.parseColor("#5682c4"));

        mLayout2.setBackgroundResource(R.drawable.btn_type1_nor);
        mImageView2.setBackgroundResource(R.drawable.icon_airplane2_nor);
        mTextView2.setTextColor(Color.parseColor("#ffffff"));
    }
}
