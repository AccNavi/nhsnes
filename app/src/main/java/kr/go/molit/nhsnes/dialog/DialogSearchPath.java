package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.activity.NhsMapSearchActivity;
import kr.go.molit.nhsnes.net.realm.FlightPlanInfo;
import kr.go.molit.nhsnes.widget.TextViewEx;

import static kr.go.molit.nhsnes.activity.NhsMapSearchActivity.POPUP_COMPLATE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.KEY_MODE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.MODE_SIMULATION;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.SHOW_POPUP;

/**
 * Created by jongrakmoon on 2017. 4. 2..
 */

public class DialogSearchPath extends DialogBase implements View.OnClickListener {
    TextViewEx textViewExTitle;

    Context context;

    TextViewEx tv_planArrival;
    TextViewEx tv_departureAerodrome;
    TextViewEx tv_waypoint;
    TextViewEx tv_plandate;

    FlightPlanInfo mFlightPlanInfo = null;

    public DialogSearchPath(Context context, FlightPlanInfo info) {
        super(context);

        this.context = context;
        this.mFlightPlanInfo = info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_search_path);

        ((TextViewEx) findViewById(R.id.tv_title)).setText(this.mFlightPlanInfo.getPlanDeparture());
        findViewById(R.id.btn_simulation).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);

        this.tv_planArrival = ((TextViewEx) findViewById(R.id.tv_title));
        this.tv_departureAerodrome = ((TextViewEx) findViewById(R.id.tv_departureAerodrome));
        this.tv_waypoint = ((TextViewEx) findViewById(R.id.tv_waypoint));
        this.tv_plandate = ((TextViewEx) findViewById(R.id.tv_plandate));

        this.tv_planArrival.setText(mFlightPlanInfo.getPlanArrival());
        this.tv_departureAerodrome.setText(mFlightPlanInfo.getPlanDeparture());
        this.tv_waypoint.setText(mFlightPlanInfo.getPlanRoute());
        this.tv_plandate.setText(mFlightPlanInfo.getPlanDate());

        ((TextViewEx)findViewById(R.id.tv_total_distance)).setText(mFlightPlanInfo.getTotalDistanc()+" miles");
        ((TextViewEx)findViewById(R.id.tv_avg_speed)).setText(mFlightPlanInfo.getAvgSpeed()+" Knotd");
        ((TextViewEx)findViewById(R.id.tv_avg_altitue)).setText(mFlightPlanInfo.getAvgAltitude()+" ft");
        ((TextViewEx)findViewById(R.id.tv_total_flight_time)).setText((mFlightPlanInfo.getTotalFlightTime()/1000)/60+" Min");
        ((TextViewEx)findViewById(R.id.tv_start_time)).setText(mFlightPlanInfo.getStartTime());
        ((TextViewEx)findViewById(R.id.tv_end_time)).setText(mFlightPlanInfo.getEndTime());

        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isPushButton) {
                    DialogSearchPath.this.onClick(findViewById(R.id.btn_simulation));
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_simulation:
                Intent searchMap = new Intent(context, NhsMapSearchActivity.class);
                searchMap.putExtra(KEY_MODE, MODE_SIMULATION);
                searchMap.putExtra("data", this.mFlightPlanInfo);
                searchMap.putExtra(SHOW_POPUP, POPUP_COMPLATE);
                //searchMap.putExtra(DATA_START, startData);
                //searchMap.putExtra(DATA_END, endData);
                //searchMap.putExtra(DATA_ROUTE, routeData);
                context.startActivity(searchMap);
                dismiss();
                break;
            case R.id.btn_close:
                dismiss();
                break;
        }
    }
}
