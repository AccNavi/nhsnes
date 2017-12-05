package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import kr.go.molit.nhsnes.R;

/**
* (주행 완료) 비행완료보고
* @author FIESTA
* @since  오전 3:37
**/
public class DialogSendReportFlight extends DialogBase {

    private View.OnClickListener onClickListener;

    public DialogSendReportFlight(@NonNull Context context, View.OnClickListener onClickListener) {
        super(context);
        this.onClickListener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_report_complate_flight);
        findViewById(R.id.btn_cancel).setOnClickListener(this.onClickListener);

        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                onClickListener.onClick(findViewById(R.id.btn_cancel));
            }
        });
    }

}
