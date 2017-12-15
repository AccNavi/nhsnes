package kr.go.molit.nhsnes.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import kr.go.molit.nhsnes.Network.NetworkProcess;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.adapter.RecyclerMainNhsFlightPlanListAdapter;
import kr.go.molit.nhsnes.common.DateTimeUtil;
import kr.go.molit.nhsnes.common.MagicSE_Util;
import kr.go.molit.nhsnes.common.NetworkParamUtil;
import kr.go.molit.nhsnes.common.NetworkUrlUtil;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.dialog.DialogSelectFlightCategory;
import kr.go.molit.nhsnes.dialog.DialogSelectFlightPlain;
import kr.go.molit.nhsnes.dialog.DialogType1;
import kr.go.molit.nhsnes.dialog.DialogType2;
import kr.go.molit.nhsnes.dialog.LoadingDialog;
import kr.go.molit.nhsnes.model.NhsFlightPlainModel;
import kr.go.molit.nhsnes.net.model.FlightPlanModel;
import kr.go.molit.nhsnes.net.realm.FlightPlanInfo;
import kr.go.molit.nhsnes.net.service.FlightPlanService;
import kr.go.molit.nhsnes.net.service.NetConst;
import kr.go.molit.nhsnes.net.service.NetUtil;
import kr.go.molit.nhsnes.widget.CheckableButtonEx;
import kr.go.molit.nhsnes.widget.CustomButtonIcon;
import kr.go.molit.nhsnes.widget.TextViewEx;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kr.go.molit.nhsnes.adapter.RecyclerMainNhsFlightPlanListAdapter.VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_TMP;
import static kr.go.molit.nhsnes.dialog.DialogSelectFlightPlain.INTENT_PLAN_ID;
import static kr.go.molit.nhsnes.dialog.DialogSelectFlightPlain.INTENT_PLAN_SN;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class NhsFlightPlanListActivity extends NhsBaseFragmentActivity implements View.OnClickListener {
    private int selectedTab;
    private static final int TAB_ALL_SELECTED = 0;
    private static final int TAB_APPROVED_SELECTED = 1;
    private static final int TAB_DENIED_SELECTED = 2;
    private static final int TAB_TMP_SELECTED = 3;


    CustomButtonIcon btNew;
    CustomButtonIcon btSave;
    CustomButtonIcon btDelete;

    CheckableButtonEx btAll;
    CheckableButtonEx btApproved;
    CheckableButtonEx btDenied;
    CheckableButtonEx btTmp;


    ViewGroup.LayoutParams tabAllParam;
    ViewGroup.LayoutParams tabApprovedParam;
    ViewGroup.LayoutParams tabDeniedParam;
    ViewGroup.LayoutParams tabTmpParam;

    EditText etSearch;

    RecyclerView rvList;
    /*
    List<NhsFlightPlainModel> allList;
    List<NhsFlightPlainModel> approvedList;
    List<NhsFlightPlainModel> deniedList;
    List<NhsFlightPlainModel> tmpList;
    */
    private DialogType2 deleteDialog;
    private DialogType2 saveDialog;
    private DialogType1 deleteDialog2;
    private DialogType1 saveDialog2;

    private Context mContext = null;
    private RecyclerMainNhsFlightPlanListAdapter mRecyclerMainNhsFlightPlanListAdapter = null;

    private List<FlightPlanInfo> flightPlanList = new ArrayList<FlightPlanInfo>();
    private int infoCount = 0;

    private int currentTabSelected = TAB_ALL_SELECTED;  // 선택된 탭


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_nhs_flight_plan_list);
        this.mContext = this;
//        setLayout();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout();
    }

    private void setLayout() {
        btNew = (CustomButtonIcon) findViewById(R.id.bt_new);
        btNew.setOnClickListener(this);
        btSave = (CustomButtonIcon) findViewById(R.id.bt_save);
        btSave.setOnClickListener(this);
        btDelete = (CustomButtonIcon) findViewById(R.id.bt_delete);
        btDelete.setOnClickListener(this);
        rvList = (RecyclerView) findViewById(R.id.rv_plan_list);


        btAll = (CheckableButtonEx) findViewById(R.id.bt_all);
        btAll.setOnClickListener(this);
        btApproved = (CheckableButtonEx) findViewById(R.id.bt_approved);
        btApproved.setOnClickListener(this);
        btDenied = (CheckableButtonEx) findViewById(R.id.bt_denied);
        btDenied.setOnClickListener(this);
        btTmp = (CheckableButtonEx) findViewById(R.id.bt_tmp);
        btTmp.setOnClickListener(this);
        etSearch = (EditText) findViewById(R.id.et_search);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (v.getId() == R.id.et_search && actionId == EditorInfo.IME_ACTION_DONE) { // 뷰의 id를 식별, 키보드의 완료 키 입력 검출
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                }
                return false;
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //setRecyclerView(selectedTab);
                String searchText = s.toString();

                List<FlightPlanInfo> arr = new ArrayList<FlightPlanInfo>();

                if (flightPlanList != null) {
                    for (FlightPlanInfo item : flightPlanList) {

                        try {
                            if (Util.NullString(item.getPlanArrival(), "").trim().contains(searchText)) {
                                arr.add(item);
                            } else if (Util.NullString(item.getPlanDate(), "").trim().contains(searchText)) {
                                arr.add(item);
                            }
                        } catch (Exception ex) {

                        }
                    }
                    Log.d("JeLib", "size:  " + arr.size());
                    if (mRecyclerMainNhsFlightPlanListAdapter != null && arr != null) {
                        mRecyclerMainNhsFlightPlanListAdapter.setData(arr, RecyclerMainNhsFlightPlanListAdapter.VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_ALL);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etSearch.setSingleLine();

        rvList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mRecyclerMainNhsFlightPlanListAdapter = new RecyclerMainNhsFlightPlanListAdapter(null, VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_TMP, this);
        rvList.setAdapter(mRecyclerMainNhsFlightPlanListAdapter);
        /*
        // TODO: 2017. 3. 31. 리스트 서버 연동해야할 부분
        allList = new ArrayList<>();
        approvedList = new ArrayList<>();
        deniedList = new ArrayList<>();
        tmpList = new ArrayList<>();
        */
        setTabLayout(this.currentTabSelected);
        setRecyclerView(this.currentTabSelected);
    }

    //탭 선택 시 리스트 연동 부분
    public void setRecyclerView(int which_tab) {
        etSearch.setText("");
        int listSize = 0;
        // 전체탭
        if (which_tab == TAB_ALL_SELECTED) {
            callFlightPlan("");
        }
        // 승인탭
        else if (which_tab == TAB_APPROVED_SELECTED) {
            callFlightPlan("01");
        }
        // 거절탭
        else if (which_tab == TAB_DENIED_SELECTED) {
            callFlightPlan("99");
        }
        // 임시저장탭
        else if (which_tab == TAB_TMP_SELECTED) {
            this.flightPlanList = new FlightPlanInfo().find(NhsFlightPlainModel.FindType.TMP.ordinal());
            mRecyclerMainNhsFlightPlanListAdapter.setData(flightPlanList, RecyclerMainNhsFlightPlanListAdapter.VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_TMP);
            ((TextViewEx) findViewById(R.id.tv_total)).setText(flightPlanList.size() + "");
            Log.d("JeLib", "this.flightPlanList.size()::" + this.flightPlanList.size());
        }
    }

    //탭 선택 시 화면 연동 부분
    private void setTabLayout(int which_tab) {
        tabAllParam = btAll.getLayoutParams();
        tabApprovedParam = btApproved.getLayoutParams();
        tabDeniedParam = btDenied.getLayoutParams();
        tabTmpParam = btTmp.getLayoutParams();

        btAll.setChecked(false);
        btApproved.setChecked(false);
        btDenied.setChecked(false);
        btTmp.setChecked(false);


        tabAllParam.height = getResources().getDimensionPixelSize(R.dimen.dimen_tab_height_nor);
        tabApprovedParam.height = getResources().getDimensionPixelSize(R.dimen.dimen_tab_height_nor);
        tabDeniedParam.height = getResources().getDimensionPixelSize(R.dimen.dimen_tab_height_nor);
        tabTmpParam.height = getResources().getDimensionPixelSize(R.dimen.dimen_tab_height_nor);


        if (which_tab == TAB_ALL_SELECTED) {
            btAll.setChecked(true);
            tabAllParam.height = getResources().getDimensionPixelSize(R.dimen.dimen_tab_height_sel);
        } else if (which_tab == TAB_APPROVED_SELECTED) {
            btApproved.setChecked(true);
            tabApprovedParam.height = getResources().getDimensionPixelSize(R.dimen.dimen_tab_height_sel);
        } else if (which_tab == TAB_DENIED_SELECTED) {
            btDenied.setChecked(true);
            tabDeniedParam.height = getResources().getDimensionPixelSize(R.dimen.dimen_tab_height_sel);
        } else if (which_tab == TAB_TMP_SELECTED) {
            btTmp.setChecked(true);
            tabTmpParam.height = getResources().getDimensionPixelSize(R.dimen.dimen_tab_height_sel);
        }

        btAll.setLayoutParams(tabAllParam);
        btApproved.setLayoutParams(tabApprovedParam);
        btDenied.setLayoutParams(tabDeniedParam);
        btTmp.setLayoutParams(tabTmpParam);

        currentTabSelected = which_tab;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_all:
                setTabLayout(TAB_ALL_SELECTED);
                setRecyclerView(TAB_ALL_SELECTED);
                selectedTab = TAB_ALL_SELECTED;
                break;
            case R.id.bt_approved:
                setTabLayout(TAB_APPROVED_SELECTED);
                setRecyclerView(TAB_APPROVED_SELECTED);
                selectedTab = TAB_APPROVED_SELECTED;
                break;
            case R.id.bt_denied:
                setTabLayout(TAB_DENIED_SELECTED);
                setRecyclerView(TAB_DENIED_SELECTED);
                selectedTab = TAB_DENIED_SELECTED;
                break;
            case R.id.bt_tmp:
                setTabLayout(TAB_TMP_SELECTED);
                setRecyclerView(TAB_TMP_SELECTED);
                selectedTab = TAB_TMP_SELECTED;
                break;

            case R.id.bt_new:

                FlightPlanInfo flightPlanInfo = null;

                if (flightPlanList != null) {
                    if (flightPlanList.size() > 0) {
                        flightPlanInfo = flightPlanList.get(0);
                    }
                }

                DialogSelectFlightCategory selectPlaneDialog = new DialogSelectFlightCategory(NhsFlightPlanListActivity.this, flightPlanInfo);
                selectPlaneDialog.show();
                break;

            case R.id.bt_save:
                final SparseArray<FlightPlanInfo> selected;
                if (rvList.getAdapter() instanceof RecyclerMainNhsFlightPlanListAdapter) {
                    selected = ((RecyclerMainNhsFlightPlanListAdapter) rvList.getAdapter()).getSelectedItemIds();
                } else {
                    selected = null;
                }

                if (selected == null) {
                    saveDialog = new DialogType2(NhsFlightPlanListActivity.this, getString(R.string.fpl_title), getString(R.string.fpl_message_1), getString(R.string.btn_confirm), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveDialog.hideDialog();
                        }
                    });
                    return;
                } else if (selected.size() > 1 || selected.size() == 0) {
                    Toast.makeText(NhsFlightPlanListActivity.this, "하나의 파일을 선택하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveDialog2 = new DialogType1(NhsFlightPlanListActivity.this, "수정 확인", getString(R.string.fpl_message_4), getString(R.string.btn_confirm), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveDialog2.hideDialog();
                        // TODO: 2017. 4. 24. 수정 기능 추가

                        Intent intent = new Intent(NhsFlightPlanListActivity.this, NhsFlightWriteActivity.class);
                        intent.putExtra(DialogSelectFlightPlain.INTENT_PLAN_ID, selected.get(0).getPlanId());
                        intent.putExtra(DialogSelectFlightPlain.INTENT_PLAN_SN, selected.get(0).getPlanSn());
                        intent.putExtra(DialogSelectFlightPlain.INTENT_PLAN_TYPE, VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_TMP);

                        if (selectedTab == TAB_TMP_SELECTED) {
                            intent.putExtra("isTmp", true);
                        }

                        startActivity(intent);

                    }
                }, getString(R.string.btn_cancel), new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        saveDialog2.hideDialog();
                    }
                });


                break;

            case R.id.bt_delete:

                final SparseArray<FlightPlanInfo> selected2;
                if (rvList.getAdapter() instanceof RecyclerMainNhsFlightPlanListAdapter) {
                    selected2 = ((RecyclerMainNhsFlightPlanListAdapter) rvList.getAdapter()).getSelectedItemIds();
                } else {
                    selected2 = null;
                }


                if (selected2 == null || selected2.size() <= 0) {
                    deleteDialog = new DialogType2(NhsFlightPlanListActivity.this, getString(R.string.fpl_title), getString(R.string.fpl_message_2), getString(R.string.btn_confirm), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteDialog.hideDialog();
                        }
                    });
                    return;
                }/* else if (selected2.size() > 1 || selected2.size() == 0) {
                    Toast.makeText(NhsFlightPlanListActivity.this, "하나의 파일을 선택하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }*/


                if (selectedTab == TAB_TMP_SELECTED) {
                    deleteDialog2 = new DialogType1(NhsFlightPlanListActivity.this, "임시저장 삭제", getString(R.string.fpl_message_5), getString(R.string.btn_confirm), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteDialog2.hideDialog();

                            List<FlightPlanInfo> deletes = mRecyclerMainNhsFlightPlanListAdapter.getData();
                            for (int i = 0; i < mRecyclerMainNhsFlightPlanListAdapter.getItemCount(); i++) {
                                FlightPlanInfo info = selected2.get(i);
                                if (info != null) {
                                    Log.i("TEST", "info.delete();");
                                    info.delete();
                                    deletes.remove(info);
                                }
                            }
                            Log.i("TEST", "notifyDataSetChanged");
                            mRecyclerMainNhsFlightPlanListAdapter.notifyDataSetChanged();

                        }
                    }, getString(R.string.btn_cancel), new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            deleteDialog2.hideDialog();
                        }
                    });
                } else {
                    deleteDialog2 = new DialogType1(NhsFlightPlanListActivity.this, "비행계획서 삭제", getString(R.string.fpl_message_3), getString(R.string.btn_confirm), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteDialog2.hideDialog();
                        /*
                        NhsFlightPlainModel model = NhsFlightPlainModel.getOneById(selected2.get(selected2.keyAt(0)));
                        model.delete();
                        Toast.makeText(NhsFlightPlanListActivity.this, "삭제", Toast.LENGTH_SHORT).show();
                        setRecyclerView(TAB_ALL_SELECTED);
                        // TODO: 2017. 4. 24. 삭제 기능 추가
                        */

                            // 임시저장 체크 유무 확인
//                            if (btTmp.isChecked()) {
//
//                            } else {

                            List<FlightPlanInfo> deletes = new ArrayList<FlightPlanInfo>();
                            for (int i = 0; i < mRecyclerMainNhsFlightPlanListAdapter.getItemCount(); i++) {
                                FlightPlanInfo info = selected2.get(i);
                                if (info != null) {
                                    deletes.add(info);
                                }
                            }
                            infoCount = deletes.size();
                            cancelFlightPlan(deletes);
//                            }

                        }
                    }, getString(R.string.btn_cancel), new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            deleteDialog2.hideDialog();
                        }
                    });
                }


                break;

        }
    }

    private void cancelFlightPlan(final List<FlightPlanInfo> deletes) {
        if (deletes != null && deletes.size() > 0) {

            final LoadingDialog loading = LoadingDialog.create(mContext, null, null);
            loading.show();

            FlightPlanInfo info = deletes.get(deletes.size() - infoCount);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("planId", info.getPlanId());
            params.put("planSn", info.getPlanSn());
            params.put("messageType", "CNL");
            params.put("acrftCd", info.getAcrftCd());
            params.put("planDeparture", info.getPlanDeparture());
            params.put("planEtd", info.getPlanEtd());
            params.put("planArrival", info.getPlanArrival());
            params.put("callsign", info.getCallsign());

            RequestBody body = NetUtil.mapToJsonBody(NhsFlightPlanListActivity.this, params);

            FlightPlanService service = FlightPlanService.retrofit.create(FlightPlanService.class);
            Call<Map<String, Object>> callback = service.repoFlightPlanCancel(body);
            callback.enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    Map<String, Object> model = response.body();
                    Log.d("JeLib", "model::" + model);
                    if (response.code() == 200) {

                        JSONObject resultDataObject = null;

                        if (response != null) {

                            String resultCode = (String) model.get("result_code");

                            if (resultCode.equalsIgnoreCase("Y")) {

                                String resultData =  (String) model.get("result_data");

                                try {

                                    // 복호화한다.
                                    resultData = new MagicSE_Util(NhsFlightPlanListActivity.this).getDecData(resultData);

                                    // 복호화하 내용을 json으로 변환한다.
                                    resultDataObject = new JSONObject(resultData);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                        }

                        String result_code = (String) resultDataObject.optString("result_code");
                        String result_msg = (String) resultDataObject.optString("result_msg");
                        if (result_code == null) {
                            result_code = (String) model.get("result_code");
                        }
                        if (result_msg == null) {
                            result_msg = (String) model.get("result_msg");
                        }
                        if (result_code.trim().equals(NetConst.RESPONSE_SUCCESS)) {

                        } else {
                            new ToastUtile().showCenterText(mContext, result_msg);
                        }
                    } else {
                        new ToastUtile().showCenterText(mContext, getString(R.string.error_network));
                    }
                    loading.dismiss();
                    infoCount--;
                    if (infoCount <= 0) {
                        setRecyclerView(selectedTab);
                    } else {
                        cancelFlightPlan(deletes);
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    infoCount--;
                    loading.dismiss();
                    if (infoCount <= 0) {
                        new ToastUtile().showCenterText(mContext, getString(R.string.error_network));
                    }
                }
            });
        }
    }

    private void callFlightPlan(final String planStatus) {

        int listViewType = RecyclerMainNhsFlightPlanListAdapter.VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_ALL;

        if ("".equals(planStatus)) {
            listViewType = RecyclerMainNhsFlightPlanListAdapter.VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_ALL;
        } else if ("01".equals(planStatus)) {
            listViewType = RecyclerMainNhsFlightPlanListAdapter.VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_APPROVED;
        } else if ("99".equals(planStatus)) {
            listViewType = RecyclerMainNhsFlightPlanListAdapter.VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_DENIED;
        } else {
            listViewType = RecyclerMainNhsFlightPlanListAdapter.VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_TMP;
        }

        final int finalListViewType = listViewType;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                flightPlanList.clear();
                mRecyclerMainNhsFlightPlanListAdapter.setData(flightPlanList, finalListViewType);
                mRecyclerMainNhsFlightPlanListAdapter.notifyDataSetChanged();
            }
        });


        String planMbrId = StorageUtil.getStorageModeEx(this, NhsLoginActivity.LOGIN_MBR_ID);//getStorageMode(this,NhsLoginActivity.LOGIN_MBR_ID);
        String planDate = DateTimeUtil.date(DateTimeUtil.DEFUALT_DATE_FORMAT1);
        String planDoccd = "";    //이부분 수정해야함

        NetworkParamUtil networkParamUtil = new NetworkParamUtil();
        NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();

        StringEntity param = networkParamUtil.getFlightPlanList(planMbrId, planDate, planStatus.trim(), planDoccd);

        NetworkProcess networkProcess = new NetworkProcess(NhsFlightPlanListActivity.this,
                networkUrlUtil.getFpmList(),
                param,
                new NetworkProcess.OnResultListener() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        new ToastUtile().showCenterText(mContext, getString(R.string.error_network) + "(" + statusCode + ")");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        new ToastUtile().showCenterText(mContext, getString(R.string.error_network) + "(" + statusCode + ")");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        try {
                            String msg = response.optString("result_msg");
                            String resultCode = response.optString("result_code");

                            // 메세지 출력
                            new ToastUtile().showCenterText(NhsFlightPlanListActivity.this, msg);

                            if (resultCode.equalsIgnoreCase("Y")) {

                                JSONArray list = response.optJSONArray("list");

                                int size = list.length();
                                int i = 0;
                                FlightPlanInfo flightPlanInfo = null;
                                JSONObject data = null;

                                for (i = 0; i < size; i++) {

                                    flightPlanInfo = new FlightPlanInfo();
                                    data = list.getJSONObject(i);

                                    flightPlanInfo.setFlightRule(data.optString("flightRule", ""));
                                    flightPlanInfo.setPlanDate(data.optString("flightRule"));
                                    flightPlanInfo.setPlanDate(data.optString("planDate", ""));
                                    flightPlanInfo.setPlanRfsDate(data.optString("planRfsDate", ""));
                                    flightPlanInfo.setFlightType(data.optString("flightType", ""));
                                    flightPlanInfo.setPlanDeparture(data.optString("planDeparture", ""));
                                    flightPlanInfo.setPlanRoute(data.optString("planRoute", ""));
                                    flightPlanInfo.setPlanTeet(data.optString("planTeet", ""));
                                    flightPlanInfo.setPlanId(data.optString("planId", ""));
                                    flightPlanInfo.setCallsign(data.optString("callsign", ""));
                                    flightPlanInfo.setPlanAtd(data.optString("planAtd", ""));
                                    flightPlanInfo.setAcrftType(data.optString("acrftType", ""));
                                    flightPlanInfo.setPlanDoccd(data.optString("planDoccd", ""));
                                    flightPlanInfo.setPlanArrival(data.optString("planArrival", ""));
                                    flightPlanInfo.setAcrftCd(data.optString("acrftCd", ""));
                                    flightPlanInfo.setPlanEtd(data.optString("planEtd", ""));
                                    flightPlanInfo.setPlanPurpose(data.optString("planPurpose", ""));
                                    flightPlanInfo.setPlanArvDate(data.optString("planArvDate", ""));
                                    flightPlanInfo.setPlanEta(data.optString("planEta", ""));
                                    flightPlanInfo.setPlanStatus(data.optString("planStatus", ""));
                                    flightPlanInfo.setPlanSn(data.optString("planSn", ""));

                                    flightPlanList.add(flightPlanInfo);
                                }


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mRecyclerMainNhsFlightPlanListAdapter.setData(flightPlanList, finalListViewType);
                                        ((TextViewEx) findViewById(R.id.tv_total)).setText(flightPlanList.size() + "");
                                    }
                                });


                            } else {
                                new ToastUtile().showCenterText(mContext, msg);
                            }
                        } catch (Exception ex) {

                        } finally {

                        }
                    }
                });

        // 네트워크 통신 시작
        networkProcess.sendEmptyMessage(0);

    }

}
