package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import kr.go.molit.nhsnes.R;

/**
* 비행계획서 작성 후, 승인 요청 중 메세지
* @author FIESTA
* @since  오전 12:23
**/
public class DialogWaitFlight extends DialogBase {
    private Context context;
    private View.OnClickListener cancelListener;
    private String date = "";
    private String msg = "";

    public DialogWaitFlight(@NonNull Context context, String date, String msg, View.OnClickListener cancelListener) {
        super(context);
        this.context = context;
        this.cancelListener = cancelListener;

        this.date = date;
        this.msg = msg;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wait_flight);

        findViewById(R.id.btn_cancel).setOnClickListener(this.cancelListener);
        findViewById(R.id.btn_ok).setOnClickListener(this.cancelListener);

        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                cancelListener.onClick(findViewById(R.id.btn_ok));
            }
        });

        String msgs[] = msg.split("\\n");

        try {

            ((TextView) findViewById(R.id.tv_date)).setText(this.date);
            ((TextView) findViewById(R.id.tv_msg1)).setText(msgs[0]);
            ((TextView) findViewById(R.id.tv_msg2)).setText(msgs[1]);

        }catch (Exception ex) {

        }

    }

}
