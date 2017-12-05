package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.activity.NhsSelectPointActivity;
import kr.go.molit.nhsnes.interfaces.OnSearchWayPointListener;
import kr.go.molit.nhsnes.widget.TextViewEx;

/**
 * Created by jongrakmoon on 2017. 4. 1..
 */

public class DialogFavorites extends DialogBase implements View.OnClickListener {

    private DialogType1 errorDialog;
    private int mode = NhsSelectPointActivity.MODE_NONE;
    private OnSearchWayPointListener onSearchWayPointListener;

    public DialogFavorites(@NonNull Context context, int mode) {
        super(context);
        this.mode = mode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_favorites);

        View vMenu2 = findViewById(R.id.bt_menu2);
        View vMenu3 = findViewById(R.id.bt_menu3);
        View vMenu4 = findViewById(R.id.bt_menu4);
        View vMenu1 = findViewById(R.id.bt_menu1);
        View vMenu8 = findViewById(R.id.bt_menu8);

        vMenu2.setOnClickListener(this);
        vMenu3.setOnClickListener(this);
        vMenu4.setOnClickListener(this);
        vMenu8.setOnClickListener(this);

        findViewById(R.id.btn_cancel).setOnClickListener(this);

        TextViewEx tvOk = (TextViewEx)findViewById(R.id.tv_menu2);

        switch (mode) {

            case NhsSelectPointActivity.MODE_ARRIVAL:
                tvOk.setText("도착지 확정");
                break;

            case NhsSelectPointActivity.MODE_DEPARTURE:
                tvOk.setText("출발지 확정");
                break;

            case NhsSelectPointActivity.MODE_ROUTE:
                tvOk.setText("경유지 확정");
                vMenu3.setVisibility(View.GONE);
                vMenu1.setVisibility(View.VISIBLE);
                vMenu8.setVisibility(View.VISIBLE);
                ((TextViewEx)vMenu4.findViewById(R.id.tv_menu4)).setText("Waypoint 삭제");
                break;

            default:
                break;


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.bt_menu2:
                if (this.onSearchWayPointListener != null) {
                    this.onSearchWayPointListener.onComplate();
                }
                dismiss();
                break;
            case R.id.bt_menu3:
                DialogCoordinateAliases dialog = new DialogCoordinateAliases(getContext());
                dialog.show();
                break;

            case R.id.bt_menu8:
                dismiss();
                break;

            case R.id.bt_menu1:
                new DialogChangeLocation(getContext()).show();

                break;
            case R.id.bt_menu4:

                switch (mode) {

                    case NhsSelectPointActivity.MODE_ARRIVAL:
                    case NhsSelectPointActivity.MODE_DEPARTURE:
                        errorDialog = new DialogType1(getContext(), "삭제 확인", getContext().getString(R.string.err_message_5), getContext().getString(R.string.btn_confirm), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                errorDialog.hideDialog();
                                // TODO: 2017. 4. 24. 수정 기능 추가

                                if (onSearchWayPointListener != null) {
                                    onSearchWayPointListener.onDelete();
                                }
                                DialogFavorites.this.dismiss();
                                //Toast.makeText(getContext(), "삭제", Toast.LENGTH_SHORT).show();

                            }
                        }, getContext().getString(R.string.btn_cancel), new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                errorDialog.hideDialog();
                            }
                        });

                        break;

                    case NhsSelectPointActivity.MODE_ROUTE:
                        errorDialog = new DialogType1(getContext(), "삭제 확인", "Waypoint 정보를\n삭제하시겠습니까?", getContext().getString(R.string.btn_confirm), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                errorDialog.hideDialog();
                                // TODO: 2017. 4. 24. 수정 기능 추가

                                if (onSearchWayPointListener != null) {
                                    onSearchWayPointListener.onDelete();
                                }
//                                if (onSearchWayPointListener != null) {
//                                    onSearchWayPointListener.onCancel();
//                                }
//
//                                Toast.makeText(getContext(), "삭제", Toast.LENGTH_SHORT).show();
                                DialogFavorites.this.dismiss();


                            }
                        }, getContext().getString(R.string.btn_cancel), new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                errorDialog.hideDialog();
                            }
                        });

                        break;

                }

                break;
        }
    }

    public OnSearchWayPointListener getOnSearchWayPointListener() {
        return onSearchWayPointListener;
    }

    public void setOnSearchWayPointListener(OnSearchWayPointListener onSearchWayPointListener) {
        this.onSearchWayPointListener = onSearchWayPointListener;
    }

}
