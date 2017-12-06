package kr.go.molit.nhsnes.activity;

import static com.modim.lan.lanandroid.Constants.NAVI_MAP_DEM;
import static com.modim.lan.lanandroid.Constants.NAVI_MAP_SATELLITE;
import static com.modim.lan.lanandroid.Constants.NAVI_MAP_VECTOR;
import static com.modim.lan.lanandroid.NativeImplement.KML_DATA_PATH;
import static com.modim.lan.lanandroid.NativeImplement.getKmlFileName;
import static com.modim.lan.lanandroid.NativeImplement.lanGetPortCodeName;
import static com.modim.lan.lanandroid.NativeImplement.lanGetRouteDoyupList;
import static com.modim.lan.lanandroid.NativeImplement.lanGetRouteDoyupListCount;
import static com.modim.lan.lanandroid.NativeImplement.lanGetRouteInfo;
import static com.modim.lan.lanandroid.NativeImplement.lanIsTrajectory;
//import static com.modim.lan.lanandroid.NativeImplement.lanMapToScreen;
import static com.modim.lan.lanandroid.NativeImplement.lanReceiveAroundAviation;
import static com.modim.lan.lanandroid.NativeImplement.lanReceiveGPSData;
import static com.modim.lan.lanandroid.NativeImplement.lanReceiveNotam;
import static com.modim.lan.lanandroid.NativeImplement.lanReceiveSnowtam;
import static com.modim.lan.lanandroid.NativeImplement.lanSetDisplayLayer;
import static com.modim.lan.lanandroid.NativeImplement.lanSetKMLDataPath;
import static com.modim.lan.lanandroid.NativeImplement.lanSetMapColor;
import static com.modim.lan.lanandroid.NativeImplement.lanSetWeatherInfo;
import static com.modim.lan.lanandroid.NativeImplement.lanSetZoomLevelByPosition;
import static com.modim.lan.lanandroid.NativeImplement.lanStartTrajectory;
import static com.modim.lan.lanandroid.NativeImplement.lanStopTrajectory;
import static com.modim.lan.lanandroid.NativeImplement.lanZoomByPosition;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_MBR_ID;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_TOKEN_KEY;
import static kr.go.molit.nhsnes.fragment.FlightInfoFragment.ALERT_MESSAGE_SET;
import static kr.go.molit.nhsnes.fragment.FlightInfoFragment.AUTOZOOM_LEVEL_SETTING;
import static kr.go.molit.nhsnes.fragment.FlightInfoFragment.EXPLORING_PATH_EXITS;
import static kr.go.molit.nhsnes.fragment.FlightInfoFragment.FLYING_DISTANCE;
import static kr.go.molit.nhsnes.fragment.FlightInfoFragment.HIGHEST_FLIGHT_ALTITUDE;
import static kr.go.molit.nhsnes.fragment.FlightInfoFragment.LOWEST_FLIGHT_ALTITUDE;
import static kr.go.molit.nhsnes.fragment.FlightInfoFragment.PATH_EXIT_BUFFER_ZONE;
import static kr.go.molit.nhsnes.fragment.FlightInfoFragment.PRESERVATION_OF_OBSTACLES;
import static kr.go.molit.nhsnes.fragment.FlightInfoFragment.SET_THE_MAP_DOWNLOAD_SCOPE;
import static kr.go.molit.nhsnes.fragment.FlightInfoFragment.SPACE_CONSERVATION;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.AIRCRAFT_POSITIONING_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.AIRSPACE_INFORMATION_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.EXPRESSWAY_DISPLAY_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.ISL_LAYOUT_AIRCRAFTSPEED;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.IS_LAYOUT_ALTITUDE;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.IS_LAYOUT_DIRECTION;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.IS_LAYOUT_DISTANCE;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.IS_LAYOUT_NW;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.KML_DATA;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.LANDING_INFORMATION_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.MAP_TYPE;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.MOUNTAIN_INFORMATION_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.NOTAM_INFO_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.OBSTACLE_INFORMATION_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.RIVER_INFORMATION_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.WEATHER_INFORMATION;
import static kr.go.molit.nhsnes.fragment.SystemSettingFragment.CHANGE_COLOR_AUTOMATICALLY;
import static kr.go.molit.nhsnes.fragment.SystemSettingFragment.IS_NIGHTTIME;
import static kr.go.molit.nhsnes.fragment.SystemSettingFragment.IS_TTS_SOUND;
import static com.modim.lan.lanandroid.NativeImplement.lanExceuteGuide;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.modim.lan.lanandroid.AirDoyupList;
import com.modim.lan.lanandroid.AirGPSData;
import com.modim.lan.lanandroid.AirPoint;
import com.modim.lan.lanandroid.AirRouteStatus;
import com.modim.lan.lanandroid.AirSystemParametersInfo;
import com.modim.lan.lanandroid.AlarmType;
import com.modim.lan.lanandroid.AroundAviation;
import com.modim.lan.lanandroid.Constants;
import com.modim.lan.lanandroid.GuideType;
import com.modim.lan.lanandroid.INativeImple;
import com.modim.lan.lanandroid.LanStorage;
import com.modim.lan.lanandroid.NativeImplement;
import com.modim.lan.lanandroid.NhsLanView;
import com.modim.lan.lanandroid.Notam;
import com.modim.lan.lanandroid.Snowtam;
import com.modim.lan.lanandroid.WeatherInfo;
import com.yayandroid.locationmanager.LocationManager;
import com.yayandroid.locationmanager.constants.FailType;
import com.yayandroid.locationmanager.providers.locationprovider.DefaultLocationProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import io.realm.Realm;
import kr.go.molit.nhsnes.Network.NetworkProcess;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.MagicSE_Util;
import kr.go.molit.nhsnes.common.NetworkParamUtil;
import kr.go.molit.nhsnes.common.NetworkUrlUtil;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.dialog.DialogAddFavorites;
import kr.go.molit.nhsnes.dialog.DialogFinishFlight;
import kr.go.molit.nhsnes.dialog.DialogSelectHotkey;
import kr.go.molit.nhsnes.dialog.DialogSendComplateFlight;
import kr.go.molit.nhsnes.dialog.DialogSendReportFlight;
import kr.go.molit.nhsnes.dialog.DialogType1;
import kr.go.molit.nhsnes.dialog.LoadingDialog;
import kr.go.molit.nhsnes.interfaces.OnClickOptionMapMenu;
import kr.go.molit.nhsnes.interfaces.OnGpsListener;
import kr.go.molit.nhsnes.model.FlightRouteModel;
import kr.go.molit.nhsnes.model.NhsFlightHistoryModel;
import kr.go.molit.nhsnes.net.model.AlmostAcrftModel;
import kr.go.molit.nhsnes.net.model.FlightDriveModel;
import kr.go.molit.nhsnes.net.model.FlightPlanModel;
import kr.go.molit.nhsnes.net.model.NetSecurityModel;
import kr.go.molit.nhsnes.net.model.PsmHkMessage;
import kr.go.molit.nhsnes.net.model.TpmArrivalModel;
import kr.go.molit.nhsnes.net.realm.FlightPlanInfo;
import kr.go.molit.nhsnes.net.service.FlightDriverService;
import kr.go.molit.nhsnes.net.service.FlightInfoService;
import kr.go.molit.nhsnes.net.service.FlightPlanService;
import kr.go.molit.nhsnes.net.service.NetConst;
import kr.go.molit.nhsnes.net.service.NetUtil;
import kr.go.molit.nhsnes.widget.TextViewEx;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 운항 화면
 *
 * @author FIESTA
 * @since 오전 3:07
 **/
public class NhsFlightActivity extends NhsBaseFragmentActivity implements SensorEventListener, View.OnClickListener, OnClickOptionMapMenu {

    private final static int SHOW_VALUE_TIME = 5000;          // 5초마다 데이터 값을 보여준다.(메뉴 우측 값들)
    private final static int SHOW_VALUE_GYRO_TIME = 200;    // 1초마다 데이터 자이로 핀을 보여준다(제일 하단 핀 움직이는 구간)

    public final static String MODE = "MODE";   // 모드
    public final static int NONE_DRIVE = 0;    // 없음
    public final static int REAL_DRIVE = 1;    // 실제 주행


    private final static int ELEVATION = 0;               // 해발고도
    private final static int ALTITUDE_ALTITUDE = 1;      // 지표고도
    private final static int SEND_TIMER_INTERVAL = 5000;    // 항적 데이터를 지상 관제로 전송 주기 5초
    private final static int SAVE_TIMER_INTERVAL = 1000;    // 항적 데이터 저장 주기 1초
    private final static int TEST_DRIVE_TIMER_INTERVAL = 1000;    // 테스트 드라이브 주기 1초
    private final static int TTS_TIMER_INTERVAL = 1000;             // TTS 알람 체크 주기

    private TextToSpeech mTts = null;

    private TextView mTvAltitude;
    private TextView mTvAircraftSpeed;
    private TextView mTvDirection;
    private TextView mTvDistance;
    private TextView mTvNw;
    private TextView mTv2d3d = null;
    private View vWarring = null;

    private RelativeLayout rlMenu;              // 비행 메뉴
    private NhsLanView mNlvView;                 // 맵
    //private Sensor mSensor;


    private SensorManager mSensorManager;
    private Sensor mAccSensor;                  // 가속도 센서

    private DialogSelectHotkey dialogSelectHotkey;      // 핫키 선택 다이얼로그
    private DialogFinishFlight dialogFinishFlight;      // 비행 완료 다이얼로그
    private DialogAddFavorites dialogAddFavorites;          // 즐겨찾기 추가
    private DialogSendComplateFlight dialogSendComplateFlight;  // 비행 완료 보고
    private Context mContext = null;

    private TextView tvN = null;
    private TextView tvE = null;

    private TimerTask senderTimerTask = null;   // 지상 관제센터 전송 타이머
    private Timer senderTimer = null;           // 지상 관제센터 전송 타이머

    private TimerTask saveTimerTask = null;   //  항적 데이터 저장 타이머
    private Timer saveTimer = null;           // 항적 데이터 저장 타이머


    private int altitudeType = ELEVATION;


    private String currentHeading = "";          // 해당 방향
    private double currentSpeed = 0;           // 현재 속도
    private int currentDegree = 0;             // 현재 비행 각도

    private boolean isDrive = false;            // 주행 중인가?
    private DialogSendReportFlight dialogSendReportFlight;

    private int mode = NONE_DRIVE;

    private long beforeShowSpeedData = 0;     // 이전에 보여전 속도 데이터 시간
    private long beforeShowAltitudeData = 0;  // 이전에 보여전 고도 데이터 시간
    private long beforeShowDirectionData = 0;  // 이전에 보여전 비행 진행 방향 데이터 시간
    private long beforeShowGyroPin = 0;       // 이전에 보여전  자이로 핀 시간


    private Animation warringAnimation = null;

    private DialogType1 mPopup1 = null;
    private DialogType1 mPopup2 = null;

    private FlightPlanInfo flightPlanInfo;
    private String tempSaveFileName = "";       // 통신 두절시 저장될 파일 이름
    private ArrayList<FlightRouteModel> route;  // 경로
    /**
     * 하단 자이로 방향판
     **/
    private int beforAngle = 0;
    private LinearLayout llNavPin;

    private String flightId = "";   // 비행 시작 보고때 받은 고유 ID
    private long gpsLogDate = 0;    // gps 로그 기록 시작 시간

    private ProgressDialog mProgressDialog;

    private boolean isTestDrive = false;    // 테스트 주행

    // 테스트 주행 좌표 리스트
    private String[] testDrivePointList = null;
//    private JSONArray testDrivePointList = null;

    private TimerTask testDriveTimerTask = null;   // 테스트 드라이브 타이머
    private Timer testDriveTimer = null;           // 테스트 드라이브 타이머

    private int testDriveIndex = 0;                 // 테스트 드라이브 주소 위치
    private int testDrivePlus = 1;
    private int testDriveToggle = -1;
    private boolean testDriveDir = false;
    private int testSpeedMin = 300;
    private int testSpeedMax = 600;
    private int testSpeedStep = 50;
    private int testSpeed = testSpeedMin;
    private int testAltMin = 2500;
    private int testAltMax = 5000;
    private int testAltStep = 500;
    private int testAlt = testAltMax;

    private TimerTask ttsTimerTask = null;   // tts 알람
    private Timer ttsTimer = null;           // tts 알람

    private DialogType1 weatherDialog = null;
    private AirRouteStatus routeStatus = null;
    private String startDate = "";      // 시작 날짜
    private String startTime = "";      // 시작 시간
    private String endTime = "";        // 도착 시간
    private long totalSpeed = 0;        // 총 누적 속도
    private long totalAltitude = 0;     // 총 누적 고도
    private long totalSpeedCount = 0;        // 총 누적 속도 카운트
    private long totalAltitudeCount = 0;    //  총 누적 곧 ㅗ카운트
    private Date objStartDate = null;
    private float[] gyroValues = null;

    protected Location beforeLocation = null;    // 이전 gps

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.act_nhs_flight);

        this.objStartDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        this.startDate = sdf.format(this.objStartDate);

        sdf = new SimpleDateFormat("HH:mm:ss");
        this.startTime = sdf.format(new Date());

        // 프로그래스바를 보여준다.
//        showProgress();

        Bundle data = getIntent().getExtras();

        if (data != null) {

            this.flightPlanInfo = (FlightPlanInfo) data.getSerializable("data");
            this.flightId = flightPlanInfo.getFlightId();
        }

        getMode();
        setLayout();
        setupTTS();

        settingSensors();

        // 초기 위치 (정방향)
        updateNavPin(0, 1000);

        // 테스트 모드 주행 체크
        if (this.flightPlanInfo != null) {
            checkTestMode(this.flightPlanInfo.getCallsign());
        }

        // test
//        checkTestMode("fplccw");

        // 테스트 모드가 아니면
        if (!this.isTestDrive) {
            // mode에 따라서 경로를 보여주거나 조회해서 보여준다.
            settingMode();
        }

        // 통신 두절시 저장될 파일 이름을 설정한다.
        sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        this.tempSaveFileName = sdf.format(new Date()) + "_Loss_of_comm.dat";

    }

    /**
     * mode에 따라서 경로를 보여주거나 조회해서 보여준다.
     *
     * @author FIESTA
     * @since 오전 12:36
     **/
    private void settingMode() {

        switch (mode) {

            case REAL_DRIVE:

                mPopup1 = new DialogType1(getContext(), getString(R.string.dialog_weather_title), getString(R.string.dialog_weather_msg), getString(R.string.btn_agree), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopup1.hideDialog();

                        mPopup2 = new DialogType1(getContext(), getString(R.string.dialog_notam_title), getString(R.string.dialog_notam_msg), getString(R.string.btn_agree), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPopup2.hideDialog();
                                // 비행 상세 정보를 조회한다.
                                if (flightPlanInfo != null) {
                                    callFlightPlanDetail(flightPlanInfo.getPlanId(), flightPlanInfo.getPlanSn());
                                } else {
                                    startFlight();
                                }
                            }
                        }, "", null);

                    }
                }, "", null);

                break;

            default:
                // 비행 상세 정보를 조회한다.
                if (flightPlanInfo != null) {
                    callFlightPlanDetail(flightPlanInfo.getPlanId(), flightPlanInfo.getPlanSn());
                } else {
                    startFlight();
                }
                start5secodTts();
                break;


        }

    }


    /**
     * 관측기상을 보여준다.
     *
     * @author FIESTA
     * @since 오전 12:36
     **/
    private void showWeather(final int type) {

        String title = "";
        String url = "";
        String startDate = "";
        String endDate = "";
        String isuYear = "";

        String apCd = "";   // 공항코드

        try {
            String[] start = this.flightPlanInfo.getPlanRoute().split(" ");
            StringBuffer strBuffer = new StringBuffer();
            lanGetPortCodeName(Double.parseDouble(start[0]), Double.parseDouble(start[1]), strBuffer);
            apCd = strBuffer.toString().split("@@")[0];
        } catch (Exception ex) {

        }

        NetworkUrlUtil nuu = new NetworkUrlUtil();

        switch (type) {
            case 0:
                title = "관측기상";
                url = nuu.getWeatherMetar();
                break;
            case 1:
                title = "기상(Taf) 조회";
                url = nuu.getWeatherTaf();
                startDate = "201708150000";
                endDate = "201708150600";
                break;
            case 2:
                title = "기상(Wrng) 조회";
                url = nuu.getWeatherWrng();
                startDate = "201708230800";
                endDate = "201708231000";
                break;
            case 3:
                title = "기상(Sigmet) 조회";
                url = nuu.getWeatherSigmet();
                startDate = "201707160000";
                endDate = "201707162359";
                break;
            case 4:
                title = "기상(Airmet) 조회";
                url = nuu.getWeatherAirmet();
                startDate = "201707160000";
                endDate = "201707162359";
                break;
            case 5:
                title = "항공고시보(SNOWTAM) 조회";
                url = nuu.getSnotam();
                apCd = "";
                startDate = "201711010000";
                endDate = "201711301530";
                isuYear = "2017";
                break;

        }


        NetworkParamUtil networkParamUtil = new NetworkParamUtil();

        StringEntity param = networkParamUtil.getWeather(apCd, StorageUtil.getStorageModeEx(NhsFlightActivity.this, LOGIN_TOKEN_KEY, "")
                , startDate, endDate, isuYear);
        final String finalTitle = title;
        NetworkProcess networkProcess = new NetworkProcess(NhsFlightActivity.this,
                url,
                param,
                new NetworkProcess.OnResultListener() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        try {

                            new ToastUtile().showCenterText(NhsFlightActivity.this, NhsFlightActivity.this.getString(R.string.error_network) + "(" + statusCode + ")");

                        } catch (Exception ex) {

                        } finally {
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        try {

                            new ToastUtile().showCenterText(NhsFlightActivity.this, NhsFlightActivity.this.getString(R.string.error_network) + "(" + statusCode + ")");

                        } catch (Exception ex) {

                        } finally {
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        StringBuilder sb = new StringBuilder();

                        try {

                            String msg = response.optString("result_msg");
                            String resultCode = response.optString("result_code");


                            if (resultCode.equalsIgnoreCase("Y")) {

                                try {
                                    JSONArray resultData = response.optJSONArray("result_data");

                                    int size = resultData.length();
                                    int i = 0;

                                    for (i = 0; i < size; i++) {

                                        Iterator iterator = resultData.optJSONObject(i).keys();


                                        while (iterator.hasNext()) {

                                            try {

                                                String key = (String) iterator.next();
                                                String value = resultData.getJSONObject(i).get(key).toString();
                                                sb.append(key);
                                                sb.append(" : ");
                                                if (value.equals("null")) {
                                                    value = "데이터 없음";
                                                }
                                                sb.append(value);
                                                sb.append("\n");

                                            } catch (Exception ex) {

                                            }
                                        }
                                    }

                                } catch (Exception ex) {
                                    sb.append("데이터가 없습니다.");
                                }

                            } else {

                                sb.append("데이터가 없습니다.");

                            }

                        } catch (Exception ex) {
                            sb.append("데이터가 없습니다.");
                        }

                        weatherDialog = new DialogType1(NhsFlightActivity.this,
                                finalTitle, sb.toString(), "확인",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        weatherDialog.hideDialog();

                                        //차례대로 보여준다.
                                        int tempType = type + 1;

                                        if (tempType <= 5) {
                                            showWeather(tempType);
                                        } else {
                                            showNotams();
                                        }

                                    }
                                }, "", null);

                    }

                }, true);
        networkProcess.sendEmptyMessage(0);

    }

    private void showNotams() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // 지점 기상 조회를 조회해서 맵에 보여준다.
                showWeatherPoint();

                // 노탐 정보를 가져와서  맵에 보여준다.
                getNotam();

                // S노탐 정보를 가져와서  맵에 보여준다.
                getSNotam();

            }
        }, 1500);
    }

    /**
     * 특정 콜사인이 들어오면 테스트 모드 주행한다.
     *
     * @author FIESTA
     * @since 오전 12:36
     **/
    private void checkTestMode(final String callsign) {

        /**
         콜사인이 fplccw 이면 input1.txt 를 호출
         콜사인이 fplwon 이면 input1.txt 를 거꾸로 읽어서 호출
         콜사인이 fplkdw 이면 input2.txt 를 호출
         콜사인이 fplnow 이면 input3.txt 를 호출
         **/
        if (callsign.equals("fplccw") || callsign.equals("fplwon") ||
                callsign.equals("fplkdw") || callsign.equals("fplnow")) {

            // 현재 상태, 테스트 주행 중
            this.isTestDrive = true;
            isDrive = true;

            // 좌표 값을 읽어온다.
            try {

                if (callsign.equals("fplccw") || callsign.equals("fplwon")) {

                    if (callsign.equals("fplccw")) {
                        testDrivePointList = Util.readFileTexts(Environment.getExternalStorageDirectory() + "/ACC_NAVI/", "input1.txt", " ", true);
                    } else {
                        testDrivePointList = Util.readFileTexts(Environment.getExternalStorageDirectory() + "/ACC_NAVI/", "input1-1.txt", " ", true);
                    }
                } else if (callsign.equals("fplkdw")) {
                    testDrivePointList = Util.readFileTexts(Environment.getExternalStorageDirectory() + "/ACC_NAVI/", "input2.txt", " ", true);
                } else if (callsign.equals("fplnow")) {
                    testDrivePointList = Util.readFileTexts(Environment.getExternalStorageDirectory() + "/ACC_NAVI/", "input3.txt", " ", true);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {

                        // TODO: 2017-11-15   시작, 종료, 경유지를 설정한다.

                        mNlvView.clearRoutePosition();

                        // 시나리오 좌표를 설정한다.
                        setTestRoute(callsign);

                        // 경로 확정
                        int result = mNlvView.executeRP(NhsFlightActivity.this, 0, 0, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                switch (view.getId()) {

                                    case R.id.alert_btn_ok:
                                        mNlvView.executeRPDirect(0, 0);
                                        routeStatus = lanGetRouteInfo();

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                showDistanceTime(routeStatus);
                                            }
                                        }, 1000);

                                        break;

                                    case R.id.alert_btn_cancel:
                                        NhsFlightActivity.this.route.clear();
                                        mNlvView.clearRoutePosition();
                                        break;

                                }
                            }
                        });

                        if (result != -1) {

                            testDriveTimer = new Timer();
                            testDriveTimerTask = new TimerTask() {
                                @Override
                                public void run() {

                                    int size = testDrivePointList.length;

                                    if (testDriveIndex < size) {

                                        try {

                                            String temp = testDrivePointList[testDriveIndex];
                                            Log.d("jincocotest", "index : " + testDriveIndex + " value :" + temp);

                                            String[] pos = temp.split(",");

                                            AirGPSData airGPSData = new AirGPSData();


                                            airGPSData.uLat = (float) Double.parseDouble(pos[1]);
                                            airGPSData.uLon = (float) Double.parseDouble(pos[0]);

                                            airGPSData.uBear = Float.parseFloat(pos[3]);
                                            airGPSData.uAltitude = Integer.parseInt(pos[2]);
                                            airGPSData.uSpeed = Float.parseFloat(pos[4]);


                                            Log.d("step", testDriveIndex + "");

                                            /**
                                             // 고도가 3000 을 넘으면 자동 줌레벨을 시작한다.
                                             if (airGPSData.uAltitude > 2900) {
                                             if (testDriveToggle == -1)
                                             testDriveToggle = 0;
                                             }

                                             // 자동 줌레벨이 시작되었다면
                                             if (testDriveToggle != -1) {
                                             // 고도를 조절
                                             if (testDriveToggle == 0) {
                                             airGPSData.uAltitude = getTestAlt();
                                             } else // 스피드를 조절
                                             {
                                             getTestSpeed();
                                             airGPSData.uAltitude = testAlt;
                                             }
                                             }

                                             **/

                                            // 해발값 표시
                                            updateAtltitude(airGPSData.uAltitude);

                                            // 고도 표시
                                            updateDirection(airGPSData.uBear);

                                            // 속도 표시
                                            updateSpeed(airGPSData.uSpeed);

                                            // 자이로값 전달
                                            if (gyroValues != null) {
                                                airGPSData.fGyroX = gyroValues[0];
                                                airGPSData.fGyroY = gyroValues[1];
                                                airGPSData.fGyroZ = gyroValues[2];
                                            }

                                            boolean isMobileDataEnabled = Util.isMobileDataEnabled(NhsFlightActivity.this);
                                            boolean isWifiConnected = Util.isWifiConnected(NhsFlightActivity.this);


                                            if (callsign.equals("fplwon") &&    // 시나리오2 이고, 좌표 값이 같다면, 시나리오 종료
                                                    pos[0].equals("126.772003") &&
                                                    pos[1].equals("37.356934")) {
                                                stopTestDriveTimer();
                                                stopTtsTimer();

                                            }else if (callsign.equals("fplnow") &&    // 시나리오4 이고, 좌표 값이 같다면, 시나리오 종료
                                                    pos[0].equals("129.234344") &&
                                                    pos[1].equals("35.980221")) {
                                                stopTestDriveTimer();
                                                stopTtsTimer();

                                            }else if (isMobileDataEnabled || isWifiConnected) { // 통신이 끊겼으면 중지
//                                            } else if (isWifiConnected) {
                                                // 1초에 한번씩 gps 정보를 맵에게 전달한다.
                                                lanReceiveGPSData(airGPSData);
                                                lanExceuteGuide();
                                            }

//                                            int lvl = getZoomLevel((int) airGPSData.uAltitude, (int) airGPSData.uSpeed);
//                                            LanStorage.mNative.lanZoomByPositionWrapper(lvl, -1, -1);

                                        } catch (Exception ex) {

                                        } finally {

                                            testDriveIndex += testDrivePlus;

                                        }

                                    } else {

                                        // 시나리오가 끝났으면 tts를 중지한다.
                                        stopTtsTimer();
                                    }

                                }
                            };

                            // 1초에 한번씩 처리한다.
                            testDriveTimer.schedule(testDriveTimerTask, 2000, TEST_DRIVE_TIMER_INTERVAL);

                            // tts 타이머 실행
                            startTtsTimer();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    routeStatus = lanGetRouteInfo();

                                    if (routeStatus != null) {
                                        showDistanceTime(routeStatus);
                                    } else {
                                        // 관측기상 부터 차례대로 보여준다.
                                        showWeather(0);
                                    }

                                }
                            }, 1000);


                        }

                    } catch (Exception ex) {

                    }
                }
            }, 1000);


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    if (mNlvView != null) {

                        // 맵 타입에 맞게 변경한다.
                        String type = StorageUtil.getStorageModeEx(mContext, MAP_TYPE, String.valueOf(NAVI_MAP_VECTOR));
                        mNlvView.setMapKind(Integer.parseInt(type));

                        // 맵 레이아웃을 설정한다.
                        setMapLayout();

                        // 시스템 설정을한다.
                        setSystemParametersInfo();

                        boolean isAuto = StorageUtil.getStorageMode(getContext(), CHANGE_COLOR_AUTOMATICALLY);
                        boolean isNight = StorageUtil.getStorageMode(getContext(), IS_NIGHTTIME);

                        // 센서가 auto가 아니면 그에 맞는 조도로 변경한다.
                        if (!isAuto) {

                            if (isNight) {  // 야간 모드
                                lanSetMapColor(1);
                                Log.d("sensortest", "1");
                            } else {        // 주간 모드
                                lanSetMapColor(0);
                                Log.d("sensortest", "0");
                            }

                        }

                        // 관제센터 전송 타이머 설정
                        setSenderTimer();

                        try {
                            // kml data가 활성화되어 있으면 표시한다.
                            boolean useKmlData = StorageUtil.getStorageMode(mContext, KML_DATA);

                            if (useKmlData) {

                                // kml 파일 이름을 가져온다.
                                ArrayList<String> fileNameList = getKmlFileName();

                                int size = fileNameList.size();
                                int i = 0;

                                for (i = 0; i < size; i++) {
                                    lanSetKMLDataPath(KML_DATA_PATH + fileNameList.get(i));
                                    Log.d("KML", KML_DATA_PATH + fileNameList.get(i) + " LOAD KML");
                                }

                            }
                        } catch (Exception ex) {

                        }
                    }

                }
            }, 500);

        }

    }

    /**
     * 시나리오 경로를 지정한다.
     *
     * @author FIESTA
     * @since 오전 00:25
     **/
    private void setTestRoute(String callsign) {

        if (callsign.equals("fplccw")) {    // 시나리오 1

            // 시작 위치 (인천)
            mNlvView.setRoutePosition(NhsFlightActivity.this, Constants.NAVI_SETPOSITION_START, 126.794442, 37.558825,
                    "start name", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

            /// 종료 위치 (수원)
            mNlvView.setRoutePosition(NhsFlightActivity.this, Constants.NAVI_SETPOSITION_GOAL, 127.013864, 37.281467,
                    "goal name", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });


            // 경유지 (인천공항)
            mNlvView.setRoutePosition(NhsFlightActivity.this, Constants.NAVI_SETPOSITION_WAYPOINT, 126.440696, 37.460310,
                    "waypoint name1", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });


        } else if (callsign.equals("fplwon")) {  // 시나리오 2

            // 시작 위치 (수원)
            mNlvView.setRoutePosition(NhsFlightActivity.this, Constants.NAVI_SETPOSITION_START, 127.013864, 37.281467,
                    "start name", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

            // 종료 위치 (인천공항)
            mNlvView.setRoutePosition(NhsFlightActivity.this, Constants.NAVI_SETPOSITION_GOAL, 126.794442, 37.558825,
                    "goal name", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });


            // 경유지 (인천공항)
            mNlvView.setRoutePosition(NhsFlightActivity.this, Constants.NAVI_SETPOSITION_WAYPOINT, 126.440696, 37.460310,
                    "waypoint name1", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });


        } else if (callsign.equals("fplkdw")) {  // 시나리오 3

            // 시작 위치 (춘천 시청)
            mNlvView.setRoutePosition(NhsFlightActivity.this, Constants.NAVI_SETPOSITION_START, 127.733290, 37.882018,
                    "start name", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

            // 종료 위치 (묵호항)
            mNlvView.setRoutePosition(NhsFlightActivity.this, Constants.NAVI_SETPOSITION_GOAL, 129.117326, 37.553725,
                    "goal name", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });


            // 경유지 (하조대)
            mNlvView.setRoutePosition(NhsFlightActivity.this, Constants.NAVI_SETPOSITION_WAYPOINT, 128.726984, 38.022287,
                    "waypoint name1", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });


        } else if (callsign.equals("fplnow")) {  // 시나리오 4

            // 시작 위치 (대구항공교통본부)
            mNlvView.setRoutePosition(NhsFlightActivity.this, Constants.NAVI_SETPOSITION_START, 128.703004, 35.894373,
                    "start name", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

            // 종료 위치 (포항공항)
            mNlvView.setRoutePosition(NhsFlightActivity.this, Constants.NAVI_SETPOSITION_GOAL, 129.433929, 35.984843,
                    "goal name", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });


            // 경유지 (영천시청)
            mNlvView.setRoutePosition(NhsFlightActivity.this, Constants.NAVI_SETPOSITION_WAYPOINT, 128.938619, 35.973368,
                    "waypoint name1", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });


        }


    }

    // 고도값 상승, 하락 로직
    private int getTestAlt() {
        if (testDriveDir == true) { // 증가 방향
            testAlt += testAltStep;
            if (testAlt > testAltMax)
                testDriveDir = false;
        } else { // 감소방향
            testAlt -= testAltStep;
            if (testAlt < testAltMin) {
                testDriveDir = true;
                testDriveToggle = 1;
            }
        }
        return testAlt;
    }

    // 스피드 상승, 하락 로직
    private int getTestSpeed() {
        if (testDriveDir == true) { // 증가 방향
            testSpeed += testSpeedStep;
            if (testSpeed > testSpeedMax)
                testDriveDir = false;
        } else { // 감소방향
            testSpeed -= testSpeedStep;
            if (testSpeed < testSpeedMin) {
                testDriveDir = true;
                testDriveToggle = 0;
            }
        }

        testDrivePlus = (int) (testSpeed / 100); // 기본 속도가 100 이므로 100으로 나눈다.

        updateSpeed(testSpeed);

        return testSpeed;
    }

    public int getZoomLevel(int iAlt, int iSpeed) {
        int maxStep = 3;

        int[] tableSpeed = {310, 410, 510};
        int[] tableAlt = {3500, 4500, 5500};
        int[] tableZoom = {0, 1, 2, 3};

        int iSpeedFind = maxStep;
        for (int i = 0; i < maxStep; i++) {
            if (iSpeed <= tableSpeed[i]) {
                iSpeedFind = i;
                break;
            }
        }

        int iAltFind = maxStep;
        for (int i = 0; i < maxStep; i++) {
            if (iAlt <= tableAlt[i]) {
                iAltFind = i;
                break;
            }
        }

        return tableZoom[Math.max(iSpeedFind, iAltFind)];
    }

    /**
     * tts 음성 안내 중지
     *
     * @author FIESTA
     * @since 오전 1:08
     **/
    private void stopTtsTimer() {
        if (this.ttsTimer != null) {
            this.ttsTimer.cancel();
            this.ttsTimer = null;
        }
    }


    /**
     * 상황을 알려주는 tts 타이머
     *
     * @author FIESTA
     * @since 오전 12:36
     **/
    private void startTtsTimer() {
        this.ttsTimer = new Timer();
        this.ttsTimerTask = new TimerTask() {
            @Override
            public void run() {

                try {

                    int uType = LanStorage.mNative.lanNaviEventType();
                    int uValue = 0;
                    switch (uType) {

                        case Constants.NAVI_EVT_ALARM: {
                            AlarmType alarm = new AlarmType();
                            LanStorage.mNative.lanNaviEvent(alarm);
                            uValue = alarm.uAlarmType;

//                            switch (uValue) {
//
//                                case 1:
//                                    playTTS("경로 이탈 경보");
//                                    break;
//
//                            }
//
                            break;


                        }

                        case Constants.NAVI_EVT_GUIDE: {
                            GuideType guide = new GuideType();
                            LanStorage.mNative.lanNaviEvent(guide);
                            String strGuide = guide.szGuideText;
                            playTTS(strGuide);

                            if (strGuide.indexOf("목적지 부근입니다") > -1) {
                                stopTestDriveTimer();
                                stopTtsTimer();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showExitDialog();
                                    }
                                });

                            }

                            break;
                        }

                    }

                    Log.d("NAVI_EVT_ALARM", "uType : " + uType + " uValue " + uValue);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        };

        this.ttsTimer.schedule(this.ttsTimerTask, 500, TTS_TIMER_INTERVAL);

    }

    /**
     * 노탐 정보를 가져와서  맵에 보여준다.
     *
     * @author FIESTA
     * @since 오전 12:36
     **/
    private void getNotam() {

        NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
        NetworkParamUtil networkParamUtil = new NetworkParamUtil();

        StringEntity param = networkParamUtil.notamParam(getContext());
        NetworkProcess networkProcess = new NetworkProcess(getContext(),
                networkUrlUtil.getNotam(),
                param,
                new NetworkProcess.OnResultListener() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        try {

                            new ToastUtile().showCenterText(getContext(), getContext().getString(R.string.error_network) + "(" + statusCode + ")");

                        } catch (Exception ex) {

                        } finally {
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        try {

                            new ToastUtile().showCenterText(getContext(), getContext().getString(R.string.error_network) + "(" + statusCode + ")");

                        } catch (Exception ex) {

                        } finally {
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        try {
                            String msg = response.optString("result_msg");
                            String resultCode = response.optString("result_code");

                            if (resultCode.equalsIgnoreCase("Y")) {

                                JSONArray resultData = response.optJSONArray("result_data");

                                int size = resultData.length();
                                int i = 0;
                                JSONObject data = null;
                                String gisType = "CIRCLE";

                                Notam av = new Notam(size);

                                for (i = 0; i < size; i++) {

                                    try {

                                        data = resultData.getJSONObject(i);
                                        JSONArray gisList = data.optJSONArray("GIS_LIST");

                                        int j = 0;
                                        int x = 0;
                                        int jSize = gisList.length();
                                        int xSize = 0;
                                        int temp = 0;
                                        int temp2 = 0;

                                        JSONObject gis = null;
                                        String gisCrdnt = "";
                                        String[] gisCrdnts = null;

                                        for (j = 0; j < jSize; j++) {

                                            gis = gisList.optJSONObject(j);
                                            gisCrdnt = gis.optString("GIS_CRDNT");
                                            gisType = gis.optString("GIS_TYPE", "CIRCLE");

                                            gisCrdnts = gisCrdnt.split(",");
                                            xSize = gisCrdnts.length;
                                            av.item[i] = av.new Item(xSize);

                                            for (x = 0; x < xSize; x++) {

                                                temp = gisCrdnts[x].length();
                                                temp2 = gisCrdnts[x].indexOf('N');

                                                // 도분초를 좌표로 변환한다.
                                                av.item[i].pPoint[x].x = (float) Util.getDoToDgree(gisCrdnts[x].substring(0, temp2));
                                                av.item[i].pPoint[x].y = (float) Util.getDoToDgree(gisCrdnts[x].substring(temp2 + 1, temp - 1));

                                            }

                                        }

                                        av.item[i].uMinFL = Integer.parseInt(data.optString("MIN_FL", "0"));
                                        av.item[i].uMaxFL = Integer.parseInt(data.optString("MAX_FL", "0"));
                                        av.item[i].cGisType = (gisType.equalsIgnoreCase("CIRCLE") ? '1' : '0');
                                        av.item[i].rad = Integer.parseInt(data.optString("GIS_RADIUS", "0"));
                                        av.item[i].strEcode = data.optString("QCODE", "0");
                                        av.item[i].strQcode = data.optString("E_CODE", "0");
                                        ;

                                    } catch (Exception ex) {

                                    } finally {

                                    }

                                }

                                if (size > 0) {
                                    lanReceiveNotam(av);
                                }

                            }

                        } catch (Exception ex) {

                        }

                    }
                }, false);
        networkProcess.sendEmptyMessage(0);

    }


    /**
     * S노탐 정보를 가져와서  맵에 보여준다.
     *
     * @author FIESTA
     * @since 오전 12:36
     **/
    private void getSNotam() {

        NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
        NetworkParamUtil networkParamUtil = new NetworkParamUtil();

        StringEntity param = networkParamUtil.sNotamParam(getContext());
        NetworkProcess networkProcess = new NetworkProcess(getContext(),
                networkUrlUtil.getSnotam(),
                param,
                new NetworkProcess.OnResultListener() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        try {

                            new ToastUtile().showCenterText(getContext(), getContext().getString(R.string.error_network) + "(" + statusCode + ")");

                        } catch (Exception ex) {

                        } finally {
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        try {

                            new ToastUtile().showCenterText(getContext(), getContext().getString(R.string.error_network) + "(" + statusCode + ")");

                        } catch (Exception ex) {

                        } finally {
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        try {
                            String msg = response.optString("result_msg");
                            String resultCode = response.optString("result_code");

                            if (resultCode.equalsIgnoreCase("Y")) {

                                JSONArray resultData = response.optJSONArray("result_data");

                                int size = resultData.length();
                                int jSize = 0;
                                int i, j = 0;
                                JSONObject data = null;
                                JSONArray rwyList = null;
                                JSONObject rwy = null;

                                Snowtam av = new Snowtam(size);

                                for (i = 0; i < size; i++) {

                                    try {
                                        data = resultData.optJSONObject(i);
                                        rwyList = data.optJSONArray("RWY_LIST");
                                        jSize = rwyList.length();

                                        String apCd = data.optString("AP_CD", "");  //  공항 코드

                                        av.item[i] = av.new Item(jSize);

                                        for (j = 0; j < jSize; j++) {

                                            rwy = rwyList.getJSONObject(j);

                                            av.item[i].strAPCD = apCd;                   // 공항코드
                                            av.item[i].pSub[j].strRWYNM = rwy.optString("RWY_NM", "");
                                            av.item[i].pSub[j].strRWY_DEPST = rwy.optString("RWY_DEPST", "");
                                            av.item[i].pSub[j].strFCT = rwy.optString("FCT", "");

                                        }

                                    } catch (Exception ex) {

                                    }

                                }

                                if (size > 0) {
                                    lanReceiveSnowtam(av);
                                }

                            }

                        } catch (Exception ex) {

                        }


                    }
                }, false);
        networkProcess.sendEmptyMessage(0);

    }

    /**
     * 지점 기상 조회를 조회해서 맵에 보여준다.
     *
     * @author FIESTA
     * @since 오전 3:50
     **/
    private void showWeatherPoint() {

        NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
        NetworkParamUtil networkParamUtil = new NetworkParamUtil();
        StringEntity param = networkParamUtil.getWeatherPoint(NhsFlightActivity.this,
                "201709270100",
                "201709272359");
        new NetworkProcess(NhsFlightActivity.this, networkUrlUtil.getWeatherPoint(),
                param, new NetworkProcess.OnResultListener() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {

                    String msg = response.optString("result_msg");
                    String resultCode = response.optString("result_code");

                    // 메세지 출력
                    new ToastUtile().showCenterText(NhsFlightActivity.this, msg);

                    if (resultCode.equalsIgnoreCase("Y")) {

                        try {

                            JSONArray list = response.getJSONArray("result_data");
                            int size = list.length();
                            int i = 0;
                            JSONObject data = null;
                            WeatherInfo weatherInfo = new WeatherInfo(size);
                            String point = "";
                            String[] points = null;

                            for (i = 0; i < size; i++) {

                                data = list.getJSONObject(i);

                                try {

                                    if (!data.isNull("POINT_CRDNT")) {
                                        point = data.optString("POINT_CRDNT");
                                        points = point.split(" ");

                                        weatherInfo.item[i].uLat = (float) Double.parseDouble(points[0]);
                                        weatherInfo.item[i].uLon = (float) Double.parseDouble(points[1]);
                                    }

                                    if (!data.isNull("CLDALT")) {
                                        weatherInfo.item[i].uCLDALT = Integer.valueOf(data.optString("CLDALT", "0"));
                                    }
                                    if (!data.isNull("CLDAMT")) {
                                        weatherInfo.item[i].uCLDAMT = Integer.valueOf(data.optString("CLDAMT", "0"));
                                    }

                                    String wspdUnit = "";   // 풍속 단위

                                    if (!data.isNull("WSPD_UNIT")) {

                                        wspdUnit = data.optString("WSPD_UNIT", "m/s");
                                        if (wspdUnit.equalsIgnoreCase("m/s")) {
                                            weatherInfo.item[i].uElevUnit = 0; // "m/s"; 해발 고도 단위
                                        } else {

                                        }

                                    } else {
                                        wspdUnit = "m/s";
                                        weatherInfo.item[i].uElevUnit = 0; // "m/s"; 해발 고도 단위
                                    }


                                    if (!data.isNull("WSPD")) {

                                        if (wspdUnit.equalsIgnoreCase("m/s")) {

                                            // WSPD_UNIT = "m/s" 면 WD * 10 해서 소숫자리를 정수로 바꾸어 uSpeed
                                            weatherInfo.item[i].uSpeed = Math.round(Float.parseFloat(data.optString("WSPD", "0")) * 10);
                                        } else {
                                            // WSPD_UNIT = "m/s" 가 아니면 (WD / 2) * 10 해서 uSpeed
                                            weatherInfo.item[i].uSpeed = Math.round((Float.parseFloat(data.optString("WSPD", "0")) / 2) * 10);
                                        }

                                    }

                                    weatherInfo.item[i].uElev = 99; // 145.5   //해발 고도
                                    weatherInfo.item[i].uHeading = 27;


                                    if (!data.isNull("VIS")) {
                                        weatherInfo.item[i].uVIS = Integer.parseInt(data.optString("VIS", "0"));
                                    }

                                } catch (Exception ex) {

                                } finally {

                                }

                            }

                            if (size > 0) {
                                lanSetWeatherInfo(weatherInfo);
                            }

//\                // 기상 정보 (대구)
//                        WeatherInfo weatherInfo = new WeatherInfo(1);
//                        weatherInfo.item[0].uLat = (float)35.87797000;
//                        weatherInfo.item[0].uLon = (float)128.65295000;
//                        weatherInfo.item[0].uCLDALT = 0;
//                        weatherInfo.item[0].uCLDAMT = 0;
//                        weatherInfo.item[0].uElev = 145; // 145.5   //해발 고도
//                        weatherInfo.item[0].uHeading = 27;
//                        weatherInfo.item[0].uElevUnit = 0; // "m/s"; 해발 고도 단위
//                        weatherInfo.item[0].uSpeed = 7; //7.0; //풍속
//                        weatherInfo.item[0].uVIS = 2000;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception ex) {

                }

            }
        }, false).sendEmptyMessage(0);

    }

    private int zoomCount = 0;

    private void testZoom(long time) {

//        new Handler().postDelayed(zoomRun, time);

    }

//    Runnable zoomRun = new Runnable() {
//        @Override
//        public void run() {
//
//            zoomCount += 1;
//
//
////            final double fx = 128.703293d;
////            final double fy = 35.893513d;
//
//            final double fx = 126.9942216;
//            final double fy = 37.4366621;
//
//
//            if (zoomCount <= 2) {
//                Log.d("TEST", "======================================");
//                Log.d("TEST", " ");
//                Log.d("TEST", " ");
//                Log.d("TEST", " ");
//                Log.d("TEST", " ");
//                Log.d("TEST", " ");
//                Log.d("TEST", " ");
//                Log.d("TEST", " ");
//                Log.d("TEST", " ");
//                Log.d("TEST", " ");
//                Log.d("TEST", " ");
//                Log.d("TEST", " ");
//                Log.d("TEST", "======================================");
//
//                AirPoint point = lanMapToScreen(fx, fy);
//                lanZoomByPosition(0, point.x, point.y);
//                testZoom(1000);
//            } else if (zoomCount <= 4) {
//
//                if (zoomCount == 3) {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            AirPoint point = lanMapToScreen(fx, fy);
//                            lanZoomByPosition(1, point.x, point.y);
//                            testZoom(1000);
//                        }
//                    }, 2000);
//                } else {
//                    AirPoint point = lanMapToScreen(fx, fy);
//                    lanZoomByPosition(1, point.x, point.y);
//
//                }
//            }
//        }
//    };

    /**
     * 프로그래스바를 보여준다.
     *
     * @author FIESTA
     * @since 오전 3:50
     **/
    private void showProgress() {

        try {
            this.mProgressDialog = new ProgressDialog(NhsFlightActivity.this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
            this.mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            this.mProgressDialog.setTitle("");
            this.mProgressDialog.setMessage(NhsFlightActivity.this.getString(R.string.wait_message));
            this.mProgressDialog.setIndeterminate(true);
            this.mProgressDialog.setCancelable(false);
            this.mProgressDialog.show();
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

        if (this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
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
        final LoadingDialog loading = LoadingDialog.create(mContext, null, null);
        loading.show();

        FlightPlanService service = FlightPlanService.retrofit.create(FlightPlanService.class);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("planId", planId);
        params.put("planSn", planSn);

        RequestBody body = NetUtil.mapToJsonBody(NhsFlightActivity.this, params);
        //FlightPlanModel

        Call<NetSecurityModel> callback = service.repoFlightPlanDetail(body);
        callback.enqueue(new Callback<NetSecurityModel>() {
            @Override
            public void onResponse(Call<NetSecurityModel> call, Response<NetSecurityModel> response) {
                loading.dismiss();
                NetSecurityModel netSecurityModel = response.body();

                if (response.code() == 200) {

                    if (netSecurityModel.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {

                        String dec = new MagicSE_Util(getContext()).getDecData(netSecurityModel.getResult_data());

                        FlightPlanModel flightPlanModel = new Gson().fromJson(dec, FlightPlanModel.class);

                        if (flightPlanModel.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {

                            flightPlanInfo = flightPlanModel.getFplDetail();
                            route = flightPlanModel.getRoute();
                            flightPlanInfo.setFlightId(flightId);

                            // 비행을 시작한다.
                            startFlight();

                        } else {
                            new ToastUtile().showCenterText(mContext, flightPlanModel.getResult_msg());
                        }

                    }
                } else {
//                    new ToastUtile().showCenterText(mContext, getString(R.string.error_network));
                }
            }

            @Override
            public void onFailure(Call<NetSecurityModel> call, Throwable t) {
                loading.dismiss();
//                new ToastUtile().showCenterText(mContext, getString(R.string.error_network));
            }
        });
    }

    /**
     * 비행을 시작한다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-13 오후 11:26
     **/
    private void startFlight() {

        // 모의주행 시작
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (mNlvView != null) {

                    // 맵 타입에 맞게 변경한다.
                    String type = StorageUtil.getStorageModeEx(mContext, MAP_TYPE, String.valueOf(NAVI_MAP_VECTOR));
                    mNlvView.setMapKind(Integer.parseInt(type));

                    // 맵 레이아웃을 설정한다.
                    setMapLayout();

                    // 시스템 설정을한다.
                    setSystemParametersInfo();

                    boolean isAuto = StorageUtil.getStorageMode(getContext(), CHANGE_COLOR_AUTOMATICALLY);
                    boolean isNight = StorageUtil.getStorageMode(getContext(), IS_NIGHTTIME);

                    // 센서가 auto가 아니면 그에 맞는 조도로 변경한다.
                    if (!isAuto) {

                        if (isNight) {  // 야간 모드
                            lanSetMapColor(1);
                            Log.d("sensortest", "1");
                        } else {        // 주간 모드
                            lanSetMapColor(0);
                            Log.d("sensortest", "0");
                        }

                    }

                    // 맵에 떠 있는 pin들을 모두 지운다.
                    mNlvView.clearRoutePosition();

                    //test
//          mode = REAL_DRIVE;

                    switch (mode) {

                        case REAL_DRIVE:

                            // 경로 좌표를 표시한다.
                            setRealDrive();

                            // 관제센터 전송 타이머 설정
                            setSenderTimer();

                            // 관측기상 부터 차례대로 보여준다.
//                            showWeather(0);
                            break;

                        default:

                            // test
//                            setSaveTimer();
                            break;

                    }

                    // kml data가 활성화되어 있으면 표시한다.
                    boolean useKmlData = StorageUtil.getStorageMode(mContext, KML_DATA);

                    if (useKmlData) {

                        // kml 파일 이름을 가져온다.
                        ArrayList<String> fileNameList = getKmlFileName();

                        int size = fileNameList.size();
                        int i = 0;

                        for (i = 0; i < size; i++) {
                            lanSetKMLDataPath(KML_DATA_PATH + fileNameList.get(i));
                            Log.d("KML", KML_DATA_PATH + fileNameList.get(i) + " LOAD KML");
                        }

                    }

//                    showNotams();

                }
            }
        }, 1000);

    }


    /**
     * 화면에서 수행할 모드를 받는다.
     *
     * @author FIESTA
     * @since 오후 4:33
     **/
    private void getMode() {

        Bundle data = getIntent().getExtras();

        if (data != null) {
            this.mode = data.getInt(MODE, NONE_DRIVE);
        }
    }

    /**
     * 항적 데이터 저장 타이머
     *
     * @author FIESTA
     * @since 오전 1:12
     **/
    private void setSaveTimer() {

        // 1. lanStartTrajectory 시작
        // 2. lanReceiveGPSData
        // 3. lanStopTrajectory  종료
        lanStartTrajectory();

        // 로그 기록 시작 시간 기록
        gpsLogDate = new Date().getTime();

        // test 파일 생성

//        SimpleDateFormat sif = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
//        Util.writeStringAsFile(NativeImplement.GPS_LOG_DATA_PATH, "TRK_" + sif.format(gpsLogDate) +".trk", "test");

        //

        this.saveTimer = new Timer();
        this.saveTimerTask = new TimerTask() {
            @Override
            public void run() {

                try {
                    if (NhsFlightActivity.super.currentLocation != null) {

                        try {

                            float bear = 0;

                            // 방향각을 구한다.
                            if (beforeLocation != null) {
                                bear = Util.bearingP1toP2(beforeLocation.getLatitude(), beforeLocation.getLongitude(),
                                        NhsFlightActivity.super.currentLocation.getLatitude(),
                                        NhsFlightActivity.super.currentLocation.getLongitude());

                                if (Float.isNaN(bear)) {
                                    bear = 0;
                                }
                            }


                            AirGPSData airGPSData = new AirGPSData();

                            airGPSData.uLat = (float) NhsFlightActivity.super.currentLocation.getLatitude();
                            airGPSData.uLon = (float) NhsFlightActivity.super.currentLocation.getLongitude();

                            airGPSData.uSpeed = (float) currentSpeed;
                            airGPSData.uBear = bear;
                            airGPSData.uAltitude = (float) NhsFlightActivity.super.currentLocation.getAltitude();

                            if (gyroValues != null) {
                                airGPSData.fGyroX = gyroValues[0];
                                airGPSData.fGyroY = gyroValues[1];
                                airGPSData.fGyroZ = gyroValues[2];
                            }

                            Log.d("calGps", "android gps lat : " + NhsFlightActivity.super.currentLocation.getLatitude() + "");
                            Log.d("calGps", "android gps long : " + NhsFlightActivity.super.currentLocation.getLongitude() + "");
                            Log.d("calGps", "uLat : " + airGPSData.uLat + "");
                            Log.d("calGps", "uLon : " + airGPSData.uLon + "");
                            Log.d("calGps", "uSpeed : " + airGPSData.uSpeed + "");
                            Log.d("calGps", "uBear : " + airGPSData.uBear + "");
                            Log.d("calGps", "uAltitude : " + airGPSData.uAltitude + "");

                            if (!isTestDrive) {

                                lanReceiveGPSData(airGPSData);
                                lanExceuteGuide();
                            }

                            beforeLocation = NhsFlightActivity.super.currentLocation;

                            updateDirection(airGPSData.uBear);

//                    boolean is = lanIsTrajectory();
                            Log.d("test", "etst");
                        } catch (Exception ex) {

                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        this.saveTimer.schedule(this.saveTimerTask, 500, SAVE_TIMER_INTERVAL);

        super.setGpsListener(new OnGpsListener() {
            @Override
            public void onLocationChanged(Location location) {
                // gps 수신 요청한다.
                getLocation();
            }

            @Override
            public void onLocationFailed(String message) {

            }
        });

    }

    /**
     * 항적 데이터 저장 종료
     *
     * @author FIESTA
     * @since 오전 1:15
     **/
    private void stopSaveTimer() {

        lanStopTrajectory();

        if (this.saveTimer != null) {
            this.saveTimer.cancel();
        }
    }

    /**
     * 1. 5초마다 항적 데이터를 지상 관제로 전송(인터페이스 호출 :  covi_if_dv_017)
     * 2. 1초마다 맵에 항적을 던져줘야한다.
     * 비행시작시부터
     * lanStartTrajectory--> 항적데이터 저장 시작
     * 비행이 끝나면
     * lanStopTrajectory
     *
     * @author FIESTA
     * @since 오후 11:47
     **/
    private void setSenderTimer() {

        this.senderTimer = new Timer();
        this.senderTimerTask = new TimerTask() {
            @Override
            public void run() {

                // 현재 스피드가 10 이하면 2자리 수 이상 서버에 전송하기 때문에, 2자리로 맞춘다.
                if (currentSpeed < 10) {
                    currentSpeed = 10;
                }

                boolean isMobileDataEnabled = Util.isMobileDataEnabled(NhsFlightActivity.this);
                boolean isWifiConnected = Util.isWifiConnected(NhsFlightActivity.this);

                // TODO: 2017-09-20 테스트폰이 wifi용이라서.. wifi만 우선 체크한다. 빌드할때 아래 주석 풀것
                if (isMobileDataEnabled || isWifiConnected) {
//                if (isWifiConnected) {

                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (vWarring.getVisibility() == View.VISIBLE) {
                                    vWarring.clearAnimation();
                                    vWarring.setVisibility(View.GONE);
                                }
                            }
                        });

                        if (flightPlanInfo != null) {

                            FlightDriverService service = FlightDriverService.retrofit.create(FlightDriverService.class);

                            Map<String, Object> params = new HashMap<String, Object>();

                            try {
                                params.put("acrftCd", flightPlanInfo.getAcrftCd());        // 항공기ID(CallSign)
                                params.put("planId", flightPlanInfo.getPlanId());          // 비행계획서ID
                                params.put("planSn", flightPlanInfo.getPlanSn());        // 비행계획서 일련번호
                            } catch (Exception ex) {

                            }

                            if (isTestDrive) {

                                try {
                                    int size = testDrivePointList.length;
                                    String temp = "";

                                    if (testDriveIndex < size) {
                                        temp = testDrivePointList[testDriveIndex];
                                    } else {
                                        temp = testDrivePointList[size - 1];
                                    }

                                    if (temp.isEmpty()) {
                                        params.put("lat", "0");            // 위도좌표
                                        params.put("lon", "0");            // 경도좌표
                                        params.put("elev", "0");           // 해발고도
                                        params.put("speed", "10");          // 순항속도
                                    } else {
                                        String[] pos = temp.split(",");

                                        params.put("lat", (float) Double.parseDouble(pos[1]) + "");            // 위도좌표
                                        params.put("lon", (float) Double.parseDouble(pos[0]) + "");            // 경도좌표
                                        params.put("elev", pos[2]+"");                 // 해발고도
                                        params.put("gaeroInfo", Math.round(Float.parseFloat(pos[3]))+"");      // 자이로센서
                                        params.put("speed", Math.round(Float.parseFloat(pos[4]))+"");          // 순항속도
                                    }
                                } catch (Exception ex) {

                                }

                            } else {
                                try {
                                    if (currentLocation != null) {
                                        params.put("lat", currentLocation.getLatitude() + "");            // 위도좌표
                                        params.put("lon", currentLocation.getLongitude() + "");            // 경도좌표
                                        params.put("elev", currentLocation.getAltitude() + "");           // 해발고도
                                    } else {
                                        params.put("lat", "0");            // 위도좌표
                                        params.put("lon", "0");            // 경도좌표
                                        params.put("elev", "0");           // 해발고도
                                    }
                                } catch (Exception ex) {

                                }

                                params.put("speed", currentSpeed + "");          // 순항속도

                            }



                            try {

                                params.put("heading", "27");        // 해딩방향
                                params.put("flgtlogGb", "GPS");      // ADSB 와 GPS 구분
                                params.put("flgtIdx", flightPlanInfo.getFlightId());        // 비행정보 일련번호

                                params.put("callsign", flightPlanInfo.getCallsign());       // 콜사인
                                params.put("mbrId", StorageUtil.getStorageModeEx(NhsFlightActivity.this, LOGIN_MBR_ID));
                            } catch (Exception ex) {

                            }
                            RequestBody body = NetUtil.mapToJsonBody(NhsFlightActivity.this, params);

                            Call<NetSecurityModel> callback = service.repoFlightDriver(body);
                            callback.enqueue(new Callback<NetSecurityModel>() {
                                @Override
                                public void onResponse(Call<NetSecurityModel> call, Response<NetSecurityModel> response) {

                                    try {

                                        if (response.code() == 200) {

                                            NetSecurityModel netSecurityModel = response.body();

                                            if (netSecurityModel.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {

                                                String dec = new MagicSE_Util(getContext()).getDecData(netSecurityModel.getResult_data());

                                                final FlightDriveModel model = new Gson().fromJson(dec, FlightDriveModel.class);

                                                if (model.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {

                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            try {
                                                                if (model.getMsgsendContent() != null &&
                                                                        !model.getMsgsendContent().isEmpty()) {

                                                                    playTTS(model.getMsgsendContent());

                                                                    // 긴급 메세지가 있으면 1초 뒤에 재생한다.
                                                                    // 그런데 1초 너무 작은것 같다 1.5 초로 해놓자..
                                                                    if (!model.getEegcmsgMemo().isEmpty()) {
                                                                        new Handler().postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                playTTS(model.getEegcmsgMemo());
                                                                                showWarring();
                                                                            }
                                                                        }, 1500);
                                                                    }

                                                                } else if (model.getEegcmsgMemo() != null &&
                                                                        !model.getEegcmsgMemo().isEmpty()) {
                                                                    playTTS(model.getEegcmsgMemo());
                                                                    showWarring();
                                                                }
                                                            } catch (Exception ex) {

                                                            }
                                                        }

                                                    });

                                                    try {

                                                        if (model.getList() != null) {
                                                            // 맵에 데이터를 넘겨준다.
                                                            int size = model.getList().size();
                                                            int i = 0;

                                                            AroundAviation av = new AroundAviation(size);
                                                            AlmostAcrftModel almostAcrftModel = null;

                                                            for (i = 0; i < size; i++) {

                                                                try {

                                                                    almostAcrftModel = model.getList().get(i);

                                                                    av.item[i].strCallSign = flightPlanInfo.getCallsign();

                                                                    av.item[i].lon = (float) Double.parseDouble(almostAcrftModel.getLon());
                                                                    av.item[i].lat = (float) Double.parseDouble(almostAcrftModel.getLat());

                                                                    if (almostAcrftModel.getElev().isEmpty()) {
                                                                        av.item[i].elev = 0;
                                                                    } else {
                                                                        av.item[i].elev = Integer.parseInt(almostAcrftModel.getElev());
                                                                    }

                                                                    if (almostAcrftModel.getHeading().isEmpty()) {
                                                                        av.item[i].heading = 27;
                                                                    } else {
                                                                        av.item[i].heading = Integer.parseInt(almostAcrftModel.getHeading());
                                                                    }
                                                                    av.item[i].uState = 0;
                                                                } catch (Exception ex) {

                                                                } finally {

                                                                }

                                                            }

                                                            lanReceiveAroundAviation(av);

                                                        }

                                                    } catch (Exception ex) {

                                                    }
                                                    //new ToastUtile().showCenterText(mContext, "\"항적데이터 전송완료\"");


                                                } else {

                                                }
                                            }
                                        } else {
                                            Log.d("JeLib", "----------1-----------");
                                        }

                                    } catch (Exception ex) {

                                    }

                                }

                                @Override
                                public void onFailure(Call<NetSecurityModel> call, Throwable t) {
                                }
                            });


                        }
                        Log.d("test", "send");

                    } catch (Exception ex) {

                    }
                } else {   // 통신이 두절 되었을 경우

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (vWarring.getVisibility() == View.GONE) {
                                warringAnimation = AnimationUtils.loadAnimation(NhsFlightActivity.this, R.anim.warring);
                                vWarring.setVisibility(View.VISIBLE);
                                vWarring.startAnimation(warringAnimation);
                            }
                        }
                    });


                    try {

                        // 저장할 데이터를 구성한다.
                        JSONObject tempJson = new JSONObject();
                        tempJson.put("acrftCd", flightPlanInfo.getAcrftCd() + "");
                        tempJson.put("planId", flightPlanInfo.getPlanId() + "");
                        tempJson.put("planSn", flightPlanInfo.getPlanSn() + "");


                        if (isTestDrive) {

                            try {
                                int size = testDrivePointList.length;
                                String temp = "";

                                if (testDriveIndex < size) {
                                    temp = testDrivePointList[testDriveIndex];
                                } else {
                                    temp = testDrivePointList[size - 1];
                                }

                                if (temp.isEmpty()) {
                                    tempJson.put("lat", "0");            // 위도좌표
                                    tempJson.put("lon", "0");            // 경도좌표
                                    tempJson.put("elev", "0");           // 해발고도
                                    tempJson.put("speed", "10");          // 순항속도
                                } else {
                                    String[] pos = temp.split(",");

                                    tempJson.put("lat", (float) Double.parseDouble(pos[1]) + "");            // 위도좌표
                                    tempJson.put("lon", (float) Double.parseDouble(pos[0]) + "");            // 경도좌표
                                    tempJson.put("elev", pos[2]+"");                 // 해발고도
                                    tempJson.put("gaeroInfo", Math.round(Float.parseFloat(pos[3]))+"");      // 자이로센서
                                    tempJson.put("speed", Math.round(Float.parseFloat(pos[4]))+"");          // 순항속도
                                }
                            } catch (Exception ex) {

                            }

                        } else {
                            try {
                                if (currentLocation != null) {
                                    tempJson.put("lat", currentLocation.getLatitude() + "");            // 위도좌표
                                    tempJson.put("lon", currentLocation.getLongitude() + "");            // 경도좌표
                                    tempJson.put("elev", currentLocation.getAltitude() + "");           // 해발고도
                                } else {
                                    tempJson.put("lat", "0");            // 위도좌표
                                    tempJson.put("lon", "0");            // 경도좌표
                                    tempJson.put("elev", "0");           // 해발고도
                                }
                            } catch (Exception ex) {

                            }

                            tempJson.put("speed", currentSpeed + "");

                        }


                        tempJson.put("heading", "100");
                        tempJson.put("flgtlogGb", "GPS");
                        tempJson.put("gaeroInfo", "");
                        tempJson.put("callsign", flightPlanInfo.getCallsign() + "");
                        tempJson.put("flgtIdx", flightPlanInfo.getFlightId() + "");
                        tempJson.put("mbrId", StorageUtil.getStorageModeEx(NhsFlightActivity.this, LOGIN_MBR_ID));

                        // 파일에 저장한다.
                        Util.appendStringAsFile(Environment.getExternalStorageDirectory() + "/ACC_NAVI",
                                tempSaveFileName, tempJson.toString());

                    } catch (Exception ex) {

                    }
                }
            }
        };
        this.senderTimer.schedule(this.senderTimerTask, 0, SEND_TIMER_INTERVAL);


    }


    /**
     * 경고 깜빡이기..
     *
     * @author FIESTA
     * @since 오전 1:08
     **/
    private void showWarring() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 경고 깜빡이기..
                if (vWarring.getVisibility() == View.GONE) {
                    warringAnimation = AnimationUtils.loadAnimation(NhsFlightActivity.this, R.anim.warring);
                    vWarring.setVisibility(View.VISIBLE);
                    vWarring.startAnimation(warringAnimation);
                }
            }
        });

    }

    /**
     * 지상 관제 센터 전송 중지
     *
     * @author FIESTA
     * @since 오전 1:08
     **/
    private void stopSenderTimer() {
        if (this.senderTimer != null) {
            this.senderTimer.cancel();
        }
    }

    /**
     * 테스트 드라이브 중지
     *
     * @author FIESTA
     * @since 오전 1:08
     **/
    private void stopTestDriveTimer() {
        if (this.testDriveTimer != null) {
            this.testDriveTimer.cancel();
        }
    }

    /**
     * Nmea 리스너를 설정한다
     *
     * @author FIESTA
     * @since 오전 12:39
     **/
    private void setNmeaListener() {

        android.location.LocationManager location1 = (android.location.LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);

        String bestProvider = android.location.LocationManager.GPS_PROVIDER;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location1.addNmeaListener(nml);
        location1.requestLocationUpdates(bestProvider, 1000, 0, new DefaultLocationProvider());

    }

    GpsStatus.NmeaListener nml = new GpsStatus.NmeaListener() {

        public void onNmeaReceived(long timestamp, String nmea) {
            // TODO Auto-generated method stub
            //Toast.makeText(_context, "nmea: " + nmea, Toast.LENGTH_SHORT).show();
            Log.d("test", "test " + nmea);
        }
    };

    ////////////////////////////////////////////// 조도 센서 테스트 시작
    private void sensorTest() {

        //mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        //lanSetMapColor(1);

    }

    /**
     * 레이아웃 설정
     *
     * @author FIESTA
     * @since 오전 1:20
     **/
    private void setLayout() {

        this.mNlvView = (NhsLanView) findViewById(R.id.nlv_view);
        this.mNlvView.setOnClickOptionMapMenu(this);
        this.mNlvView.setShowPopup(true);

        this.mTvAltitude = (TextView) findViewById(R.id.tv_altitude);
        this.mTvAircraftSpeed = (TextView) findViewById(R.id.tv_aircraft_speed);
        this.mTvDirection = (TextView) findViewById(R.id.tv_direction);
        this.mTvDistance = (TextView) findViewById(R.id.tv_distance);
        this.mTvNw = (TextView) findViewById(R.id.tv_nw);
        this.mTv2d3d = (TextView) findViewById(R.id.tv_2d_3d);
        this.vWarring = findViewById(R.id.ll_warring);
        this.tvN = (TextView) findViewById(R.id.tv_n);
        this.tvE = (TextView) findViewById(R.id.tv_e);
        this.llNavPin = (LinearLayout) this.findViewById(R.id.ll_nav_pin);

        this.rlMenu = (RelativeLayout) findViewById(R.id.rl_menu);
        this.rlMenu.setVisibility(View.GONE);

        findViewById(R.id.ll_ete_eta).setOnClickListener(this);
        findViewById(R.id.tv_ete_eta).setTag(1);

        // 부모 뷰를 터치하면 메뉴가 나오도록 한다.
        findViewById(R.id.rl_parent).setOnClickListener(this);

        // 이머전씨 클릭
        findViewById(R.id.ll_emergency).setOnClickListener(this);

        // 맵 클릭
        mNlvView = (NhsLanView) findViewById(R.id.nlv_view);
        findViewById(R.id.nlv_view).setOnClickListener(this);

        // 종료
//    findViewById(R.id.tv_exit).setOnClickListener(this);

        // 비행 정보 레이아웃 설정
        if (!StorageUtil.getStorageMode(getContext(), ISL_LAYOUT_AIRCRAFTSPEED)) {
            findViewById(R.id.ll_aircraft_speed).setVisibility(View.GONE);
        }

        if (!StorageUtil.getStorageMode(getContext(), IS_LAYOUT_DIRECTION)) {
            findViewById(R.id.ll_direction).setVisibility(View.GONE);
        }

        if (!StorageUtil.getStorageMode(getContext(), IS_LAYOUT_DISTANCE)) {
            findViewById(R.id.ll_distance).setVisibility(View.GONE);
        }

        if (!StorageUtil.getStorageMode(getContext(), IS_LAYOUT_ALTITUDE)) {
            findViewById(R.id.ll_altitude).setVisibility(View.GONE);
        }

        if (!StorageUtil.getStorageMode(getContext(), IS_LAYOUT_NW)) {
            findViewById(R.id.ll_nw).setVisibility(View.GONE);
        }

        findViewById(R.id.ll_main).setOnClickListener(this);
        findViewById(R.id.ll_godo).setOnClickListener(this);
        findViewById(R.id.ll_handup).setOnClickListener(this);
        findViewById(R.id.ll_godo2).setOnClickListener(this);
        findViewById(R.id.ll_north_up).setOnClickListener(this);
        findViewById(R.id.ll_weather).setOnClickListener(this);
        findViewById(R.id.ll_3d).setOnClickListener(this);


    }

    /**
     * 각종 센서 및 기능을 설정한다
     *
     * @author FIESTA
     * @since 오전 1:22
     **/
    private void settingSensors() {

        // GPS 위치 얻기 시작
        getLocationManager().get();

        // GPS 로그 여부
        LocationManager.enableLog(true);

        // 센서 매니저 얻기
        this.mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.mAccSensor = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //sensorTest();

    }

    /**
     * 고도 값을 표시한다.
     *
     * @author FIESTA
     * @since 오전 2:18
     **/
    private void updateAtltitude(final double atltitude) {

        long now = Calendar.getInstance().getTimeInMillis();

        long calTime = now - this.beforeShowAltitudeData;

        if (calTime > SHOW_VALUE_TIME) {

            this.totalAltitude += atltitude;
            this.totalAltitudeCount += 1;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("atltitude", atltitude + "");

                    if (altitudeType == ELEVATION) {  // 해발고도
                        mTvAltitude.setText(String.format("%.2f", atltitude));
                    } else {  // 지표고도
                        mTvAltitude.setText(String.format("%.2f", atltitude - 30.5));
                    }
                }
            });

            // 이전에 속도를 보여준 시간을 기록한다.
            this.beforeShowAltitudeData = Calendar.getInstance().getTimeInMillis();
            ;

        }

    }

    /**
     * 비행진행 방향 표시한다.
     *
     * @author FIESTA
     * @since 오전 2:18
     **/
    private void updateDirection(final double direction) {

        long now = Calendar.getInstance().getTimeInMillis();

        long calTime = now - this.beforeShowDirectionData;

        if (calTime > SHOW_VALUE_TIME) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("direction", direction + "");

                    mTvDirection.setText(String.format("%.2f", direction));

                }
            });

            // 이전에 진행 방향을 기록한다.
            this.beforeShowDirectionData = Calendar.getInstance().getTimeInMillis();
            ;

        }

    }

    /**
     * 속도 값을 표시한다.
     *
     * @author FIESTA
     * @since 오전 2:18
     **/
    private void updateSpeed(final double speed) {

        this.currentSpeed = speed;

        long now = Calendar.getInstance().getTimeInMillis();
        ;
        long calTime = now - this.beforeShowSpeedData;

        if (calTime > SHOW_VALUE_TIME) {

            this.totalSpeed += speed;
            this.totalSpeedCount += 1;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTvAircraftSpeed.setText(String.format("%.2f", speed));
                }
            });

            // 이전에 속도를 보여준 시간을 기록한다.
            this.beforeShowSpeedData = Calendar.getInstance().getTimeInMillis();
        }


    }

    /**
     * 모의 주행 시작
     *
     * @author FIESTA
     * @since 오전 12:09
     **/
    private void setRealDrive() {

        mNlvView.clearRoutePosition();


        String arrival = this.flightPlanInfo.getPlanArrival(); //"1269726409 374093284";
        String departure = this.flightPlanInfo.getPlanDeparture(); //"1269127807 374182090";
        final String route = this.flightPlanInfo.getPlanRoute(); //"1269240188 373384475";
//        String arrival = "1269726409 374093284";
//        String departure = "1269127807 374182090";
//        String route = "1269240188 373384475";
        String totalEet = flightPlanInfo.getPlanTeet();

        //String arrival = this.flightPlanInfo.getPlanArrival();
        //String departure =this.flightPlanInfo.getPlanDeparture();
        //String route = this.flightPlanInfo.getPlanRoute();
        //String totalEet = this.flightPlanInfo.getPlanTeet();

//      String arrival = data.getString("arrival", "");
//      String departure = data.getString("departure", "");
//      String route = data.getString("route", "");
//      String totalEet = data.getString("totalEet", "");

        // 총 소요시간을 보여준다.
        String convertTotalEet = String.format(getString(R.string.ete), totalEet);
        ((TextView) findViewById(R.id.tv_ete_eta)).setText(convertTotalEet);

        // 도착예정시간을 보여준다.
//        Date date = new Date();
//        SimpleDateFormat std = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
//        date.setTime(date.getTime() + ((1000 * 60) * 60) * Integer.parseInt(totalEet));
//        String convertStrDate = std.format(date);
//        ((TextView) findViewById(R.id.tv_eta)).setText("ETA : " + " " + convertStrDate);

        if (this.route != null) {

            int size = this.route.size();
            int i = 0;

            // 시작 위치
            int result = mNlvView.setRoutePosition(NhsFlightActivity.this, Constants.NAVI_SETPOSITION_START, Double.parseDouble(this.route.get(0).getLon()), Double.parseDouble(this.route.get(0).getLat()),
                    "start name", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

            if (result == 0) {
                // 종료 위치
                result = mNlvView.setRoutePosition(NhsFlightActivity.this, Constants.NAVI_SETPOSITION_GOAL, Double.parseDouble(this.route.get(this.route.size() - 1).getLon()), Double.parseDouble(this.route.get(this.route.size() - 1).getLat()),
                        "goal name", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });


                if (result == 0) {
                    // 경유지 등록
                    for (i = 1; i < size - 1; i++) {

                        if (result == 0) {
                            result = mNlvView.setRoutePosition(NhsFlightActivity.this, Constants.NAVI_SETPOSITION_WAYPOINT, Double.parseDouble(this.route.get(i).getLon()), Double.parseDouble(this.route.get(i).getLat()),
                                    "waypoint name", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    });
                        } else {
                            break;
                        }
                    }
                }
            }

            result = mNlvView.executeRP(NhsFlightActivity.this, 0, 0, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {

                        case R.id.alert_btn_ok:
                            mNlvView.executeRPDirect(0, 0);
                            routeStatus = lanGetRouteInfo();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showDistanceTime(routeStatus);
                                }
                            }, 1000);

                            break;

                        case R.id.alert_btn_cancel:
                            NhsFlightActivity.this.route.clear();
                            mNlvView.clearRoutePosition();
                            break;

                    }
                }
            });

            if (result != -1) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        routeStatus = lanGetRouteInfo();

                        if (routeStatus != null) {
                            showDistanceTime(routeStatus);
                        } else {
                            // 관측기상 부터 차례대로 보여준다.
                            showWeather(0);
                        }

                    }
                }, 1000);

                // 경로에 따라 위성사진 다운로드할 path 및 파일이름이 나온다.
                /**
                 AirDoyupList doyupList = lanGetRouteDoyupList();

                 int nSize = lanGetRouteDoyupListCount();
                 Log.d("test", "Doyup Download Total Count: " + nSize + "");
                 if (nSize != 0) {
                 for (i = 0; i < nSize; i++) {
                 try {
                 doyupList.lists[i].path = new String(doyupList.lists[i].arrPath, "ksc5601");
                 } catch (UnsupportedEncodingException e) {
                 e.printStackTrace();
                 }
                 Log.d("test", "Doyup Download Path: " + doyupList.lists[i].path + "");
                 }
                 }

                 **/
            }

        }

        // 항적 데이터 저장 시작
        setSaveTimer();

        isDrive = true;
    }

    private DialogType1 distanceDialog = null;

    private void showDistanceTime(AirRouteStatus routeStatus) {


//        dialog = new Dialog(NhsFlightActivity.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        //팝업 주위를 눌러도 창 닫히지 않음
//        dialog.setCanceledOnTouchOutside(false);
//
//        dialog.setContentView(R.layout.dialog_popup_type1);
//
//        TextView alert_title = (TextView) dialog.findViewById(R.id.alert_title);
//        TextView alert_msg = (TextView) dialog.findViewById(R.id.alert_msg);

//        alert_title.setText("예상시간 및 거리");

        StringBuilder sb = new StringBuilder();
        sb.append("총 거리 : ");
        sb.append(routeStatus.uTotalDist);
        sb.append("Km");
        sb.append("\n");
        sb.append("총 소요시간 : ");
        sb.append(routeStatus.uTotalTime);
        sb.append("초");
        sb.append("\n");

        if (routeStatus.waypointList != null) {

            int size = routeStatus.waypointList.length;
            int i = 0;

            for (i = 0; i < size; i++) {

                sb.append("+ 경유지 " + (i + 1));
                sb.append("\n");
                sb.append("거리 : ");
                sb.append(routeStatus.waypointList[i].dist);
                sb.append("Km");
                sb.append("\n");
                sb.append("소요시간 : ");
                sb.append(routeStatus.waypointList[i].time);
                sb.append("초");
                sb.append("\n");

            }

        }


//        alert_msg.setText(sb.toString());

//        TextViewEx alert_btn_ok = (TextViewEx) dialog.findViewById(R.id.alert_btn_ok);
//        TextViewEx alert_btn_cancel = (TextViewEx) dialog.findViewById(R.id.alert_btn_cancel);

//        alert_btn_ok.setText("확인");
//        alert_btn_cancel.setVisibility(View.GONE);
//
//        alert_btn_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });

        distanceDialog = new DialogType1(NhsFlightActivity.this,
                "예상시간 및 거리", sb.toString(), "확인",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        distanceDialog.hideDialog();
                        // 관측기상 부터 차례대로 보여준다.
                        showWeather(0);
                    }
                }, "", null);

        //팝업 바탕 알파값 주기
//        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//        lp.dimAmount = 0.95f;
//        dialog.getWindow().setAttributes(lp);
//        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

//        dialog.show();

    }

    @Override
    public void onLocationChanged(final Location location) {
        super.onLocationChanged(location);
        getLocationManager().get();

        if (!this.isTestDrive) {
            updateAtltitude(location.getAltitude());
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String nData = String.format(getResources().getString(R.string.n_data), location.getLongitude() + "");
                String eData = String.format(getResources().getString(R.string.e_data), location.getLatitude() + "");
                tvN.setText(nData);
                tvE.setText(eData);
            }
        });
    }

    @Override
    public void onLocationFailed(@FailType int failType) {
        super.onLocationFailed(failType);
        getLocationManager().get();
    }

    private void start5secodTts() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // step 1
                playTTS("경유지 부근입니다.");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        findViewById(R.id.ll_test2).setVisibility(View.GONE);
                        findViewById(R.id.ll_test1).setVisibility(View.VISIBLE);
                        findViewById(R.id.ll_test1).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                view.setVisibility(View.GONE);
                                findViewById(R.id.ll_test1).setVisibility(View.GONE);
                                findViewById(R.id.ll_test2).setVisibility(View.VISIBLE);

                                findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        findViewById(R.id.ll_test1).setVisibility(View.GONE);
                                        findViewById(R.id.ll_test2).setVisibility(View.GONE);
                                        vWarring.clearAnimation();
                                        vWarring.setVisibility(View.GONE);

                                    }
                                });

                            }
                        });

                        // step 3
                        playTTS("풍량 NW 350.1  풍속 5.1 노트, 시정 6.2 킬로미터, 기온 8.9도씨, 운고 1500 피트");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                // step 2
                                playTTS("해당 비행 구역은 안전의 이유로 비행이 제한된 구역입니다. 비행자는 해당 구역을 벗어나기를 권장합니다.");

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        warringAnimation = AnimationUtils.loadAnimation(NhsFlightActivity.this, R.anim.warring);
                                        vWarring.setVisibility(View.VISIBLE);
                                        vWarring.startAnimation(warringAnimation);
                                        // step 4
                                        playTTS("현재 고도가 너무 낮습니다. 고도를 높이시기 바랍니다.");

                                    }
                                }, (12000));  // 음성 출력 시간이 있어서 2초 더 둔다. 10초 텀을 둔다

                            }
                        }, (12000));  // 음성 출력 시간이 있어서 2초 더 둔다. 10초 텀을 둔다


                    }
                }, (12000));  // 음성 출력 시간이 있어서 2초 더 둔다. 10초 텀을 둔다

            }
        }, (10000));

    }

    /**
     * mTts 재생
     *
     * @author FIESTA
     * @since 오전 3:35
     **/
    private void playTTS(final String msg) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean isSoundMode = StorageUtil.getStorageMode(getContext(), IS_TTS_SOUND);
                TextView t = (TextView) findViewById(R.id.tv_info_msg);
                TextView t2 = (TextView) findViewById(R.id.tv_info_msg2);
                findViewById(R.id.ll_test1).setVisibility(View.VISIBLE);
                t.setSelected(true);
                t.setText(msg);
                t2.setText(msg);

                if (isSoundMode) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ttsGreater21(msg);
                    } else {
                        ttsUnder20(msg);
                    }

                }

                // 상단 위에 메시창을 클릭하면, 화면 중앙에 메세지가 출력되도록 하는 기능이다.
                findViewById(R.id.ll_test1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        view.setVisibility(View.GONE);
                        findViewById(R.id.ll_test1).setVisibility(View.GONE);
                        findViewById(R.id.ll_test2).setVisibility(View.VISIBLE);

                        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                findViewById(R.id.ll_test1).setVisibility(View.GONE);
                                findViewById(R.id.ll_test2).setVisibility(View.GONE);
                                vWarring.clearAnimation();
                                vWarring.setVisibility(View.GONE);

                            }
                        });

                    }
                });
            }
        });

    }

    private String beforeTtsMsg = "";   // 이전 tts 메세지

    @SuppressWarnings("deprecation")
    synchronized private void ttsUnder20(String text) {

        // 읽는 중이 아니라면 이전 tts 메세지 삭제하고, 읽을 수 있도록 한다.
        if (!mTts.isSpeaking()) {
            this.beforeTtsMsg = "";
        }

        if (!this.beforeTtsMsg.equals(text)) {   // 같은 메세지이면 읽지 않는다.
            HashMap<String, String> map = new HashMap<>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
            mTts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
            this.beforeTtsMsg = text;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    synchronized private void ttsGreater21(String text) {

        // 읽는 중이 아니라면 이전 tts 메세지 삭제하고, 읽을 수 있도록 한다.
        if (!mTts.isSpeaking()) {
            this.beforeTtsMsg = "";
        }

        if (!this.beforeTtsMsg.equals(text)) {   // 같은 메세지이면 읽지 않는다.
            String utteranceId = this.hashCode() + "";
            mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        }
    }

    /**
     * TTS 설정
     *
     * @author FIESTA
     * @since 오전 3:26
     **/
    private void setupTTS() {

        this.mTts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    mTts.setLanguage(Locale.KOREAN);
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mNlvView != null) mNlvView.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNlvView != null) mNlvView.onResume();
        try {
            if (this.mSensorManager != null) {

                // 센서 등록 (UI 표시를 위한)
                this.mSensorManager.registerListener(this, mAccSensor, SensorManager.SENSOR_DELAY_UI);
                //this.mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNlvView != null) mNlvView.onPause();
        try {
            if (this.mSensorManager != null) {
                this.mSensorManager.unregisterListener(this);
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {

            if (this.mTts != null) {
                this.mTts.stop();
                this.mTts.shutdown();
            }


        } catch (Exception e) {

        }

        // 지상 관제센터 전송 중지
        stopSenderTimer();

        // 항적 데이터 저장 중지
        stopSaveTimer();

        // 테스트 드라이브 중지
        stopTestDriveTimer();

        // tts 타이머 종료
        stopTtsTimer();

        mNlvView.clearRoutePosition();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // 가속 센서가 전달한 데이터인 경우
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            gyroValues = sensorEvent.values;
            final float gyroX = sensorEvent.values[0];

            double calVal = Math.sqrt(Math.pow(sensorEvent.values[0], 2) + Math.pow(sensorEvent.values[1], 2));

            // 속도 표시
            if (!this.isTestDrive) {
                updateSpeed(calVal);
            }

            long now = Calendar.getInstance().getTimeInMillis();
            long calTime = now - this.beforeShowGyroPin;

            if (calTime > SHOW_VALUE_GYRO_TIME) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateNavPin(gyroX, 0);
                    }
                });

                // 이전에 속도를 보여준 시간을 기록한다.
                this.beforeShowGyroPin = Calendar.getInstance().getTimeInMillis();

            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View view) {

        int visible = 0;

        switch (view.getId()) {

            case R.id.ll_ete_eta:

                int type = 0;

                try {
                    type = (int) view.getTag();
                } catch (Exception ex) {

                }

                TextView tvEteEta = (TextView) findViewById(R.id.tv_ete_eta);

                if (flightPlanInfo != null) {

                    String totalEet = flightPlanInfo.getPlanTeet();

                    if (type == 1) {

                        // 도착예정시간을 보여준다.
                        Date date = new Date();
                        SimpleDateFormat std = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
                        date.setTime(date.getTime() + ((1000 * 60) * 60) * Integer.parseInt(totalEet));
                        String convertStrDate = std.format(date);
                        tvEteEta.setText("ETA : " + " " + convertStrDate);
                        view.setTag(0);

                    } else {

                        // 총 소요시간을 보여준다.
                        String convertTotalEet = String.format(getString(R.string.ete), totalEet);
                        tvEteEta.setText(convertTotalEet);
                        view.setTag(1);
                    }

                }

                break;

            case R.id.ll_godo2:

                visible = findViewById(R.id.ll_godo2).getVisibility();

                if (visible == View.VISIBLE) {
                    this.altitudeType = ALTITUDE_ALTITUDE;  // 해발고도
                    initMenu();
                }


                break;
            case R.id.ll_godo:

                visible = findViewById(R.id.ll_godo2).getVisibility();

                if (visible == View.VISIBLE) {
                    this.altitudeType = ELEVATION;  // 지표고도
                    initMenu();
                } else {
                    findViewById(R.id.ll_godo2).setVisibility(View.VISIBLE);
                }

                break;

            case R.id.ll_north_up:

                visible = findViewById(R.id.ll_3d).getVisibility();

                if (visible == View.VISIBLE) {
                    mNlvView.setMapViewmode(1);
                    initMenu();
                }


                break;

            case R.id.ll_handup:

                visible = findViewById(R.id.ll_3d).getVisibility();

                if (visible == View.VISIBLE) {
                    mNlvView.setMapViewmode(0);
                    initMenu();
                } else {
                    findViewById(R.id.ll_3d).setVisibility(View.VISIBLE);
                    findViewById(R.id.ll_north_up).setVisibility(View.VISIBLE);
                }

                break;

            case R.id.ll_3d:

                visible = findViewById(R.id.ll_3d).getVisibility();

                if (visible == View.VISIBLE) {

                    String mapType = StorageUtil.getStorageModeEx(getContext(), MAP_TYPE);

                    // 3d면 2d로 2d면 3d로
                    if (mapType.equalsIgnoreCase(String.valueOf(NAVI_MAP_VECTOR)) ||
                            mapType.equalsIgnoreCase(String.valueOf(NAVI_MAP_SATELLITE))) {

                        mNlvView.setMapKind(NAVI_MAP_DEM);
                        StorageUtil.setStorageMode(getContext(), MAP_TYPE, String.valueOf(NAVI_MAP_DEM));
                        this.mTv2d3d.setText("2D");

                    } else {

                        mNlvView.setMapKind(NAVI_MAP_VECTOR);
                        StorageUtil.setStorageMode(getContext(), MAP_TYPE, String.valueOf(NAVI_MAP_VECTOR));
                        this.mTv2d3d.setText("3D");

                    }

                    initMenu();

                }

                break;

            case R.id.ll_weather:
                mPopup1 = new DialogType1(getContext(), "기상특보", "풍량 NW 350.1\n풍속 5.1 노트\n시정 6.2 킬로미터\n기온 8.9도씨\n운고 1500 피트", getString(R.string.btn_confirm), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopup1.hideDialog();

                    }
                }, "", null);
                playTTS("풍량 NW 350.1  풍속 5.1 노트, 시정 6.2 킬로미터, 기온 8.9도씨, 운고 1500 피트");
                break;
            // 부모뷰를 클릭하면
            case R.id.rl_parent:
            case R.id.nlv_view:     // 지도를 클릭하면

                // 메뉴를 보여준다.
                this.rlMenu.setVisibility(View.GONE);

                break;

            // 이미전씨 (핫키 선택)
            case R.id.ll_emergency:

                showHokey();

                break;
            case R.id.ll_main:
                if (this.isDrive) {
                    showExitDialog();
                } else {
                    finish();
                }
                break;
            case R.id.tv_exit:

                this.dialogFinishFlight = new DialogFinishFlight(this, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        switch (view.getId()) {

                            case R.id.fl_finish_flight:

                                dialogSendComplateFlight = new DialogSendComplateFlight(getContext());
                                dialogSendComplateFlight.show();

                                break;

                            case R.id.btn_cancel:

                                dialogFinishFlight.dismiss();
                                finish();

                                break;

                            case R.id.fl_favorites:

                                dialogAddFavorites = new DialogAddFavorites(getContext(), new DialogAddFavorites.IFavorite() {
                                    @Override
                                    public void onSave(String name) {

                                    }

                                    @Override
                                    public void onCancel() {
                                        finish();
                                    }
                                });

                                dialogFinishFlight.show();

                                break;

                        }

                        dialogFinishFlight.dismiss();
                    }
                });

                this.dialogFinishFlight.show();
                break;

        }

    }


    /**
     * hot key 다이얼로그를 보여준다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-13 오후 11:25
     **/
    public void showHokey() {

        this.dialogSelectHotkey = new DialogSelectHotkey(this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String type = "";
                String msg = "";
                String title = "";

                switch (view.getId()) {

                    // 메이데이
                    case R.id.bt_menu1:
                        type = "1";
                        title = "메이데이";
                        msg = "메이데이 전송";
                        // new ToastUtile().showCenterText(NhsFlightActivity.this, "메이데이, 메이데이 전송완료");
                        break;

                    // 임시 작륙
                    case R.id.bt_menu2:
                        type = "2";
                        title = "임시착륙";
                        msg = "임시착륙 보고";
                        //new ToastUtile().showCenterText(NhsFlightActivity.this, "임시착륙 보고 완료");
                        break;

                    // 적란운
                    case R.id.bt_menu3:
                        type = "6";
                        title = "적란운";
                        msg = "적란운 발생";
                        //new ToastUtile().showCenterText(NhsFlightActivity.this, "적란운 발생 보고 완료");
                        break;

                    // 난기류
                    case R.id.bt_menu4:
                        type = "4";
                        title = "난기류";
                        msg = "난기류 발생";
                        //new ToastUtile().showCenterText(NhsFlightActivity.this, "난기류 발생 보고 완료");
                        break;

                    // 기체착빙
                    case R.id.bt_menu5:
                        type = "5";
                        title = "기체착빙";
                        msg = "기체착빙 발생";
                        //new ToastUtile().showCenterText(NhsFlightActivity.this, "기체착빙 보고 완료");
                        break;

                    // 우박
                    case R.id.bt_menu6:
                        type = "7";
                        title = "우박";
                        msg = "우박 발생";
                        //new ToastUtile().showCenterText(NhsFlightActivity.this, "우박 발생 보고 완료");
                        break;

                    case R.id.btn_cancel:
                        dialogSelectHotkey.dismiss();
                        break;
                }

                if (!type.isEmpty()) {
                    final String finalType = type;

                    mPopup1 = new DialogType1(getContext(), title, msg, "전송", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPopup1.hideDialog();
                            sendHotkey(finalType);

                        }
                    }, "취소", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mPopup1.hideDialog();
                        }
                    });
                }

            }
        });

        this.dialogSelectHotkey.show();


    }

    /**
     * 메뉴 초기화
     *
     * @author FIESTA
     * @since 오후 5:02
     **/
    private void sendHotkey(String type){

        // 상황별로 서버로 요청한다.
        if (!type.isEmpty() && flightPlanInfo != null) {

            final LoadingDialog loading = LoadingDialog.create(mContext, null, null);
            loading.show();

            // 속도가 2자리 이상, 4자리 이하로 서버에게 요청을 해야한다.
            if (currentSpeed < 10) {
                currentSpeed = 10;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String nowDate = sdf.format(new Date());

            FlightInfoService service = FlightInfoService.retrofit.create(FlightInfoService.class);

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("acrftCd", flightPlanInfo.getAcrftCd() + "");          //항공기ID(CallSign)
            params.put("planId", flightPlanInfo.getPlanId() + "");           //비행계획서ID


            if (isTestDrive) {

                try {

                    int size = testDrivePointList.length;
                    String temp = "";

                    if (testDriveIndex < size) {
                        temp = testDrivePointList[testDriveIndex];
                    } else {
                        temp = testDrivePointList[size - 1];
                    }

                    if (temp.isEmpty()) {
                        params.put("lat", "0");            // 위도좌표
                        params.put("lon", "0");            // 경도좌표
                        params.put("elev", "0");           // 해발고도
                        params.put("speed", "10");          // 순항속도
                    } else {
                        String[] pos = temp.split(",");

                        params.put("lat", (float) Double.parseDouble(pos[1]) + "");            // 위도좌표
                        params.put("lon", (float) Double.parseDouble(pos[0]) + "");            // 경도좌표
                        params.put("elev", pos[2]+"");                 // 해발고도
                        params.put("gaeroInfo", Math.round(Float.parseFloat(pos[3]))+"");      // 자이로센서
                        params.put("speed", Math.round(Float.parseFloat(pos[4]))+"");          // 순항속도
                    }
                } catch (Exception ex) {

                }

            } else {
                try {
                    if (currentLocation != null) {
                        params.put("lat", currentLocation.getLatitude() + "");            // 위도좌표
                        params.put("lon", currentLocation.getLongitude() + "");            // 경도좌표
                        params.put("elev", currentLocation.getAltitude() + "");           // 해발고도
                    } else {
                        params.put("lat", "0");            // 위도좌표
                        params.put("lon", "0");            // 경도좌표
                        params.put("elev", "0");           // 해발고도
                    }

                    params.put("speed", currentSpeed + "");            //순항속도

                } catch (Exception ex) {

                }

            }


            params.put("heading", "100");       //해딩방향
            params.put("flgtlogGb", "GPS");     //ADSB 와 GPS 구분
            params.put("eegcmsgDate", nowDate);     //메시지 발생 시간
            params.put("eegcmsgType", type);        // 상황 타입

            RequestBody body = NetUtil.mapToJsonBody(NhsFlightActivity.this, params);
//PsmHkMessage
            Call<NetSecurityModel> callback = service.psmHkMessage(body);
            callback.enqueue(new Callback<NetSecurityModel>() {
                @Override
                public void onResponse(Call<NetSecurityModel> call, Response<NetSecurityModel> response) {
                    loading.dismiss();
                    NetSecurityModel netSecurityModel = response.body();


                    if (response.code() == 200) {

                        if (netSecurityModel.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {

                            String dec = new MagicSE_Util(getContext()).getDecData(netSecurityModel.getResult_data());
                            PsmHkMessage flightPlanModel = new Gson().fromJson(dec, PsmHkMessage.class);
                            new ToastUtile().showCenterText(mContext, flightPlanModel.getResult_msg());

                        }

                    }
                }

                @Override
                public void onFailure(Call<NetSecurityModel> call, Throwable t) {
                    loading.dismiss();
                    new ToastUtile().showCenterText(mContext, getString(R.string.error_network));
                }
            });

        }

    }

    @Override
    public void onClick(AirPoint curPos) {

        Log.d("clickMap", "CLICK");
        int visigle = this.rlMenu.getVisibility();

        if (visigle == View.GONE) {

            this.rlMenu.setVisibility(View.VISIBLE);

        } else {

            this.rlMenu.setVisibility(View.GONE);
            findViewById(R.id.ll_godo2).setVisibility(View.INVISIBLE);
            findViewById(R.id.ll_3d).setVisibility(View.INVISIBLE);
            findViewById(R.id.ll_north_up).setVisibility(View.INVISIBLE);

        }


    }

    /**
     * 메뉴 초기화
     *
     * @author FIESTA
     * @since 오후 5:02
     **/
    private void initMenu() {
        this.rlMenu.setVisibility(View.VISIBLE);
        findViewById(R.id.ll_godo2).setVisibility(View.INVISIBLE);
        findViewById(R.id.ll_3d).setVisibility(View.INVISIBLE);
        findViewById(R.id.ll_north_up).setVisibility(View.INVISIBLE);
    }

    private DialogType1 exitDialog = null;

    @Override
    public void onBackPressed() {

        // 주행중이면
        if (this.isDrive) {

            showExitDialog();

        } else {
            super.onBackPressed();
        }
    }

    /**
     * 종료 팝업 다이얼로그
     *
     * @author FIESTA
     * @since 오후 5:02
     **/
    private void showExitDialog() {
        exitDialog = new DialogType1(getContext(), "비행 종료 확인", "비행을 종료하시겠습니까?", getContext().getString(R.string.btn_confirm), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.hideDialog();
                // TODO: 2017. 4. 24. 수정 기능 추가

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

                try {

                    stopSenderTimer();
                    stopSaveTimer();

                    // Realm을 초기화합니다.
                    Realm.init(mContext);
                    Realm realm = Realm.getDefaultInstance();

                    NhsFlightHistoryModel nhsFlightHistoryModel = new NhsFlightHistoryModel();
                    nhsFlightHistoryModel.setAcrftCd(flightPlanInfo.getAcrftCd());
                    nhsFlightHistoryModel.setPlanDeparture(flightPlanInfo.getPlanDeparture());
                    nhsFlightHistoryModel.setPlanArrival(flightPlanInfo.getPlanArrival());
                    nhsFlightHistoryModel.setPlanTeet(flightPlanInfo.getPlanTeet());
                    nhsFlightHistoryModel.setPlanDate(flightPlanInfo.getPlanDate());
                    nhsFlightHistoryModel.setPlanRoute(flightPlanInfo.getPlanRoute());
                    nhsFlightHistoryModel.setPlanId(flightPlanInfo.getPlanId());
                    nhsFlightHistoryModel.setCallsign(flightPlanInfo.getCallsign());
                    nhsFlightHistoryModel.setAcrftType(flightPlanInfo.getAcrftType());
                    nhsFlightHistoryModel.setDepartureAerodrome(flightPlanInfo.getPlanDeparture());
                    nhsFlightHistoryModel.setRegDate(new Date().getTime());
                    nhsFlightHistoryModel.setPlanSn(flightPlanInfo.getPlanSn());
                    nhsFlightHistoryModel.setGpsLogDate(gpsLogDate);
                    nhsFlightHistoryModel.setFlightId(flightPlanInfo.getFlightId());
                    nhsFlightHistoryModel.setPlanDoccd(flightPlanInfo.getPlanDoccd());

                    // 총 비행 시간
                    long totalFlightTime = new Date().getTime() - objStartDate.getTime();


                    endTime = sdf.format(new Date());

                    int avgSpeed = 0;

                    // 평균 속도
                    if (totalSpeed > 0 && totalSpeedCount > 0) {
                        avgSpeed = (int) (totalSpeed / totalSpeedCount);
                    }

                    int avgAlitu = 0;

                    // 평균 고도
                    if (totalAltitude > 0 && totalAltitudeCount > 0) {
                        avgAlitu = (int) (totalAltitude / totalAltitudeCount);
                    }

                    nhsFlightHistoryModel.setAvgSpeed(avgSpeed);
                    nhsFlightHistoryModel.setAvgAltitude(avgAlitu);
                    nhsFlightHistoryModel.setTotalFlightTime(totalFlightTime);
                    nhsFlightHistoryModel.setStartTime(startTime);
                    nhsFlightHistoryModel.setEndTime(endTime);

                    if (routeStatus != null) {

                        // 총 거리
                        nhsFlightHistoryModel.setTotalDistanc(routeStatus.uTotalDist);

                    }

                    nhsFlightHistoryModel.realmSave(realm);

                } catch (Exception ex) {

                }

                FlightDriverService service = FlightDriverService.retrofit.create(FlightDriverService.class);

                // 유저 정보를 가져온다.
                String mbrId = StorageUtil.getStorageModeEx(NhsFlightActivity.this, LOGIN_MBR_ID);

                Map<String, Object> mainParams = new HashMap<String, Object>();
                Map<String, Object> subParams = new HashMap<String, Object>();

                try {
                    subParams.put("planId", flightPlanInfo.getPlanId());          // 비행계획서 ID
                    subParams.put("planSn", flightPlanInfo.getPlanSn());          // 비행계획서 일련번호
                    subParams.put("messageType", "ARR");     // 전문타입
                    subParams.put("acrftCd", flightPlanInfo.getAcrftCd());        // 항공기식별부호
                    subParams.put("planDeparture", flightPlanInfo.getPlanDeparture()); // 출발비행장

                    sdf = new SimpleDateFormat("HHmm");
                    subParams.put("planAta", sdf.format(new Date()));        // 도착시간

                    subParams.put("planArrival", flightPlanInfo.getPlanArrival());    // 비도착비행장
                    subParams.put("mbrId", mbrId);           // 회원일련번호
                    subParams.put("callsign", flightPlanInfo.getCallsign());       // 콜사인

                    mainParams.put("fpl", subParams);
                } catch (Exception ex) {

                }

                try {
                    mainParams.put("flgtlog", Util.readFileTextToJsonArray(Environment.getExternalStorageDirectory() + "/ACC_NAVI",
                            tempSaveFileName));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                RequestBody body = NetUtil.mapToJsonBody(NhsFlightActivity.this, mainParams);
//TpmArrivalModel
                Call<NetSecurityModel> callback = service.repoFpmArrival(body);
                callback.enqueue(new Callback<NetSecurityModel>() {
                    @Override
                    public void onResponse(Call<NetSecurityModel> call, Response<NetSecurityModel> response) {

                        try {
                            NetSecurityModel netSecurityModel = response.body();

                            if (response.code() == 200) {


                                if (netSecurityModel.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {

                                    String dec = new MagicSE_Util(getContext()).getDecData(netSecurityModel.getResult_data());

                                    TpmArrivalModel model = new Gson().fromJson(dec, TpmArrivalModel.class);

                                    if (model.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {

                                        new ToastUtile().showCenterText(mContext, model.getResult_msg());

                                        /**
                                         dialogSendReportFlight = new DialogSendReportFlight(getContext(), new View.OnClickListener() {
                                        @Override public void onClick(View view) {

                                        dialogSendReportFlight.dismiss();
                                        NhsFlightActivity.this.finish();
                                        }
                                        });
                                         dialogSendReportFlight.show();
                                         **/

                                    } else {

                                        new ToastUtile().showCenterText(mContext, model.getResult_msg());

                                    }

                                }

                            } else {
                                Log.d("JeLib", "----------1-----------");
//                                new ToastUtile().showCenterText(mContext, getString(R.string.error_network));

                            }
                        } catch (Exception ex) {

                        } finally {

                            if (exitDialog != null) {
                                exitDialog.hideDialog();

                            }

                            NhsFlightActivity.this.finish();
                        }

                    }

                    @Override
                    public void onFailure(Call<NetSecurityModel> call, Throwable t) {
//                            Toast.makeText(mContext, R.string.error_network, Toast.LENGTH_SHORT).show();
//            new ToastUtile().showCenterText(mContext, getString(R.string.error_network) );

                        if (exitDialog.isShowDialog()) {
                            exitDialog.hideDialog();
                        }
                    }
                });

//                exitDialog.hideDialog();

                // test
//                finish();

            }
        }, getContext().getString(R.string.btn_cancel), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                exitDialog.hideDialog();
            }
        });
    }

    @Override
    public void setStart(AirPoint curPos) {

    }

    @Override
    public void setGoal(AirPoint curPos) {

    }

    @Override
    public void setWaypoint(AirPoint curPos) {

    }

    @Override
    public void OnSearch() {

    }

    @Override
    public void OnReset() {

    }

    /**
     * 자이로 방향을 변경한다
     *
     * @author FIESTA
     * @since 오후 5:02
     **/
    private void updateNavPin(float gyroX, int startOffset) {

        /**
         * 0 이 정중앙으로 기준으로한다.
         * gyroVlaue = 4000 : 정 중앙
         * gyroValue = 1000 : 왼쪽으로 완전히 누웠을때
         * gyroValue = 7000 : 오른쪽으로 완전히 누웠을떄
         *
         * gyroX를 위 값으로 바꿔야한다.
         * gyroX = 10 : 1000(gyroVlaue)
         * gyroX = -10 : 7000(gyroVlaue)
         * gyroX = 0 : 4000(gyroVlaue)
         */

        int gyroValue = Math.round(4000 - (gyroX * 300));
        Log.d("gyroValue", gyroValue + "");


//    int gyroValue = 0;

        if (llNavPin != null) {

            this.currentDegree = (int) Math.round(gyroValue / 33.33333) - 30;

            Animation ani = llNavPin.getAnimation();

            if (ani != null) {
                if (llNavPin.getAnimation().hasEnded()) {

                    RotateAnimation rotateAnim = new RotateAnimation((float) beforAngle, (float) this.currentDegree,
                            RotateAnimation.RELATIVE_TO_SELF, 1f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnim.setDuration(500);
                    rotateAnim.setStartOffset(startOffset);
                    rotateAnim.setFillAfter(true);
                    llNavPin.startAnimation(rotateAnim);
                    rotateAnim.start();

                    beforAngle = this.currentDegree;

                }
            } else {

                RotateAnimation rotateAnim = new RotateAnimation(0, (float) this.currentDegree,
                        RotateAnimation.RELATIVE_TO_SELF, 1f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                rotateAnim.setDuration(500);
                rotateAnim.setStartOffset(startOffset);
                rotateAnim.setFillAfter(true);
                llNavPin.startAnimation(rotateAnim);
                rotateAnim.start();

                beforAngle = this.currentDegree;

            }

        }

    }


    /**
     * 맵 설정 레이어에 맞게 설정한다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-18 오후 3:40
     **/
    private void setMapLayout() {

        Integer[] bitData = new Integer[32];
        int i = 0;

        //초기화
        for (i = 0; i < 32; i++) {
            bitData[i] = 1;
        }

        // NOTAM 정보 레이어
        bitData[26] = (StorageUtil.getStorageMode(getContext(), NOTAM_INFO_LAYER) ? 1 : 0);

        // 항공기 위치 표시 레이어
        bitData[25] = (StorageUtil.getStorageMode(getContext(), AIRCRAFT_POSITIONING_LAYER) ? 1 : 0);

        // 기상정보
        bitData[24] = (StorageUtil.getStorageMode(getContext(), WEATHER_INFORMATION) ? 1 : 0);
        ;

        // 공역_정보_레이어
        bitData[5] = (StorageUtil.getStorageMode(getContext(), AIRSPACE_INFORMATION_LAYER) ? 1 : 0);

        // 장애물_정보_레이어
        bitData[7] = (StorageUtil.getStorageMode(getContext(), OBSTACLE_INFORMATION_LAYER) ? 1 : 0);


        // 이착륙장_정보_레이어
        bitData[6] = (StorageUtil.getStorageMode(getContext(), LANDING_INFORMATION_LAYER) ? 1 : 0);


        // 고속도로_표시_레이어
        bitData[1] = (StorageUtil.getStorageMode(getContext(), EXPRESSWAY_DISPLAY_LAYER) ? 1 : 0);


        // 강 정보 레이어
        bitData[2] = (StorageUtil.getStorageMode(getContext(), RIVER_INFORMATION_LAYER) ? 1 : 0);

        // 산악_정보_레이어
        bitData[3] = (StorageUtil.getStorageMode(getContext(), MOUNTAIN_INFORMATION_LAYER) ? 1 : 0);

        // 지도 레이어는 항상 1 이다
        bitData[0] = 1;

        // 배열을 리스트로 변환
        List<Integer> list = Arrays.asList(bitData);

        // 리스트 뒤집어 주기
        Collections.reverse(list);

        // 리스트를 배열로 다시 변환
        bitData = list.toArray(new Integer[list.size()]);

        byte[] convertByte = Util.encodeToByteArray(bitData);
        int convertInt = Util.byteToint(convertByte);
        lanSetDisplayLayer(convertInt);

    }

    @Override
    public boolean onKeyDown(int keyboard, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {

            switch (keyboard) {
                // 줌인
                case KeyEvent.KEYCODE_F2:
                    lanZoomByPosition(1, 0, 0);
                    return false;
//                    showMessage("F2 Touch!");

                // 줌 아웃
                case KeyEvent.KEYCODE_F3:
                    lanZoomByPosition(0, 0, 0);
                    return false;
//                    showMessage("F3 Touch!");

                case KeyEvent.KEYCODE_F4:
//                    showMessage("F4 Touch!");
                    break;
                case KeyEvent.KEYCODE_F5:
//                    showMessage("F5 Touch!");
                    break;
                case KeyEvent.KEYCODE_F6:
//                    showMessage("F6 Touch!");
                    break;
                case KeyEvent.KEYCODE_F7:
//                    showMessage("F7 Touch!");
                    break;
                case KeyEvent.KEYCODE_F8:
//                    showMessage("F8 Touch!");
                    showHokey();
                    return false;
                case KeyEvent.KEYCODE_ALT_LEFT:
//                    showMessage("alt left Touch!");
                    break;
                case KeyEvent.KEYCODE_ALT_RIGHT:
//                    showMessage("alt right Touch!");
                    break;
                case KeyEvent.KEYCODE_SPACE:
//                    showMessage("space Touch!");
                    break;
                case KeyEvent.KEYCODE_ENTER:
//                    showMessage("enter Touch!");
                    break;
                case KeyEvent.KEYCODE_CTRL_LEFT:
//                    showMessage("ctrl left Touch!");
                    break;
                case KeyEvent.KEYCODE_CTRL_RIGHT:
//                    showMessage("ctrl right Touch!");
                    break;
            }
        }

        return super.onKeyDown(keyboard, event);

    }

    /**
     * 시스템 설정을 변경
     *
     * @author FIESTA
     * @since 오전 12:39
     **/
    private void setSystemParametersInfo() {
        NativeImplement nativeImplement = INativeImple.getInstance(getContext());

        AirSystemParametersInfo airSystemParametersInfo = new AirSystemParametersInfo();

        int pathExitBufferZone = Integer.parseInt(StorageUtil.getStorageModeEx(NhsFlightActivity.this, PATH_EXIT_BUFFER_ZONE, "100"));
        int preservationOfObstacles = Integer.parseInt(StorageUtil.getStorageModeEx(NhsFlightActivity.this, PRESERVATION_OF_OBSTACLES, "2"));
        int paceConservation = Integer.parseInt(StorageUtil.getStorageModeEx(NhsFlightActivity.this, SPACE_CONSERVATION, "2"));
        int flyingDistance = Integer.parseInt(StorageUtil.getStorageModeEx(NhsFlightActivity.this, FLYING_DISTANCE, "2"));
        int highestFlightAltitude = Integer.parseInt(StorageUtil.getStorageModeEx(NhsFlightActivity.this, HIGHEST_FLIGHT_ALTITUDE, "4500"));
        int lowestFlightAltitude = Integer.parseInt(StorageUtil.getStorageModeEx(NhsFlightActivity.this, LOWEST_FLIGHT_ALTITUDE, "500"));
        int setTheMapDownloadScope = Integer.parseInt(StorageUtil.getStorageModeEx(NhsFlightActivity.this, SET_THE_MAP_DOWNLOAD_SCOPE, "3"));
        boolean isDeviation = StorageUtil.getStorageModeWithDefaultValue(NhsFlightActivity.this, EXPLORING_PATH_EXITS, false);
        boolean isAlarm = StorageUtil.getStorageModeWithDefaultValue(NhsFlightActivity.this, ALERT_MESSAGE_SET, false);

        // 사용할 마스크 설정
        airSystemParametersInfo.setuParamMask(AirSystemParametersInfo.SYSP_BUFAIRCRAFT1 |
                AirSystemParametersInfo.SYSP_BUFOBSTACLE1 |
                AirSystemParametersInfo.SYSP_BUFAIRTERRAIN1 |
                AirSystemParametersInfo.SYSP_BUFAIRCRAFT1 |
                AirSystemParametersInfo.SYSP_BUFMINALTITUDE |
                AirSystemParametersInfo.SYSP_BUFMAPDOWNRANGE |
                AirSystemParametersInfo.SYSP_DEVIATION |
                AirSystemParametersInfo.SYSP_ALARM |
                AirSystemParametersInfo.SYSP_SHOWALTITUDE);

        airSystemParametersInfo.setuBufDeviation1(pathExitBufferZone);          // 경로 이탈 버퍼존
        airSystemParametersInfo.setuBufObstacle1(preservationOfObstacles);      // 장애물/공역/비행금지구역 버퍼존
        airSystemParametersInfo.setuBufAirCraft1(400);                          // 지형 버퍼존
        airSystemParametersInfo.setuBufAirCraft1(pathExitBufferZone);           // 인접항공기
        airSystemParametersInfo.setuBufMinAltitude(lowestFlightAltitude);       // 비행 안전 고도 (최저)
        airSystemParametersInfo.setuBufMaxAltitude(highestFlightAltitude);      // 비행 안전 고도 (최고)
        airSystemParametersInfo.setuBufMapDownloadRange(setTheMapDownloadScope);      // 맵 다운로드 범위
        airSystemParametersInfo.setbDeviation(isDeviation); //  경로이탈 재탐색 여부
        airSystemParametersInfo.setbAlarm(isAlarm);         //  경보 설정 여부
        airSystemParametersInfo.setbShowAltitude(true);    //   측면고도를 표출 여부 설정

        nativeImplement.lanSystemParametersInfo(airSystemParametersInfo);

    }
}