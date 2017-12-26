package kr.go.molit.nhsnes.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.modim.lan.lanandroid.AlarmType;
import com.modim.lan.lanandroid.Constants;
import com.modim.lan.lanandroid.GuideType;
import com.modim.lan.lanandroid.LanStorage;
import com.modim.lan.lanandroid.NhsLanView;
import com.modim.lan.lanandroid.POIInfo;


import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import io.realm.Realm;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.adapter.RecyclerDestinationSearchAdapter;
import kr.go.molit.nhsnes.common.ActivityStack;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.dialog.DialogAddFavorites;
import kr.go.molit.nhsnes.dialog.DialogComplateSelectMap;
import kr.go.molit.nhsnes.dialog.DialogConfirmSelectMap;
import kr.go.molit.nhsnes.dialog.DialogModifyRoute;
import kr.go.molit.nhsnes.dialog.DialogType2;
import kr.go.molit.nhsnes.interfaces.OnSearchWayPointListener;
import kr.go.molit.nhsnes.model.NhsDestinationSearchModel;
import kr.go.molit.nhsnes.model.NhsFavoriteModel;
import kr.go.molit.nhsnes.model.NhsMapModel;
import kr.go.molit.nhsnes.model.NhsSearchWayPointModel;

import static com.modim.lan.lanandroid.LanStorage.mNative;
import static com.modim.lan.lanandroid.NativeImplement.lanPoiBySearch;
import static com.modim.lan.lanandroid.NativeImplement.lanPoiGetList;
import static kr.go.molit.nhsnes.activity.NhsMapSearchActivity.ROUTE_SEARCH_STEP_END;
import static kr.go.molit.nhsnes.activity.NhsMapSearchActivity.ROUTE_SEARCH_STEP_ROUTE;
import static kr.go.molit.nhsnes.activity.NhsMapSearchActivity.ROUTE_SEARCH_STEP_START;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_END;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_ROUTE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_START;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.KEY_MODE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.MODE_ARRIVAL;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.MODE_DEPARTURE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.MODE_ROUTE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.MODE_ROUTE_SEARCH;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.MODE_SEARCH_IN_ROUTE_SEARCH;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.MODE_SIMULATION;
import static kr.go.molit.nhsnes.fragment.SystemSettingFragment.IS_TTS_SOUND;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class NhsDestinationSearchActivity extends NhsBaseFragmentActivity implements OnSearchWayPointListener {

    private final static int TTS_TIMER_INTERVAL = 1000;             // TTS 알람 체크 주기
    private Context mContext;

    private RecyclerView mRecyclerViewSearchDestination;
    private RecyclerDestinationSearchAdapter mRecyclerDestinationSearchAdapter;
    private EditText et_search = null;
    List<NhsDestinationSearchModel> models = new ArrayList<>();
    private NhsLanView mNlvView = null;

    private int mode = MODE_DEPARTURE;              // 출발/도착 플래그
    private String startData = "";                    // 이미 선택된 지정된 출발 좌표
    private String endData = "";                      // 이미 선택된 지정된 도착 좌표
    private String routeData = "";                   // 이미 선택된 지정된 경유지 좌표

    private int routeSearchStep = 0;                // 경로 탐색은 몇단계로 나누어서서 나오므로, 그러한 현재 스탭을 기록한다.

    private DialogConfirmSelectMap mDialogConfirmSelectMap = null;      // 경로 확정 다이얼로그
    private DialogComplateSelectMap dialogComplateSelectMap = null;    // 결로 검색 중 경로 확정 다이얼로그그
    private DialogModifyRoute dialogModifyRoute = null;                 // 경로 수정 다이얼로그
    private DialogAddFavorites dialogFavorites;                          // 즐겨찾기 다이얼로그

    private TimerTask ttsTimerTask = null;   // tts 알람
    private Timer ttsTimer = null;           // tts 알람
    private TextToSpeech mTts = null;

    private Dialog mDialog;     // 기본 다이얼로그

    private Realm mRealm;
    private View v_overlay = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_destination_search);
        mContext = this;
        // Realm을 초기화합니다.
        Realm.init(mContext);
        mRealm = Realm.getDefaultInstance();

        showProgress();

        Bundle data = getIntent().getExtras();
        Log.d("JeLib", "--------1---------");
        if (data != null) {

            this.mode = data.getInt(KEY_MODE, MODE_DEPARTURE);
            this.startData = data.getString(DATA_START, "");
            this.endData = data.getString(DATA_END, "");
            this.routeData = data.getString(DATA_ROUTE, "");
        }

        setLayout();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissProgress();
            }
        }, 3000);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mNlvView != null) mNlvView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNlvView != null) mNlvView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mNlvView != null) mNlvView.onResume();

    }

    private void showProgress() {

        try {
            if (v_overlay == null) {
                v_overlay = findViewById(R.id.v_overlay);
                v_overlay.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.d("JeLib", "방지");
                        return true;
                    }
                });
            }
        } catch (Exception e) {

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
            if (v_overlay != null) {
                v_overlay.setVisibility(View.GONE);
                v_overlay.setOnTouchListener(null);
                v_overlay = null;
            }
        } catch (Exception e) {

        }
    }

    private HashMap<String, NhsDestinationSearchModel> hashList = null;

    private void searchList(String mPOIText) {
        byte[] strKSC5601 = null;
        try {
            strKSC5601 = mPOIText.getBytes("ksc5601");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int nCount = lanPoiBySearch(strKSC5601, false);
        strKSC5601 = null;
        Log.d("JeLib", "nCount:" + nCount);
        if (nCount != 0) {

            this.hashList = new HashMap<>();

            models.clear();

            try {
                NhsDestinationSearchModel model = new NhsDestinationSearchModel();

                POIInfo poiList = lanPoiGetList();

                for (int i = 0; i < poiList.mMaxCount; i++) {
                    // poi 데이타는 Y축이 반대로 되어 있다.
                    try {
                        poiList.item[i].title = new String(poiList.item[i].arrTitle, "ksc5601");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.d("JeLib", "POI ( " + i + " ) : Name(" + poiList.item[i].title + ") , X(" + poiList.item[i].nX + ") , Y(" + poiList.item[i].nY + ")");
                    model = new NhsDestinationSearchModel();
                    model.setDestinationName(poiList.item[i].title);
                    model.setLongitude("" + (poiList.item[i].nX / (double) 10000000));
                    model.setLatitude("" + (poiList.item[i].nY / (double) 10000000));
                    model.setDate(System.currentTimeMillis());

                    // 해쉬맵 저장.
                    this.hashList.put(model.getDestinationName(), model);

                }
            } catch (Exception e) {
            } finally {


                TreeMap<String,NhsDestinationSearchModel> tm = new TreeMap<String,NhsDestinationSearchModel>(this.hashList);

                Iterator<String> iteratorKey = tm.keySet( ).iterator( );   //키값 오름차순 정렬(기본)
                //Iterator<String> iteratorKey = tm.descendingKeySet().iterator(); //키값 내림차순 정렬

                while(iteratorKey.hasNext()){
                    String key = iteratorKey.next();
                    models.add(this.hashList.get(key));
                }

            }
            mRecyclerDestinationSearchAdapter.setData(models);
            mRecyclerDestinationSearchAdapter.notifyDataSetChanged();
        }
    }

    private void setLayout() {

        mNlvView = (NhsLanView) findViewById(R.id.nlv_view);

        mRecyclerViewSearchDestination = (RecyclerView) findViewById(R.id.rv_search_destination);
        mRecyclerViewSearchDestination.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerDestinationSearchAdapter = new RecyclerDestinationSearchAdapter(models);
        mRecyclerDestinationSearchAdapter.setOnClickListener(new RecyclerDestinationSearchAdapter.ISearchClickListener() {
            @Override
            public void onClick(NhsDestinationSearchModel model) {
                // 검색 클릭
                onSearchListClick(model);
            }
        });
        mRecyclerViewSearchDestination.setAdapter(mRecyclerDestinationSearchAdapter);

        et_search = (EditText) findViewById(R.id.et_search);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        onSearchStart();
                        break;
                    default:
//                        Toast.makeText(getApplicationContext(), "기본", Toast.LENGTH_LONG).show();
                        return false;
                }
                return true;
            }
        });


        findViewById(R.id.ib_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchStart();
            }
        });

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mNlvView != null) {

                    mNlvView.setMapKind(Constants.NAVI_MAP_VECTOR);
                    reloadMap();
                }
            }
        }, 1000);

    }


    /**
     * 검색 시작
     */
    private void onSearchStart() {
        if (et_search.getText().length() > 0) {
            searchList(et_search.getText().toString());
            hideKeyboard(et_search);        // 키보드 숨기기
        }
    }

    /**
     * 검색 클릭 데이터 전달 함수
     *
     * @param model
     */
    private void onSearchListClick(NhsDestinationSearchModel model) {

        switch (this.mode) {

            case ROUTE_SEARCH_STEP_START:
                Log.d("JeLib", "ROUTE_SEARCH_STEP_START");
                setStart(model);
                break;

            case ROUTE_SEARCH_STEP_END:
                setGoal(model);
                break;

            case ROUTE_SEARCH_STEP_ROUTE:
                setWaypoint(model);
                break;

            case MODE_SEARCH_IN_ROUTE_SEARCH:

                switch (this.routeSearchStep) {

                    case ROUTE_SEARCH_STEP_START:
                        setStart(model);
                        break;

                    case ROUTE_SEARCH_STEP_END:
                        setGoal(model);
                        break;

                    case ROUTE_SEARCH_STEP_ROUTE:
                        setWaypoint(model);
                        break;

                }

                break;

        }
    }


    /**
     * 도착지 클릭시
     *
     * @author FIESTA
     * @since 오후 8:00
     **/
    public void setGoal(final NhsDestinationSearchModel m) {
        Log.d("JeLib", "setGoal");
        if (mDialogConfirmSelectMap != null) {

        }
        this.mDialogConfirmSelectMap = new DialogConfirmSelectMap(getContext(),
                NhsSelectPointActivity.MODE_ARRIVAL,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        switch (view.getId()) {

                            case R.id.btn_complate:

                                double _x = Double.parseDouble(m.getLongitude());
                                double _y = Double.parseDouble(m.getLatitude());

                                endData = _x + " " + _y;
                                Log.i("TEST", "endData:::" + endData);

                                if (!(mode == NhsSelectPointActivity.MODE_ROUTE_SEARCH)) {

                                    resultData(true);

                                } else {

                                    int result = mNlvView.setRoutePosition(NhsDestinationSearchActivity.this, Constants.NAVI_SETPOSITION_GOAL, _x, _y, "goal_name", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    });

                                    if (result == 0) {
                                        routeSearchStep += 1;
                                    }
                                }

                                break;

                            case R.id.btn_cancel:
                                if (!(mode == NhsSelectPointActivity.MODE_ROUTE_SEARCH)) {
                                    mNative.lanClearRoutePosition();
                                }
                                break;

                        }
                        mDialogConfirmSelectMap.dismiss();

                    }
                });

        this.mDialogConfirmSelectMap.show();

    }

    /**
     * 시작지 클릭시
     *
     * @author FIESTA
     * @since 오후 8:00
     **/
    public void setStart(final NhsDestinationSearchModel m) {

        this.mDialogConfirmSelectMap = new DialogConfirmSelectMap(getContext(),
                NhsSelectPointActivity.MODE_DEPARTURE,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                    double fx = (double) Double.parseDouble(m.getLatitude())/10000000;
//                    double fy = (double) Double.parseDouble(m.getLongitude())/10000000;

                        switch (view.getId()) {

                            case R.id.btn_complate:

                                double _x = Double.parseDouble(m.getLongitude());
                                double _y = Double.parseDouble(m.getLatitude());

                                int result = mNlvView.setRoutePosition(NhsDestinationSearchActivity.this, Constants.NAVI_SETPOSITION_START, _x, _y, "start_name", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                });

                                if (result == 0) {
                                    startData = _x + " " + _y;
                                    Log.i("TEST", "startData:::" + startData);

                                    if (!(mode == NhsSelectPointActivity.MODE_ROUTE_SEARCH)) {
                                        resultData(true);
                                    } else {
                                        routeSearchStep += 1;
                                    }
                                }
                                break;

                            case R.id.btn_cancel:
                                if (!(mode == NhsSelectPointActivity.MODE_ROUTE_SEARCH)) {
                                    mNative.lanClearRoutePosition();
                                }
                                break;

                        }

                        mDialogConfirmSelectMap.dismiss();

                    }
                });

        this.mDialogConfirmSelectMap.show();

    }

    /**
     * waypoint 클릭시
     *
     * @author FIESTA
     * @since 오후 8:00
     **/
    public void setWaypoint(final NhsDestinationSearchModel m) {
        Log.d("JeLib", "setWaypoint");

        this.mDialogConfirmSelectMap = new DialogConfirmSelectMap(getContext(),
                NhsSelectPointActivity.MODE_ROUTE,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        switch (view.getId()) {

                            case R.id.btn_complate:
                                // 경유지 추가

                                double _x = Double.parseDouble(m.getLongitude());
                                double _y = Double.parseDouble(m.getLatitude());

                                int result = mNlvView.setRoutePosition(NhsDestinationSearchActivity.this, Constants.NAVI_SETPOSITION_WAYPOINT, _x, _y, "watpoint name", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                });

                                if (result == 0) {

                                    if (routeData.isEmpty()) {
                                        routeData = _x + " " + _y + "\n";
                                    } else {
                                        routeData += (_x + " " + _y + "\n");
                                    }

                                }
                                break;

                            case R.id.btn_cancel:
                                // 경로 확정

                                /**
                                 int resultCode = mNlvView.executeRP(NhsDestinationSearchActivity.this, 0, 0, new View.OnClickListener() {
                                @Override public void onClick(View view) {
                                switch (view.getId()) {

                                case R.id.alert_btn_ok:
                                mNlvView.executeRPDirect(0, 0);

                                // 경로 확정 된 내용 최근검색 되도록 추가
                                mRealm.beginTransaction();
                                NhsSearchWayPointModel searchWayPoint = mRealm.createObject(NhsSearchWayPointModel.class, NhsSearchWayPointModel.findAll().size()+1);
                                searchWayPoint.setName(""+new SimpleDateFormat("MM/dd HH:mm:ss").format(System.currentTimeMillis()));
                                searchWayPoint.setStartData(startData);
                                searchWayPoint.setEndData(endData);
                                searchWayPoint.setRouteData(routeData);
                                mRealm.commitTransaction();

                                if (!(mode == NhsSelectPointActivity.MODE_ROUTE_SEARCH)) {
                                resultData(true);
                                }

                                break;

                                case R.id.alert_btn_cancel:
                                mNlvView.clearRoutePosition();
                                routeSearchStep = 0;

                                break;

                                }
                                }
                                });
                                 **/
                                //if(0 == resultCode){
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
                                } else {

                                    Intent flightIntent = new Intent(getContext(), NhsMapSearchActivity.class);
                                    flightIntent.putExtra(DATA_START, startData);
                                    flightIntent.putExtra(DATA_END, endData);
                                    flightIntent.putExtra(DATA_ROUTE, routeData);
                                    flightIntent.putExtra(KEY_MODE, MODE_SIMULATION);
                                    startActivityForResult(flightIntent, 0);
                                }

//                            }
                                //else if (resultCode != -1){
                                //    rpErrorMsg(resultCode);
                                //}

//                            if (resultCode != -1) {
//                                onEditRouteDialog();
//                            }

                                break;

                        }
                        mDialogConfirmSelectMap.dismiss();

                    }
                });

        this.mDialogConfirmSelectMap.show();

    }

    @Override
    public void onComplate(int type, Object model) {

    }

    @Override
    public void onComplate() {

        Intent intent = new Intent();
        intent.putExtra(NhsSelectPointActivity.DATA, (Serializable) mRecyclerDestinationSearchAdapter.getSelectedListObj());
        intent.putExtra(NhsSelectPointActivity.KEY_MODE, mode);
        setResult(RESULT_OK, intent);
        finish();

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


    @Override
    public void onCancel() {

    }

    @Override
    public void onNextSelect() {

    }

    @Override
    public void onDelete() {

    }

    private void onEditRouteDialog() {
        Log.i("TEST", "onEditRouteDialog");

        if (this.routeSearchStep > 0) {

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
                                                                stopTtsTimer();
                                                                startTtsTimer();
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


    /**
     * 경로를 지우고 다시 로드한다.
     *
     * @author FIESTA
     * @since 오전 11:24
     **/
    private void reloadMap() {

        // 경로를 지우고 새로 그린다
        mNlvView.clearRoutePosition();
        routeSearchStep = 0;

        // 시자 위치 지정
        try {
            if (!this.startData.isEmpty()) {
                String[] spritStart = startData.split(" ");
                double x = Double.parseDouble(spritStart[0]);
                double y = Double.parseDouble(spritStart[1]);

                Log.i("JeLib", "1:::" + x + ", " + y);

                int result = mNlvView.setRoutePosition(NhsDestinationSearchActivity.this, Constants.NAVI_SETPOSITION_START, x, y, "start name", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

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

                int result = mNlvView.setRoutePosition(NhsDestinationSearchActivity.this, Constants.NAVI_SETPOSITION_GOAL, x, y, "goal name", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

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

                    int result = mNlvView.setRoutePosition(NhsDestinationSearchActivity.this, Constants.NAVI_SETPOSITION_WAYPOINT, x, y, "waypoint nam", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

                }
            }

        } catch (Exception ex) {

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (2 <= routeSearchStep) {
                    int resultCode = mNlvView.executeRP(NhsDestinationSearchActivity.this, 0, 0, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (view.getId()) {

                                case R.id.alert_btn_ok:
                                    mNlvView.executeRPDirect(0, 0);
                                    break;

                                case R.id.alert_btn_cancel:
                                    routeSearchStep = 0;

                                    mNlvView.clearRoutePosition();
                                    break;

                            }
                        }
                    });

                    if (resultCode != -1) {
                        rpErrorMsg(resultCode);
                    }

                }
            }
        }, 1500);

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
     * 모의 주행 실행
     */
    private void setSimulateDriving() {
        // 경로 생성 안됬을 경우
        if (mNative.lanIsRoute() == 0) {
            if (null != mDialog) {
                mDialog.dismiss();
            }

            DialogType2 dTpye2 = new DialogType2(mContext, getString(R.string.err_title), getString(R.string.err_message_6), getString(R.string.btn_confirm), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            mDialog = dTpye2.getDialog();

        } else {
            mNative.lanSimulStartTrajectory();  // 시뮬레이터 준비 시작
            mNative.lanSimulResumeTrajectory(); // 시뮬레이터 시작
        }
    }


    /**
     * 즐겨찾기 추가 팝업 생성
     */
    private void onDialogAddFavorites() {
        dialogFavorites = new DialogAddFavorites(mContext,
                new DialogAddFavorites.IFavorite() {

                    @Override
                    public void onSave(final String name) {

                        mRealm.beginTransaction();
                        NhsFavoriteModel favorite = mRealm.createObject(NhsFavoriteModel.class, NhsFavoriteModel.findAll().size() + 1);
                        favorite.setName(name);
                        favorite.setStartData(startData);
                        favorite.setEndData(endData);
                        favorite.setRouteData(routeData);
                        mRealm.commitTransaction();

                        Toast.makeText(mContext, R.string.save_favorite, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
        dialogFavorites.setStart(startData);
        dialogFavorites.setEnd(endData);
        dialogFavorites.setRoute(routeData);
        dialogFavorites.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.mNlvView.Init();
        this.routeSearchStep = 0;
    }

    /**
     * 키보드 숨기기
     *
     * @param et
     */
    private void hideKeyboard(EditText et) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    /**
     * RP 에러 메세지 보여주는 함수
     *
     * @param resultCode
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
    /*
    @Override
    public boolean onKeyDown(int keyboard, KeyEvent event) {
        //super.onKeyDown(keyboard, event);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {

            switch (keyboard) {
                case KeyEvent.KEYCODE_F1:
                    //goMain();

                    Log.d("JeLib","size:::"+ActivityStack.getInstance().size());
                    ActivityStack activityStack =  ActivityStack.getInstance();
                    String[] taskIds = activityStack.getAliveIDs();
                    for(String ids : taskIds){
                        String[] arr = ids.split(":");
                        if(!arr[1].equals("kr.go.molit.nhsnes.activity.NhsMainActivity"))
                        {
                            Activity act = activityStack.getActivity(ids);
                            Log.d("JeLib","ids:"+ids);
                            try {
                                act.finish();
                            } catch (Exception e){
                                Log.d("JeLib","ex:::"+e.getMessage());
                            }
                        }
                    }
                    return false;
                default:
                    super.onKeyDown(keyboard, event);
                    break;
            }
        }
        return false;
    }*/
}
