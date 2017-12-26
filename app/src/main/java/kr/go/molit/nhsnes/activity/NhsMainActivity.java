package kr.go.molit.nhsnes.activity;

import static com.modim.lan.lanandroid.NativeImplement.lanDestroy;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_AUTO;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_NAME;
import static kr.go.molit.nhsnes.adapter.RecyclerMainNhsFlightPlanListAdapter.VIEWTYPE_NHS_FLIGHT_HISTORY_ACT;
import static kr.go.molit.nhsnes.adapter.RecyclerMainNhsFlightPlanListAdapter.VIEWTYPE_NHS_MAIN_ACT_RECENT;
import static kr.go.molit.nhsnes.fragment.SystemSettingFragment.IS_USE_UTC;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.entity.StringEntity;
import kr.go.molit.nhsnes.Network.NetworkProcess;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.adapter.RecyclerMainNhsFlightPlanListAdapter;
import kr.go.molit.nhsnes.common.NetworkParamUtil;
import kr.go.molit.nhsnes.common.NetworkUrlUtil;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.dialog.DialogType1;
import kr.go.molit.nhsnes.model.NhsFlightHistoryModel;
import kr.go.molit.nhsnes.net.realm.FlightPlanInfo;
import kr.go.molit.nhsnes.widget.CheckableButtonEx;
import kr.go.molit.nhsnes.widget.TextViewEx;

/**
 * 메인화면
 *
 * @author 정제영
 * @version 1.0, 2017.03.06 최초 작성
 **/

/**
 * fragment 가져오기
 *
 * @author 문종락.
 * @version 1.1, 2017.03.31 최초 작성
 **/

public class NhsMainActivity extends NhsBaseFragmentActivity implements View.OnClickListener {

    public static String MAP_VERSION_VECTOR = "MAP_VERSION_VECTOR";
    public static String MAP_VERSION_DEM = "MAP_VERSION_DEM";


    CheckableButtonEx btRecent;
    CheckableButtonEx btFav;
    ViewGroup.LayoutParams tabRecentParam;
    ViewGroup.LayoutParams tabFavParam;
    RecyclerView rvList;

    List<FlightPlanInfo> recentList;
    private Timer timeTimer;
    private TimerTask timeTimerTask;
    private final int TAB_RECENT_SELECTED = 0;
    private final int TAB_FAV_SELECTED = 1;

    private DialogType1 messageDialog = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        setLayout();

    }

    /**
     * layout res 생성 및 onClick 셋팅
     *
     * @author 문종락.
     * @version 1.1, 2017.03.31 최초 작성
     **/
    private void setLayout() {
        findViewById(R.id.bt_info).setOnClickListener(this);
        findViewById(R.id.bt_nhswaypoint).setOnClickListener(this);
        findViewById(R.id.bt_nhsweatherinfo).setOnClickListener(this);
        findViewById(R.id.bt_nhsgpsinfo).setOnClickListener(this);
        findViewById(R.id.bt_nhsconfig).setOnClickListener(this);
        findViewById(R.id.bt_nhsflighthistory).setOnClickListener(this);
        findViewById(R.id.bt_nhsflightplanlist).setOnClickListener(this);
        findViewById(R.id.bt_flight).setOnClickListener(this);
        findViewById(R.id.fl_my_info).setOnClickListener(this);
        findViewById(R.id.tv_logout).setOnClickListener(this);

        btRecent = (CheckableButtonEx) findViewById(R.id.bt_recent);
        btRecent.setOnClickListener(this);
        rvList = (RecyclerView) findViewById(R.id.rv_main_list);

        // TODO: 2017. 3. 31. 리스트 서버 연동해야할 부분

        List<NhsFlightHistoryModel> list = NhsFlightHistoryModel.findAll();
        recentList = new ArrayList<>();

        for (NhsFlightHistoryModel model : list) {
            Log.d("JeLib", "call:" + model.getCallsign());
            Log.d("JeLib", "ac:" + model.getAcrftCd());
            Log.d("JeLib", "getPlanDate:" + model.getAcrftType());

            FlightPlanInfo flightPlanInfo = new FlightPlanInfo();
            flightPlanInfo.setAcrftCd(model.getAcrftCd());
            flightPlanInfo.setPlanDeparture(model.getPlanDeparture());
            flightPlanInfo.setPlanArrival(model.getPlanArrival());
            flightPlanInfo.setPlanTeet(model.getPlanTeet());
            flightPlanInfo.setPlanDate(model.getPlanDate());
            flightPlanInfo.setPlanRoute(model.getPlanRoute());
            flightPlanInfo.setPlanId(model.getPlanId());
            flightPlanInfo.setCallsign(model.getCallsign());
            flightPlanInfo.setAcrftType(model.getAcrftType());
            flightPlanInfo.setPlanSn(model.getPlanSn());
            flightPlanInfo.setFlightId(model.getFlightId());
            flightPlanInfo.setStartDate(model.getStartDate());
            flightPlanInfo.setTotalDistanc(model.getTotalDistanc());
            flightPlanInfo.setAvgSpeed(model.getAvgSpeed());
            flightPlanInfo.setAvgAltitude(model.getAvgAltitude());
            flightPlanInfo.setStartTime(model.getStartTime());
            flightPlanInfo.setEndTime(model.getEndTime());
            flightPlanInfo.setTotalFlightTime(model.getTotalFlightTime());

            recentList.add(flightPlanInfo);

        }

        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(new RecyclerMainNhsFlightPlanListAdapter(recentList, VIEWTYPE_NHS_MAIN_ACT_RECENT, this));

        setRecyclerView(TAB_RECENT_SELECTED);
        setTabLayout(TAB_RECENT_SELECTED);

        Log.d("##", recentList.size() + "");

        // 사용자 이름을 표시한다.
        String userName = StorageUtil.getStorageModeEx(NhsMainActivity.this, LOGIN_NAME);
        ((TextViewEx) findViewById(R.id.tv_name)).setText(userName);

    }

    @Override
    public void onClick(View v) {
        Log.d("JeLib", "----------");
        switch (v.getId()) {

            case R.id.fl_my_info:
                startActivity(new Intent(this, NhsMyPageActivity.class));
                break;
            case R.id.bt_info:
                startActivity(new Intent(this, NhsAppInfoActivity.class));
                break;
            case R.id.bt_flight:
                startActivity(new Intent(this, NhsFlightActivity.class));
                break;

            case R.id.tv_logout:
                NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
                NetworkParamUtil networkParamUtil = new NetworkParamUtil();

                // 로그인 요청
                StringEntity param = networkParamUtil.requestLogout(NhsMainActivity.this);
                NetworkProcess networkProcess = new NetworkProcess(NhsMainActivity.this,
                        networkUrlUtil.getLogout(),
                        param,
                        logoutResult, true);
                networkProcess.sendEmptyMessage(0);
                break;

            case R.id.bt_nhsflightplanlist:
                startActivity(new Intent(this, NhsFlightPlanListActivity.class));
                break;
            case R.id.bt_nhswaypoint:
                Intent intentWayPpint = new Intent(this, NhsSelectPointActivity.class);
                intentWayPpint.putExtra(NhsSelectPointActivity.KEY_MODE, NhsSelectPointActivity.MODE_ROUTE_SEARCH);
                startActivity(intentWayPpint);
                break;
            case R.id.bt_nhsflighthistory:
                startActivity(new Intent(this, NhsFlightHistoryActivity.class));
                break;
            case R.id.bt_nhsweatherinfo:
                startActivity(new Intent(this, NhsWeatherInfoActivity.class));
                break;
            case R.id.bt_nhsgpsinfo:
                startActivity(new Intent(this, NhsGpsInfoActivity.class));
                break;
            case R.id.bt_nhsconfig:
                startActivity(new Intent(this, NhsSettingtActivity.class));
                break;

            case R.id.bt_recent:
                setTabLayout(TAB_RECENT_SELECTED);
                setRecyclerView(TAB_RECENT_SELECTED);
                break;
        }
    }

    //탭 선택 시 리스트 연동 부분
    private void setRecyclerView(int which_tab) {
        if (which_tab == TAB_RECENT_SELECTED) {
            //rvList.setAdapter(new RecyclerMainNhsFlightPlanListAdapter(recentList, RecyclerMainNhsFlightPlanListAdapter.VIEWTYPE_NHS_MAIN_ACT_RECENT, this));
        }

    }

    //탭 선택 시 화면 연동 부분
    private void setTabLayout(int which_tab) {

        tabRecentParam = btRecent.getLayoutParams();

        if (which_tab == TAB_RECENT_SELECTED) {
            btRecent.setChecked(true);
            tabRecentParam.height = getResources().getDimensionPixelSize(R.dimen.dimen_tab_height_sel);
        } else if (which_tab == TAB_FAV_SELECTED) {
            btRecent.setChecked(false);
            tabRecentParam.height = getResources().getDimensionPixelSize(R.dimen.dimen_tab_height_nor);
        }
        btRecent.setLayoutParams(tabRecentParam);


    }


    @Override
    protected void onResume() {
        super.onResume();
        setRecyclerView(TAB_RECENT_SELECTED);

        timeTimer = new Timer();
        timeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        TextViewEx textViewExTime = (TextViewEx) findViewById(R.id.tv_time);
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss yyyy/MM/dd");
                        Date date = new Date();

                        long nowTime = System.currentTimeMillis();
                        if (StorageUtil.getStorageMode(getContext(), IS_USE_UTC)) {
                            date.setTime(nowTime - ((((60 * 1000) * 60) * 9)));
                            textViewExTime.setText("UTC " + format.format(date));
                        } else {
                            date.setTime(nowTime);
                            textViewExTime.setText("KST " + format.format(date));
                        }

                    }
                });
            }
        }, 0, 1000);

    }

    @Override
    protected void onPause() {
        timeTimer.cancel();
        super.onPause();
    }

    @Override
    public void onBackPressed() {

        messageDialog = new DialogType1(NhsMainActivity.this, "", "종료하시겠습니까?", getString(R.string.btn_confirm), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

                // 모두 종료
                allFlinishAllActivity();

            }
        }, getString(R.string.btn_cancel), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                messageDialog.hideDialog();
            }
        });

//        super.onBackPressed();
    }

    /**
     * 로그아웃 결과
     *
     * @author FIESTA
     * @since 오전 12:35
     **/
    NetworkProcess.OnResultListener logoutResult = new NetworkProcess.OnResultListener() {
        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
            try {

                new ToastUtile().showCenterText(NhsMainActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

            } catch (Exception ex) {

            } finally {
            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
            try {

                new ToastUtile().showCenterText(NhsMainActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

            } catch (Exception ex) {

            } finally {
            }
        }

        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

            String msg = response.optString("result_msg");
            String resultCode = response.optString("result_code");

            // 메세지 출력
            new ToastUtile().showCenterText(NhsMainActivity.this, msg);

            if (resultCode.equalsIgnoreCase("Y")) {

                StorageUtil.setStorageMode(NhsMainActivity.this, LOGIN_AUTO, false);
                finish();
                startActivity(new Intent(NhsMainActivity.this, NhsLoginActivity.class));

            }

        }


    };


    @Override
    protected void onDestroy() {
        lanDestroy();
        super.onDestroy();

    }
}
