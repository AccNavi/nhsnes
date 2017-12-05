package kr.go.molit.nhsnes.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.modim.lan.lanandroid.NativeImplement;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.adapter.RecyclerMainNhsFlightPlanListAdapter;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.model.NhsFlightHistoryModel;
import kr.go.molit.nhsnes.model.NhsFlightPlainModel;
import kr.go.molit.nhsnes.model.NhsFlightPlanListModel;
import kr.go.molit.nhsnes.net.realm.FlightPlanInfo;
import kr.go.molit.nhsnes.widget.EditTextEx;
import kr.go.molit.nhsnes.widget.TextViewEx;

import static kr.go.molit.nhsnes.adapter.RecyclerMainNhsFlightPlanListAdapter.VIEWTYPE_NHS_FLIGHT_HISTORY_ACT;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class NhsFlightHistoryActivity extends NhsBaseFragmentActivity implements View.OnClickListener {
    RecyclerView rvList;
    EditTextEx etSearch;
    List<FlightPlanInfo> pushList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_nhs_flight_history);
        setLayout();
        loadDataList();

    }


    /**
     * 데이터 리스트 출력
     *
     * @author FIESTA
     * @since 오전 12:49
     **/
    private void loadDataList() {

        pushList.clear();
        final List<NhsFlightHistoryModel> list = NhsFlightHistoryModel.findAll();

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
            flightPlanInfo.setIdx(model.getIdx());
            flightPlanInfo.setGpsLogDate(model.getGpsLogDate());
            flightPlanInfo.setPlanSn(model.getPlanSn());
            flightPlanInfo.setFlightId(model.getFlightId());
            flightPlanInfo.setStartDate(model.getStartDate());
            flightPlanInfo.setTotalDistanc(model.getTotalDistanc());
            flightPlanInfo.setAvgSpeed(model.getAvgSpeed());
            flightPlanInfo.setAvgAltitude(model.getAvgAltitude());
            flightPlanInfo.setStartTime(model.getStartTime());
            flightPlanInfo.setEndTime(model.getEndTime());
            flightPlanInfo.setTotalFlightTime(model.getTotalFlightTime());
            flightPlanInfo.setPlanDoccd(model.getPlanDoccd());

            pushList.add(flightPlanInfo);
        }

        // callsign + " " + result.getAcrftType() + " " + result.getPlanDeparture() + " " + result.getPlanArrival() + " (" + result.getPlanDate() + ")


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RecyclerMainNhsFlightPlanListAdapter adapter = new RecyclerMainNhsFlightPlanListAdapter(pushList, VIEWTYPE_NHS_FLIGHT_HISTORY_ACT, NhsFlightHistoryActivity.this);
                rvList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                ((TextViewEx) findViewById(R.id.list_number)).setText(list.size() + "");
            }
        });

    }


    private void setLayout() {
        rvList = (RecyclerView) findViewById(R.id.rv_plan_list);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        etSearch = (EditTextEx) findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                List<NhsFlightHistoryModel> list = NhsFlightHistoryModel.findCodeWithDate(s.toString());
                List<FlightPlanInfo> pushList = new ArrayList<>();

                for (NhsFlightHistoryModel model : list) {
                    Log.d("JeLib", "call:" + model.getCallsign());
                    Log.d("JeLib", "ac:" + model.getAcrftCd());
                    Log.d("JeLib", "getPlanDate:" + model.getPlanDate());

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
                    flightPlanInfo.setPlanDeparture(model.getPlanDeparture());
                    flightPlanInfo.setPlanSn(model.getPlanSn());
                    flightPlanInfo.setPlanDoccd(model.getPlanDoccd());
                    pushList.add(flightPlanInfo);
                }
                rvList.setAdapter(new RecyclerMainNhsFlightPlanListAdapter(pushList, VIEWTYPE_NHS_FLIGHT_HISTORY_ACT, getContext()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 삭제
        this.findViewById(R.id.bt_delete).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            // 삭제하기
            case R.id.bt_delete:

                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        RecyclerMainNhsFlightPlanListAdapter adapter = (RecyclerMainNhsFlightPlanListAdapter) rvList.getAdapter();

                        SparseArray<FlightPlanInfo> selectedItemIds = adapter.getSelectedItemIds();
                        int i = 0;
                        int size = selectedItemIds.size();
                        RealmQuery<NhsFlightHistoryModel> result = realm.where(NhsFlightHistoryModel.class);

                        for (i = 0; i < size; i++) {

                            final int key = selectedItemIds.keyAt(i);
                            result.equalTo("idx", selectedItemIds.get(key).getIdx());

                            if (i < size-1) {
                                result.or();
                            }
                        }

                        RealmResults<NhsFlightHistoryModel> findData = result.findAll();

                        size = findData.size();

                        for (i=0; i<size; i++) {

                            try {
                                // trk 파일 삭제
                                SimpleDateFormat sif = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                                String fileName = sif.format(new Date(selectedItemIds.get(i).getGpsLogDate()));
                                File file = new File(NativeImplement.GPS_LOG_DATA_PATH + "TRK_" + fileName + ".trk");

                                // 파일이 있으면 파일 삭제
                                if (file.exists()) {
                                    file.delete();
                                }
                            }catch (Exception ex) {

                            }

                        }

                        if (findData!=null) {
                            findData.deleteAllFromRealm();
                        }

                        loadDataList();

                    }
                });
        }


    }
}
