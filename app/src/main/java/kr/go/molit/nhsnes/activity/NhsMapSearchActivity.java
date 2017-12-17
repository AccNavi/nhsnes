package kr.go.molit.nhsnes.activity;

import static com.modim.lan.lanandroid.Constants.NAVI_MAP_VECTOR;
import static com.modim.lan.lanandroid.LanStorage.mNative;
import static com.modim.lan.lanandroid.NativeImplement.lanClearRoutePosition;
import static com.modim.lan.lanandroid.NativeImplement.lanSetDisplayLayer;
import static com.modim.lan.lanandroid.NativeImplement.lanSimulSpeedTrajectory;
import static com.modim.lan.lanandroid.NativeImplement.lanSimulStopTrajectory;
import static com.modim.lan.lanandroid.NativeImplement.lanZoomByPosition;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_END;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_ROUTE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_START;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.KEY_MODE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.MODE_SEARCH_IN_ROUTE_SEARCH;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.MODE_SIMULATION;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.SHOW_POPUP;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.AIRCRAFT_POSITIONING_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.AIRSPACE_INFORMATION_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.EXPRESSWAY_DISPLAY_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.IS_LAYOUT_NW;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.KML_DATA;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.LANDING_INFORMATION_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.MOUNTAIN_INFORMATION_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.NOTAM_INFO_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.OBSTACLE_INFORMATION_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.RIVER_INFORMATION_LAYER;
import static kr.go.molit.nhsnes.fragment.SystemSettingFragment.IS_TTS_SOUND;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.modim.lan.lanandroid.AirPoint;
import com.modim.lan.lanandroid.AlarmType;
import com.modim.lan.lanandroid.Constants;
import com.modim.lan.lanandroid.GuideType;
import com.modim.lan.lanandroid.LanStorage;
import com.modim.lan.lanandroid.NhsLanView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.adapter.RecyclerFavoriteAdapter;
import kr.go.molit.nhsnes.common.MagicSE_Util;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.dialog.DialogAddFavorites;
import kr.go.molit.nhsnes.dialog.DialogComplateSelectMap;
import kr.go.molit.nhsnes.dialog.DialogConfirmSelectMap;
import kr.go.molit.nhsnes.dialog.DialogModifyRoute;
import kr.go.molit.nhsnes.dialog.DialogType1;
import kr.go.molit.nhsnes.dialog.DialogType2;
import kr.go.molit.nhsnes.dialog.LoadingDialog;
import kr.go.molit.nhsnes.interfaces.OnClickOptionMapMenu;
import kr.go.molit.nhsnes.interfaces.OnGpsListener;
import kr.go.molit.nhsnes.interfaces.OnLongClickOptionMapMenu;
import kr.go.molit.nhsnes.model.FlightRouteModel;
import kr.go.molit.nhsnes.model.NhsFavoriteModel;
import kr.go.molit.nhsnes.model.NhsMapModel;
import kr.go.molit.nhsnes.model.NhsSearchWayPointModel;
import kr.go.molit.nhsnes.net.model.FlightPlanModel;
import kr.go.molit.nhsnes.net.model.NetSecurityModel;
import kr.go.molit.nhsnes.net.realm.FlightPlanInfo;
import kr.go.molit.nhsnes.net.service.FlightPlanService;
import kr.go.molit.nhsnes.net.service.NetConst;
import kr.go.molit.nhsnes.net.service.NetUtil;
import kr.go.molit.nhsnes.widget.ActionBarEx;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 화면 검색
 *
 * @author FIESTA
 * @since 오전 1:39
 **/
public class NhsMapSearchActivity extends NhsBaseFragmentActivity implements View.OnClickListener, OnClickOptionMapMenu, OnLongClickOptionMapMenu {

    private final static int TTS_TIMER_INTERVAL = 1000;             // TTS 알람 체크 주기
    public final static int ROUTE_SEARCH_STEP_START = 0;           // 경로 탐색 출발지
    public final static int ROUTE_SEARCH_STEP_END = 1;             // 경로 탐색 도착지
    public final static int ROUTE_SEARCH_STEP_ROUTE = 2;           // 경로 탐색 경유지 추가
    public final static int LOG_DRIVE = 33;                          // 로그 주행

    public final static int POPUP_COMPLATE = 1;                     // 결로 검색 중 경로 확정 다이얼로그

    private Context mContext;

    private int mode = NhsSelectPointActivity.MODE_DEPARTURE;              // 출발/도착 플래그
    private NhsLanView mNlvView = null;
    private DialogConfirmSelectMap mDialogConfirmSelectMap = null;      // 경로 확정 다이얼로그
    private DialogModifyRoute dialogModifyRoute = null;                 // 경로 수정 다이얼로그
    private DialogComplateSelectMap dialogComplateSelectMap = null;    // 결로 검색 중 경로 확정 다이얼로그
    private DialogAddFavorites dialogFavorites;                          // 즐겨찾기 다이얼로그
    private DialogType1 messageDialog = null;

    private ActionBarEx mAbeTitle;     // Title

    private View vController = null;                                     // 하단 컨트롤러 (모의비행)
    private View vController2 = null;                                     // 하단 컨트롤러 (즐찾,모의비행,닫기)
    private TextView mFavorite;                                          // 즐겨찾기 버튼튼

    private String startData = "";                    // 이미 선택된 지정된 출발 좌표
    private String endData = "";                      // 이미 선택된 지정된 도착 좌표
    private String routeData = "";                   // 이미 선택된 지정된 경유지 좌표

    private int routeSearchStep = 0;                // 경로 탐색은 몇단계로 나누어서서 나오므로, 그러한 현재 스탭을 기록한다.
    private int routeWaypointCnt = 0;              // 경로 탐색 - 경유지 추가한 횟수

    private int nextPopup = 0;

    private int mType = 0;
    public static final String MAP_TYPE = "map_type";
    public static final int TYPE_FAVORITE = 1;              // 즐겨찾기
    public static final int TYPE_SEARCH_WAYPOINT = 2;       // 좌표검색
    public static final int TYPE_SEARCH_RECENT = 3;         // 최근검색

    private Dialog mDialog;     // 기본 다이얼로그

    private ImageView mIvPlayState;

    private Realm mRealm;

    // 즐겨찾기
    private RecyclerView mRecyclerViewFavorites;
    private RecyclerFavoriteAdapter mRecyclerFavorites;
    private FrameLayout mFlFavorites;

    private NhsMapModel mSelectModel;

    private TimerTask ttsTimerTask = null;   // tts 알람
    private Timer ttsTimer = null;           // tts 알람
    private TextToSpeech mTts = null;
    private DialogType1 complateDialog = null;
    private DialogType1 currentGps = null;
    private boolean showFavoriteButton = false; // 팝업창에 즐겨찾기 추가 버튼 활성화 여부
    private boolean isAppedMapPin = false;            // 맵 경로 선택했었는지에 대한 여부
    private ProgressDialog progressDialog;

    private FlightPlanInfo flightPlanInfo = null;
    private String flightId = "";
    private ArrayList<FlightRouteModel> route;  // 경로
    private boolean isSimulStop = false;

    private View v_overlay  =   null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_map_search);

        mContext = this;
        // Realm을 초기화합니다.
        Realm.init(mContext);
        mRealm = Realm.getDefaultInstance();


        Bundle data = getIntent().getExtras();

        if (data != null) {

            this.flightPlanInfo = (FlightPlanInfo) data.getSerializable("data");

            if (this.flightPlanInfo != null) {
                this.flightId = flightPlanInfo.getFlightId();
            }

        }

        // wait 프로그래스바를 보여준다.
        showProgress();

        getMode();

        //setupTTS();

        setLayout();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (mNlvView != null) {

                    // 맵 타입에 맞게 변경한다.
                    String type = StorageUtil.getStorageModeEx(mContext, MAP_TYPE, String.valueOf(NAVI_MAP_VECTOR));
                    mNlvView.setMapKind(Integer.parseInt(type));

                    mNlvView.setOnClickOptionMapMenu(NhsMapSearchActivity.this);
                    mNlvView.setOnLongClickOptionMapMenu(NhsMapSearchActivity.this);
                    mNlvView.showPopup = true;

                    // 맵 설정 레이어에 맞게 설정한다.
                    setMapLayout();

                    Log.d("JeLib", "-------------0----");
                    if (nextPopup == POPUP_COMPLATE) {
                        Log.d("JeLib", "------------1-----");
                        try {

                            dialogComplateSelectMap = new DialogComplateSelectMap(getContext(),
                                    NhsSelectPointActivity.MODE_ROUTE_SEARCH,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            switch (view.getId()) {

                                                // 즐겨 찾기 추가
                                                case R.id.btn_add:

                                                    onDialogAddFavorites();
//                                  dialogComplateSelectMap.dismiss();
                                                    break;

                                                // 모의 주행
                                                case R.id.btn_flight:
                                                    /*
                                                    stopTtsTimer();

                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            startTtsTimer();
                                                        }
                                                    }, 1000);
                                                    */
                                                    vController.setVisibility(View.VISIBLE);
                                                    dialogComplateSelectMap.dismiss();
                                                    break;

                                                case R.id.btn_cancel:
                                                    dialogComplateSelectMap.dismiss();
                                                    break;

                                            }


                                        }
                                    });


                        } catch (Exception ex) {

                        }

                    }
                    /*
                    if(mode == NhsSelectPointActivity.MODE_HISTORY_MAP){
                        String log = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LANMap/GPSLog/TRK_2017_08_03_14_31_20.trk";
                        Log.d("JeLib","log:"+log);
                        mNlvView.startLogTrajectory(log);
                    } else */
                    if (mode == LOG_DRIVE) {

                        String logName = reloadMap();
//                        test();
                        startLogDrive(logName);
                        {
//                            vController.setVisibility(View.VISIBLE);
//                            vController2.setVisibility(View.GONE);
//                        stopTtsTimer();
//                        startTtsTimer();
                        }

                    } else {
                        reloadMap();
                    }

                    // test
//                    testZoom();

                    // 프로그래스바를 종료한다.
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismissProgress();
                        }
                    },3000);

                }
            }
        }, 1000);
    }


    private void test() {

        ArrayList<NhsFavoriteModel> list = NhsFavoriteModel.findAll();

        if (list != null) {

            if (list.size() > 0) {


                vController2.setVisibility(View.VISIBLE);
                vController.setVisibility(View.GONE);
                vController2.findViewById(R.id.tv_flight).setOnClickListener(this);
                vController2.findViewById(R.id.tv_controll_close2).setOnClickListener(this);
                findViewById(R.id.tv_controll_close).setOnClickListener(mController2ClickListener);
                mIvPlayState = (ImageView) vController.findViewById(R.id.iv_play_state);
                mIvPlayState.setOnClickListener(mController2ClickListener);

                NhsFavoriteModel nhsFavoriteModel = list.get(0);

//                mSelectModel = nhsFavoriteModel;
//                mFlFavorites.setVisibility(View.GONE);

                startData = nhsFavoriteModel.getStartData();
                endData = nhsFavoriteModel.getEndData();
                routeData = nhsFavoriteModel.getRouteData();

                Log.i("TEST", "onClick:::" + nhsFavoriteModel.getName());
                Log.i("JeLib", "startData:::" + startData);
                Log.i("JeLib", "endData:::" + endData);
                Log.i("JeLib", "routeData:::" + routeData);

                reloadMap();

            }

        }

    }

    /**
     * 프로그래스바를 보여준다.
     *
     * @author FIESTA
     * @since 오전 3:50
     **/
    private View overView = null;
    private void showProgress() {

        try {
            /*
            this.progressDialog = new ProgressDialog(NhsMapSearchActivity.this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            this.progressDialog.setTitle("");
            this.progressDialog.setMessage(NhsMapSearchActivity.this.getString(R.string.wait_message));
            this.progressDialog.setIndeterminate(true);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
            */
            if(v_overlay == null) {
                v_overlay = findViewById(R.id.v_overlay);
                v_overlay.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.d("JeLib","방지");
                        return true;
                    }
                });
            }
        } catch (Exception e){

        }
    }

    /**
     * 프로그래스바를 종료한다.
     *
     * @author FIESTA
     * @since 오전 3:50
     **/
    private void dismissProgress() {
        try {
            /*
            if (this.progressDialog != null) {
                this.progressDialog.dismiss();
            }*/
            if(v_overlay != null) {
                v_overlay.setVisibility(View.GONE);
                v_overlay.setOnTouchListener(null);
                v_overlay = null;
            }
        } catch (Exception e){

        }
    }

    /**
     * 로그 주행 시작
     *
     * @author FIESTA
     * @since 오전 3:50
     **/
    private void startLogDrive(String fileName) {
        String log = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LANMap/GPSLog/" + fileName;
        this.mNlvView.startLogTrajectory(log);


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
        }
    }

    private void startTtsTimer() {

        this.ttsTimer = new Timer();
        this.ttsTimerTask = new TimerTask() {
            @Override
            public void run() {

                int uType = LanStorage.mNative.lanNaviEventType();
                int uValue = 0;
                switch (uType) {
                    case Constants.NAVI_EVT_ALARM: {
                        AlarmType alarm = new AlarmType();
                        LanStorage.mNative.lanNaviEvent(alarm);
                        uValue = alarm.uAlarmType;

                        switch (uValue) {

                            case 0:
                                playTTS("경로 이탈 경보");
                                break;
                            case 1:
                                playTTS("장애물 경보");
                                break;
                            case 2:
                                playTTS("비행 안전 최소 고도 경보");
                                break;
                            case 3:
                                playTTS("비행 안전 최대 고도 경보");
                                break;
                            case 4:
                                playTTS("승인 지역 이탈 경보");
                                break;
                            case 5:
                                break;


                        }

                        break;


                    }

                    case Constants.NAVI_EVT_GUIDE: {
                        final GuideType guide = new GuideType();
                        LanStorage.mNative.lanNaviEvent(guide);
                        String strGuide = guide.szGuideText;
                        playTTS(strGuide);

                        break;
                    }

                }

            }
        };

        this.ttsTimer.schedule(this.ttsTimerTask, 500, TTS_TIMER_INTERVAL);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {

//            stopTtsTimer();

//            if (mode == LOG_DRIVE) {
//
//                mNlvView.stopLogTrajectory();   // 로그 비행을 중지한다.
//
//            } else {
//
//                mNative.lanSimulPauseTrajectory();  // 일시정지 시킴
//                mNative.lanSimulStopTrajectory();   // 중지시킨다.
//
//            }
            this.mNlvView.clearRoutePosition();



        } catch (Exception e) {

        }

    }


    /**
     * 레이아웃 설정
     *
     * @author FIESTA
     * @since 오전 1:47
     **/
    private void setLayout() {

        mAbeTitle = (ActionBarEx) findViewById(R.id.abe_title);
        mAbeTitle.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    isSimulStop = false;

                    if (mode == LOG_DRIVE) {
                        if (!mNative.lanLogIsPause()) {  // 로그 주행 중이면
                            mNative.lanLogPauseTrajectory(); // 정지
                        }
                    }

                    if (mNative.lanSimulIsPause() != 1) {
                        mNative.lanSimulPauseTrajectory();  // 일시정지 시킴
                        isSimulStop = true;
                        mIvPlayState.setImageResource(R.drawable.btn_play_nor);
                    }

                    messageDialog = new DialogType1(NhsMapSearchActivity.this, "", "종료하시겠습니까?", getString(R.string.btn_confirm), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (mode == LOG_DRIVE) {
                                mNative.lanLogStopTrajectory(); // 로그 주행 정지
                            } else {
                                if (isSimulStop) {
                                    mNative.lanSimulStopTrajectory(); //   시뮬레이션 정지
                                }
                            }

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 500);

                        }
                    }, getString(R.string.btn_cancel), new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            // 재기동
                            if (mode == LOG_DRIVE) {
                                mNative.lanLogResumeTrajectory();
                            } else {
                                mNative.lanSimulResumeTrajectory();
                            }

                            messageDialog.hideDialog();
                        }
                    });
                }catch(Exception e){

                }
            }
        });

        // 기본 탭 초기화
        vController2 = findViewById(R.id.ll_controller2);
        vController = findViewById(R.id.ll_controller);

        mFavorite = (TextView) vController2.findViewById(R.id.tv_add_favorites);
        mIvPlayState = (ImageView) vController.findViewById(R.id.iv_play_state);

        findViewById(R.id.ll_speed_low).setOnClickListener(mController2ClickListener);
        findViewById(R.id.ll_speed_high).setOnClickListener(mController2ClickListener);

        if (mode == MODE_SEARCH_IN_ROUTE_SEARCH || mode == MODE_SIMULATION) {
            vController2.setVisibility(View.VISIBLE);
            vController.setVisibility(View.GONE);

            mFavorite.setOnClickListener(this);
            vController2.findViewById(R.id.tv_flight).setOnClickListener(this);
            vController2.findViewById(R.id.tv_controll_close2).setOnClickListener(this);

            // 모의비행 탭 초기화
            this.vController = findViewById(R.id.ll_controller);
            mIvPlayState.setOnClickListener(mController2ClickListener);
            vController.findViewById(R.id.tv_speed).setOnClickListener(mController2ClickListener);
            findViewById(R.id.tv_controll_close).setOnClickListener(mController2ClickListener);


        } else {

        }

        this.mNlvView = (NhsLanView) findViewById(R.id.nlv_view);
        this.mNlvView.showPopup = false;

        initTypeView();
    }


    /**
     * 출발지인지, 도착지인지 구분하는 플래그를 받는다
     *
     * @author FIESTA
     * @since 오전 12:44
     **/
    private void getMode() {

        Bundle data = getIntent().getExtras();

        if (data != null) {
            this.mode = data.getInt(KEY_MODE, NhsSelectPointActivity.MODE_DEPARTURE);
            this.startData = data.getString(DATA_START, "");
            this.endData = data.getString(DATA_END, "");
            this.routeData = data.getString(DATA_ROUTE, "");
            this.nextPopup = data.getInt(SHOW_POPUP, 0);
            this.mType = data.getInt(MAP_TYPE, 0);

            Log.i("TEST", "mode:::" + mode);
        }
    }


    @Override
    public void onClick(AirPoint curPos) {

        Log.d("clicklistener", "CLICK!");
        if(vController.getVisibility() == View.VISIBLE)return;
        switch (this.mode) {

            // 경로탐색일 경우
            case NhsSelectPointActivity.MODE_ROUTE_SEARCH:

                // 즐겨찾기는 표시하지 않는다.
                if (mType != TYPE_FAVORITE && mType != TYPE_SEARCH_RECENT) {

                    switch (this.routeSearchStep) {

                        case ROUTE_SEARCH_STEP_START:
                            setStart(curPos);
                            break;

                        case ROUTE_SEARCH_STEP_END:
                            setGoal(curPos);
                            break;

                        case ROUTE_SEARCH_STEP_ROUTE:
                            setWaypoint(curPos);
                            break;

                    }

                }
                break;

            case NhsSelectPointActivity.MODE_DEPARTURE:
                setStart(curPos);
                break;

            case NhsSelectPointActivity.MODE_ARRIVAL:
                setGoal(curPos);
                break;

            case NhsSelectPointActivity.MODE_ROUTE:
                setWaypoint(curPos);
                break;

        }

    }

    /**
     * 시작지 클릭시
     *
     * @author FIESTA
     * @since 오후 8:00
     **/
    @Override
    public void setStart(final AirPoint curPos) {

        Log.i("TEST", "setStart:::" + curPos.x + ", " + curPos.y);
        this.mDialogConfirmSelectMap = new DialogConfirmSelectMap(getContext(),
                NhsSelectPointActivity.MODE_DEPARTURE,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        switch (view.getId()) {

                            case R.id.btn_complate:
                                try {
                                    int result = mNlvView.setRoutePosition(NhsMapSearchActivity.this, Constants.NAVI_SETPOSITION_START, curPos.x, curPos.y, "start name", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    });

                                    if (result == 0) {
                                        startData = curPos.x + " " + curPos.y;

                                        if (!(mode == NhsSelectPointActivity.MODE_ROUTE_SEARCH)) {


                                            resultData(true);

                                        } else {

                                            routeSearchStep += 1;
                                            isAppedMapPin = true;

                                        }
                                    }
                                } catch(Exception e){

                                }
                                break;

                            case R.id.btn_cancel:
                                break;

                            // 즐겨찾기 추가
                            case R.id.btn_add_favorite:
                                try {
                                    dialogFavorites = new DialogAddFavorites(mContext,
                                            new DialogAddFavorites.IFavorite() {

                                                @Override
                                                public void onSave(final String name) {

                                                    mRealm.beginTransaction();
                                                    NhsFavoriteModel favorite = mRealm.createObject(NhsFavoriteModel.class, NhsFavoriteModel.findAll().size() + 1);
                                                    favorite.setName(name);
                                                    favorite.setStartData(curPos.x + " " + curPos.y);
                                                    mRealm.commitTransaction();

                                                    Toast.makeText(mContext, R.string.save_favorite, Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onCancel() {

                                                }
                                            });
                                    dialogFavorites.setStart(curPos.x + " " + curPos.y);
                                    dialogFavorites.setSingleMode("출발지");
                                    dialogFavorites.show();
                                }catch(Exception e){

                                }
                                break;

                        }

                        mDialogConfirmSelectMap.dismiss();

                    }
                });

        if (this.showFavoriteButton) {
            this.mDialogConfirmSelectMap.visibleFavoriteButton();
        }
        this.mDialogConfirmSelectMap.show();

    }

    /**
     * 도착지 클릭시
     *
     * @author FIESTA
     * @since 오후 8:00
     **/
    @Override
    public void setGoal(final AirPoint curPos) {

        Log.i("TEST", "setGoal:::" + curPos.x + ", " + curPos.y);
        this.mDialogConfirmSelectMap = new DialogConfirmSelectMap(getContext(),
                NhsSelectPointActivity.MODE_ARRIVAL,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        switch (view.getId()) {

                            case R.id.btn_complate:
                                try {
                                    int result = mNlvView.setRoutePosition(NhsMapSearchActivity.this, Constants.NAVI_SETPOSITION_GOAL, curPos.x, curPos.y, "goal name", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    });

                                    if (result == 0) {
                                        endData = curPos.x + " " + curPos.y;

                                        if (!(mode == NhsSelectPointActivity.MODE_ROUTE_SEARCH)) {
                                            resultData(true);
                                        } else {
                                            //mNative.lanSetRoutePosition(Constants.NAVI_SETPOSITION_GOAL, curPos.x, curPos.y);
//                                    mNative.lanExecuteRP();
                                            routeSearchStep += 1;
                                            isAppedMapPin = true;
                                        }
                                    }
                                } catch(Exception e){

                                }
                                break;

                            case R.id.btn_cancel:
//                mNative.lanClearRoutePosition();
                                break;

                            // 즐겨찾기 추가
                            case R.id.btn_add_favorite:
                                try {
                                    dialogFavorites = new DialogAddFavorites(mContext,
                                            new DialogAddFavorites.IFavorite() {

                                                @Override
                                                public void onSave(final String name) {

                                                    mRealm.beginTransaction();
                                                    NhsFavoriteModel favorite = mRealm.createObject(NhsFavoriteModel.class, NhsFavoriteModel.findAll().size() + 1);
                                                    favorite.setName(name);
                                                    favorite.setEndData(curPos.x + " " + curPos.y);
                                                    mRealm.commitTransaction();

                                                    Toast.makeText(mContext, R.string.save_favorite, Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onCancel() {

                                                }
                                            });
                                    dialogFavorites.setEnd(curPos.x + " " + curPos.y);
                                    dialogFavorites.setSingleMode("도착지");
                                    dialogFavorites.show();
                                } catch(Exception e){

                                }
                                break;

                        }

                        mDialogConfirmSelectMap.dismiss();

                    }
                });

        if (this.showFavoriteButton) {
            this.mDialogConfirmSelectMap.visibleFavoriteButton();
        }

        this.mDialogConfirmSelectMap.show();

    }

    /**
     * 경유지
     * waypoint 클릭시
     *
     * @author FIESTA
     * @since 오후 8:00
     **/
    @Override
    public void setWaypoint(final AirPoint curPos) {

        Log.i("TEST", "way:::" + curPos.x + ", " + curPos.y);

        this.mDialogConfirmSelectMap = new DialogConfirmSelectMap(getContext(),
                NhsSelectPointActivity.MODE_ROUTE,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        switch (view.getId()) {

                            // 경유지 추가
                            case R.id.btn_complate:
                                try {
                                    int result = mNlvView.setRoutePosition(NhsMapSearchActivity.this, Constants.NAVI_SETPOSITION_WAYPOINT, curPos.x, curPos.y, "watpoint name", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    });

                                    if (result == 0) {
                                        if (routeData.isEmpty()) {
                                            routeData = curPos.x + " " + curPos.y + "\n";
                                        } else {
                                            routeData += (curPos.x + " " + curPos.y + "\n");
                                        }
                                    }
                                } catch(Exception e){

                                }
                                break;

                            case R.id.btn_cancel:
                                Log.d("JeLib","--------------------------");
                                Log.d("JeLib","--------------------------");
                                Log.d("JeLib","--------경로확정-------");
                                Log.d("JeLib","--------------------------");
                                Log.d("JeLib","--------------------------");
                                try {
                                    // 경로 확정
                                    int resultCode = mNlvView.executeRP(NhsMapSearchActivity.this, 0, 0, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            switch (view.getId()) {

                                                case R.id.alert_btn_ok:
                                                    //mNative.lanExecuteRP();
                                                    routeWaypointCnt += 1;
                                                    isAppedMapPin = true;

//                                                mNlvView.executeRPDirect(0, 0);

                                                    // 경로 확정 된 내용 최근검색 되도록 추가
                                                    mRealm.beginTransaction();
                                                    NhsSearchWayPointModel searchWayPoint = mRealm.createObject(NhsSearchWayPointModel.class, NhsSearchWayPointModel.findAll().size() + 1);
                                                    searchWayPoint.setName("" + new SimpleDateFormat("MM/dd HH:mm:ss").format(System.currentTimeMillis()));
                                                    searchWayPoint.setStartData(startData);
                                                    searchWayPoint.setEndData(endData);
                                                    searchWayPoint.setRouteData(routeData);
                                                    mRealm.commitTransaction();

                                                    if (!(mode == NhsSelectPointActivity.MODE_ROUTE_SEARCH)) {
                                                        resultData(true);
                                                    }

                                                    break;

                                                case R.id.alert_btn_cancel:
                                                    routeWaypointCnt = 0;
                                                    routeSearchStep = 0;
                                                    mNlvView.clearRoutePosition();
                                                    if (!(mode == NhsSelectPointActivity.MODE_ROUTE_SEARCH)) {
                                                        resultData(false);
                                                    }
                                                    break;


                                            }
                                        }
                                    });


                                    if (resultCode == 0 && !(mode == NhsSelectPointActivity.MODE_ROUTE_SEARCH)) {

                                        resultData(true);

                                    } else if (resultCode != 0 && !(mode == NhsSelectPointActivity.MODE_ROUTE_SEARCH)) {


                                    } else {

                                        if (0 == resultCode) {
                                            //mNative.lanExecuteRP();
                                            routeWaypointCnt += 1;
                                            isAppedMapPin = true;

                                            // 경로 확정 된 내용 최근검색 되도록 추가
                                            mRealm.beginTransaction();
                                            NhsSearchWayPointModel searchWayPoint = mRealm.createObject(NhsSearchWayPointModel.class, NhsSearchWayPointModel.findAll().size() + 1);
                                            searchWayPoint.setName("" + new SimpleDateFormat("MM/dd HH:mm:ss").format(System.currentTimeMillis()));
                                            searchWayPoint.setStartData(startData);
                                            searchWayPoint.setEndData(endData);
                                            searchWayPoint.setRouteData(routeData);
                                            mRealm.commitTransaction();

                                            if (!(mode == NhsSelectPointActivity.MODE_ROUTE_SEARCH)) {
                                                resultData(true);
                                            }
                                        } else if (resultCode != -1) {
                                            rpErrorMsg(resultCode);

                                            // 위치 클리어
                                            lanClearRoutePosition();

                                            routeWaypointCnt = 0;
                                            routeSearchStep = 0;

                                        }

                                    }
                                } catch(Exception e){

                                }
                                break;

                            // 즐겨찾기 추가
                            case R.id.btn_add_favorite:
                                try {
                                    dialogFavorites = new DialogAddFavorites(mContext,
                                            new DialogAddFavorites.IFavorite() {

                                                @Override
                                                public void onSave(final String name) {

                                                    mRealm.beginTransaction();
                                                    NhsFavoriteModel favorite = mRealm.createObject(NhsFavoriteModel.class, NhsFavoriteModel.findAll().size() + 1);
                                                    favorite.setName(name);
                                                    favorite.setRouteData(curPos.x + " " + curPos.y);
                                                    mRealm.commitTransaction();

                                                    Toast.makeText(mContext, R.string.save_favorite, Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onCancel() {

                                                }
                                            });
                                    dialogFavorites.setRoute(curPos.x + " " + curPos.y);
                                    dialogFavorites.setSingleMode("경유지");
                                    dialogFavorites.show();

                                } catch(Exception e){

                                }
                                break;

                        }

                        mDialogConfirmSelectMap.dismiss();
                    }
                });


        if (this.showFavoriteButton) {
            this.mDialogConfirmSelectMap.visibleFavoriteButton();
        }

        this.mDialogConfirmSelectMap.setAddWayPointCnt(this.routeWaypointCnt);
        this.mDialogConfirmSelectMap.show();

    }

    /**
     * search  클릭시
     *
     * @author FIESTA
     * @since 오후 8:00
     **/
    @Override
    public void OnSearch() {

    }

    /**
     * reset 클릭시
     *
     * @author FIESTA
     * @since 오후 8:02
     **/
    @Override
    public void OnReset() {

        this.startData = "";
        this.endData = "";
        this.routeData = "";

    }

    /**
     * 롱클릭시
     *
     * @author FIESTA
     * @since 오전 6:48
     **/
    @Override
    public void onLongClick(AirPoint curPos) {

        Log.d("clicklistener", "LONG CLICK!");

        if (this.routeSearchStep == ROUTE_SEARCH_STEP_ROUTE) {

            if (this.routeWaypointCnt > 0) {

                this.dialogModifyRoute = new DialogModifyRoute(getContext(),
                        NhsSelectPointActivity.MODE_ROUTE_SEARCH,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                switch (view.getId()) {

                                    case R.id.btn_complate:

                                        try {
                                            reloadMap();

                                            dialogComplateSelectMap = new DialogComplateSelectMap(getContext(),
                                                    NhsSelectPointActivity.MODE_ROUTE_SEARCH,
                                                    new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {

                                                            switch (view.getId()) {

                                                                // 즐겨 찾기 추가
                                                                case R.id.btn_add:
                                                                    onDialogAddFavorites();
//                                  dialogComplateSelectMap.dismiss();
                                                                    break;

                                                                // 모의 주행
                                                                case R.id.btn_flight:
                                                                    setSimulateDriving();
                                                                    dialogComplateSelectMap.dismiss();
                                                                    break;

                                                                case R.id.btn_cancel:
                                                                    dialogComplateSelectMap.dismiss();
                                                                    break;

                                                            }
                                                        }
                                                    });

                                        } catch (Exception ex) {

                                        }

                                        List<String> routes = (List<String>) view.getTag();
                                        int i = 0;
                                        int size = routes.size();
                                        routeData = "";

                                        for (i = 0; i < size; i++) {
                                            routeData += (routes.get(i) + "\n");
                                        }

                                        dialogComplateSelectMap.setStart(startData);
                                        dialogComplateSelectMap.setEnd(endData);
                                        dialogComplateSelectMap.setRoute(routeData);
                                        dialogComplateSelectMap.show();

                                        break;

                                    case R.id.btn_cancel:
                                        dialogModifyRoute.dismiss();
                                        reloadMap();
//                    mNative.lanClearRoutePosition();
                                        break;

                                }

                                dialogModifyRoute.dismiss();

                            }
                        });

                this.dialogModifyRoute.setRoutes(this.routeData);
                this.dialogModifyRoute.setRoutesListener(new DialogModifyRoute.IRouteResultListener() {
                    @Override
                    public void onResult(String routes) {
                        routeData = routes;
                    }
                });
                this.dialogModifyRoute.show();
            }
        }

    }


    /**
     * 경로를 지우고 다시 로드한다.
     *
     * @author FIESTA
     * @since 오전 11:24
     **/
    private String reloadMap() {
                /*
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {


                            }
                        });

                    }
                });
                 */

        if (this.flightPlanInfo != null) {

            // TRK_2017_12_13_16_45_09.trk
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

            Date date = new Date();
            date.setTime(flightPlanInfo.getGpsLogDate());

            String logName = "TRK_" + sdf.format(date) + ".trk";

            callFlightPlanDetail(this.flightPlanInfo.getPlanId(), this.flightPlanInfo.getPlanSn());

            return logName;

        } else {

            // 경로를 지우고 새로 그린다
            mNlvView.clearRoutePosition();
            routeSearchStep = 0;
            routeWaypointCnt = 0;

            // test
//            startData = "128.703004 35.894373";
//            endData = "128.746608 35.504016";
//            routeData = "128.344745 36.118153";


            // 시자 위치 지정
            try {
                if (!this.startData.isEmpty()) {
                    String[] spritStart = startData.split(" ");
                    double x = Double.parseDouble(spritStart[0]);
                    double y = Double.parseDouble(spritStart[1]);

                    Log.i("JeLib", "1:::" + x + ", " + y);

                    int result = mNlvView.setRoutePosition(NhsMapSearchActivity.this, Constants.NAVI_SETPOSITION_START, x, y, "start name", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    //mNative.lanSetRoutePosition(Constants.NAVI_SETPOSITION_START, x, y);
                    if (result == 0) {
                        routeSearchStep++;
                    }

                }
            } catch (Exception ex) {

            } finally {

            }

            // 종료 위치 지정
            try {

                if (!this.endData.isEmpty()) {
                    String[] spritEnd = endData.split(" ");

                    double x = Double.parseDouble(spritEnd[0]);
                    double y = Double.parseDouble(spritEnd[1]);

                    Log.i("JeLib", "2:::" + x + ", " + y);
                    int result = mNlvView.setRoutePosition(NhsMapSearchActivity.this, Constants.NAVI_SETPOSITION_GOAL, x, y, "goal name", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

                    //mNative.lanSetRoutePosition(Constants.NAVI_SETPOSITION_GOAL, x, y);
                    if (result == 0) {
                        routeSearchStep++;
                    }
                }

            } catch (Exception ex) {

            } finally {

            }

            try {
                // 경유지 지정
                if (!routeData.isEmpty()) {
                    String[] spritStart = routeData.split("\\n");
                    String[] dataRoute;
                    int size = spritStart.length;
                    int i = 0;
                    for (i = 0; i < size; i++) {

                        dataRoute = spritStart[i].split(" ");

                        double x = Double.parseDouble(dataRoute[0]);
                        double y = Double.parseDouble(dataRoute[1]);

                        Log.i("JeLib", "3:::" + x + ", " + y);

                        int result = mNlvView.setRoutePosition(NhsMapSearchActivity.this, Constants.NAVI_SETPOSITION_WAYPOINT, x, y, "waypoint name", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                    }
                }

            } catch (Exception ex) {

            }

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
            if (2 <= routeSearchStep) {

                if (!(mode == NhsSelectPointActivity.MODE_ROUTE_SEARCH)) {
                    mNlvView.executeRPDirect(0, 0);
                } else {
                    mNlvView.executeRP(NhsMapSearchActivity.this, 0, 0, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (view.getId()) {

                                case R.id.alert_btn_ok:
                                    mNlvView.executeRPDirect(0, 0);
                                    break;

                                case R.id.alert_btn_cancel:
                                    mNlvView.clearRoutePosition();
                                    routeWaypointCnt = 0;
                                    routeSearchStep = 0;
                                    break;

                            }
                        }
                    });
                }
//                    rpErrorMsg(resultCode);
            }
//            }
//        }, 1500);
        }

        return "";
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

        RequestBody body = NetUtil.mapToJsonBody(NhsMapSearchActivity.this, params);

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
                            route = (ArrayList)flightPlanModel.getRoute();
                            flightPlanInfo.setFlightId(flightId);

                            // 비행을 시작한다.
                            setDrive();

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

    private void setDrive() {

        mNlvView.clearRoutePosition();


        String arrival = this.flightPlanInfo.getPlanArrival(); //"1269726409 374093284";
        String departure = this.flightPlanInfo.getPlanDeparture(); //"1269127807 374182090";
        final String route = this.flightPlanInfo.getPlanRoute(); //"1269240188 373384475";
        String totalEet = flightPlanInfo.getPlanTeet();

        // 총 소요시간을 보여준다.
        try {
            String convertTotalEet = String.format(getString(R.string.ete), totalEet);
            ((TextView) findViewById(R.id.tv_ete_eta)).setText(convertTotalEet);
        } catch (Exception ex) {


        }

        if (this.route != null) {

            int size = this.route.size();
            int i = 0;

            // 시작 위치
            int result = mNlvView.setRoutePosition(NhsMapSearchActivity.this, Constants.NAVI_SETPOSITION_START, Double.parseDouble(this.route.get(0).getLon()), Double.parseDouble(this.route.get(0).getLat()),
                    "start name", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

            if (result == 0) {
                // 종료 위치
                result = mNlvView.setRoutePosition(NhsMapSearchActivity.this, Constants.NAVI_SETPOSITION_GOAL, Double.parseDouble(this.route.get(this.route.size() - 1).getLon()), Double.parseDouble(this.route.get(this.route.size() - 1).getLat()),
                        "goal name", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });


                if (result == 0) {
                    // 경유지 등록
                    for (i = 1; i < size - 1; i++) {

                        if (result == 0) {
                            result = mNlvView.setRoutePosition(NhsMapSearchActivity.this, Constants.NAVI_SETPOSITION_WAYPOINT, Double.parseDouble(this.route.get(i).getLon()), Double.parseDouble(this.route.get(i).getLat()),
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

            result = mNlvView.executeRP(NhsMapSearchActivity.this, 0, 0, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {

                        case R.id.alert_btn_ok:
                            mNlvView.executeRPDirect(0, 0);


                            break;

                        case R.id.alert_btn_cancel:
                            NhsMapSearchActivity.this.route.clear();
                            mNlvView.clearRoutePosition();
                            break;

                    }
                }
            });

            if (result != -1) {


            }

        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            // 컨트롤러2 하단 즐겨찾기 추가
            case R.id.tv_add_favorites:

                // 경로 생성 안됬을 경우
//                if (mNative.lanIsRoute() == 0) {
//
//                    if (null != mDialog) {
//                        mDialog.dismiss();
//                    }
//
//                    DialogType2 dTpye2 = new DialogType2(mContext, getString(R.string.err_title), getString(R.string.err_message_6), getString(R.string.btn_confirm), new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mDialog.dismiss();
//                        }
//                    });
//
//                    mDialog = dTpye2.getDialog();
//
//                } else {

                if (!startData.isEmpty()) {
                    onDialogAddFavorites();
                }

//                }


                break;

            // 컨트롤러2 모의 주행
            case R.id.tv_flight:

                // kml data가 활성화되어 있으면 표시한다.
                boolean useKmlData = StorageUtil.getStorageMode(mContext, KML_DATA);

                if (useKmlData) {
//                    lanSetKMLDataPath(KML_DATA_PATH);
                }

                // 경로 생성 안됬을 경우
                setSimulateDriving();

                //stopTtsTimer();
                /*
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startTtsTimer();
                    }
                }, 1000);

                */
                break;

            // 컨트롤러2 하단 닫기
            case R.id.tv_controll_close2:
                finish();
                break;


        }

    }

    private int currentSimSpeed = 2;

    /**
     * 모의주행 탭 클릭 리스너
     */
    private View.OnClickListener mController2ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // 속도조절
            if (R.id.tv_speed == v.getId()) {

                TextView tvCurrentSpeed = ((TextView) v);
                String currentSpeed = tvCurrentSpeed.getText().toString();

                if (currentSpeed.equalsIgnoreCase("x1")) {
                    currentSimSpeed = 1;
                    tvCurrentSpeed.setText("x2");
                } else if (currentSpeed.equalsIgnoreCase("x2")) {
                    currentSimSpeed = 2;
                    tvCurrentSpeed.setText("x4");
                } else if (currentSpeed.equalsIgnoreCase("x4")) {
                    currentSimSpeed = 0;
                    tvCurrentSpeed.setText("x1");
                }

                lanSimulSpeedTrajectory(currentSimSpeed);

            }
            // 모의비행 플레이, 일시정지 버튼
            else if (mIvPlayState == v) {

                // 전체 항공값이 없을경우
                if (mNative.lanIsRoute() == 0 || mNative.lanSimulIsTrajectory() == 0) {
                    return;
                }

                //mNlvView.setMapViewmode(2);   // NAVIVIEW_BIRDVIEW

                // 모의주행중 인지 체크
                if (mNative.lanSimulIsPause() == 1) {    // 일시정지중
                    mNative.lanSimulResumeTrajectory(); // 일시정지를 다시 복귀시킴
                    mIvPlayState.setImageResource(R.drawable.btn_pause_nor);
                    //test
                    String kml = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LANMap/test.kml";
                    //mNlvView.setKMLDataPath(kml);
                    Log.d("JeLib", kml);
                } else {
                    mNative.lanSimulPauseTrajectory();  // 일시정지 시킴
                    mIvPlayState.setImageResource(R.drawable.btn_play_nor);
                }
            }
            // 모의비행 종료버튼튼
            else if (R.id.tv_controll_close == v.getId()) {

                isSimulStop = false;

                if (mNative.lanSimulIsPause() != 1) {
                    mNative.lanSimulPauseTrajectory();  // 일시정지 시킴
                    isSimulStop= true;
                    mIvPlayState.setImageResource(R.drawable.btn_play_nor);
                }

                messageDialog = new DialogType1(NhsMapSearchActivity.this, "모의 비행 종료", "모의 비행을 종료하시겠습니까?", getString(R.string.btn_confirm), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //mNative.lanSimulStartTrajectory();

                        if (isSimulStop) {
                            mNative.lanSimulStopTrajectory(); //   시뮬레이션 정지
                        }

                        messageDialog.hideDialog();

                        vController2.setVisibility(View.VISIBLE);
                        vController.setVisibility(View.GONE);

                    }
                }, getString(R.string.btn_cancel), new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        // 재기동
                        mNative.lanSimulResumeTrajectory();
                        mIvPlayState.setImageResource(R.drawable.btn_pause_nor);

                        messageDialog.hideDialog();
                    }
                });
            } else if (R.id.ll_speed_low == v.getId()) {

                TextView tvCurrentSpeed = ((TextView) findViewById(R.id.tv_speed));

                if (currentSimSpeed > 0) {

                    currentSimSpeed -= 1;

                    if (currentSimSpeed == 0) {
                        tvCurrentSpeed.setText("x1");
                    } else if (currentSimSpeed == 1) {
                        tvCurrentSpeed.setText("x2");
                    } else if (currentSimSpeed == 2) {
                        tvCurrentSpeed.setText("x4");
                    }

                }

                lanSimulSpeedTrajectory(currentSimSpeed);

            } else if (R.id.ll_speed_high == v.getId()) {

                TextView tvCurrentSpeed = ((TextView) findViewById(R.id.tv_speed));

                if (currentSimSpeed < 2) {
                    currentSimSpeed += 1;
                }

                if (currentSimSpeed == 0) {
                    tvCurrentSpeed.setText("x1");
                } else if (currentSimSpeed == 1) {
                    tvCurrentSpeed.setText("x2");
                } else if (currentSimSpeed == 2) {
                    tvCurrentSpeed.setText("x4");
                }

                lanSimulSpeedTrajectory(currentSimSpeed);

            }

        }
    };


    /**
     * 즐겨찾기 추가 팝업 생성
     */
    private void onDialogAddFavorites() {
        dialogFavorites = new DialogAddFavorites(NhsMapSearchActivity.this,
                new DialogAddFavorites.IFavorite() {

                    @Override
                    public void onSave(final String name) {

                        if (null != mSelectModel && mSelectModel.getmType() == NhsMapModel.TYPE_FAVORITE) {

                            // 중간에 경로가 수정되었다면, 새로 추가한다.
                            if (isAppedMapPin) {

                                // 객체 값 추가
                                mRealm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        NhsFavoriteModel favorite = mRealm.createObject(NhsFavoriteModel.class, NhsFavoriteModel.findAll().size() + 1);
                                        favorite.setName(name);
                                        favorite.setStartData(startData);
                                        favorite.setEndData(endData);
                                        favorite.setRouteData(routeData);
                                    }
                                });

                            } else {
                                // 객체 값 변경
                                mRealm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        NhsFavoriteModel model = realm.where(NhsFavoriteModel.class).equalTo("id", mSelectModel.getId()).findFirst();
                                        model.setName(name);
                                        model.setStartData(startData);
                                        model.setEndData(endData);
                                        model.setRouteData(routeData);
                                    }
                                });
                            }

                        } else {

                            // 객체 값 추가
                            mRealm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    NhsFavoriteModel favorite = mRealm.createObject(NhsFavoriteModel.class, NhsFavoriteModel.findAll().size() + 1);
                                    favorite.setName(name);
                                    favorite.setStartData(startData);
                                    favorite.setEndData(endData);
                                    favorite.setRouteData(routeData);
                                }
                            });
//                            mRealm.beginTransaction();
//                            NhsFavoriteModel favorite = mRealm.createObject(NhsFavoriteModel.class, NhsFavoriteModel.findAll().size() + 1);
//                            favorite.setName(name);
//                            favorite.setStartData(startData);
//                            favorite.setEndData(endData);
//                            favorite.setRouteData(routeData);
////                        mRealm.copyToRealmOrUpdate(NhsFavoriteModel.addFindAll(favorite));
//                            mRealm.commitTransaction();
                        }

                        Toast.makeText(mContext, R.string.save_favorite, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
        dialogFavorites.setStart(startData);
        dialogFavorites.setEnd(endData);
        dialogFavorites.setRoute(routeData);
        dialogFavorites.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                startData = dialogFavorites.getStart();
                endData = dialogFavorites.getEnd();
                routeData = dialogFavorites.getRoute();

                Log.d("routedate", routeData);
                reloadMap();

            }
        });
        dialogFavorites.show();
    }


    /**
     * 타입별 레이아웃 초기화
     */
    private void initTypeView() {

        Log.i("TEST", "mType:::" + mType);
        if (mType == TYPE_FAVORITE) { // 접속경로가 즐겨찾기 일 경우
            setSaveModelList(true);
//            vController2.setVisibility(View.INVISIBLE);
            mAbeTitle.setTitleText("즐겨찾기");
        }
        // 좌표 검색일 경우
        else if (mType == TYPE_SEARCH_WAYPOINT) {
//            reloadMap();
            mAbeTitle.setTitleText("좌표검색");
        }
        // 최근검색
        else if (mType == TYPE_SEARCH_RECENT) {
            setSaveModelList(false);
            mAbeTitle.setTitleText("최근검색");
        } else if (mode == MODE_SIMULATION) {
            mAbeTitle.setTitleText("모의비행");
        } else if (mode == LOG_DRIVE) {
            mAbeTitle.setTitleText("모의비행");
        } else {
            mAbeTitle.setTitleText("수동검색");
            showFavoriteButton = true;

            boolean showCurrentGpsDialog = false;

            switch (this.mode) {

                // 경로탐색일 경우
                case NhsSelectPointActivity.MODE_ROUTE_SEARCH:

                    // 즐겨찾기는 표시하지 않는다.
                    if (mType != TYPE_FAVORITE) {

                        switch (this.routeSearchStep) {

                            case ROUTE_SEARCH_STEP_START:
                                showCurrentGpsDialog = true;
                                break;

                        }

                    }
                    break;

                case NhsSelectPointActivity.MODE_DEPARTURE:
                    showCurrentGpsDialog = true;
                    break;

            }

            if (showCurrentGpsDialog) {

                currentGps = new DialogType1(getContext(), "", "현재 위치를 출발지로 확정하시겠습니까?",
                        getContext().getString(R.string.btn_confirm),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                currentGps.hideDialog();
                                NhsMapSearchActivity.super.displayGpsProgressBar();

                                // gps 가져온다.
                                NhsMapSearchActivity.super.setGpsListener(new OnGpsListener() {
                                    @Override
                                    public void onLocationChanged(Location location) {
                                        Log.d("JeLib","-------------onLocationChanged---------");
                                        Log.d("JeLib","-------------onLocationChanged---------");
                                        Log.d("JeLib","-------------onLocationChanged---------");
                                        Log.d("JeLib","-------------onLocationChanged---------");
                                        Log.d("JeLib","-------------onLocationChanged---------");

                                        NhsMapSearchActivity.super.setGpsListener(null);

                                        NhsMapSearchActivity.super.dismissGpsProgress();

                                        int result = mNlvView.setRoutePosition(NhsMapSearchActivity.this, Constants.NAVI_SETPOSITION_START, location.getLongitude(), location.getLatitude(), "start name", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        });

                                        if (result == 0) {
                                            startData = location.getLongitude() + " " + location.getLatitude();

                                            if (!(mode == NhsSelectPointActivity.MODE_ROUTE_SEARCH)) {


                                                resultData(true);

                                            } else {

                                                routeSearchStep += 1;
                                                isAppedMapPin = true;

                                            }
                                        }


                                    }

                                    @Override
                                    public void onLocationFailed(String message) {

                                        NhsMapSearchActivity.super.dismissGpsProgress();

                                        new ToastUtile().showCenterText(NhsMapSearchActivity.this, "GPS가 연결되지 않았습니다.\n잠시 후에 다시 시도해 주세요.");

                                    }
                                });

                                NhsMapSearchActivity.super.getLocation();

                            }
                        }, getContext().getString(R.string.btn_cancel),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                currentGps.hideDialog();
                            }
                        });

            }
        }
    }

    /**
     * 저장된 리스트 불러오기
     *
     * @param favoriteState 즐겨찾기 상태 유무
     */
    private void setSaveModelList(boolean favoriteState) {
        mFlFavorites = (FrameLayout) findViewById(R.id.fl_favorites);
        mRecyclerViewFavorites = (RecyclerView) findViewById(R.id.rv_favorites);
        mRecyclerViewFavorites.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerFavorites = new RecyclerFavoriteAdapter(new RecyclerFavoriteAdapter.IFavorite() {
            @Override
            public void onClick(NhsMapModel data) {

                mSelectModel = data;
                mFlFavorites.setVisibility(View.GONE);

                startData = data.getStartData();
                endData = data.getEndData();
                routeData = data.getRouteData();

                Log.i("TEST", "onClick:::" + data.getName());
                Log.i("JeLib", "startData:::" + startData);
                Log.i("JeLib", "endData:::" + endData);
                Log.i("JeLib", "routeData:::" + routeData);


                if (!(mode == NhsSelectPointActivity.MODE_ROUTE_SEARCH)) {
                    resultData(true);
                } else {
                    if (data.getmType() == NhsMapModel.TYPE_FAVORITE) {
                        mFavorite.setText("명칭변경");
                    }

                    reloadMap();
                }
            }

            @Override
            public void onLongClick(final NhsMapModel data) {

                complateDialog = new DialogType1(getContext(), "", "즐겨찾기를 삭제하시겠습니까?",
                        getContext().getString(R.string.btn_delete),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                // 삭제
                                NhsFavoriteModel.delete(data);
                                complateDialog.hideDialog();

                                // 리스트 갱신
                                setSaveModelList(true);

                            }
                        }, getContext().getString(R.string.btn_cancel),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                complateDialog.hideDialog();
                            }
                        });

            }
        });
        mRecyclerFavorites.setFavoriteData(NhsFavoriteModel.findAll());

        // 즐겨찾기가 아닐 경우 좌표검색한 내용도 추가 시켜준다
        if (!favoriteState) {
            mRecyclerFavorites.setWayPointData(NhsSearchWayPointModel.findAll(), true);
        }
        mRecyclerViewFavorites.setAdapter(mRecyclerFavorites);

        mFlFavorites.setVisibility(View.VISIBLE);
    }


    /**
     * 모의 주행 실행
     */
    private void setSimulateDriving() {
        // 경로 생성 안됬을 경우
        if (mNative.lanIsRoute() == 0) {
            try {
                if (null != mDialog) {
                    mDialog.dismiss();
                    mDialog = null;
                }

                DialogType2 dTpye2 = new DialogType2(mContext, getString(R.string.err_title), getString(R.string.err_message_6), getString(R.string.btn_confirm), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                        mDialog = null;
                    }
                });
                mDialog = dTpye2.getDialog();
            } catch(Exception e){
                mDialog = null;
            }
        } else {

            // 경로가 있을 경우
            vController.setVisibility(View.VISIBLE);
            vController2.setVisibility(View.GONE);

            mNative.lanSimulStartTrajectory();  // 시뮬레이터 준비 시작
            mNative.lanSimulResumeTrajectory(); // 시뮬레이터 시작
            mIvPlayState.setImageResource(R.drawable.btn_pause_nor);
            //stopTtsTimer();
            /*
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startTtsTimer();
                }
            }, 1000);
               */

        }
    }

    /**
     * mTts 재생
     *
     * @author FIESTA
     * @since 오전 3:35
     **/
    private void playTTS(String msg) {

        boolean isSoundMode = StorageUtil.getStorageMode(getContext(), IS_TTS_SOUND);

        if (isSoundMode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ttsGreater21(msg);
            } else {
                ttsUnder20(msg);
            }

        }
    }


    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        if (!mTts.isSpeaking()) {
            HashMap<String, String> map = new HashMap<>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
            mTts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        if (!mTts.isSpeaking()) {
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


    /**
     * RP 에러 메세지 보여주는 함수
     */
    private void rpErrorMsg(int resultCode) {

        if (0 == resultCode) {
            return;
        }

        String errorMsg = "";
        switch (resultCode) {
            case 1:
                errorMsg = getString(R.string.rp_error_msg_1);
                break;
            case 2:
                errorMsg = getString(R.string.rp_error_msg_2);
                break;
            case 3:
                errorMsg = getString(R.string.rp_error_msg_3);
                break;
            case 4:
                errorMsg = getString(R.string.rp_error_msg_4);
                break;
            case 5:
                errorMsg = getString(R.string.rp_error_msg_5);
                break;
            case 6:
                errorMsg = getString(R.string.rp_error_msg_6);
                break;
            case 7:
                errorMsg = getString(R.string.rp_error_msg_7);
                break;
        }


        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
    }

    private void resultData(boolean isSucc) {
        Intent intent = getIntent();
        intent.putExtra(DATA_START, startData);
        intent.putExtra(DATA_END, endData);
        intent.putExtra(DATA_ROUTE, routeData);

        if (isSucc) {
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, intent);
        }

        finish();
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

        // 가상 정보 레이어
        bitData[24] = (StorageUtil.getStorageMode(getContext(), IS_LAYOUT_NW) ? 1 : 0);

        // NOTAM 정보 레이어
        bitData[26] = (StorageUtil.getStorageMode(getContext(), NOTAM_INFO_LAYER) ? 1 : 0);

        // 항공기 위치 표시 레이어
        bitData[25] = (StorageUtil.getStorageMode(getContext(), AIRCRAFT_POSITIONING_LAYER) ? 1 : 0);


        // 공역_정보_레이어
        bitData[24] = (StorageUtil.getStorageMode(getContext(), AIRSPACE_INFORMATION_LAYER) ? 1 : 0);

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
        super.onKeyDown(keyboard, event);

        if (event.getAction() == KeyEvent.ACTION_DOWN) {

            switch (keyboard) {
                // 줌인
                case KeyEvent.KEYCODE_F2:
                    lanZoomByPosition(1, 0, 0);
//                    showMessage("F2 Touch!");
                    break;

                // 줌 아웃
                case KeyEvent.KEYCODE_F3:
                    lanZoomByPosition(0, 0, 0);
//                    showMessage("F3 Touch!");
                    break;
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
                    break;
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
        return false;
    }

}


