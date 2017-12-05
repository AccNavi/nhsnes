package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.model.NhsFavoriteModel;
import kr.go.molit.nhsnes.widget.EditTextEx;

/**
* 좌표 별칭 지정
* @author FIESTA
* @since  오전 12:18
**/
public class DialogCoordinateAliases extends DialogBase implements View.OnClickListener {

    private NhsFavoriteModel selectedModel;


    public DialogCoordinateAliases(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_coordinate_aliases);

        EditTextEx edName = (EditTextEx)findViewById(R.id.et_n_2);
        edName.setCustomStyle(getContext(), 5); // input type를 test로 변경

        // 클릭 리스너 등록
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);

        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isPushButton) {
                    DialogCoordinateAliases.this.onClick(findViewById(R.id.btn_ok));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
            case R.id.btn_cancel:
                dismiss();
                break;
           
        }
    }


}
