package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import kr.go.molit.nhsnes.R;

/**
 * Created by jongrakmoon on 2017. 4. 1..
 */

public class DialogNotamInfo extends DialogBase implements View.OnClickListener {

    public DialogNotamInfo(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_notam_info);
        findViewById(R.id.btn_cancel).setOnClickListener(this);

        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isPushButton) {
                    DialogNotamInfo.this.onClick(findViewById(R.id.btn_cancel));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }
}
