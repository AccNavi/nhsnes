package kr.go.molit.nhsnes.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.Util;

/**
 * 앱 실행 시 기록 데이터 저장 매체 설정 팝업
 *
 * @author 정제영
 * @version 1.0, 2017.03.08 최초 작성
 **/

public class DialogSelectStorage extends DialogBase{


    Activity activity;
    Dialog dialog;

    FrameLayout bt_menu1;
    FrameLayout bt_menu2;

    ImageView iv_icon1;
    ImageView iv_icon2;

    TextView tv_title;

    TextView tv_menu1;
    TextView tv_menu2;

    TextView btn_ok;
    TextView btn_cancel;

    private StorageUtil.Storage saveType;

    //***************************************


    public DialogSelectStorage(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public DialogSelectStorage(Activity activity, String title, String ok, View.OnClickListener okListener, String cancel, View.OnClickListener cancelListener) {
        super(activity);

        this.activity = activity;
        // 일반 팝업 다이얼로그로 진입 => 버튼 2개
        showCustomDialog(title, ok, okListener, cancel, cancelListener);
    }

    /**
     * 다이얼로그 숨기기
     *
     * @date 2017. 3. 08.
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
     *
     * @return
     * @date 2017. 3. 08.
     * @author 정제영
     * @brief
     */
    public boolean isShowDialog() {
        if (dialog != null && dialog.isShowing()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 팝업 띄우기
     *
     * @param title          타이틀
     * @param ok             좌측 버튼 텍스트
     * @param okListener     좌측버튼 리스너
     * @param cancel         우측 버튼 텍스트
     * @param cancelListener 우측버튼 리스너
     * @return
     * @date 2017. 3. 08.
     * @author 정제영
     * @brief
     */
    private void showCustomDialog(String title,
                                  String ok, final View.OnClickListener okListener, String cancel, View.OnClickListener cancelListener) {
        Log.d("JeLib","showCustomDialog::");
        dialog = new DialogBase(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //팝업 주위를 눌러도 창 닫히지 않음
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.dialog_select_storage);

        bt_menu1 = (FrameLayout) dialog.findViewById(R.id.bt_menu1);
        bt_menu2 = (FrameLayout) dialog.findViewById(R.id.bt_menu2);
        bt_menu1.setOnClickListener(clickListener);
        bt_menu2.setOnClickListener(clickListener);

        iv_icon1 = (ImageView) dialog.findViewById(R.id.iv_icon1);
        iv_icon2 = (ImageView) dialog.findViewById(R.id.iv_icon2);

        tv_menu1 = (TextView) dialog.findViewById(R.id.tv_menu1);
        tv_menu2 = (TextView) dialog.findViewById(R.id.tv_menu2);

        tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        tv_title.setText(title);

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

        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                if (isPushButton) {
                    okListener.onClick(findViewById(R.id.btn_ok));
                }
            }
        });

        if (cancelListener != null) {
            btn_cancel.setOnClickListener(cancelListener);
        }

        //팝업 바탕 알파값 주기
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.95f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        String dir = Util.getMicroSDCardDirectory();
        Log.d("JeLib","dir::"+dir);
        if(dir!=null && dir.length() > 0)
        {
            bt_menu2.setVisibility(View.VISIBLE);
        } else {
            clickListener.onClick(bt_menu1);
            bt_menu2.setVisibility(View.GONE);
        }
        dialog.show();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_menu1:
                    saveType = StorageUtil.Storage.LOCAL;
                    bt_menu1.setBackgroundResource(R.drawable.btn_type1_nor);
                    iv_icon1.setBackgroundResource(R.drawable.icon_localmemory_nor);
                    tv_menu1.setTextColor(Color.parseColor("#ffffff"));

                    bt_menu2.setBackgroundResource(R.drawable.btn_type1_pre);
                    iv_icon2.setBackgroundResource(R.drawable.icon_memory_dis);
                    tv_menu2.setTextColor(Color.parseColor("#5682c4"));
                    break;
                case R.id.bt_menu2:
                    if (StorageUtil.checkStoragePermission(activity)) {
                        saveType = StorageUtil.Storage.EXTERNAL;
                        bt_menu2.setBackgroundResource(R.drawable.btn_type1_nor);
                        iv_icon2.setBackgroundResource(R.drawable.icon_memory_nor);
                        tv_menu2.setTextColor(Color.parseColor("#ffffff"));

                        bt_menu1.setBackgroundResource(R.drawable.btn_type1_pre);
                        iv_icon1.setBackgroundResource(R.drawable.icon_localmemory_dis);
                        tv_menu1.setTextColor(Color.parseColor("#5682c4"));
                    }
                    break;

            }
        }
    };

    public StorageUtil.Storage getSaveType() {
        return saveType;
    }


}
