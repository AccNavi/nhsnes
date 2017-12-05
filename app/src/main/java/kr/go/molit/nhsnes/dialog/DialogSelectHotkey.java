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
public class DialogSelectHotkey extends DialogBase{
    private Context context;
    private View.OnClickListener cancelListener;


    public DialogSelectHotkey(@NonNull Context context, View.OnClickListener cancelListener) {
        super(context);
        this.context = context;
        this.cancelListener = cancelListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_hotkey);

        findViewById(R.id.btn_cancel).setOnClickListener(this.cancelListener);
        findViewById(R.id.bt_menu1).setOnClickListener(this.cancelListener);
        findViewById(R.id.bt_menu2).setOnClickListener(this.cancelListener);
        findViewById(R.id.bt_menu3).setOnClickListener(this.cancelListener);
        findViewById(R.id.bt_menu4).setOnClickListener(this.cancelListener);
        findViewById(R.id.bt_menu5).setOnClickListener(this.cancelListener);
        findViewById(R.id.bt_menu6).setOnClickListener(this.cancelListener);


        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isPushButton) {
                    cancelListener.onClick(findViewById(R.id.btn_cancel));
                }
            }
        });


    }



}