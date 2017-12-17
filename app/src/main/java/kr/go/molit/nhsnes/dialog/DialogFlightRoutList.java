package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.activity.NhsLoginActivity;
import kr.go.molit.nhsnes.adapter.RecyclerMainNhsFlightPlanListAdapter;
import kr.go.molit.nhsnes.common.DateTimeUtil;
import kr.go.molit.nhsnes.common.MagicSE_Util;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.net.model.FlightPlanModel;
import kr.go.molit.nhsnes.net.model.NetSecurityModel;
import kr.go.molit.nhsnes.net.realm.FlightPlanInfo;
import kr.go.molit.nhsnes.net.service.FlightPlanService;
import kr.go.molit.nhsnes.net.service.NetConst;
import kr.go.molit.nhsnes.net.service.NetUtil;
import kr.go.molit.nhsnes.widget.TextViewEx;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kr.go.molit.nhsnes.adapter.RecyclerMainNhsFlightPlanListAdapter.VIEWTYPE_NHS_FLIGHT_PLAN_GET_ROUTE;
import static kr.go.molit.nhsnes.adapter.RecyclerMainNhsFlightPlanListAdapter.VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_TMP;

/**
* 경로 가져오기 팝업
* @author FIESTA
* @since  오전 3:37
**/
public class DialogFlightRoutList extends DialogBase implements View.OnClickListener {

    public interface OnClickListener{
        public void onClickList(FlightPlanInfo flightPlanInfo);
    }


    private List<FlightPlanInfo> flightPlanList =   new ArrayList<FlightPlanInfo>();
    private RecyclerMainNhsFlightPlanListAdapter mRecyclerMainNhsFlightPlanListAdapter =   null;
    private RecyclerView rvList;
    private OnClickListener onClickListener = null;

    public DialogFlightRoutList(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_flight_rout_list);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        setLayout();
        callFlightPlan("");
    }


    /**
     * 레이어 설정
     * @author FIESTA
     * @since  오전 3:37
     **/
    private void setLayout(){

        rvList = (RecyclerView) findViewById(R.id.rv_plan_list);
        rvList.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerMainNhsFlightPlanListAdapter = new RecyclerMainNhsFlightPlanListAdapter(null, VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_TMP, getContext());
        rvList.setAdapter(mRecyclerMainNhsFlightPlanListAdapter);
        mRecyclerMainNhsFlightPlanListAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 클릭한 다이얼로그 위치
                int position = (int)view.getTag();

                if (onClickListener != null) {
                    onClickListener.onClickList(flightPlanList.get(position));
                }

                // 다이얼로그 창 닫기
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    private void callFlightPlan(final String planStatus){
        final LoadingDialog loading = LoadingDialog.create(getContext(),null,null);
        loading.show();

        String planMbrId = StorageUtil.getStorageModeEx(getContext(), NhsLoginActivity.LOGIN_MBR_ID);//getStorageMode(this,NhsLoginActivity.LOGIN_MBR_ID);
        FlightPlanService service = FlightPlanService.retrofit.create(FlightPlanService.class);
        String planDate = DateTimeUtil.date(DateTimeUtil.DEFUALT_DATE_FORMAT1);
        String planDoccd = "00";    //이부분 수정해야함

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("planMbrId",planMbrId);
        params.put("planDate",planDate);
//        params.put("planDate","20170912");  // test
        params.put("planStatus",planStatus.trim());
        params.put("planDoccd",planDoccd);
        RequestBody body = NetUtil.mapToJsonBody(getContext(), params);

        Call<NetSecurityModel> callback = service.repoFlightPlanList(body);
        callback.enqueue(new Callback<NetSecurityModel>() {
            @Override
            public void onResponse(Call<NetSecurityModel> call, Response<NetSecurityModel> response) {
                loading.dismiss();
                flightPlanList.clear();

                mRecyclerMainNhsFlightPlanListAdapter.Clear();
                try {
                    if (response.code() == 200) {

                        NetSecurityModel netSecurityModel = response.body();

                        if (netSecurityModel.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {

                            String dec = new MagicSE_Util(getContext()).getDecData(netSecurityModel.getResult_data());
                            Log.d("JeLib","dec:::::::"+dec);
                            FlightPlanModel flightPlanModel = new Gson().fromJson(dec, FlightPlanModel.class);
                            Log.d("JeLib", "flightPlanModel:"+flightPlanModel);
                            if (flightPlanModel.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {
                                flightPlanList = flightPlanModel.getList();
                                if (flightPlanModel.getList() != null) {
                                    Log.d("JeLib","-----"+flightPlanModel.getList().getClass());
                                    if(flightPlanModel.getList()!=null) {
                                        mRecyclerMainNhsFlightPlanListAdapter.setData(flightPlanModel.getList(), VIEWTYPE_NHS_FLIGHT_PLAN_GET_ROUTE);
                                    }
                                }

                            } else {
                                new ToastUtile().showCenterText(getContext(), flightPlanModel.getResult_msg());
                            }
                        }

                    } else {
                        Log.d("JeLib", "----------1-----------");
//                    new ToastUtile().showCenterText(getContext(), getContext().getString(R.string.error_network));
                    }
                } catch (Exception e){
                    Log.d("JeLib", "e:"+e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<NetSecurityModel> call, Throwable t) {
                loading.dismiss();
                flightPlanList.clear();
                mRecyclerMainNhsFlightPlanListAdapter.Clear();
                ((TextViewEx) findViewById(R.id.tv_total)).setText("0");
                new ToastUtile().showCenterText(getContext(), getContext().getString(R.string.error_network) );
            }
        });
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
