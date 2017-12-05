package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import kr.go.molit.nhsnes.R;

/**
 * Created by jongrakmoon on 2017. 4. 2..
 */

public class DialogPointAliasSpecify extends DialogBase implements View.OnClickListener {
    public DialogPointAliasSpecify(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_point_alias_specify);

        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);

        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isPushButton) {
                    DialogPointAliasSpecify.this.onClick(findViewById(R.id.btn_ok));
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }
}
