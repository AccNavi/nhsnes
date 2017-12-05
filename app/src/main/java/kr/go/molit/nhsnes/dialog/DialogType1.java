package kr.go.molit.nhsnes.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import kr.go.molit.nhsnes.R;


/**
 * 확인, 취소 버튼이 있는 팝업
 * (개인정보 수집, 이용동의 팝업)
 * @author 정제영
 * @version 1.0, 2017.03.07 최초 작성
 **/

public class DialogType1{
    Context _context;
    DialogBase dialog;

    TextView tv_title;
    TextView tv_msg;
    TextView btn_ok;
    TextView btn_cancel;

    //***************************************


    public DialogType1(Context context){

        _context = context;
    }

    public DialogType1(Context context, String title, String msg, String ok, View.OnClickListener okListener, String cancel, View.OnClickListener cancelListener){

        _context = context;
        // 일반 팝업 다이얼로그로 진입 => 버튼 2개
        showCustomDialog(title, msg, ok, okListener, cancel, cancelListener);
    }

    /**
     * 다이얼로그 숨기기
     * @date 2017. 3. 07.
     * @author 정제영
     * @brief
     */
    public void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 다이얼로그를 사용하고 있는지 확인
     * @date 2017. 3. 07.
     * @author 정제영
     * @return
     * @brief
     */
    public boolean isShowDialog(){
        if(dialog != null && dialog.isShowing()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 팝업 띄우기
     * @date 2017. 3. 07.
     * @author 정제영
     * @param title 타이틀
     * @param msg 메시지
     * @param ok 좌측 버튼 텍스트
     * @param okListener 좌측버튼 리스너
     * @param cancel 우측 버튼 텍스트
     * @param cancelListener 우측버튼 리스너
     * @return
     * @brief
     */
    private void showCustomDialog(String title, String msg,
                                  String ok, final View.OnClickListener okListener, String cancel, View.OnClickListener cancelListener){

        dialog = new DialogBase(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //팝업 주위를 눌러도 창 닫히지 않음
        dialog.setCanceledOnTouchOutside(false);
        //하드웨어 백버튼 눌러도 닫히지 않음
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.dialog_type1);

        tv_title = (TextView)dialog.findViewById(R.id.tv_title);
        tv_msg = (TextView)dialog.findViewById(R.id.tv_msg);

        tv_title.setText(title);
        tv_msg.setText(msg);

        btn_ok = (TextView) dialog.findViewById(R.id.btn_ok);
        btn_cancel = (TextView) dialog.findViewById(R.id.btn_cancel);

        if (ok == null || ok.length() == 0) {
            btn_ok.setVisibility(View.GONE);
        } else {
            btn_ok.setText(ok);
        }

        if (okListener != null) {
            btn_ok.setOnClickListener(okListener);
        }

        if (cancel == null || cancel.length() == 0) {
            btn_cancel.setVisibility(View.GONE);
        } else {
            btn_cancel.setText(cancel);
        }

        if (cancelListener != null) {
            btn_cancel.setOnClickListener(cancelListener);
        }

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                if (dialog.isPushButton) {
                    okListener.onClick(btn_ok);
                }
            }
        });

        //팝업 바탕 알파값 주기
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount= 0.95f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.show();
    }
}
