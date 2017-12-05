package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import kr.go.molit.nhsnes.R;

/**
 * Created by jongrakmoon on 2017. 4. 1..
 */

public class DialogFindLocationName extends DialogBase implements View.OnClickListener {

    public DialogFindLocationName(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_find_location_name);


        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.bt_menu1).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.bt_menu1:
                break;
        }
    }
}
