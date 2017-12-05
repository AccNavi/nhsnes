package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.activity.NhsSelectPointActivity;
import kr.go.molit.nhsnes.interfaces.OnSearchWayPointListener;
import kr.go.molit.nhsnes.widget.TextViewEx;


/**
* 맵 지도의 경로 확정을 붇는 다이얼로그
* @author FIESTA
* @since  오후 8:02
**/
public class DialogConfirmSelectMap extends DialogBase {

    private DialogType1 errorDialog;
    private int mode = NhsSelectPointActivity.MODE_NONE;
    private OnSearchWayPointListener onSearchWayPointListener;
    private View.OnClickListener onClickListener;
    private int addWayPointCnt = 0;
    private View vFavorite = null;  // 즐겨 찾기 추가
    private boolean isSingleMode =false;

    public DialogConfirmSelectMap(@NonNull Context context, int mode, View.OnClickListener onClickListener) {
        super(context);
        this.mode = mode;
        this.onClickListener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confrim_select_map);

        TextViewEx tvOk = (TextViewEx)findViewById(R.id.tv_menu2);
        TextViewEx tvTitle = (TextViewEx)findViewById(R.id.tv_title);
        TextViewEx tvCancel = (TextViewEx)findViewById(R.id.tv_menu3);
        this.vFavorite = findViewById(R.id.btn_add_favorite);
        this.vFavorite.setOnClickListener(this.onClickListener);

        findViewById(R.id.btn_complate).setOnClickListener(this.onClickListener);
        findViewById(R.id.btn_cancel).setOnClickListener(this.onClickListener);

        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isPushButton) {
                    DialogConfirmSelectMap.this.onClickListener.onClick(findViewById(R.id.btn_complate));
                }
            }
        });

        if (isSingleMode) {
            this.vFavorite.setVisibility(View.VISIBLE);
        }

        switch (mode) {

            case NhsSelectPointActivity.MODE_ARRIVAL:
                tvOk.setText("도착지 확정");
                tvTitle.setText("도착지");
                break;

            case NhsSelectPointActivity.MODE_DEPARTURE:
                tvOk.setText("출발지 확정");
                tvTitle.setText("출발지");
                break;

            case NhsSelectPointActivity.MODE_ROUTE:
                tvTitle.setText("경유지");

                tvOk.setText("경유지 추가");
                tvCancel.setText("경로 확정");

                if (this.addWayPointCnt >= 10) {
                    tvOk.setEnabled(true);
                }

                break;

            case NhsSelectPointActivity.MODE_ROUTE_SEARCH:
                tvOk.setText("경로 순서 변경");
                tvTitle.setText("변경");
                break;

            default:
                break;


        }


    }

    public int getAddWayPointCnt() {
        return addWayPointCnt;
    }

    public void setAddWayPointCnt(int addWayPointCnt) {
        this.addWayPointCnt = addWayPointCnt;
    }

    /**
    * 즐겨찾기 추가 버튼을 보여준다.
    * @author FIESTA
    * @version 1.0.0
    * @since 2017-10-20 오전 10:14
    **/
    public void visibleFavoriteButton(){
        this.isSingleMode = true;
    }

}
