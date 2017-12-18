package kr.go.molit.nhsnes.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOutOfMemoryException;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import kr.go.molit.nhsnes.Network.NetworkProcess;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.DateTimeUtil;
import kr.go.molit.nhsnes.common.MagicSE_Util;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.dialog.DialogFlightRoutList;
import kr.go.molit.nhsnes.dialog.DialogSelectFlightPlain;
import kr.go.molit.nhsnes.dialog.LoadingDialog;
import kr.go.molit.nhsnes.model.FlightRouteModel;
import kr.go.molit.nhsnes.model.NhsFlightHistoryModel;
import kr.go.molit.nhsnes.net.model.FlightPlanModel;
import kr.go.molit.nhsnes.net.model.NetSecurityModel;
import kr.go.molit.nhsnes.net.model.TpmDepartureModel;
import kr.go.molit.nhsnes.net.realm.FlightPlanInfo;
import kr.go.molit.nhsnes.net.service.FlightPlanService;
import kr.go.molit.nhsnes.net.service.NetConst;
import kr.go.molit.nhsnes.net.service.NetUtil;
import kr.go.molit.nhsnes.widget.ActionBarEx;
import kr.go.molit.nhsnes.widget.EditTextEx;
import kr.go.molit.nhsnes.widget.TextViewEx;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.modim.lan.lanandroid.NativeImplement.lanGetPortCodeName;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_ACRFTCD;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_MBR_ID;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_NAME;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_END;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_ROUTE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_START;
import static kr.go.molit.nhsnes.adapter.RecyclerMainNhsFlightPlanListAdapter.VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_ALL;
import static kr.go.molit.nhsnes.adapter.RecyclerMainNhsFlightPlanListAdapter.VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_TMP;
import static kr.go.molit.nhsnes.dialog.DialogSelectFlightCategory.SELECTED_PLANE;
import static kr.go.molit.nhsnes.dialog.DialogSelectFlightPlain.INTENT_PLAN_ID;
import static kr.go.molit.nhsnes.dialog.DialogSelectFlightPlain.INTENT_PLAN_SN;
import static kr.go.molit.nhsnes.dialog.DialogSelectFlightPlain.IS_NEW_MODE;

/**
 * 비행계획서 작성
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class NhsFlightWriteActivity extends NhsBaseFragmentActivity implements View.OnClickListener {
    private Spinner spinner1;
    private Spinner spinner2;
    private List<String> list1;
    private List<String> list2;

    private boolean isSuperLight;
    private boolean isEditMode;
    private String planid;
    private String planSn;
    private boolean isTmp = false;
    private ActionBarEx actionbar;

    private LinearLayout layoutSuperLight;
    private FrameLayout layoutButtonDefault;
    private LinearLayout layoutEdit;

    private FrameLayout buttonEdit;
    private FrameLayout buttonList;

    private EditText tvRoute;

    private View vDeparture;
    private View vArrival;

    private Context mContext = null;
    private FlightPlanInfo mFlightPlanInfo = null;

    private Realm mRealm;
    private int viewType = VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_ALL;
    private ArrayList<FlightRouteModel> route;  // 경로
    private String flightId = "";   // 비행 시작 보고때 받은 고유 ID

    //private String planDeparture = "";
    //private String planArrival = "";

    private boolean isFailed = true; //비행계획서 전송실패 후 재전송 확인을 위한 플래그 값

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_nhs_flight_write);
        this.mContext = this;

        Realm.init(mContext);
        mRealm = Realm.getDefaultInstance();

        isSuperLight = getIntent() != null && getIntent().getBooleanExtra(SELECTED_PLANE, false);
        this.planid = getIntent().getStringExtra(INTENT_PLAN_ID);
        this.planSn = getIntent().getStringExtra(INTENT_PLAN_SN);
        this.isTmp = getIntent().getBooleanExtra("isTmp", false);
        this.isEditMode= !(getIntent().getBooleanExtra(IS_NEW_MODE, false));
        Log.d("JeLib","isSuperLight:"+isSuperLight);
//        if (!isEditMode) {
//            isEditMode = (planid != null);
//        }
        try {
            Log.i("TEST", "isEditMode:::" + isEditMode);
            setLayout();

            Bundle data = getIntent().getExtras();
            String getCallsign = "";

            if (data != null) {

                getCallsign = data.getString(DialogSelectFlightPlain.INTENT_PLAN_DATA);
                this.viewType = data.getInt(DialogSelectFlightPlain.INTENT_PLAN_TYPE, VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_ALL);

            }

            if (null != getCallsign && getCallsign.length() > 0) {
                //mFlightPlanInfo = new FlightPlanInfo().find(getCallsign);
                //idx 값을 기존에 쓰던 callsign넘겨서 사용함
                if(route == null)route = new ArrayList<FlightRouteModel>();
                mFlightPlanInfo = new FlightPlanInfo().findIdx(Long.parseLong(getCallsign));
                if (null != mFlightPlanInfo) {
                    // 경유지 지정
                    String departure = Util.NullString(mFlightPlanInfo.getPlanDeparture(),"");
                    String arrival = Util.NullString(mFlightPlanInfo.getPlanArrival(),"");
                    String[] start = departure.split(" ");
                    String[] end = arrival.split(" ");
                    if(start!=null && start.length > 0) {
                        Log.d("JeLib","------------1---------");
                        //mFlightPlanInfo.setPlanDeparture(start[0]);
                        Log.d("JeLib","------------2---------");
                        if(start.length >= 3){
                            FlightRouteModel model = new FlightRouteModel();
                            model.setLon(start[1]);
                            model.setLat(start[2]);
                            model.setAreaId("0");
                            model.setAreaNm("0");
                            model.setElev("0");
                            model.setHeading("0");
                            model.setStepNum("1");
                            model.setrType("s");
                            route.add(model);
                        }
                    }

                    if (!Util.NullString(mFlightPlanInfo.getPlanRoute(),"").isEmpty() && route.size()>0) {
                        String[] spritStart = mFlightPlanInfo.getPlanRoute().split("\\n");
                        int size = spritStart.length;
                        for (int i = 0; i < size; i++) {
                            String[] dataRoute = spritStart[i].split(" ");
                            FlightRouteModel model = new FlightRouteModel();
                            model.setLon(dataRoute[0]);
                            model.setLat(dataRoute[1]);
                            model.setAreaId("0");
                            model.setAreaNm("0");
                            model.setElev("0");
                            model.setHeading("0");
                            model.setStepNum(""+(route.size()+1));
                            model.setrType("w");
                            route.add(model);
                        }
                    }
                    if(end!=null && end.length > 0 && route.size()>0) {
                        //mFlightPlanInfo.setPlanArrival(end[0]);
                        if(end.length >= 3){
                            FlightRouteModel model = new FlightRouteModel();
                            model.setLon(end[1]);
                            model.setLat(end[2]);
                            model.setAreaId("0");
                            model.setAreaNm("0");
                            model.setElev("0");
                            model.setHeading("0");
                            model.setStepNum(""+(route.size()+1));
                            model.setrType("e");
                            route.add(model);
                        }
                    }
                    for(FlightRouteModel m : route){
                        Log.d("JeLib",""+m.getLat()+":::"+m.getLon());
                    }
                    try {
                        if (mFlightPlanInfo.getFlightId() == null) {
                            setFlightData(mFlightPlanInfo);
                        } else {
                            this.flightId = mFlightPlanInfo.getFlightId();
                        }
                    } catch (Exception ex) {
                        setFlightData(mFlightPlanInfo);
                    }
                }
            }
        } catch (Exception e){
            Log.d("JeLib","ex:::::::::::::"+e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
    }

    /**
     * 비행 상세 정보를 조회한다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-13 오후 11:25
     **/
    private void callFlightPlanDetail(String planId, String planSn) {
        if(planId==null || planSn ==null)return;
        final LoadingDialog loading = LoadingDialog.create(mContext, null, null);
        loading.show();

        FlightPlanService service = FlightPlanService.retrofit.create(FlightPlanService.class);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("planId", planId);
        params.put("planSn", planSn);

        RequestBody body = NetUtil.mapToJsonBody(NhsFlightWriteActivity.this, params);

        Call<NetSecurityModel> callback = service.repoFlightPlanDetail(body);
        callback.enqueue(new Callback<NetSecurityModel>() {
            @Override
            public void onResponse(Call<NetSecurityModel> call, Response<NetSecurityModel> response) {
                loading.dismiss();

                if (response.code() == 200) {

                    NetSecurityModel netSecurityModel = response.body();

                    if (netSecurityModel.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {

                        String dec = new MagicSE_Util(getContext()).getDecData(netSecurityModel.getResult_data());
                        Log.d("JeLib","dec:"+dec);
                        FlightPlanModel flightPlanModel = new Gson().fromJson(dec, FlightPlanModel.class);

                        if (flightPlanModel.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {
                            mFlightPlanInfo = flightPlanModel.getFplDetail();
                            route = flightPlanModel.getRoute();
                            mFlightPlanInfo.setFlightId(flightId);
                            setFlightData(mFlightPlanInfo);
                            int i = 0;
                            for(FlightRouteModel m : route){
                                if(i == 0){
                                    m.setrType("s");
                                } else if(route.size()-1 == i){
                                    m.setrType("e");
                                } else {
                                    m.setrType("w");
                                }
                                i++;
                                Log.d("JeLib",""+m.getLat()+":::"+m.getLon());
                            }
                        } else {
                            new ToastUtile().showCenterText(mContext, flightPlanModel.getResult_msg());
                        }

                    }
                } else {
                    new ToastUtile().showCenterText(mContext, getString(R.string.error_network));
                }
            }

            @Override
            public void onFailure(Call<NetSecurityModel> call, Throwable t) {
                loading.dismiss();
                new ToastUtile().showCenterText(mContext, getString(R.string.error_network));
            }
        });
    }


    private void setSpinnerData() {
        list1 = new ArrayList<>();
        list1.add("S-정기항공");
        list1.add("N-부정기항공");
        list1.add("G-일반항공");
        list1.add("X-해당없음");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.custom_spinner_dropdown_item, list1);
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        list2 = new ArrayList<>();
        list2.add("훈련비행");
        list2.add("기술착륙");
        list2.add("시험비행");
        list2.add("정비입출");
        list2.add("전세기");
        list2.add("비행점검");
        list2.add("항공이동");
        list2.add("인원수송");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.custom_spinner_dropdown_item, list2);
        adapter2.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setSelected(true);

    }

    private void setLayout() {

        vDeparture = findViewById(R.id.ll_3_2);
        vArrival = findViewById(R.id.ll_6_1);

        vDeparture.setOnClickListener(this);
        vArrival.setOnClickListener(this);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);

        setSpinnerData();

        layoutSuperLight = (LinearLayout) findViewById(R.id.layout_super_light);
        layoutEdit = (LinearLayout) findViewById(R.id.layout_edit);


        buttonEdit = (FrameLayout) findViewById(R.id.bt_edit);
        buttonEdit.setOnClickListener(this);
        buttonList = (FrameLayout) findViewById(R.id.bt_list);
        buttonList.setOnClickListener(this);
        findViewById(R.id.layout_load).setOnClickListener(this);

        actionbar = (ActionBarEx) findViewById(R.id.action_bar);

        layoutButtonDefault = (FrameLayout) findViewById(R.id.layout_send);
        layoutButtonDefault.setOnClickListener(this);

        findViewById(R.id.layout_save).setOnClickListener(this);
        findViewById(R.id.layout_save2).setOnClickListener(this);

        findViewById(R.id.fl_new_route).setOnClickListener(this);

        tvRoute = (EditText) findViewById(R.id.ed_route);
//        tvRoute.setOnClickListener(this);
        tvRoute.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.ed_route) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        if (isSuperLight) {
            layoutSuperLight.setVisibility(View.VISIBLE);
            ((CheckBox) findViewById(R.id.cb_light)).setChecked(true);
        }
        if (isEditMode) {
            actionbar.setTitleText("비행계획서 상세보기");
            layoutButtonDefault.setVisibility(View.GONE);
            findViewById(R.id.layout_save).setVisibility(View.GONE);
            findViewById(R.id.layout_load).setVisibility(View.GONE);
            layoutEdit.setVisibility(View.VISIBLE);
            findViewById(R.id.edit_mode).setVisibility(View.VISIBLE);

            if (!this.isTmp) {
                callFlightPlanDetail(planid, planSn);
            } else {
                layoutButtonDefault.setVisibility(View.VISIBLE);
                findViewById(R.id.layout_save).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_load).setVisibility(View.VISIBLE);
                layoutEdit.setVisibility(View.GONE);
            }

        } else {
            actionbar.setTitleText("비행계획서 작성");
            layoutButtonDefault.setVisibility(View.VISIBLE);
            findViewById(R.id.layout_save).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_load).setVisibility(View.VISIBLE);
            layoutEdit.setVisibility(View.GONE);

            if (planid != null && planSn != null) {
                if (!this.planid.isEmpty() && !planSn.isEmpty()) {
                    callFlightPlanDetail(planid, planSn);
                }
            }
        }


        findViewById(R.id.ll_load_route).setOnClickListener(this);

    }

    private void setFlightData(FlightPlanInfo model) {
        //NhsFlightPlainModel model = NhsFlightPlainModel.getOneById(planId);

        if (null == model) {
            return;
        }

        ((EditTextEx) findViewById(R.id.et_mode_0_4)).setText(model.getMessageType());

        String callsign = this.mFlightPlanInfo.getCallsign();
        if (callsign == null) {
            callsign = this.mFlightPlanInfo.getAcrftCd();
        }

        ((EditTextEx) findViewById(R.id.et_1_1)).setText(callsign);
        findViewById(R.id.et_1_1).setClickable(false);

        if (null != model.getFlightRule()) {
            ((EditTextEx) findViewById(R.id.et_1_2)).setText(model.getFlightRule());
            Log.d("##", model.getFlightType() + "");
            spinner1.setSelection(Util.StringToInt(model.getFlightType()), false);
        }

        if (null != model.getPlanNumber()) {
            ((EditTextEx) findViewById(R.id.et_2_1)).setText(Util.NullString(model.getPlanNumber(), ""));
        }
        if (null != model.getAcrftType()) {
            ((EditTextEx) findViewById(R.id.et_2_2)).setText(Util.NullString(model.getAcrftType(), ""));
        }
        if (null != model.getWakeTurbcat()) {
            ((EditTextEx) findViewById(R.id.et_2_3)).setText(model.getWakeTurbcat());
        }

        if (null != model.getPlanEquipment()) {
            ((EditTextEx) findViewById(R.id.et_3_1)).setText(model.getPlanEquipment());//.getEquipment());
        }

        String planArrival = model.getPlanArrival();
        String planDeparture = model.getPlanDeparture();

        if (planDeparture!=null && planDeparture.length() >= 4 && !planDeparture.isEmpty()) {
            ((TextViewEx) findViewById(R.id.et_3_2)).setText(planDeparture.substring(0,4));
        } else {
            ((TextViewEx) findViewById(R.id.et_3_2)).setText("");
        }
        /*
        if (null != model.getPlanAtd()) {
            ((EditTextEx) findViewById(R.id.et_3_3)).setText(model.getPlanAtd());
        }
        */
        if (null != model.getPlanEtd()) {
            ((EditTextEx) findViewById(R.id.et_3_3)).setText(model.getPlanEtd());
        }

        if (null != model.getCruisingSpeed()) {
            ((EditTextEx) findViewById(R.id.et_4_1)).setText(model.getCruisingSpeed());//.getCrusingSpeed());
        }
        if (null != model.getFlightLevel()) {
            ((EditTextEx) findViewById(R.id.et_4_2)).setText(model.getFlightLevel());//.getFlightLevel());
        }
        if (null != model.getPlanPurpose()) {
            spinner2.setSelection(Util.StringToInt(model.getPlanPurpose()));
        }
        if (null != model.getPlanRoute()) {
            ((EditText) findViewById(R.id.ed_route)).setText(model.getPlanRoute());
        }

        if (planArrival!=null && planArrival.length() >= 4 && !planArrival.isEmpty()) {
            ((TextViewEx) findViewById(R.id.et_6_1)).setText(planArrival.substring(0,4));
        } else {
            ((TextViewEx) findViewById(R.id.et_6_1)).setText("");
        }

        if (null != model.getPlanTeet()) {
            ((EditTextEx) findViewById(R.id.et_6_2)).setText(model.getPlanTeet());
        }
        if (null != model.getOneAltn()) {
            ((EditTextEx) findViewById(R.id.et_7_1)).setText(model.getOneAltn());
        }
        if (null != model.getTwoAltn()) {
            ((EditTextEx) findViewById(R.id.et_7_2)).setText(model.getTwoAltn());
        }
        if (null != model.getOtherInfo()) {
            ((EditTextEx) findViewById(R.id.et_8_1)).setText(model.getOtherInfo());
        }
        if (null != model.getFlightPsbtime()) {
            ((EditTextEx) findViewById(R.id.et_9_1)).setText(model.getFlightPsbtime());
        }
        if (null != model.getFlightPerson()) {
            ((EditTextEx) findViewById(R.id.et_9_2)).setText(model.getFlightPerson());
        }

        /////////////////////////////R
        if (model.getRrUhf() != null && model.getRrUhf().trim().equals("Y")) {
            ((CheckBox) findViewById(R.id.rb_10_1)).setChecked(true);
        } else {
            ((CheckBox) findViewById(R.id.rb_10_1)).setChecked(false);
        }

        if (model.getRrVhf() != null && model.getRrVhf().trim().equals("Y")) {
            ((CheckBox) findViewById(R.id.rb_10_2)).setChecked(true);
        } else {
            ((CheckBox) findViewById(R.id.rb_10_2)).setChecked(false);
        }

        if (model.getRrElt() != null && model.getRrElt().trim().equals("Y")) {
            ((CheckBox) findViewById(R.id.rb_10_3)).setChecked(true);
        } else {
            ((CheckBox) findViewById(R.id.rb_10_3)).setChecked(false);
        }

        if (model.getEmgcPolar() != null && model.getEmgcPolar().trim().equals("Y")) {
            ((CheckBox) findViewById(R.id.rb_11_1)).setChecked(true);
        } else {
            ((CheckBox) findViewById(R.id.rb_11_1)).setChecked(false);
        }
        /////////////////////////////S
        if (model.getEmgcDesert() != null && model.getEmgcDesert().trim().equals("Y")) {
            ((CheckBox) findViewById(R.id.rb_11_2)).setChecked(true);
        } else {
            ((CheckBox) findViewById(R.id.rb_11_2)).setChecked(false);
        }

        if (model.getEmgcMaritime() != null && model.getEmgcMaritime().trim().equals("Y")) {
            ((CheckBox) findViewById(R.id.rb_11_3)).setChecked(true);
        } else {
            ((CheckBox) findViewById(R.id.rb_11_3)).setChecked(false);
        }

        if (model.getEmgcJungle() != null && model.getEmgcJungle().trim().equals("Y")) {
            ((CheckBox) findViewById(R.id.rb_11_4)).setChecked(true);
        } else {
            ((CheckBox) findViewById(R.id.rb_11_4)).setChecked(false);
        }
        /////////////////////////////J
        if (model.getLifejkLight() != null && model.getLifejkLight().trim().equals("Y")) {
            ((CheckBox) findViewById(R.id.rb_12_1)).setChecked(true);
        } else {
            ((CheckBox) findViewById(R.id.rb_12_1)).setChecked(false);
        }

        if (model.getLifejkFluores() != null && model.getLifejkFluores().trim().equals("Y")) {
            ((CheckBox) findViewById(R.id.rb_12_2)).setChecked(true);
        } else {
            ((CheckBox) findViewById(R.id.rb_12_2)).setChecked(false);
        }

        if (model.getLifejkUhf() != null && model.getLifejkUhf().trim().equals("Y")) {
            ((CheckBox) findViewById(R.id.rb_12_3)).setChecked(true);
        } else {
            ((CheckBox) findViewById(R.id.rb_12_3)).setChecked(false);
        }

        if (model.getLifejkVhf() != null && model.getLifejkVhf().trim().equals("Y")) {
            ((CheckBox) findViewById(R.id.rb_12_4)).setChecked(true);
        } else {
            ((CheckBox) findViewById(R.id.rb_12_4)).setChecked(false);
        }

        if (null != model.getLifebtPerson()) {
            ((EditTextEx) findViewById(R.id.et_13_1)).setText(model.getLifebtPerson());
        }
        if (null != model.getLifebtNumber()) {
            ((EditTextEx) findViewById(R.id.et_13_2)).setText(model.getLifebtNumber());
        }
        if (null != model.getLifebtCover()) {
            ((EditTextEx) findViewById(R.id.et_13_3)).setText(model.getLifebtCover());
        }
        if (null != model.getLifebtColor()) {
            ((EditTextEx) findViewById(R.id.et_13_4)).setText(model.getLifebtColor());
        }
        if (null != model.getAcrftColor()) {
            ((EditTextEx) findViewById(R.id.et_14_1)).setText(model.getAcrftColor());
        }
        if (null != model.getCaptainPhone()) {
            ((EditTextEx) findViewById(R.id.et_14_2)).setText(model.getCaptainPhone());
        }
        if (null != model.getPlanPresent()) {
            ((EditTextEx) findViewById(R.id.et_14_3)).setText(model.getPlanPresent());
        }
        if (isSuperLight) {
            layoutSuperLight.setVisibility(View.VISIBLE);
            ((CheckBox) findViewById(R.id.cb_light)).setChecked(true);
        } else {
            if (isEditMode) {
                String doccd = model.getPlanDoccd();
                Log.d("JeLib","doccd--"+doccd);
                if (doccd != null && doccd.trim().equals("01")) {
                    layoutSuperLight.setVisibility(View.VISIBLE);
                    ((CheckBox) findViewById(R.id.cb_light)).setChecked(true);
                } else {
                    layoutSuperLight.setVisibility(View.GONE);
                    ((CheckBox) findViewById(R.id.cb_light)).setChecked(false);
                }
            } else {
                layoutSuperLight.setVisibility(View.GONE);
                ((CheckBox) findViewById(R.id.cb_light)).setChecked(false);
            }
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            // 경로 가져오기
            case R.id.ll_load_route:
                DialogFlightRoutList dialogFlightRoutList = new DialogFlightRoutList(NhsFlightWriteActivity.this);
                dialogFlightRoutList.setOnClickListener(new DialogFlightRoutList.OnClickListener(){

                    @Override
                    public void onClickList(FlightPlanInfo flightPlanInfo) {

                        ((EditText) findViewById(R.id.ed_route)).setText(flightPlanInfo.getPlanRoute());

                    }
                });
                dialogFlightRoutList.show();


                break;

            // departure aerodrome
            case R.id.ll_3_2: {
                String planDeparture = "";
                String planArrival = "";
                if (route != null && route.size() > 0) {
                    planDeparture = route.get(0).getLon() + " " + route.get(0).getLat();
                    if (route.size() > 1) {
                        planArrival = route.get(route.size() - 1).getLon() + " " + route.get(route.size() - 1).getLat();
                    }
                }
                Log.d("JeLib",""+planDeparture+":::::::::::::::::"+planArrival);
                Intent departurePointIntent = new Intent(this, NhsSelectPointActivity.class);
                departurePointIntent.putExtra(NhsSelectPointActivity.KEY_MODE, NhsSelectPointActivity.MODE_DEPARTURE);
                departurePointIntent.putExtra(DATA_START, planDeparture);
                departurePointIntent.putExtra(DATA_END, planArrival);
                departurePointIntent.putExtra(DATA_ROUTE, ((EditText) findViewById(R.id.ed_route)).getText().toString());
                startActivityForResult(departurePointIntent, NhsSelectPointActivity.MODE_DEPARTURE);
                break;
            }
            // arrival aerodrome
            case R.id.ll_6_1: {
                String planDeparture = "";
                String planArrival = "";
                if (route != null && route.size() > 0) {
                    planDeparture = route.get(0).getLon() + " " + route.get(0).getLat();
                    if (route.size() > 1) {
                        planArrival = route.get(route.size() - 1).getLon() + " " + route.get(route.size() - 1).getLat();
                    }
                }
                Log.d("JeLib",""+planDeparture+":::::::::::::::::"+planArrival);
                Intent arrivalPointIntent = new Intent(this, NhsSelectPointActivity.class);
                arrivalPointIntent.putExtra(NhsSelectPointActivity.KEY_MODE, NhsSelectPointActivity.MODE_ARRIVAL);
                arrivalPointIntent.putExtra(DATA_START, planDeparture);
                arrivalPointIntent.putExtra(DATA_END, planArrival);
                arrivalPointIntent.putExtra(DATA_ROUTE, ((EditText) findViewById(R.id.ed_route)).getText().toString());
                startActivityForResult(arrivalPointIntent, NhsSelectPointActivity.MODE_ARRIVAL);
                break;
            }
      /*case R.id.bt_edit:
        setCheckSumData();
        Toast.makeText(this, "수정을 완료하였습니다.", Toast.LENGTH_SHORT).show();
        break;*/
            case R.id.layout_load:
                finish();
                break;
            case R.id.bt_list:
                finish();
                break;

            case R.id.layout_save2:
            case R.id.layout_save:
                saveImsiData();

                break;

            case R.id.fl_new_route:
            case R.id.ed_route: {
                String planDeparture = "";
                String planArrival = "";
                if (route != null && route.size() > 0) {
                    planDeparture = route.get(0).getLon() + " " + route.get(0).getLat();
                    if (route.size() > 1) {
                        planArrival = route.get(route.size() - 1).getLon() + " " + route.get(route.size() - 1).getLat();
                    }
                }
                Log.d("JeLib",""+planDeparture+":::::::::::::::::"+planArrival);
                Intent routePointIntent = new Intent(this, NhsSelectPointActivity.class);
                routePointIntent.putExtra(NhsSelectPointActivity.KEY_MODE, NhsSelectPointActivity.MODE_ROUTE);
                routePointIntent.putExtra(DATA_START, planDeparture);
                routePointIntent.putExtra(DATA_END, planArrival);
                routePointIntent.putExtra(DATA_ROUTE, ((EditText) findViewById(R.id.ed_route)).getText().toString());
                startActivityForResult(routePointIntent, NhsSelectPointActivity.MODE_ROUTE);
                break;
            }
            case R.id.bt_edit:

                if (viewType == VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_TMP) {

                    mRealm.executeTransaction(new Realm.Transaction() {

                        @Override
                        public void execute(Realm realm) {

                            try {

                                getModel();

                                realm.copyToRealmOrUpdate(mFlightPlanInfo);

                            } catch (io.realm.exceptions.RealmPrimaryKeyConstraintException keyEx) {

                                new ToastUtile().showCenterText(NhsFlightWriteActivity.this, "Flight Identity 값이 존재합니다.");
                                ((EditTextEx) findViewById(R.id.et_1_1)).requestFocus();
                            } finally {

                                NhsFlightWriteActivity.this.finish();
                                new ToastUtile().showCenterText(mContext, "수정 되었습니다.");

                            }
                        }
                    });

                    break;
                }


            case R.id.layout_send:

                // 우선 저장
                //saveData(NhsFlightPlainModel.FindType.TMP.ordinal());

                // 화면을 체크한다..
                boolean isNext = setCheckSumData();

                if (isNext) {

                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            // 화면에 설정된 데이터를 가져온다.
                            FlightPlanInfo model = getModel();

                            // 유저 정보를 가져온다.
                            String mbrId = StorageUtil.getStorageModeEx(NhsFlightWriteActivity.this, LOGIN_MBR_ID);
                            String userName = StorageUtil.getStorageModeEx(NhsFlightWriteActivity.this, LOGIN_NAME);

                            final LoadingDialog loading = LoadingDialog.create(mContext, null, null);
                            loading.show();

                            FlightPlanService service = FlightPlanService.retrofit.create(FlightPlanService.class);

                            Map<String, Object> params = new HashMap<String, Object>();
                            Map<String, Object> fpl = new HashMap<String, Object>();

                            if (isEditMode) {
                                fpl.put("planId", mFlightPlanInfo.getPlanId());
//          if (mFlightPlanInfo.getMessageType().trim().equals(FlightPlanInfo.TYPE_MESSAGE_CHG)) {
                                fpl.put("planSn", mFlightPlanInfo.getPlanSn());
//          }
                            }
                            fpl.put("planPriority", mFlightPlanInfo.getPlanPriority());
                            fpl.put("planFltm", DateTimeUtil.date(DateTimeUtil.DEFUALT_DATE_FORMAT9));
                            fpl.put("planRqstdept", "1111");//부서코드
                            fpl.put("planDate", DateTimeUtil.date(DateTimeUtil.DEFUALT_DATE_FORMAT1));
                            fpl.put("messageType", mFlightPlanInfo.getMessageType());
                            fpl.put("acrftCd", mFlightPlanInfo.getAcrftCd());
                            fpl.put("flightRule", mFlightPlanInfo.getFlightRule().substring(0, 1));
                            fpl.put("flightType", mFlightPlanInfo.getFlightType());
                            fpl.put("planPurpose", mFlightPlanInfo.getPlanPurpose());
                            fpl.put("planNumber", mFlightPlanInfo.getPlanNumber());

                            if(((CheckBox) findViewById(R.id.cb_light)).isChecked()){
                                mFlightPlanInfo.setPlanDoccd("01");
                                fpl.put("planDoccd", "01");
                            } else {
                                mFlightPlanInfo.setPlanDoccd("00");
                                fpl.put("planDoccd", "00");
                            }

                            fpl.put("acrftType", mFlightPlanInfo.getAcrftType());
                            fpl.put("wakeTurbcat", mFlightPlanInfo.getWakeTurbcat().substring(0, 1));
                            fpl.put("planEquipment", mFlightPlanInfo.getPlanEquipment());
                            //fpl.put("planDeparture",mFlightPlanInfo.getPlanDeparture());

                            fpl.put("planEtd", ((EditTextEx)findViewById(R.id.et_3_3)).getText().toString());
                            fpl.put("planAtd", mFlightPlanInfo.getPlanAtd());
                            fpl.put("cruisingSpeed", mFlightPlanInfo.getCruisingSpeed());
                            fpl.put("flightLevel", mFlightPlanInfo.getFlightLevel());
                            fpl.put("planRoute", mFlightPlanInfo.getPlanRoute());
                            //fpl.put("planArrival",mFlightPlanInfo.getPlanArrival());
                            fpl.put("planTeet", mFlightPlanInfo.getPlanTeet());

                            String departure = ((TextViewEx) findViewById(R.id.et_3_2)).getText().toString();
                            String arrival = ((TextViewEx) findViewById(R.id.et_6_1)).getText().toString();
                            if(departure!=null && departure.length()>=4){
                                departure = departure.substring(0,4);
                            }
                            if(arrival!=null && arrival.length()>=4){
                                arrival = arrival.substring(0,4);
                            }
                            Log.d("JeLib","getPlanDeparture:"+departure);
                            Log.d("JeLib","getPlanArrival:"+arrival);

                            fpl.put("planDeparture", departure);
                            fpl.put("planArrival", arrival);

                            fpl.put("oneAltn", mFlightPlanInfo.getOneAltn());
                            fpl.put("twoAltn", mFlightPlanInfo.getTwoAltn());
                            fpl.put("otherInfo", mFlightPlanInfo.getOtherInfo());
                            fpl.put("flightPsbtime", mFlightPlanInfo.getFlightPsbtime());
                            fpl.put("flightPerson", mFlightPlanInfo.getFlightPerson());
                            fpl.put("rrUhf", mFlightPlanInfo.getRrUhf());
                            fpl.put("rrVhf", mFlightPlanInfo.getRrVhf());
                            fpl.put("rrElt", mFlightPlanInfo.getRrElt());
                            fpl.put("emgcPolar", mFlightPlanInfo.getEmgcPolar());
                            fpl.put("emgcDesert", mFlightPlanInfo.getEmgcDesert());
                            fpl.put("emgcMaritime", mFlightPlanInfo.getEmgcMaritime());
                            fpl.put("emgcJungle", mFlightPlanInfo.getEmgcJungle());
                            fpl.put("lifejkLight", mFlightPlanInfo.getLifejkLight());
                            fpl.put("lifejkFluores", mFlightPlanInfo.getLifejkFluores());
                            fpl.put("lifejkUhf", mFlightPlanInfo.getLifejkUhf());
                            fpl.put("lifejkVhf", mFlightPlanInfo.getLifejkVhf());
                            fpl.put("lifebtNumber", mFlightPlanInfo.getLifebtNumber());
                            fpl.put("lifebtPerson", mFlightPlanInfo.getLifebtPerson());
                            fpl.put("lifebtCover", mFlightPlanInfo.getLifebtCover());
                            fpl.put("lifebtColor", mFlightPlanInfo.getLifebtColor());
                            fpl.put("acrftColor", mFlightPlanInfo.getAcrftColor());
                            fpl.put("captainPhone", mFlightPlanInfo.getCaptainPhone());
                            fpl.put("callsign", mFlightPlanInfo.getCallsign());
                            fpl.put("planPresent", mFlightPlanInfo.getPlanPresent());
                            fpl.put("messageType", mFlightPlanInfo.getMessageType());
                            //fpl.put("planEta", Integer.parseInt(mFlightPlanInfo.getPlanTeet()) + Integer.parseInt(mFlightPlanInfo.getPlanAtd()));
                            fpl.put("planAreacd", "1234567890");

                            fpl.put("planMbrId", mbrId);
                            params.put("fpl", fpl);

                            try {

                                Calendar planatdCal = null;
                                Calendar planteetCal = null;

                                if (mFlightPlanInfo.getPlanAtd()!=null && !mFlightPlanInfo.getPlanAtd().isEmpty()) {
                                    String planatd = String.format("%04d", Integer.parseInt(mFlightPlanInfo.getPlanAtd()));
                                    planatdCal = DateTimeUtil.toCalendar(planatd, "HHmm");
                                }

                                if (mFlightPlanInfo.getPlanTeet()!=null && !mFlightPlanInfo.getPlanTeet().isEmpty()) {
                                    String planteet = String.format("%04d", Integer.parseInt(mFlightPlanInfo.getPlanTeet()));
                                    planteetCal = DateTimeUtil.toCalendar(planteet, "HHmm");
                                }

                                long atd = 0;
                                long teet = 0;

                                if (planatdCal != null) {
                                    atd = planatdCal.getTimeInMillis();
                                }
                                if (planteetCal != null) {
                                    teet = planteetCal.getTimeInMillis();
                                }

                                Calendar cal = Calendar.getInstance();
                                cal.setTimeInMillis(atd + teet);

                                String planEta = new SimpleDateFormat("HHmm").format(cal.getTime());
                                fpl.put("planEta", planEta);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            // 경로 데이터를 형식에 맞게 저장한다.
                            int cnt = 1;
                            ArrayList routeList = new ArrayList();
                            Map<String, Object> routeMap = new HashMap<String, Object>();
                            for(FlightRouteModel r : route){
                                routeMap = new HashMap<String, Object>();
                                if(r.getrType().equals("s")){
                                    routeMap.put("stepNum", "1");
                                } else if(r.getrType().equals("e")){
                                    routeMap.put("stepNum", ""+route.size());
                                } else {
                                    cnt++;
                                    routeMap.put("stepNum", ""+cnt);
                                }
                                routeMap.put("areaId", r.getAreaId());
                                routeMap.put("areaNm", r.getAreaNm());
                                routeMap.put("lat", r.getLat());
                                routeMap.put("lon", r.getLon());
                                routeMap.put("elev", r.getElev());
                                routeMap.put("heading", r.getHeading());
                                Log.d("JeLib",""+r.getLat()+":::"+r.getLon());
                                routeList.add(routeMap);
                            }

                            /*
                            String[] start = mFlightPlanInfo.getPlanDeparture().split(" ");
                            String[] end = mFlightPlanInfo.getPlanArrival().split(" ");
                            int step = 0;

                            // 출발지
                            if (!mFlightPlanInfo.getPlanDeparture().isEmpty()) {
                                Log.d("JeLib","=============="+start[1]+"  "+start[0]);
                                step += 1;
                                routeMap = new HashMap<String, Object>();
                                routeMap.put("stepNum", step + "");
                                routeMap.put("areaId", "0");
                                routeMap.put("areaNm", "0");
                                routeMap.put("lat", start[1]);
                                routeMap.put("lon", start[0]);
                                routeMap.put("elev", "0");
                                routeMap.put("heading", "0");
                                routeList.add(routeMap);

                            }

                            // 경유지 지정
                            if (!mFlightPlanInfo.getPlanRoute().isEmpty()) {

                                String[] spritStart = mFlightPlanInfo.getPlanRoute().split("\\n");
                                String[] dataRoute;

                                int size = spritStart.length;
                                int i = 0;

                                for (i = 0; i < size; i++) {

                                    step += 1;
                                    dataRoute = spritStart[i].split(" ");
                                    routeMap = new HashMap<String, Object>();
                                    routeMap.put("stepNum", step + "");
                                    routeMap.put("areaId", "0");
                                    routeMap.put("areaNm", "0");
                                    routeMap.put("lat", dataRoute[1]);
                                    routeMap.put("lon", dataRoute[0]);
                                    routeMap.put("elev", "0");
                                    routeMap.put("heading", "0");
                                    routeList.add(routeMap);

                                }


                            }

                            // 목적지
                            if (!mFlightPlanInfo.getPlanArrival().isEmpty()) {

                                step += 1;
                                routeMap = new HashMap<String, Object>();
                                routeMap.put("stepNum", step + "");
                                routeMap.put("areaId", "0");
                                routeMap.put("areaNm", "0");
                                routeMap.put("lat", end[1]);
                                routeMap.put("lon", end[0]);
                                routeMap.put("elev", "0");
                                routeMap.put("heading", "0");
                                routeList.add(routeMap);
                            }
                            */

                            params.put("route", routeList);

                            Call<Map<String, Object>> callback = null;
                            if (mFlightPlanInfo.getMessageType().trim().equals(FlightPlanInfo.TYPE_MESSAGE_CHG)) {
                                RequestBody body = NetUtil.mapToJsonBody(NhsFlightWriteActivity.this, params);
                                callback = service.repoModifyFlightPlan(body);
                            } else if (mFlightPlanInfo.getMessageType().trim().equals(FlightPlanInfo.TYPE_MESSAGE_FPL)) {
                                RequestBody body = NetUtil.mapToJsonBody(NhsFlightWriteActivity.this, params);
                                callback = service.repoRegFlightPlan(body);
                            }

                            callback.enqueue(new retrofit2.Callback<Map<String, Object>>() {
                                @Override
                                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                    loading.dismiss();
                                    Map<String, Object> model = response.body();
                                    Log.d("JeLib", "response.code::" + response.code());
                                    if (response.code() == 200) {

                                        JSONObject resultDataObject = null;

                                        if (response != null) {

                                            String resultCode = (String) model.get("result_code");

                                            if (resultCode.equalsIgnoreCase("Y")) {

                                                String resultData =  (String) model.get("result_data");

                                                try {

                                                    // 복호화한다.
                                                    resultData = new MagicSE_Util(NhsFlightWriteActivity.this).getDecData(resultData);

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
                                            result_code = (String) resultDataObject.optString("result_code");
                                        }
                                        if (result_msg == null) {
                                            result_msg = (String) resultDataObject.optString("result_msg");
                                        }

                                        if (result_code.trim().equals(NetConst.RESPONSE_SUCCESS)) {
                                            if (mFlightPlanInfo.getMessageType().trim().equals(FlightPlanInfo.TYPE_MESSAGE_CHG)) {
                                                new ToastUtile().showCenterText(mContext, "정상적으로 수정되었습니다.");
                                            } else if (mFlightPlanInfo.getMessageType().trim().equals(FlightPlanInfo.TYPE_MESSAGE_FPL)) {

                                                // 임시 저장된 데이터를 등록했다면, 임시저장 데이터를 삭제한다.
                                                if (viewType == VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_TMP) {
                                                    mRealm.executeTransaction(new Realm.Transaction() {
                                                        @Override
                                                        public void execute(Realm realm) {
                                                            RealmResults<FlightPlanInfo> result = realm.where(FlightPlanInfo.class).equalTo("idx", mFlightPlanInfo.getIdx()).findAll();
                                                            result.deleteAllFromRealm();

                                                        }
                                                    });
                                                }

                                                new ToastUtile().showCenterText(mContext, "정상적으로 등록되었습니다.");

                                            }
                                            finish();
                                        } else {
                                            new ToastUtile().showCenterText(mContext, result_msg);
                                            isFailed = false;
                                        }
                                    } else {
                                        Log.d("JeLib", "----------1-----------");
                                        new ToastUtile().showCenterText(mContext, getString(R.string.error_network));
                                    }

                                }

                                @Override
                                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                    loading.dismiss();
                                    new ToastUtile().showCenterText(mContext, getString(R.string.error_network));
                                }
                            });

                        }
                    });


                }

                break;
        }
    }

    /**
     * 비행계획서 제출요청 + 경로정보등록 결과
     *
     * @author FIESTA
     * @since 오전 12:36
     **/
    NetworkProcess.OnResultListener writeFlightResult = new NetworkProcess.OnResultListener() {
        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
            try {

                new ToastUtile().showCenterText(NhsFlightWriteActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

            } catch (Exception ex) {

            } finally {
            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
            try {

                new ToastUtile().showCenterText(NhsFlightWriteActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

            } catch (Exception ex) {

            } finally {
            }
        }

        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

            String msg = response.optString("result_msg");
            String resultCode = response.optString("result_code");

            // 메세지 출력
            new ToastUtile().showCenterText(NhsFlightWriteActivity.this, msg);

            if (resultCode.equalsIgnoreCase("Y")) {

                String planId = response.optString("planId", "");   // 비행계획서ID
                int planSn = response.optInt("planSn", 0);          // 비행계획서 일련번호


            }


        }


    };

    /**
     * 임시 저장한다.
     *
     * @author FIESTA
     * @since 오후 11:08
     **/
    private void saveImsiData() {

        try {

            mRealm.executeTransaction(new Realm.Transaction() {

                @Override
                public void execute(Realm realm) {

                    try {

                        long idx = 0;

                        try {

                            Number currentIdNum = realm.where(FlightPlanInfo.class).max("idx");


                            if (currentIdNum == null) {
                                idx = 1;
                            } else {
                                idx = currentIdNum.intValue() + 1;
                            }

                        }catch (Exception ex){

                        }

                        String currentCallsign = ((EditTextEx) findViewById(R.id.et_1_1)).getText().toString();

                        if (mFlightPlanInfo != null) {

                            mFlightPlanInfo = realm.createObject(FlightPlanInfo.class, idx);
                            mFlightPlanInfo.setCallsign(currentCallsign);
                            getModel();

                        } else {

                            mFlightPlanInfo = realm.createObject(FlightPlanInfo.class, idx);
                            mFlightPlanInfo.setCallsign(currentCallsign);
                            getModel();

                        }


                    } catch (io.realm.exceptions.RealmPrimaryKeyConstraintException keyEx) {

                        new ToastUtile().showCenterText(NhsFlightWriteActivity.this, "Flight Identity 값이 존재합니다.");
                        ((EditTextEx) findViewById(R.id.et_1_1)).requestFocus();
                    } finally {

                        NhsFlightWriteActivity.this.finish();
                        new ToastUtile().showCenterText(mContext, "임시저장 되었습니다.");

                    }
                }
            });

        } catch (io.realm.exceptions.RealmPrimaryKeyConstraintException keyEx) {

            new ToastUtile().showCenterText(NhsFlightWriteActivity.this, keyEx.getMessage());

        } finally {

        }
    }

    /**
     * 화면에 있는 데이터를 가져온다.
     *
     * @author FIESTA
     * @since 오전 12:47
     **/
    private FlightPlanInfo getModel() {

        if (mFlightPlanInfo == null) {

            if (mFlightPlanInfo == null) {
                mFlightPlanInfo = new FlightPlanInfo();
            }
            mFlightPlanInfo.setMessageType(FlightPlanInfo.TYPE_MESSAGE_FPL);
            mFlightPlanInfo.setCallsign(((EditTextEx) findViewById(R.id.et_1_1)).getText().toString());
        } else {
            if(isFailed)
            {
                /**
                 * isFailed 값을 확인하여 재전송 실패가 아닐 때 동작하도록 한다
                 */
                // 임시저장 데이터라면 새로 업로드한다.
                if (this.viewType == VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_TMP) {
                    mFlightPlanInfo.setMessageType(FlightPlanInfo.TYPE_MESSAGE_FPL);
                    mFlightPlanInfo.setCallsign(((EditTextEx) findViewById(R.id.et_1_1)).getText().toString());
                } else {
                    if (this.isEditMode) {
                        mFlightPlanInfo.setMessageType(FlightPlanInfo.TYPE_MESSAGE_CHG);
                    } else {
                        mFlightPlanInfo.setMessageType(FlightPlanInfo.TYPE_MESSAGE_FPL);
                        try {
                            mFlightPlanInfo.setCallsign(((EditTextEx) findViewById(R.id.et_1_1)).getText().toString());
                        }catch (Exception ex) {

                        }
                    }
                }
            }
        }

        String acrftCd = StorageUtil.getStorageModeEx(mContext, LOGIN_ACRFTCD);
        mFlightPlanInfo.setAcrftCd(acrftCd);

        //NhsFlightPlainModel model = new NhsFlightPlainModel();
        //FlightPlanInfo model = new FlightPlanInfo();
        //mFlightPlanInfo.setCallsign();
        mFlightPlanInfo.setPlanPriority("FF");
        mFlightPlanInfo.setFlightRule(((EditTextEx) findViewById(R.id.et_1_2)).getText().toString());
        mFlightPlanInfo.setFlightType("" + spinner1.getSelectedItemPosition());

        mFlightPlanInfo.setPlanNumber(((EditTextEx) findViewById(R.id.et_2_1)).getText().toString());
        mFlightPlanInfo.setAcrftType(((EditTextEx) findViewById(R.id.et_2_2)).getText().toString());
        mFlightPlanInfo.setWakeTurbcat(((EditTextEx) findViewById(R.id.et_2_3)).getText().toString());

        mFlightPlanInfo.setPlanEquipment(((EditTextEx) findViewById(R.id.et_3_1)).getText().toString());

        String departureCd = ((TextViewEx) findViewById(R.id.et_3_2)).getText().toString();
        if(route!=null && route.size()>0){
            departureCd = String.format("%s %s %s",departureCd,route.get(0).getLon(),route.get(0).getLat());
        }
        mFlightPlanInfo.setPlanDeparture(departureCd);
        //mFlightPlanInfo.setPlanAtd(((EditTextEx) findViewById(R.id.et_3_3)).getText().toString());
        mFlightPlanInfo.setPlanEtd(((EditTextEx) findViewById(R.id.et_3_3)).getText().toString());

        mFlightPlanInfo.setCruisingSpeed(((EditTextEx) findViewById(R.id.et_4_1)).getText().toString());
        mFlightPlanInfo.setFlightLevel(((EditTextEx) findViewById(R.id.et_4_2)).getText().toString());
        mFlightPlanInfo.setPlanPurpose("" + spinner2.getSelectedItemPosition());

        mFlightPlanInfo.setPlanRoute(((EditText) findViewById(R.id.ed_route)).getText().toString());
        String arrivalCd = ((TextViewEx) findViewById(R.id.et_6_1)).getText().toString();
        if(route!=null && route.size()>1){
            arrivalCd = String.format("%s %s %s",arrivalCd,route.get(route.size()-1).getLon(),route.get(route.size()-1).getLat());
        }
        mFlightPlanInfo.setPlanArrival(arrivalCd);
        mFlightPlanInfo.setPlanTeet(((EditTextEx) findViewById(R.id.et_6_2)).getText().toString());

        mFlightPlanInfo.setOneAltn(((EditTextEx) findViewById(R.id.et_7_1)).getText().toString());
        mFlightPlanInfo.setTwoAltn(((EditTextEx) findViewById(R.id.et_7_2)).getText().toString());

        mFlightPlanInfo.setOtherInfo(((EditTextEx) findViewById(R.id.et_8_1)).getText().toString());
        mFlightPlanInfo.setFlightPsbtime(((EditTextEx) findViewById(R.id.et_9_1)).getText().toString());
        mFlightPlanInfo.setFlightPerson(((EditTextEx) findViewById(R.id.et_9_2)).getText().toString());

        /////////////////////////////R
        if (((CheckBox) findViewById(R.id.rb_10_1)).isChecked()) {
            mFlightPlanInfo.setRrUhf("Y");
        } else {
            mFlightPlanInfo.setRrUhf("N");
        }

        if (((CheckBox) findViewById(R.id.rb_10_2)).isChecked()) {
            mFlightPlanInfo.setRrVhf("Y");
        } else {
            mFlightPlanInfo.setRrVhf("N");
        }

        if (((CheckBox) findViewById(R.id.rb_10_3)).isChecked()) {
            mFlightPlanInfo.setRrElt("Y");
        } else {
            mFlightPlanInfo.setRrElt("N");
        }

        /////////////////////////////S
        if (((CheckBox) findViewById(R.id.rb_11_1)).isChecked()) {
            mFlightPlanInfo.setEmgcPolar("Y");
        } else {
            mFlightPlanInfo.setEmgcPolar("N");
        }

        if (((CheckBox) findViewById(R.id.rb_11_2)).isChecked()) {
            mFlightPlanInfo.setEmgcDesert("Y");
        } else {
            mFlightPlanInfo.setEmgcDesert("N");
        }

        if (((CheckBox) findViewById(R.id.rb_11_3)).isChecked()) {
            mFlightPlanInfo.setEmgcMaritime("Y");
        } else {
            mFlightPlanInfo.setEmgcMaritime("N");
        }

        if (((CheckBox) findViewById(R.id.rb_11_4)).isChecked()) {
            mFlightPlanInfo.setEmgcJungle("Y");
        } else {
            mFlightPlanInfo.setEmgcJungle("N");
        }

        /////////////////////////////J
        if (((CheckBox) findViewById(R.id.rb_12_1)).isChecked()) {
            mFlightPlanInfo.setLifejkLight("Y");
        } else {
            mFlightPlanInfo.setLifejkLight("N");
        }

        if (((CheckBox) findViewById(R.id.rb_12_2)).isChecked()) {
            mFlightPlanInfo.setLifejkFluores("Y");
        } else {
            mFlightPlanInfo.setLifejkFluores("N");
        }

        if (((CheckBox) findViewById(R.id.rb_12_3)).isChecked()) {
            mFlightPlanInfo.setLifejkUhf("Y");
        } else {
            mFlightPlanInfo.setLifejkUhf("N");
        }

        if (((CheckBox) findViewById(R.id.rb_12_4)).isChecked()) {
            mFlightPlanInfo.setLifejkVhf("Y");
        } else {
            mFlightPlanInfo.setLifejkVhf("N");
        }

        mFlightPlanInfo.setLifebtNumber(((EditTextEx) findViewById(R.id.et_13_1)).getText().toString());
        mFlightPlanInfo.setLifebtPerson(((EditTextEx) findViewById(R.id.et_13_2)).getText().toString());
        mFlightPlanInfo.setLifebtCover(((EditTextEx) findViewById(R.id.et_13_3)).getText().toString());
        mFlightPlanInfo.setLifebtColor(((EditTextEx) findViewById(R.id.et_13_4)).getText().toString());

        mFlightPlanInfo.setAcrftColor(((EditTextEx) findViewById(R.id.et_14_1)).getText().toString());
        mFlightPlanInfo.setCaptainPhone(((EditTextEx) findViewById(R.id.et_14_2)).getText().toString());
        mFlightPlanInfo.setPlanPresent(((EditTextEx) findViewById(R.id.et_14_3)).getText().toString());

        if (((CheckBox) findViewById(R.id.cb_light)).isChecked()) {
            mFlightPlanInfo.setPlanDoccd("01");
        } else {
            mFlightPlanInfo.setPlanDoccd("00");
        }

        //mFlightPlanInfo.setPlanDeparture(this.planDeparture);
        //mFlightPlanInfo.setPlanArrival(this.planArrival);


    /*
    List<NhsFlightPlainModel> allList = NhsFlightPlainModel.findAll();
    int size = allList.size();

    if ((size % 2) == 0) {
      model.setFlightStatus(NhsFlightPlainModel.FLIGT_STATUS_APPROVED);
    } else {
      model.setFlightStatus(NhsFlightPlainModel.FLIGT_STATUS_DENIED);
    }
    */
        mFlightPlanInfo.setCreateAt(Calendar.getInstance().getTimeInMillis());
        return mFlightPlanInfo;

    }


    /**
     * 데이터 유효성을 체크한다
     *
     * @author FIESTA
     * @since 오후 11:12
     **/
    private boolean setCheckSumData() {

//    initTextLabel();

        if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_1_1)).getText().toString())) {
            Toast.makeText(this, "Flight Identity를 입력하세요.", Toast.LENGTH_SHORT).show();
            ((EditTextEx) findViewById(R.id.et_1_1)).requestFocus();
            ((TextViewEx) findViewById(R.id.tve_1_1_e)).setVisibility(View.VISIBLE);
            return false;
        }

        if (((EditTextEx) findViewById(R.id.et_1_1)).getText().length() <= 3) {
            Toast.makeText(this, "Flight Identity를 4자리 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
            ((EditTextEx) findViewById(R.id.et_1_1)).requestFocus();
        }

        if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_1_2)).getText().toString())) {
            Toast.makeText(this, "Flight Rules를 입력하세요.", Toast.LENGTH_SHORT).show();
            ((TextViewEx) findViewById(R.id.tve_1_2_e)).setVisibility(View.VISIBLE);
            ((EditTextEx) findViewById(R.id.et_1_2)).requestFocus();
            return false;
        }
        if (spinner1.getSelectedItem().toString().equals("선택하세요")) {
            Toast.makeText(this, "Flight Type을 선택하세요.", Toast.LENGTH_SHORT).show();
            ((TextViewEx) findViewById(R.id.tve_spinner1_e)).setVisibility(View.VISIBLE);
            spinner1.setFocusableInTouchMode(true);
            spinner1.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_2_1)).getText().toString())) {
            Toast.makeText(this, "Number를 입력하세요.", Toast.LENGTH_SHORT).show();
            ((TextViewEx) findViewById(R.id.tve_2_1_e)).setVisibility(View.VISIBLE);
            ((EditTextEx) findViewById(R.id.et_2_1)).requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_2_2)).getText().toString())) {
            Toast.makeText(this, "Aircraft Type를 입력하세요.", Toast.LENGTH_SHORT).show();
            ((TextViewEx) findViewById(R.id.tve_2_2_e)).setVisibility(View.VISIBLE);
            ((EditTextEx) findViewById(R.id.et_2_2)).requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_2_3)).getText().toString())) {
            Toast.makeText(this, "Wake Turb. Cat.를 입력하세요.", Toast.LENGTH_SHORT).show();
            ((TextViewEx) findViewById(R.id.tve_2_3_e)).setVisibility(View.VISIBLE);
            ((EditTextEx) findViewById(R.id.et_2_3)).requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_3_1)).getText().toString())) {
            Toast.makeText(this, "Equipment를 입력하세요.", Toast.LENGTH_SHORT).show();
            ((TextViewEx) findViewById(R.id.tve_3_1_e)).setVisibility(View.VISIBLE);
            ((EditTextEx) findViewById(R.id.et_3_1)).requestFocus();
            return false;
        }
        String departure = "";
        if(mFlightPlanInfo!=null) {
            departure = Util.NullString(mFlightPlanInfo.getPlanDeparture(), "");
        }
        if (TextUtils.isEmpty(((TextViewEx) findViewById(R.id.et_3_2)).getText().toString()) && departure.length() <= 0) {
            Toast.makeText(this, "Departure Aerodrome를 입력하세요.", Toast.LENGTH_SHORT).show();
            ((TextViewEx) findViewById(R.id.tve_3_2_e)).setVisibility(View.VISIBLE);
            ((TextViewEx) findViewById(R.id.et_3_2)).setFocusableInTouchMode(true);
            ((TextViewEx) findViewById(R.id.et_3_2)).requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_3_3)).getText().toString())) {
            Toast.makeText(this, "Time을 입력하세요.", Toast.LENGTH_SHORT).show();
            ((TextViewEx) findViewById(R.id.tve_3_3_e)).setVisibility(View.VISIBLE);
            ((EditTextEx) findViewById(R.id.et_3_3)).requestFocus();
            return false;
        }

        if (((EditTextEx) findViewById(R.id.et_3_3)).getText().toString().length() <= 3) {
            Toast.makeText(this, "Time을 4자리로 입력하세요.", Toast.LENGTH_SHORT).show();
            ((TextViewEx) findViewById(R.id.tve_3_3_e)).setVisibility(View.VISIBLE);
            ((EditTextEx) findViewById(R.id.et_3_3)).requestFocus();
            return false;
        }
        String arrival = "";
        if(mFlightPlanInfo!=null) {
            arrival = Util.NullString(mFlightPlanInfo.getPlanArrival(),"");
        }
        if (TextUtils.isEmpty(((TextViewEx) findViewById(R.id.et_6_1)).getText().toString()) && arrival.length() <= 0) {
            Toast.makeText(this, "Arrival Aerodrome를 입력하세요.", Toast.LENGTH_SHORT).show();
            ((TextViewEx) findViewById(R.id.tve_6_1_e)).setVisibility(View.VISIBLE);
            ((TextViewEx) findViewById(R.id.et_6_1)).setFocusableInTouchMode(true);
            ((TextViewEx) findViewById(R.id.et_6_1)).requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_4_2)).getText().toString())) {
            Toast.makeText(this, "Flight level를 입력하세요.", Toast.LENGTH_SHORT).show();
            ((TextViewEx) findViewById(R.id.tve_4_2_e)).setVisibility(View.VISIBLE);
            ((EditTextEx) findViewById(R.id.et_4_2)).requestFocus();
            return false;
        }
        if (spinner2.getSelectedItem().toString().equals("선택하세요")) {
            Toast.makeText(this, "Propose을 선택하세요.", Toast.LENGTH_SHORT).show();
            ((TextViewEx) findViewById(R.id.tve_spinner_2)).setVisibility(View.VISIBLE);
            spinner2.setFocusableInTouchMode(true);
            spinner2.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(((EditText) findViewById(R.id.ed_route)).getText().toString())) {
            Toast.makeText(this, "Route를 입력하세요.", Toast.LENGTH_SHORT).show();
            ((TextViewEx) findViewById(R.id.tve_route)).setVisibility(View.VISIBLE);
            ((TextViewEx) findViewById(R.id.tve_route)).setFocusableInTouchMode(true);
            ((TextViewEx) findViewById(R.id.tve_route)).requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_4_1)).getText().toString())) {
            Toast.makeText(this, "Crusing Speed를 입력하세요.", Toast.LENGTH_SHORT).show();
            ((TextViewEx) findViewById(R.id.tve_4_1_e)).setVisibility(View.VISIBLE);
            ((EditTextEx) findViewById(R.id.et_4_1)).requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_6_2)).getText().toString())) {
            Toast.makeText(this, "Total EET를 입력하세요.", Toast.LENGTH_SHORT).show();
            ((TextViewEx) findViewById(R.id.tve_6_2_e)).setVisibility(View.VISIBLE);
            ((EditTextEx) findViewById(R.id.et_6_2)).requestFocus();
            return false;
        }


        /**
         if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_7_1)).getText().toString())) {
         Toast.makeText(this, "1st ANTN AeroDrome를 입력하세요.", Toast.LENGTH_SHORT).show();
         return false;
         }
         if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_7_2)).getText().toString())) {
         Toast.makeText(this, "2nd ALTN AeroDrome를 입력하세요.", Toast.LENGTH_SHORT).show();
         return false;
         }
         if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_8_1)).getText().toString())) {
         Toast.makeText(this, "Other Information를 입력하세요.", Toast.LENGTH_SHORT).show();
         return false;
         }
         if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_9_1)).getText().toString())) {
         Toast.makeText(this, "E를 입력하세요.", Toast.LENGTH_SHORT).show();
         return false;
         }
         if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_9_2)).getText().toString())) {
         Toast.makeText(this, "P를 입력하세요.", Toast.LENGTH_SHORT).show();
         return false;
         }

         if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_13_1)).getText().toString())) {
         Toast.makeText(this, "D - Capacity를 입력하세요.", Toast.LENGTH_SHORT).show();
         return false;
         }
         if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_13_2)).getText().toString())) {
         Toast.makeText(this, "D - Numbers를 입력하세요.", Toast.LENGTH_SHORT).show();
         return false;
         }
         if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_13_3)).getText().toString())) {
         Toast.makeText(this, "D - Cover를 입력하세요.", Toast.LENGTH_SHORT).show();
         return false;
         }
         if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_13_4)).getText().toString())) {
         Toast.makeText(this, "D - Color를 입력하세요.", Toast.LENGTH_SHORT).show();
         return false;
         }
         if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_14_1)).getText().toString())) {
         Toast.makeText(this, "A를 입력하세요.", Toast.LENGTH_SHORT).show();
         return false;
         }
         if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_14_2)).getText().toString())) {
         Toast.makeText(this, "N를 입력하세요.", Toast.LENGTH_SHORT).show();
         return false;
         }
         if (TextUtils.isEmpty(((EditTextEx) findViewById(R.id.et_14_3)).getText().toString())) {
         Toast.makeText(this, "C를 입력하세요.", Toast.LENGTH_SHORT).show();
         return false;
         }
         **/
        return true;
//        saveData(NhsFlightPlainModel.FindType.ALL.ordinal());
//        Log.d("##", NhsFlightPlainModel.findAll().size() + "");


    }

    /**
     * 레벨 제목의 라벨을 초기화한다.
     * (강조 표시를 삭제한다)
     *
     * @author FIESTA
     * @since 오전 12:15
     **/
    private void initTextLabel() {

        ((TextViewEx) findViewById(R.id.tve_1_1_e)).setVisibility(View.GONE);
        ((TextViewEx) findViewById(R.id.tve_1_2_e)).setVisibility(View.GONE);
        ((TextViewEx) findViewById(R.id.tve_spinner1_e)).setVisibility(View.GONE);
        ((TextViewEx) findViewById(R.id.tve_2_1_e)).setVisibility(View.GONE);
        ((TextViewEx) findViewById(R.id.tve_2_2_e)).setVisibility(View.GONE);
        ((TextViewEx) findViewById(R.id.tve_2_3_e)).setVisibility(View.GONE);
        ((TextViewEx) findViewById(R.id.tve_3_1_e)).setVisibility(View.GONE);
        ((TextViewEx) findViewById(R.id.tve_3_2_e)).setVisibility(View.GONE);
        ((TextViewEx) findViewById(R.id.tve_3_3_e)).setVisibility(View.GONE);
        ((TextViewEx) findViewById(R.id.tve_4_1_e)).setVisibility(View.GONE);
        ((TextViewEx) findViewById(R.id.tve_4_2_e)).setVisibility(View.GONE);
        ((TextViewEx) findViewById(R.id.tve_spinner1_e)).setVisibility(View.GONE);
        ((TextViewEx) findViewById(R.id.tve_route)).setVisibility(View.GONE);
        ((TextViewEx) findViewById(R.id.tve_6_1_e)).setVisibility(View.GONE);
        ((TextViewEx) findViewById(R.id.tve_6_2_e)).setVisibility(View.GONE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if(route == null)route = new ArrayList<FlightRouteModel>();

            if (requestCode == NhsSelectPointActivity.MODE_DEPARTURE) { // 출발 검색 결과
                String planDeparture = data.getExtras().getString(DATA_START);
                String[] start = planDeparture.split(" ");
                boolean isAdd = true;
                if(route.size() > 0) {
                    for (FlightRouteModel r : route) {
                        if(r.getrType().equals("s")){
                            r.setAreaId("0");
                            r.setAreaNm("0");
                            r.setLat(start[1]);
                            r.setLon(start[0]);
                            r.setElev("0");
                            r.setHeading("0");
                            r.setStepNum("1");
                            isAdd = false;
                            break;
                        }
                    }
                }

                if(isAdd){
                    FlightRouteModel m = new FlightRouteModel();
                    m.setAreaId("0");
                    m.setAreaNm("0");
                    m.setLat(start[1]);
                    m.setLon(start[0]);
                    m.setElev("0");
                    m.setHeading("0");
                    m.setStepNum("1");
                    route.add(m);
                }

                StringBuffer strBuffer = new StringBuffer();
                lanGetPortCodeName(Double.parseDouble(start[0]), Double.parseDouble(start[1]), strBuffer);
                String apCd = strBuffer.toString().split("@@")[0];
                ((TextViewEx) findViewById(R.id.et_3_2)).setText(apCd);

            } else if (requestCode == NhsSelectPointActivity.MODE_ARRIVAL) {  // 도착지 검색 결과

                String planArrival = data.getExtras().getString(DATA_END);
                String[] start = planArrival.split(" ");
                boolean isAdd = true;
                if(route.size() > 0) {
                    for (FlightRouteModel r : route) {
                        if(r.getrType().equals("e")){
                            r.setAreaId("0");
                            r.setAreaNm("0");
                            r.setLat(start[1]);
                            r.setLon(start[0]);
                            r.setElev("0");
                            r.setHeading("0");
                            r.setStepNum("1");
                            isAdd = false;
                            break;
                        }
                    }
                }

                if(isAdd){
                    FlightRouteModel m = new FlightRouteModel();
                    m.setAreaId("0");
                    m.setAreaNm("0");
                    m.setLat(start[1]);
                    m.setLon(start[0]);
                    m.setElev("0");
                    m.setHeading("0");
                    m.setStepNum("1");
                    route.add(m);
                }

                StringBuffer strBuffer = new StringBuffer();
                lanGetPortCodeName(Double.parseDouble(start[0]), Double.parseDouble(start[1]), strBuffer);
                String apCd = strBuffer.toString().split("@@")[0];
                ((TextViewEx) findViewById(R.id.et_6_1)).setText(apCd);

            } else if (requestCode == NhsSelectPointActivity.MODE_ROUTE) {  // 경유지 검색 결과

                String dataRoute = data.getExtras().getString(DATA_ROUTE);

                String[] spritStart = dataRoute.split("\\n");
                String[] routeArr = null;

                int size = spritStart.length;
                int i = 0;

                ArrayList<FlightRouteModel> deletes = new ArrayList<FlightRouteModel>();
                for (i = 0; i < route.size(); i++) {
                    FlightRouteModel fm = route.get(i);
                    if(fm.getrType().equals("w")){
                        deletes.add(fm);
                    }
                }
                for (FlightRouteModel d : deletes) {
                    route.remove(d);
                }

                for (i = 0; i < size; i++) {
                    routeArr = spritStart[i].split(" ");
                    FlightRouteModel m = new FlightRouteModel();
                    m.setAreaId("0");
                    m.setAreaNm("0");
                    m.setLat(routeArr[1]);
                    m.setLon(routeArr[0]);
                    m.setElev("0");
                    m.setHeading("0");
                    m.setStepNum("1");
                    route.add(m);
                }

                ((EditText) findViewById(R.id.ed_route)).setText(data.getExtras().getString(DATA_ROUTE));

            }
        } else if (resultCode == RESULT_CANCELED){

//            planDeparture = "";
//            planArrival = "";
//            ((TextViewEx) findViewById(R.id.et_3_2)).setText("");
//            ((TextViewEx) findViewById(R.id.et_6_1)).setText("");
//            ((EditText) findViewById(R.id.ed_route)).setText("");

        }
    }
}
