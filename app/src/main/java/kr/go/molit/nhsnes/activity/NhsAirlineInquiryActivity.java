package kr.go.molit.nhsnes.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import kr.go.molit.nhsnes.Network.NetworkProcess;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.NetworkParamUtil;
import kr.go.molit.nhsnes.common.NetworkUrlUtil;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.common.Util;

import static kr.go.molit.nhsnes.activity.NhsAipInfoActivity.CALL_CALSS_NAME;
import static kr.go.molit.nhsnes.activity.NhsAipInfoActivity.SEARCH_TYPE;
import static kr.go.molit.nhsnes.adapter.RecyclerNhsFlightInfoListAdapter.INTENT_ID;

/**
 * 항공 정보
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class NhsAirlineInquiryActivity extends NhsBaseFragmentActivity implements View.OnClickListener{

    public final static String AIRSPACE_LIST = "airspace_list";                // 공역
    public final static String OBSTACLE_LIST = "obstacle_list";                // 장애물
    public final static String AIRFIELD_LIST = "airfield_list";                // 이착륙장
    public final static String AIRPORT_LIST = "airport_list";                  // 공항조회
    public final static String FLIGHT_PATH_LIST = "flight_path_List";         // 비행경로조회
    public final static String ROUTE_LIST = "route_list";                      // 항로조회
    public final static String AIP_INFO = "api_info";                           // API 정보 조회
    public final static String NOTAM_LIST = "notam_list";                      // 항공시보
    public final static String VFR_PATH = "vfr_path";                           // 시계 비행로
    public final static String PYLON_LIST = "pylon_list";                      // 철탑
    public final static String RUNWAY_LIST = "runway_list";                    // 활주로
    public final static String HELIPORT_LIST = "heliport_list";               // 헬기장

    RecyclerView rvList;
    String infoId;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_nhs_airline_inquiry_info);
        if (getIntent().getStringExtra(INTENT_ID) != null) {
            infoId = getIntent().getStringExtra(INTENT_ID);
        }
        setLayout();
    }

    private void setLayout() {
        rvList = (RecyclerView) findViewById(R.id.rv_info_list);
        findViewById(R.id.airspace_list).setOnClickListener(this);
        findViewById(R.id.obstacle_list).setOnClickListener(this);
        findViewById(R.id.airfield_list).setOnClickListener(this);
        findViewById(R.id.airport_list).setOnClickListener(this);
        findViewById(R.id.flight_path_List).setOnClickListener(this);
        findViewById(R.id.route_list).setOnClickListener(this);
        findViewById(R.id.notam_list).setOnClickListener(this);
        findViewById(R.id.flight_vfr_path).setOnClickListener(this);
        findViewById(R.id.pylon_list).setOnClickListener(this);
        findViewById(R.id.runway_list).setOnClickListener(this);
        findViewById(R.id.heliport_list).setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {

        Intent intent = null;

        switch (view.getId()) {

            // 공역
            case R.id.airspace_list:
                intent = new Intent(NhsAirlineInquiryActivity.this, NhsAipInfoActivity.class);
                intent.putExtra(CALL_CALSS_NAME, NhsAirlineInquiryActivity.class.getName());
                intent.putExtra(SEARCH_TYPE, AIRSPACE_LIST);
                startActivity(intent);
                break;

            // 장애물
            case R.id.obstacle_list:
                intent = new Intent(NhsAirlineInquiryActivity.this, NhsAipInfoActivity.class);
                intent.putExtra(CALL_CALSS_NAME, NhsAirlineInquiryActivity.class.getName());
                intent.putExtra(SEARCH_TYPE, OBSTACLE_LIST);
                startActivity(intent);
                break;

            // 이착륙장
            case R.id.airfield_list:
                intent = new Intent(NhsAirlineInquiryActivity.this, NhsAipInfoActivity.class);
                intent.putExtra(CALL_CALSS_NAME, NhsAirlineInquiryActivity.class.getName());
                intent.putExtra(SEARCH_TYPE, AIRFIELD_LIST);
                startActivity(intent);
                break;

            // 공항조회
            case R.id.airport_list:
                intent = new Intent(NhsAirlineInquiryActivity.this, NhsAipInfoActivity.class);
                intent.putExtra(CALL_CALSS_NAME, NhsAirlineInquiryActivity.class.getName());
                intent.putExtra(SEARCH_TYPE, AIRPORT_LIST);
                startActivity(intent);
                break;

            // 비행경로 조회
            case R.id.flight_path_List:
                intent = new Intent(NhsAirlineInquiryActivity.this, NhsAipInfoActivity.class);
                intent.putExtra(CALL_CALSS_NAME, NhsAirlineInquiryActivity.class.getName());
                intent.putExtra(SEARCH_TYPE, FLIGHT_PATH_LIST);
                startActivity(intent);
                break;

            // 항로 조회
            case R.id.route_list:
                intent = new Intent(NhsAirlineInquiryActivity.this, NhsAipInfoActivity.class);
                intent.putExtra(CALL_CALSS_NAME, NhsAirlineInquiryActivity.class.getName());
                intent.putExtra(SEARCH_TYPE, ROUTE_LIST);
                startActivity(intent);
                break;

            // 시계 비행로
            case R.id.flight_vfr_path:
                intent = new Intent(NhsAirlineInquiryActivity.this, NhsAipInfoActivity.class);
                intent.putExtra(CALL_CALSS_NAME, NhsAirlineInquiryActivity.class.getName());
                intent.putExtra(SEARCH_TYPE, VFR_PATH);
                startActivity(intent);
                break;

            // 철탑 조회
            case R.id.pylon_list:
                intent = new Intent(NhsAirlineInquiryActivity.this, NhsAipInfoActivity.class);
                intent.putExtra(CALL_CALSS_NAME, NhsAirlineInquiryActivity.class.getName());
                intent.putExtra(SEARCH_TYPE, PYLON_LIST);
                startActivity(intent);
                break;

            // 활주로 조회
            case R.id.runway_list:
                intent = new Intent(NhsAirlineInquiryActivity.this, NhsAipInfoActivity.class);
                intent.putExtra(CALL_CALSS_NAME, NhsAirlineInquiryActivity.class.getName());
                intent.putExtra(SEARCH_TYPE, RUNWAY_LIST);
                startActivity(intent);
                break;

            // 헬기장 조회
            case R.id.heliport_list:
                intent = new Intent(NhsAirlineInquiryActivity.this, NhsAipInfoActivity.class);
                intent.putExtra(CALL_CALSS_NAME, NhsAirlineInquiryActivity.class.getName());
                intent.putExtra(SEARCH_TYPE, HELIPORT_LIST);
                startActivity(intent);
                break;

            // 항공 시보
            case R.id.notam_list:
                NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
                NetworkParamUtil networkParamUtil = new NetworkParamUtil();

                // 노탐
                StringEntity param = networkParamUtil.notamParam(NhsAirlineInquiryActivity.this);

                NetworkProcess networkProcess = new NetworkProcess(NhsAirlineInquiryActivity.this,
                        "http://211.107.29.58:8181/NIF/nis/list/notamList.do",
                        param,
                        new NetworkProcess.OnResultListener() {
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                Util.writeStringAsFile(Environment.getExternalStorageDirectory() + "/ACC_NAVI/Flight_Info", "Notam.dat", response.toString());
                                new ToastUtile().showCenterText(NhsAirlineInquiryActivity.this, "다운로드가 완료되었습니다.");

                            }
                        }, true);
                networkProcess.sendEmptyMessage(0);
                break;

        }

    }
}
