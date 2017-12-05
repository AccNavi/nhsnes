package kr.go.molit.nhsnes.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import kr.go.molit.nhsnes.R;

/**
 * Created by jongrakmoon on 2017. 4. 2..
 */

public class DialogChangeLocation extends DialogBase implements View.OnClickListener {
    private Context context;

    public DialogChangeLocation(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_change_location);

        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);

        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isPushButton) {
                    DialogChangeLocation.this.onClick(findViewById(R.id.btn_ok));
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                dismiss();
                // TODO: 2017. 4. 25. NhsFavoritesActivity도 종료해야함
                ((Activity) context).finish();

                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }
}
