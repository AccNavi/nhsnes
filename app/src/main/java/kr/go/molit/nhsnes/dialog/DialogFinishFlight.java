package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import kr.go.molit.nhsnes.R;

/**
* 비행 완료
* @author FIESTA
* @since  오전 12:23
**/
public class DialogFinishFlight extends DialogBase {
    private Context context;
    private View.OnClickListener cancelListener;


    public DialogFinishFlight(@NonNull Context context, View.OnClickListener cancelListener) {
        super(context);
        this.context = context;
        this.cancelListener = cancelListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_finish_flight);

        findViewById(R.id.btn_cancel).setOnClickListener(this.cancelListener);
        findViewById(R.id.fl_favorites).setOnClickListener(this.cancelListener);
        findViewById(R.id.fl_finish_flight).setOnClickListener(this.cancelListener);

        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isPushButton) {
                    DialogFinishFlight.this.cancelListener.onClick(findViewById(R.id.btn_cancel));
                }
            }
        });

    }



}
