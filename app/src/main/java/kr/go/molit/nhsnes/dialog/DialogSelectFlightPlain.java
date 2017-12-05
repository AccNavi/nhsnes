package kr.go.molit.nhsnes.dialog;

import static kr.go.molit.nhsnes.common.DateTimeUtil.DEFUALT_DATE_FORMAT1;
import static kr.go.molit.nhsnes.common.DateTimeUtil.DEFUALT_DATE_FORMAT8;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.activity.NhsFlightWriteActivity;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.model.NhsFlightPlainModel;
import kr.go.molit.nhsnes.net.realm.FlightPlanInfo;
import kr.go.molit.nhsnes.widget.TextViewEx;

/**
 * Created by jongrakmoon on 2017. 4. 1..
 */

public class DialogSelectFlightPlain extends DialogBase implements View.OnClickListener {
  public static final String INTENT_PLAN_ID = "INTENT_PLAN_ID";
  public static final String INTENT_PLAN_SN = "INTENT_PLAN_SN";
  public static final String INTENT_PLAN_DATA = "INTENT_PLAN_DATA";
  public static final String INTENT_PLAN_TYPE = "INTENT_PLAN_TYPE";
  public final static String IS_NEW_MODE = "is_new_mode";   // 글쓰기 모드인가?

  String planId;
  Context context;
  FlightPlanInfo result = null;

  String flightStatus = "00";

  public DialogSelectFlightPlain(@NonNull Context context, @NonNull String planId) {
    super(context);
    this.context = context;
    this.planId = planId;
  }

  public DialogSelectFlightPlain(@NonNull Context context, @NonNull FlightPlanInfo result) {
    super(context);
    this.context = context;
    this.result = result;
    this.planId = result.getPlanId();        //result.getFlightIdentity();
    this.flightStatus =  result.getPlanStatus(); //result.getFlightStatus();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_select_flight_plain);

    TextViewEx tveTitle = (TextViewEx) findViewById(R.id.tv_title);

    findViewById(R.id.btn_cancel).setOnClickListener(this);
    findViewById(R.id.bt_menu1).setOnClickListener(this);
    findViewById(R.id.bt_menu2).setOnClickListener(this);
    findViewById(R.id.bt_menu3).setOnClickListener(this);
    findViewById(R.id.bt_menu4).setOnClickListener(this);
    findViewById(R.id.bt_menu2_1).setOnClickListener(this);


    setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss(DialogInterface dialogInterface) {

        if (isPushButton) {
          DialogSelectFlightPlain.this.onClick(findViewById(R.id.btn_cancel));
        }
      }
    });

//    if (this.flightStatus.trim().equals("00")){// == NhsFlightPlainModel.FLIGT_STATUS_NONE) {
//      findViewById(R.id.bt_menu2).setVisibility(View.GONE);
//      findViewById(R.id.bt_menu2_1).setVisibility(View.VISIBLE);
//    }

    // title 제목을 설정한다
    Date date = Util.convertStringToDate(this.result.getPlanDate(), DEFUALT_DATE_FORMAT1);
    SimpleDateFormat std = new SimpleDateFormat(DEFUALT_DATE_FORMAT8, Locale.US);
    String convertStrDate = std.format(date);
    tveTitle.setText(convertStrDate + " " + this.result.getCallsign());

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_cancel:
        dismiss();
        break;
      case R.id.bt_menu1:
        Intent intent = new Intent(getContext(), NhsFlightWriteActivity.class);
        intent.putExtra(INTENT_PLAN_ID, result.getPlanId());
        intent.putExtra(INTENT_PLAN_SN, result.getPlanSn());
        getContext().startActivity(intent);
        dismiss();
        break;
      case R.id.bt_menu2:
        DialogFlightAgree dialogFlightAgree = new DialogFlightAgree(context,result);
        dialogFlightAgree.setOnDismissListener(new OnDismissListener() {
          @Override
          public void onDismiss(DialogInterface dialogInterface) {
            DialogSelectFlightPlain.this.dismiss();
          }
        });
        dialogFlightAgree.show();
        break;
      case R.id.bt_menu3:
        DialogModifyFlightPlain dialogModifyFlightPlain = new DialogModifyFlightPlain(context, result);
        dialogModifyFlightPlain.show();
        dismiss();
        break;
      case R.id.bt_menu4:
        DialogDeleteFlightPlain dialogDeleteFlightPlain = new DialogDeleteFlightPlain(context, result);
        dialogDeleteFlightPlain.show();
        dismiss();
        break;

      case R.id.bt_menu2_1:


        // test
//        DialogFlightAgree dialogFlightAgree1 = new DialogFlightAgree(context,result);
//        dialogFlightAgree1.show();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String date = sdf.format(new Date());

        try {
          final DialogWaitFlight dialogWaitFlight = new DialogWaitFlight(getContext(), date, this.result.getPlanRoute(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              dismiss();

            }
          });

          dialogWaitFlight.show();


          // 2초 뒤에 자동 종료
          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

              try {

                if (dialogWaitFlight.isShowing()) {
                  dialogWaitFlight.dismiss();
                }

                dismiss();

              } catch (Exception ex) {

              }


            }
          }, 2000);

        } catch (Exception ex) {
          ex.printStackTrace();
        }

        break;

    }
  }
}