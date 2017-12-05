package kr.go.molit.nhsnes.dialog;

/**
 * Created by yeonjukim on 2017. 5. 4..
 */


import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.widget.TextViewEx;

/**
 * Created by jongrakmoon on 2017. 4. 1..
 */

public class DialogAppVersion extends DialogBase implements View.OnClickListener {

    private String UPDATED_VERSION = "1.0.0";

    public DialogAppVersion(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_app_version);

        setLayout();
    }

    private void setLayout() {
        findViewById(R.id.btn_no).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_yes).setOnClickListener(this);

        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isPushButton) {
                    DialogAppVersion.this.onClick(findViewById(R.id.btn_yes));
                }
            }
        });

        TextViewEx tvVersion = (TextViewEx) findViewById(R.id.tv_version);

        PackageManager manager = getContext().getPackageManager();
        try {
            String currentVersion = manager.getPackageInfo(getContext().getPackageName(), 0).versionName;
            tvVersion.setText(currentVersion);

            if (!currentVersion.equals(UPDATED_VERSION)) {
                findViewById(R.id.already_update_layout).setVisibility(View.GONE);
                findViewById(R.id.not_updated_layout).setVisibility(View.VISIBLE);

            } else {
                findViewById(R.id.already_update_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.not_updated_layout).setVisibility(View.GONE);

            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            tvVersion.setText("알 수 없음");

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                // TODO: 2017. 5. 4. 구글 플레이 연동
                break;
            case R.id.btn_no:
            case R.id.btn_close:
                dismiss();
                break;
        }
    }
}
