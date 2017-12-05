package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.LinkedList;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.activity.NhsSelectPointActivity;
import kr.go.molit.nhsnes.interfaces.OnSearchWayPointListener;
import kr.go.molit.nhsnes.model.NhsBaseModel;
import kr.go.molit.nhsnes.model.NhsDestinationSearchModel;
import kr.go.molit.nhsnes.model.NhsWaypoinSearchModel;
import kr.go.molit.nhsnes.widget.TextViewEx;

/**
 * Created by jongrakmoon on 2017. 4. 2..
 */

public class DialogConfirmPath extends DialogBase implements View.OnClickListener {

    private int mode = NhsSelectPointActivity.MODE_NONE;
    private LinkedList<NhsBaseModel> dataList = new LinkedList<>();
    private OnSearchWayPointListener onSearchWayPointListener;

    public DialogConfirmPath(@NonNull Context context) {
        super(context);
    }

    public DialogConfirmPath(@NonNull Context context, NhsBaseModel nhsDestinationSearchModel, int mode) {
        super(context);
        this.mode = mode;
        this.dataList.add(nhsDestinationSearchModel);
    }

    public DialogConfirmPath(@NonNull Context context, Object dataList, int mode) {
        super(context);
        this.mode = mode;
        this.dataList = (LinkedList<NhsBaseModel>) dataList;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_path);

        View vSave = findViewById(R.id.ll_save);
        View vComplate = findViewById(R.id.ll_complate);
        TextViewEx tvComplateButton = (TextViewEx)findViewById(R.id.tv_menu2);
        TextViewEx tvContent = (TextViewEx)findViewById(R.id.tv_content);

        findViewById(R.id.btn_simulation).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_conplate).setOnClickListener(this);

        StringBuffer sbContent = new StringBuffer();

        int size = dataList.size();
        int i = 0;
        NhsBaseModel nhsBaseModel = null;
        NhsDestinationSearchModel nhsDestinationSearchModel = null;
        NhsWaypoinSearchModel nhsWaypoinSearchModel = null;

        for (i=0; i<size; i++) {

            nhsBaseModel = dataList.get(i);

            if (nhsBaseModel instanceof NhsDestinationSearchModel) {
                nhsDestinationSearchModel = (NhsDestinationSearchModel)nhsBaseModel;
                sbContent.append(nhsDestinationSearchModel.getLatitude() + " " + nhsDestinationSearchModel.getLongitude() + "\n");
            } else if (nhsBaseModel instanceof NhsWaypoinSearchModel){
                nhsWaypoinSearchModel = (NhsWaypoinSearchModel)nhsBaseModel;
                sbContent.append(nhsWaypoinSearchModel.getLatitude() + " " + nhsWaypoinSearchModel.getLongitude() + "\n");
            }


        }


        switch (mode) {

            case NhsSelectPointActivity.MODE_DEPARTURE:
                vComplate.setVisibility(View.VISIBLE);
                vSave.setVisibility(View.GONE);
                tvContent.setText(sbContent.toString());
                tvComplateButton.setText("출발지 확정");
                break;

            case NhsSelectPointActivity.MODE_ARRIVAL:
                vComplate.setVisibility(View.VISIBLE);
                vSave.setVisibility(View.GONE);
                tvContent.setText(sbContent.toString());
                tvComplateButton.setText("목직지 확정");
                break;
            case NhsSelectPointActivity.MODE_ROUTE:
                vComplate.setVisibility(View.GONE);
                vSave.setVisibility(View.VISIBLE);
                tvContent.setText(sbContent.toString());
                break;

            default:
                vComplate.setVisibility(View.GONE);
                break;

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_simulation:
                dismiss();
                break;
            case R.id.btn_save:

                if (this.onSearchWayPointListener != null) {
                    this.onSearchWayPointListener.onComplate();
                }
                dismiss();
                break;
            case R.id.btn_conplate:

                if (this.onSearchWayPointListener != null) {
                    this.onSearchWayPointListener.onComplate();
                }

                this.dismiss();

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
