package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.net.realm.FlightPlanInfo;
import kr.go.molit.nhsnes.widget.TextViewEx;
import kr.go.molit.nhsnes.net.realm.FlightPlanInfo;
import kr.go.molit.nhsnes.widget.TextViewEx;

/**
 * Created by jongrakmoon on 2017. 4. 3..
 */

public class DialogFlightDeny extends DialogBase implements View.OnClickListener {

    private FlightPlanInfo result;

    public DialogFlightDeny(@NonNull Context context, @NonNull FlightPlanInfo result) {
        super(context);
        this.result = result;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_flight_deny);
        findViewById(R.id.btn_view).setOnClickListener(this);
        findViewById(R.id.btn_modify).setOnClickListener(this);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String date = sdf.format(new Date(this.result.getPlanDate()));//.getRegDate()));

        ((TextViewEx) findViewById(R.id.tve_date)).setText(date);
        ((TextViewEx) findViewById(R.id.tve_msg1)).setText(this.result.getPlanDeparture());//.getDepartureAerodrome());
        ((TextViewEx) findViewById(R.id.tve_msg2)).setText(this.result.getPlanArrival());//.getArrivalAerodrome());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_view:
                dismiss();
                break;
            case R.id.btn_modify:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                String date = sdf.format(new Date());

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

                        }catch (Exception ex) {

                        }


                    }
                }, 2000);

                break;
        }
    }
}
