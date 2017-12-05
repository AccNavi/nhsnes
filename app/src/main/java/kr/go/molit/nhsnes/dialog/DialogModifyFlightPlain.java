package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.activity.NhsFlightWriteActivity;
import kr.go.molit.nhsnes.net.realm.FlightPlanInfo;

import static kr.go.molit.nhsnes.dialog.DialogSelectFlightPlain.INTENT_PLAN_ID;
import static kr.go.molit.nhsnes.dialog.DialogSelectFlightPlain.INTENT_PLAN_SN;

/**
 * Created by jongrakmoon on 2017. 4. 2..
 */

public class DialogModifyFlightPlain extends DialogBase implements View.OnClickListener {
    @NonNull
    private final Context context;
    private FlightPlanInfo mFlightPlanInfo;
    public DialogModifyFlightPlain(@NonNull Context context, FlightPlanInfo mFlightPlanInfo) {
        super(context);
        this.context = context;
        this.mFlightPlanInfo = mFlightPlanInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_modify_flight_plain);

        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);

        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isPushButton) {
                    DialogModifyFlightPlain.this.onClick(findViewById(R.id.btn_ok));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                // TODO: 2017. 5. 4. 수정 기능 추가
                Intent intent = new Intent(getContext(), NhsFlightWriteActivity.class);
                intent.putExtra(INTENT_PLAN_ID, mFlightPlanInfo.getPlanId());
                intent.putExtra(INTENT_PLAN_SN, mFlightPlanInfo.getPlanSn());
                getContext().startActivity(intent);
                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }


}
