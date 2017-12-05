package kr.go.molit.nhsnes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.modim.lan.lanandroid.INativeImple;
import com.modim.lan.lanandroid.NativeImplement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.MagicSE_Util;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.dialog.DialogType1;
import kr.go.molit.nhsnes.dialog.LoadingDialog;
import kr.go.molit.nhsnes.model.FlightRouteModel;
import kr.go.molit.nhsnes.net.model.AirChartListModel;
import kr.go.molit.nhsnes.net.model.FlightPlanModel;
import kr.go.molit.nhsnes.net.model.NetSecurityModel;
import kr.go.molit.nhsnes.net.model.TpmDepartureModel;
import kr.go.molit.nhsnes.net.realm.FlightPlanInfo;
import kr.go.molit.nhsnes.net.service.FlightDriverService;
import kr.go.molit.nhsnes.net.service.FlightPlanService;
import kr.go.molit.nhsnes.net.service.NetConst;
import kr.go.molit.nhsnes.net.service.NetUtil;
import kr.go.molit.nhsnes.widget.CustomViewListType1;
import kr.go.molit.nhsnes.widget.TextViewEx;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kr.go.molit.nhsnes.activity.NhsAipInfoActivity.CALL_CALSS_NAME;
import static kr.go.molit.nhsnes.activity.NhsAipInfoActivity.DATA;
import static kr.go.molit.nhsnes.activity.NhsAipInfoActivity.SEARCH_TYPE;
import static kr.go.molit.nhsnes.activity.NhsAirlineInquiryActivity.AIP_INFO;
import static kr.go.molit.nhsnes.activity.NhsAirlineInquiryActivity.AIRSPACE_LIST;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_MBR_ID;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_END;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_ROUTE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_START;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.KEY_MODE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.MODE_SEARCH_IN_ROUTE_SEARCH;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class NhsAirlineInfoActivity extends NhsBaseFragmentActivity implements View.OnClickListener {
    private CustomViewListType1 layoutFlightInfo;
    private CustomViewListType1 layoutAIP;
    private CustomViewListType1 layoutFlightChart;
    private CustomViewListType1 layoutNGIIData;
    private CustomViewListType1 layoutWeatherInfo;
    private CustomViewListType1 layoutKmlImport;
    private CustomViewListType1 layoutMapReceive;

    private NativeImplement nativeImplement = null;

    private DialogType1 complateDialog = null;
    private FlightPlanInfo flightPlanInfo = null;
    private boolean isStartFlight = false;

    private ArrayList<FlightRouteModel> route = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_nhs_airline_info);

        nativeImplement = INativeImple.getInstance(NhsAirlineInfoActivity.this);

        // test
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                nativeImplement.lanSetRoutePosition(Constants.NAVI_SETPOSITION_START, 1270579833, 374676933,"START");
//                nativeImplement.lanSetRoutePosition(Constants.NAVI_SETPOSITION_GOAL, 1270203094, 373526077,"END");
//                nativeImplement.lanSetRoutePosition(Constants.NAVI_SETPOSITION_WAYPOINT, 1271495208, 374350547,"WAY");
//                RpOption option = new RpOption(0, 0);
//                nativeImplement.lanExecuteRP(option);
//            }
//        }, 1000);


        setLayout();

        Bundle data = getIntent().getExtras();

        if (data != null) {

            this.flightPlanInfo = (FlightPlanInfo) data.getSerializable("data");

            // 비행 상세 정보를 조회한다.
            callFlightPlanDetail(this.flightPlanInfo.getPlanId(), this.flightPlanInfo.getPlanSn());
        }

    }

    private void setLayout() {

        layoutFlightInfo = (CustomViewListType1) findViewById(R.id.layout_flight_info);
        layoutFlightInfo.setOnClickListener(this);
        layoutAIP = (CustomViewListType1) findViewById(R.id.layout_aip);
        layoutAIP.setOnClickListener(this);
        layoutFlightChart = (CustomViewListType1) findViewById(R.id.layout_flight_chart);
        layoutFlightChart.setOnClickListener(this);
        layoutNGIIData = (CustomViewListType1) findViewById(R.id.layout_ngii_data);
        layoutNGIIData.setOnClickListener(this);
        layoutWeatherInfo = (CustomViewListType1) findViewById(R.id.layout_weather_info);
        layoutWeatherInfo.setOnClickListener(this);
        layoutKmlImport = (CustomViewListType1) findViewById(R.id.layout_kml_import);
        layoutKmlImport.setOnClickListener(this);
        layoutMapReceive = (CustomViewListType1) findViewById(R.id.layout_map_receive);
        layoutMapReceive.setOnClickListener(this);

        findViewById(R.id.bt_start_flight_report).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = null;

        switch (v.getId()) {
            case R.id.layout_flight_info:
                startActivity(new Intent(this, NhsAirlineInquiryActivity.class));
                break;
            case R.id.layout_aip:
                intent = new Intent(this, NhsAipInfoActivity.class);
                intent.putExtra(CALL_CALSS_NAME, NhsAipInfoActivity.class.getName());
                intent.putExtra(SEARCH_TYPE, AIP_INFO);
                startActivity(intent);
                break;
            case R.id.layout_flight_chart:
                startActivity(new Intent(this, NhsAirlineChartActivity.class));
                break;
            case R.id.layout_ngii_data:

                intent = new Intent(NhsAirlineInfoActivity.this, NhsAipInfoActivity.class);
                intent.putExtra(CALL_CALSS_NAME, NhsNGIIDataActivity.class.getName());
                intent.putExtra(DATA, this.route);
                intent.putExtra(SEARCH_TYPE, AIRSPACE_LIST);
                startActivity(intent);

                /**
                 int count = nativeImplement.lanGetRouteDoyupListCount();

                 if (count > 0) {
                 startActivity(new Intent(this, NhsNGIIDataActivity.class));
                 } else {
                 Toast.makeText(NhsAirlineInfoActivity.this, "리스트가 없습니다.", Toast.LENGTH_SHORT).show();
                 }
                 **/
                break;
            case R.id.layout_weather_info:
                startActivity(new Intent(this, NhsAirlineWeatherActivity.class));
                break;
            case R.id.layout_kml_import:
                startActivity(new Intent(this, NhsKMLImportActivity.class));
                break;
            case R.id.layout_map_receive:
                startActivity(new Intent(this, NhsMapInfoReceiveActivity.class));
                break;

            case R.id.bt_start_flight_report:

                if (this.isStartFlight) {

                    intent = new Intent(getContext(), NhsFlightActivity.class);
                    intent.putExtra("data", flightPlanInfo);

                    intent.putExtra(NhsFlightActivity.MODE, NhsFlightActivity.REAL_DRIVE);
                    getContext().startActivity(intent);

//                    Intent flightIntent = new Intent(getContext(), NhsMapSearchActivity.class);
//                    flightIntent.putExtra(DATA_START, this.route.get(0).getLon() + " " + this.route.get(0).getLat());
//                    flightIntent.putExtra(DATA_END, this.route.get(this.route.size()-1).getLon() + " " + this.route.get(this.route.size()-1).getLat());
//                    flightIntent.putExtra(DATA_ROUTE, result.getPlanRoute());
//                    flightIntent.putExtra(KEY_MODE, MODE_SEARCH_IN_ROUTE_SEARCH);
//                    getContext().startActivity(flightIntent);

                } else {
                    FlightDriverService service = FlightDriverService.retrofit.create(FlightDriverService.class);
                    SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
                    String planAtd = sdf.format(new Date());

                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("planId", flightPlanInfo.getPlanId());                   // 비행계획서 ID
                    params.put("planSn", flightPlanInfo.getPlanSn());                   // 비행계획서 일련번호
                    params.put("messageType", "DEP");        // 전문타입
                    params.put("acrftCd", flightPlanInfo.getAcrftCd());                  // 항공기식별부호
                    params.put("planDeparture", flightPlanInfo.getPlanDeparture());     // 출발비행장
                    params.put("planAtd", planAtd);                 // 출발시간
                    params.put("planArrival", flightPlanInfo.getPlanArrival());        // 도착비행장
                    params.put("mbrId", StorageUtil.getStorageModeEx(getContext(), LOGIN_MBR_ID));               // 회원일련번호
                    params.put("callsign", flightPlanInfo.getCallsign());            // 콜사인

                    RequestBody body = NetUtil.mapToJsonBody(NhsAirlineInfoActivity.this, params);

                    // TpmDepartureModel
                    Call<NetSecurityModel> callback = service.repFpmDeparture(body);
                    callback.enqueue(new Callback<NetSecurityModel>() {
                        @Override
                        public void onResponse(Call<NetSecurityModel> call, Response<NetSecurityModel> response) {

                            if (response.code() == 200) {

                                NetSecurityModel netSecurityModel = response.body();

                                if (netSecurityModel.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {

                                    String dec = new MagicSE_Util(getContext()).getDecData(netSecurityModel.getResult_data());

                                    final TpmDepartureModel model = new Gson().fromJson(dec, TpmDepartureModel.class);

                                    if (model.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {

                                        complateDialog = new DialogType1(getContext(), "", "비행시작 보고를 완료하였습니다.", getContext().getString(R.string.btn_confirm), new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                // flightidx 값 삽입
                                                flightPlanInfo.setFlightId(String.valueOf(model.getFlgtIdx()));
                                                flightPlanInfo.setPlanSn(model.getPlanSn());
                                                flightPlanInfo.setPlanId(model.getPlanId());

                                                complateDialog.hideDialog();
                                                isStartFlight = true;
                                                ((TextViewEx) findViewById(R.id.tve_start)).setText("비행 시작");

                                                complateDialog = new DialogType1(getContext(), "", "비행계획서 최신 정보를 확인합니다.", getContext().getString(R.string.btn_confirm), new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        complateDialog.hideDialog();

                                                    }
                                                }, "", null);

                                            }
                                        }, "", null);

                                    }

                                    new ToastUtile().showCenterText(getContext(), model.getResult_msg());

                                }

                            } else {
                                Log.d("JeLib", "----------1-----------");
                                new ToastUtile().showCenterText(getContext(), getContext().getString(R.string.error_network));

                            }


                        }

                        @Override
                        public void onFailure(Call<NetSecurityModel> call, Throwable t) {
                            //Toast.makeText(mContext,R.string.error_network,Toast.LENGTH_SHORT).show();
//            new ToastUtile().showCenterText(mContext, getString(R.string.error_network) );

                            Log.d("test", "test");
                        }
                    });

                }
                break;

        }
    }

    /**
     * 비행 상세 정보를 조회한다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-13 오후 11:25
     **/
    private void callFlightPlanDetail(String planId, String planSn) {
        final LoadingDialog loading = LoadingDialog.create(getContext(), null, null);
        loading.show();

        FlightPlanService service = FlightPlanService.retrofit.create(FlightPlanService.class);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("planId", planId);
        params.put("planSn", planSn);

        RequestBody body = NetUtil.mapToJsonBody(NhsAirlineInfoActivity.this, params);

        Call<NetSecurityModel> callback = service.repoFlightPlanDetail(body);
        callback.enqueue(new Callback<NetSecurityModel>() {
            @Override
            public void onResponse(Call<NetSecurityModel> call, Response<NetSecurityModel> response) {
                loading.dismiss();

                if (response.code() == 200) {

                    NetSecurityModel netSecurityModel = response.body();

                    if (netSecurityModel.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {

                        String dec = new MagicSE_Util(getContext()).getDecData(netSecurityModel.getResult_data());

                        FlightPlanModel flightPlanModel = new Gson().fromJson(dec, FlightPlanModel.class);

                        if (flightPlanModel.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {

                            flightPlanInfo = flightPlanModel.getFplDetail();
                            route = flightPlanModel.getRoute();

                        } else {
                            new ToastUtile().showCenterText(getContext(), flightPlanModel.getResult_msg());
                        }

                    }
                } else {
                    new ToastUtile().showCenterText(getContext(), getContext().getString(R.string.error_network));
                }
            }

            @Override
            public void onFailure(Call<NetSecurityModel> call, Throwable t) {
                loading.dismiss();
                new ToastUtile().showCenterText(getContext(), getContext().getString(R.string.error_network));
            }
        });
    }
}
