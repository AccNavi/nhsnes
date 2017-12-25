package kr.go.molit.nhsnes.dialog;

import static com.modim.lan.lanandroid.NativeImplement.lanGetRouteDoyupList;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_MBR_ID;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_END;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_ROUTE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_START;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.KEY_MODE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.MODE_SEARCH_IN_ROUTE_SEARCH;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.MODE_SIMULATION;
import static kr.go.molit.nhsnes.common.DateTimeUtil.DEFUALT_DATE_FORMAT1;
import static kr.go.molit.nhsnes.common.DateTimeUtil.DEFUALT_DATE_FORMAT10;
import static kr.go.molit.nhsnes.common.DateTimeUtil.DEFUALT_DATE_FORMAT4;
import static kr.go.molit.nhsnes.common.DateTimeUtil.DEFUALT_DATE_FORMAT8;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.modim.lan.lanandroid.AirRouteStatus;
import com.modim.lan.lanandroid.Constants;
import com.modim.lan.lanandroid.INativeImple;
import com.modim.lan.lanandroid.NativeImplement;
import com.modim.lan.lanandroid.RpOption;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.activity.NhsAirlineInfoActivity;
import kr.go.molit.nhsnes.activity.NhsFlightActivity;
import kr.go.molit.nhsnes.activity.NhsMapSearchActivity;
import kr.go.molit.nhsnes.common.MagicSE_Util;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.model.FlightRouteModel;
import kr.go.molit.nhsnes.net.model.FlightPlanModel;
import kr.go.molit.nhsnes.net.model.NetSecurityModel;
import kr.go.molit.nhsnes.net.model.TpmDepartureModel;
import kr.go.molit.nhsnes.net.realm.FlightPlanInfo;
import kr.go.molit.nhsnes.net.service.FlightDriverService;
import kr.go.molit.nhsnes.net.service.FlightPlanService;
import kr.go.molit.nhsnes.net.service.NetConst;
import kr.go.molit.nhsnes.net.service.NetUtil;
import kr.go.molit.nhsnes.widget.TextViewEx;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jongrakmoon on 2017. 4. 3..
 */

public class DialogFlightAgree extends DialogBase implements View.OnClickListener {

    DialogType1 complateDialog = null;
    boolean isStartFlight = false;
    FlightPlanInfo result = null;
    private ArrayList<FlightRouteModel> route = null;
    private NativeImplement nativeImplement;
    private ProgressDialog mProgressDialog;

    private TextViewEx tveTitle = null;

    public DialogFlightAgree(@NonNull Context context, FlightPlanInfo result) {
        super(context);
        this.result = result;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_flight_agree);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        findViewById(R.id.btn_view).setOnClickListener(this);
        findViewById(R.id.btn_simulation).setOnClickListener(this);
        findViewById(R.id.bt_menu1).setOnClickListener(this);
        findViewById(R.id.bt_menu2).setOnClickListener(this);

        TextViewEx tveDate = (TextViewEx) findViewById(R.id.tve_date);
        TextViewEx tveAgreeDate = (TextViewEx) findViewById(R.id.tve_agree_date);
        TextViewEx tveArriveTime = (TextViewEx) findViewById(R.id.tve_arrive_time);
        tveTitle = (TextViewEx) findViewById(R.id.tv_title);

        // title 제목을 설정한다
        Date date = Util.convertStringToDate(this.result.getPlanDate(), DEFUALT_DATE_FORMAT1);
        SimpleDateFormat std = new SimpleDateFormat(DEFUALT_DATE_FORMAT8, Locale.US);
        date.setTime(date.getTime() - (((1000 * 60) * 60) * 24));
        String convertStrDate = std.format(date);
        tveDate.setText(convertStrDate + " " + this.result.getCallsign());

        // 승인시간 설정
        if (this.result.getPlanArvDate().isEmpty() || this.result.getPlanArvDate().equalsIgnoreCase("null")) {
            tveAgreeDate.setText("정보없음");
        } else{
            date = Util.convertStringToDate(this.result.getPlanArvDate(), DEFUALT_DATE_FORMAT4);
            std = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm", Locale.KOREA);
            tveAgreeDate.setText(std.format(date));
        }



        // 도착예정시간
        date = Util.convertStringToDate(this.result.getPlanEta(), DEFUALT_DATE_FORMAT10);
        std = new SimpleDateFormat("HH:mm", Locale.KOREA);
//        date.setTime(date.getTime() + ((1000 * 60) * 60) * Integer.parseInt(result.getPlanTeet()));
        convertStrDate = std.format(date);
        tveArriveTime.setText(convertStrDate);
//        result.setPlanStatus("01");
        // 상황에 맞게 타이틀 및 버튼 비/활성화를 한다.
        if (result.getPlanStatus().equalsIgnoreCase("01")) {    // 승인

            tveTitle.post(new Runnable() {
                @Override
                public void run() {
                    tveTitle.setText("승인 완료");
                    findViewById(R.id.bt_menu1).setClickable(true);
                    findViewById(R.id.bt_menu2).setClickable(true);
                    findViewById(R.id.btn_view).setClickable(true);
                }
            });

        } else if (result.getPlanStatus().equalsIgnoreCase("99")) { // 거절

            tveTitle.post(new Runnable() {
                @Override
                public void run() {
                    tveTitle.setText("승인 거절");
                    findViewById(R.id.bt_menu1).setClickable(false);
                    findViewById(R.id.bt_menu2).setClickable(false);
                    findViewById(R.id.btn_view).setClickable(false);
                }
            });

        } else if (result.getPlanStatus().equalsIgnoreCase("00")) { // 대기

            tveTitle.post(new Runnable() {
                @Override
                public void run() {
                    tveTitle.setText("승인 대기");
                    findViewById(R.id.bt_menu1).setClickable(false);
                    findViewById(R.id.bt_menu2).setClickable(false);
                    findViewById(R.id.btn_view).setClickable(false);
                }
            });

        }

        // 비행 상세 정보를 조회한다.
        callFlightPlanDetail(result.getPlanId(), result.getPlanSn());

        nativeImplement = INativeImple.getInstance(getContext());

    }

    /**
     * 비행 상세 정보를 조회한다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-13 오후 11:25
     **/
    private void callFlightPlanDetail(String planId, String planSn) {

        showProgress();

        FlightPlanService service = FlightPlanService.retrofit.create(FlightPlanService.class);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("planId", planId);
        params.put("planSn", planSn);

        RequestBody body = NetUtil.mapToJsonBody(getContext(), params);

        Call<NetSecurityModel> callback = service.repoFlightPlanDetail(body);
        callback.enqueue(new Callback<NetSecurityModel>() {
            @Override
            public void onResponse(Call<NetSecurityModel> call, Response<NetSecurityModel> response) {


                if (response.code() == 200) {

                    NetSecurityModel netSecurityModel = response.body();

                    if (netSecurityModel.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {

                        String dec = new MagicSE_Util(getContext()).getDecData(netSecurityModel.getResult_data());

                        FlightPlanModel flightPlanModel = new Gson().fromJson(dec, FlightPlanModel.class);

                        if (flightPlanModel.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {

                            result = flightPlanModel.getFplDetail();
                            route = (ArrayList)flightPlanModel.getRoute();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {


                                    int size = route.size();
                                    int i = 0;

                                    nativeImplement.lanClearRoutePosition();

                                    // 시작 위치
                                    nativeImplement.lanSetRoutePosition(Constants.NAVI_SETPOSITION_START, Double.parseDouble(route.get(0).getLon()), Double.parseDouble(route.get(0).getLat()),
                                            "start", 0);

                                    // 종료 위치
                                    nativeImplement.lanSetRoutePosition(Constants.NAVI_SETPOSITION_GOAL, Double.parseDouble(route.get(route.size() - 1).getLon()), Double.parseDouble(route.get(route.size() - 1).getLat()),
                                            "end", 0);


                                    // 경유지 등록
                                    for (i = 1; i < size - 1; i++) {

                                        nativeImplement.lanSetRoutePosition(Constants.NAVI_SETPOSITION_WAYPOINT, Double.parseDouble(route.get(i).getLon()), Double.parseDouble(route.get(i).getLat()),
                                                "way" + i, 0);

                                    }

                                    int result = nativeImplement.lanExecuteRP(new RpOption(0, 0));

                                    if (result != -1) {

                                        AirRouteStatus routeStatus = nativeImplement.lanGetRouteInfo();
                                        final String convertDistance = String.format("%.1f", (float)routeStatus.uTotalPredictDist / (float)1000);
                                        ((TextViewEx)findViewById(R.id.tve_distance)).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                ((TextViewEx)findViewById(R.id.tve_distance)).setText(convertDistance+"Km");

                                            }
                                        });



                                    }




//                findViewById(R.id.nlv_view).setVisibility(View.GONE);

                                }
                            }, 500);

                        } else {
                            new ToastUtile().showCenterText(getContext(), flightPlanModel.getResult_msg());
                            dismiss();
                        }

                        dismissProgress();


                    }
                } else {
                    dismissProgress();
                    new ToastUtile().showCenterText(getContext(), getContext().getString(R.string.error_network));
                    dismiss();

                }
            }

            @Override
            public void onFailure(Call<NetSecurityModel> call, Throwable t) {

                dismissProgress();
                new ToastUtile().showCenterText(getContext(), getContext().getString(R.string.error_network));
                dismiss();
            }
        });
    }


    /**
     * 프로그래스바를 보여준다.
     *
     * @author FIESTA
     * @since 오전 3:50
     **/
    private void showProgress() {

        try {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog = new ProgressDialog(getContext(), ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.setTitle("");
                    mProgressDialog.setMessage(getContext().getString(R.string.wait_message));
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                }
            }, 100);

        } catch (Exception ex) {

        }
    }

    /**
     * 프로그래스바를 종료한다.
     *
     * @author FIESTA
     * @since 오전 3:50
     **/
    private void dismissProgress() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
            }
        }, 1000);

    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()) {
            case R.id.btn_view:
                DialogASMTelegram dialogASMTelegram = new DialogASMTelegram(getContext());
                dialogASMTelegram.show();
                break;
            case R.id.btn_simulation:
                Intent flightIntent = new Intent(getContext(), NhsMapSearchActivity.class);
                flightIntent.putExtra(DATA_START, this.route.get(0).getLon() + " " + this.route.get(0).getLat());
                flightIntent.putExtra(DATA_END, this.route.get(this.route.size() - 1).getLon() + " " + this.route.get(this.route.size() - 1).getLat());
                flightIntent.putExtra(DATA_ROUTE, result.getPlanRoute());
                flightIntent.putExtra(KEY_MODE, MODE_SIMULATION);
                getContext().startActivity(flightIntent);

                dismiss();
                break;
            case R.id.bt_menu1:

                intent = new Intent(getContext(), NhsAirlineInfoActivity.class);
                intent.putExtra("data", result);

                intent.putExtra(NhsFlightActivity.MODE, NhsFlightActivity.REAL_DRIVE);
                getContext().startActivity(intent);

                break;
            case R.id.bt_menu2:
                //Toast.makeText(getContext(),"비행시작 보고",Toast.LENGTH_SHORT).show();

                if (isStartFlight) {

                    intent = new Intent(getContext(), NhsFlightActivity.class);
                    intent.putExtra("data", (Serializable) this.result);
                    intent.putExtra(NhsFlightActivity.MODE, NhsFlightActivity.REAL_DRIVE);

                    getContext().startActivity(intent);
                    dismiss();

                } else {

                    FlightDriverService service = FlightDriverService.retrofit.create(FlightDriverService.class);
                    SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
                    String planAtd = sdf.format(new Date());

                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("planId", result.getPlanId());                   // 비행계획서 ID
                    params.put("planSn", result.getPlanSn());                   // 비행계획서 일련번호
                    params.put("messageType", "DEP");        // 전문타입
                    params.put("acrftCd", result.getAcrftCd());                  // 항공기식별부호
                    params.put("planDeparture", result.getPlanDeparture());     // 출발비행장
                    params.put("planAtd", planAtd);                 // 출발시간
                    params.put("planArrival", result.getPlanArrival());        // 도착비행장
                    params.put("mbrId", StorageUtil.getStorageModeEx(getContext(), LOGIN_MBR_ID));               // 회원일련번호
                    params.put("callsign", result.getCallsign());            // 콜사인

                    RequestBody body = NetUtil.mapToJsonBody(getContext(), params);

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
                                                result.setFlightId(String.valueOf(model.getFlgtIdx()));
                                                result.setPlanSn(model.getPlanSn());
                                                result.setPlanId(model.getPlanId());

                                                complateDialog.hideDialog();
                                                isStartFlight = true;
                                                ((TextViewEx) findViewById(R.id.tv_start_report)).setText("비행 시작");

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
}