package kr.go.molit.nhsnes.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.activity.NhsMapSearchActivity;
import kr.go.molit.nhsnes.activity.NhsMapSearchWaypointActivity;
import kr.go.molit.nhsnes.activity.NhsSelectPointActivity;
import kr.go.molit.nhsnes.adapter.RecyclerWaypointAdapter;
import kr.go.molit.nhsnes.interfaces.OnSearchWayPointListener;
import kr.go.molit.nhsnes.model.NhsRecentSearch;
import kr.go.molit.nhsnes.model.NhsSearchWayPointModel;
import kr.go.molit.nhsnes.model.NhsWaypoinSearchModel;
import kr.go.molit.nhsnes.widget.TextViewEx;

import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_END;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_ROUTE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_START;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.KEY_MODE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.MODE_ARRIVAL;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.MODE_DEPARTURE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.MODE_ROUTE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.SHOW_POPUP;

/**
 * Created by jongrakmoon on 2017. 4. 3..
 */

public class DialogSearchWaypoint extends DialogBase implements View.OnClickListener, OnSearchWayPointListener, View.OnLongClickListener {

    private Activity act = null;
    private RecyclerView mRecyclerViewWayPoint;
    private RecyclerWaypointAdapter mRecyclerWaypointAdapter;
    private EditText mEditTextN1, mEditTextE1;
    private TextViewEx mTextViewEx2;
    private DialogType2 errorDialog;
    private DialogConfirmPath confirmDialog;
    private OnSearchWayPointListener onSearchWayPointListener;

    private int mode = 0;
    private String startData = "";
    private String endData = "";
    private String routeData = "";

    private DialogType1 deleteData = null;

    private Realm mRealm;

    public DialogSearchWaypoint(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_search_waypoint);
        setLayout();
    }

    public DialogSearchWaypoint(@NonNull Activity activity, int mode, String startData, String endData, String routeData, OnSearchWayPointListener onSearchWayPointListener) {
        super(activity);
        this.act = activity;
        this.mode = mode;
        this.onSearchWayPointListener = onSearchWayPointListener;
        this.startData = startData;
        this.endData = endData;
        this.routeData = routeData;

        setContentView(R.layout.dialog_search_waypoint);

        // Realm을 초기화합니다.
        Realm.init(activity);
        mRealm = Realm.getDefaultInstance();

        setLayout();
    }

    private void setLayout() {

        mEditTextN1 = (EditText) findViewById(R.id.et_n_1);
        mEditTextE1 = (EditText) findViewById(R.id.et_e_1);

        mTextViewEx2 = (TextViewEx) findViewById(R.id.tv_menu2);
        View vButton1 = findViewById(R.id.bt_menu1);
        View vButton2 = findViewById(R.id.bt_menu2);
        View vButton3 = findViewById(R.id.ll_menu3);


        vButton1.setVisibility(View.VISIBLE);

        switch (this.mode) {

            case NhsSelectPointActivity.MODE_ARRIVAL:
                mTextViewEx2.setText("도착지 확정");
                vButton2.setVisibility(View.GONE);
                vButton3.setVisibility(View.GONE);
                break;

            case NhsSelectPointActivity.MODE_DEPARTURE:
                mTextViewEx2.setText("출발지 확정");
                vButton2.setVisibility(View.GONE);
                vButton3.setVisibility(View.GONE);
                break;

            case NhsSelectPointActivity.MODE_ROUTE:
                mTextViewEx2.setText("경로 확정");
                vButton2.setVisibility(View.GONE);
                vButton3.setVisibility(View.GONE);
                break;

            case NhsSelectPointActivity.MODE_ROUTE_SEARCH:
                vButton1.setVisibility(View.GONE);
                vButton2.setVisibility(View.VISIBLE);
                vButton3.setVisibility(View.VISIBLE);
                break;


        }

        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isPushButton) {
                    DialogSearchWaypoint.this.onClick(findViewById(R.id.bt_menu1));
                }
            }
        });

        // 임의 데이터 생성
//    List<NhsWaypoinSearchModel> nhsCurrentModels = new ArrayList<>();
//    for (int i = 0; i < 20; i++) {
//      NhsWaypoinSearchModel model = new NhsWaypoinSearchModel();
//      model.setLatitude("68.1223" + i);
//      model.setLongitude("100.1233" + i);
//      nhsCurrentModels.add(model);
//    }

        mRecyclerViewWayPoint = (RecyclerView) findViewById(R.id.rv_search_waypoint);
        mRecyclerViewWayPoint.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerWaypointAdapter = new RecyclerWaypointAdapter(Color.BLUE);
        mRecyclerWaypointAdapter.setOnLongClickListener(this);
        mRecyclerViewWayPoint.setAdapter(mRecyclerWaypointAdapter);

        findViewById(R.id.btn_delete).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);
        findViewById(R.id.bt_menu1).setOnClickListener(this);
        findViewById(R.id.bt_menu2).setOnClickListener(this);

        findViewById(R.id.bt_start_complate).setOnClickListener(this);
        findViewById(R.id.bt_end_complate).setOnClickListener(this);
        findViewById(R.id.bt_route_complate).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_start_complate:     // 출발지 확정
            case R.id.bt_end_complate:      // 도착지 확정
            case R.id.bt_route_complate:    // 경유지 확정

                if (!checkInput()) {
                    errorDialog = new DialogType2(getContext(), getContext().getString(R.string.err_title), getContext().getString(R.string.err_message_1), getContext().getString(R.string.btn_confirm), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            errorDialog.hideDialog();
                        }
                    });
                } else {
                    Log.d("JeLib", mEditTextE1.getText().toString());
                    Log.d("JeLib", mEditTextN1.getText().toString());

                    final String e = mEditTextE1.getText().toString();
                    final String n = mEditTextN1.getText().toString();

                    //final double e = Double.parseDouble(mEditTextE1.getText().toString());
                    //final double n = Double.parseDouble(mEditTextN1.getText().toString());
                    Log.d("JeLib", "e:" + e);
                    Log.d("JeLib", "n:" + n);

                    NhsWaypoinSearchModel model = new NhsWaypoinSearchModel();
                    model.setLatitude(n);
                    model.setLongitude(e);

                    if (v.getId() == R.id.bt_start_complate) {
                        startData = e + " " + n;
                    } else if (v.getId() == R.id.bt_end_complate) {
                        endData = e + " " + n;
                    } else if (v.getId() == R.id.bt_route_complate) {
                        if (routeData.isEmpty()) {
                            routeData = e + " " + n;
                        } else {
                            routeData += "\n" + e + " " + n;
                        }
                    }

                    mRecyclerWaypointAdapter.clearData();

                    String[] start = startData.split(" ");
                    String[] end = endData.split(" ");
                    String[] rou = routeData.split(" ");

                    // 출발지
                    if (!startData.isEmpty()) {
                        model = new NhsWaypoinSearchModel();
                        model.setLatitude(start[0]);
                        model.setLongitude(start[1]);
                        model.setName("출발지");
                        mRecyclerWaypointAdapter.addItem(model);
                    }
                    // 목적지
                    if (!endData.isEmpty()) {
                        model = new NhsWaypoinSearchModel();
                        model.setLatitude(end[0]);
                        model.setLongitude(end[1]);
                        model.setName("목적지");
                        mRecyclerWaypointAdapter.addItem(model);
                    }

                    // 경유지 지정
                    if (!routeData.isEmpty()) {

                        String[] spritStart = routeData.split("\\n");
                        String[] dataRoute;

                        int size = spritStart.length;
                        int i = 0;

                        for (i = 0; i < size; i++) {

                            dataRoute = spritStart[i].split(" ");
                            model = new NhsWaypoinSearchModel();
                            model.setLatitude(dataRoute[0]);
                            model.setLongitude(dataRoute[1]);
                            model.setName("경유지");
                            mRecyclerWaypointAdapter.addItem(model);

                        }


                    }

                    mRecyclerWaypointAdapter.notifyDataSetChanged();

                    Realm realm = Realm.getDefaultInstance();

                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            // increment index
                            Number currentIdNum = realm.where(NhsRecentSearch.class).max("id");
                            int nextId;
                            if (currentIdNum == null) {
                                nextId = 1;
                            } else {
                                nextId = currentIdNum.intValue() + 1;
                            }

                            NhsRecentSearch nhsRecentSearch = new NhsRecentSearch();
                            nhsRecentSearch.setId(nextId);
                            nhsRecentSearch.setLatitude(n);
                            nhsRecentSearch.setLongitude(e);
                            realm.copyToRealm(nhsRecentSearch);
                        }
                    });

                    mEditTextE1.setText("");
                    mEditTextN1.setText("");
                }

                break;


            case R.id.btn_delete:
                // TODO: 2017. 4. 25. 삭제 조건 넣기
//        if (true) {
//          errorDialog = new DialogType2(getContext(), getContext().getString(R.string.err_title), getContext().getString(R.string.err_message_4), getContext().getString(R.string.btn_confirm), new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//              errorDialog.hideDialog();
//            }
//          });
//        }

                mRecyclerWaypointAdapter.clearData();
                mRecyclerWaypointAdapter.notifyDataSetChanged();

                break;
            case R.id.btn_ok:
                // TODO: 2017. 4. 25. 별칭 조건 넣기
                if (mRecyclerWaypointAdapter.getItemCount() == 0) {
                    errorDialog = new DialogType2(getContext(), getContext().getString(R.string.err_title), getContext().getString(R.string.err_message_3), getContext().getString(R.string.btn_confirm), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            errorDialog.hideDialog();
                        }
                    });
                } else {
                    DialogCoordinateAliases dialog = new DialogCoordinateAliases(getContext());
                    dialog.show();
                }
                break;

            case R.id.bt_menu1:
                if (!checkInput()) {
                    errorDialog = new DialogType2(getContext(), getContext().getString(R.string.err_title), getContext().getString(R.string.err_message_1), getContext().getString(R.string.btn_confirm), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            errorDialog.hideDialog();
                        }
                    });
                } else {

                    final String e = mEditTextE1.getText().toString();
                    final String n = mEditTextN1.getText().toString();


                    NhsWaypoinSearchModel model = new NhsWaypoinSearchModel();
                    model.setLatitude(e);
                    model.setLongitude(n);

                    if (this.mode != NhsSelectPointActivity.MODE_ROUTE) {
                        mRecyclerWaypointAdapter.clearData();
                    }

                    if (this.mode == NhsSelectPointActivity.MODE_ROUTE) {
                        mRecyclerWaypointAdapter.addItem(model);
                        mRecyclerWaypointAdapter.notifyDataSetChanged();
                    }

                    Realm realm = Realm.getDefaultInstance();

                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            // increment index
                            Number currentIdNum = realm.where(NhsRecentSearch.class).max("id");
                            int nextId;
                            if (currentIdNum == null) {
                                nextId = 1;
                            } else {
                                nextId = currentIdNum.intValue() + 1;
                            }

                            NhsRecentSearch nhsRecentSearch = new NhsRecentSearch();
                            nhsRecentSearch.setId(nextId);
                            nhsRecentSearch.setLatitude(n);
                            nhsRecentSearch.setLongitude(e);
                            realm.copyToRealm(nhsRecentSearch);
                        }
                    });

                    if (this.mode == MODE_ARRIVAL) {

                        endData = e + " " + n;

                    } else if (this.mode == MODE_DEPARTURE) {

                        startData = e + " " + n;

                    } else if (this.mode == MODE_ROUTE) {

                        if (routeData.isEmpty()) {
                            routeData = e + " " + n;
                        } else {
                            routeData += "\n" + e + " " + n;
                        }

                    }

                    Intent intent = new Intent(this.act, NhsMapSearchWaypointActivity.class);
                    intent.putExtra(NhsSelectPointActivity.KEY_MODE, mode);
                    intent.putExtra(DATA_START, startData);
                    intent.putExtra(DATA_END, endData);
                    intent.putExtra(DATA_ROUTE, routeData);
//          intent.putExtra(DATA, model);

                    this.act.startActivityForResult(intent, mode);
//                    dismiss();
                }

                break;
            case R.id.bt_menu2:
                // TODO: 2017. 4. 25. 경로확정 조건 추가하기
                if (false) {
                    errorDialog = new DialogType2(getContext(), getContext().getString(R.string.err_title), getContext().getString(R.string.err_message_2), getContext().getString(R.string.btn_confirm), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            errorDialog.hideDialog();
                        }
                    });
                } else {

//          if (mode == NhsSelectPointActivity.MOE_) {
//
//            confirmDialog = new DialogConfirmPath(getContext());
//            confirmDialog.show();

//          } else {  // 출발/도착은 바로 데이터 값을 전달한다

                    //if (mRecyclerWaypointAdapter.getSelectedListSize() > 1 && !(type == NhsSelectPointActivity.TYPE_ROUTE)) {
                    if (mRecyclerWaypointAdapter.getItemCount() > 1 && !(mode == NhsSelectPointActivity.MODE_ROUTE) && !(mode == NhsSelectPointActivity.MODE_ROUTE_SEARCH)) {

                        errorDialog = new DialogType2(getContext(), getContext().getString(R.string.err_title), "한개 이상의 좌표 선택이 불가능합니다.", getContext().getString(R.string.btn_confirm), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                errorDialog.hideDialog();
                            }
                        });

                    } //else if (mRecyclerWaypointAdapter.getSelectedListSize() == 0) {
                    else if (mRecyclerWaypointAdapter.getItemCount() == 0) {

                        errorDialog = new DialogType2(getContext(), getContext().getString(R.string.err_title), "좌표를 검색해주세요", getContext().getString(R.string.btn_confirm), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                errorDialog.hideDialog();
                            }
                        });

                    } else if (mode == NhsSelectPointActivity.MODE_ROUTE) {

                        int i = 0;
                        int size = mRecyclerWaypointAdapter.getItemCount();
                        LinkedList<NhsWaypoinSearchModel> modelList = new LinkedList<>();

                        for (i = 0; i < size; i++) {

                            modelList.add(mRecyclerWaypointAdapter.getData(i));

                        }

                        if (this.onSearchWayPointListener != null) {
                            this.onSearchWayPointListener.onComplate(mode, modelList);
                        }

//              DialogConfirmPath dialogConfirmPath = new DialogConfirmPath(getContext(), modelList, NhsSelectPointActivity.MODE_ROUTE);
//              dialogConfirmPath.setOnSearchWayPointListener(this);
//              dialogConfirmPath.show();

                    } else if (mode == NhsSelectPointActivity.MODE_ROUTE_SEARCH) {

                        // 최근검색에 뜨도록 저장
                        mRealm.beginTransaction();
                        NhsSearchWayPointModel searchWayPoint = mRealm.createObject(NhsSearchWayPointModel.class, NhsSearchWayPointModel.findAll().size()+1);
                        searchWayPoint.setName(""+new SimpleDateFormat("MM/dd HH:mm:ss").format(System.currentTimeMillis()));
                        searchWayPoint.setStartData(startData);
                        searchWayPoint.setEndData(endData);
                        searchWayPoint.setRouteData(routeData);
                        mRealm.commitTransaction();

                        Intent searchMap = new Intent(getContext(), NhsMapSearchActivity.class);
                        searchMap.putExtra(KEY_MODE, NhsSelectPointActivity.MODE_SEARCH_IN_ROUTE_SEARCH);
                        searchMap.putExtra(DATA_START, startData);
                        searchMap.putExtra(DATA_END, endData);
                        searchMap.putExtra(DATA_ROUTE, routeData);
                        searchMap.putExtra(SHOW_POPUP, NhsMapSearchActivity.POPUP_COMPLATE);
                        searchMap.putExtra(NhsMapSearchActivity.MAP_TYPE, NhsMapSearchActivity.TYPE_SEARCH_WAYPOINT);
                        getContext().startActivity(searchMap);

                    } else {

                        if (this.onSearchWayPointListener != null) {
                            LinkedList<NhsWaypoinSearchModel> modelList = new LinkedList<>();
                            modelList.add(mRecyclerWaypointAdapter.getData(0));
                            this.onSearchWayPointListener.onComplate(mode, modelList);
                        }

                        this.dismiss();

                    }


//          }
                }
                break;
        }
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(mEditTextE1.getText())
            || TextUtils.isEmpty(mEditTextN1.getText())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onComplate(int type, Object model) {

    }

    @Override
    public void onComplate() {

        if (this.onSearchWayPointListener != null) {
            this.onSearchWayPointListener.onComplate(mode, mRecyclerWaypointAdapter.getSelectedList());
        }

        dismiss();

    }


    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    public boolean onLongClick(final View view) {


        deleteData = new DialogType1(getContext(), "좌표 삭제", "좌표를 삭제하시겠습니까??", "삭제", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NhsWaypoinSearchModel selectedModel = (NhsWaypoinSearchModel) view.getTag(R.string.tag_data);
                mRecyclerWaypointAdapter.removeData(selectedModel);

                // 데이터 재설정
                List<NhsWaypoinSearchModel> list = mRecyclerWaypointAdapter.getCurrentSearchModels();

                startData = "";
                endData = "";
                routeData = "";

                for (NhsWaypoinSearchModel model : list) {

                    if (model.getName().equalsIgnoreCase("출발지")) {
                        startData = model.getLatitude() + " " + model.getLatitude();
                    } else if (model.getName().equalsIgnoreCase("목적지")) {
                        endData = model.getLatitude() + " " + model.getLatitude();
                    } else if (model.getName().equalsIgnoreCase("경유지")) {
                        routeData += (model.getLatitude() + " " + model.getLatitude() + "\n");
                    }

                }

                mRecyclerWaypointAdapter.notifyDataSetChanged();

                deleteData.hideDialog();

            }
        }, getContext().getString(R.string.btn_cancel), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteData.hideDialog();
            }
        });


        Log.d("test", "onLongClick");
        return false;
    }
}

