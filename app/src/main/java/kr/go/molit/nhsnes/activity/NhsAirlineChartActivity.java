package kr.go.molit.nhsnes.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

import kr.go.molit.nhsnes.Network.NetworkProcessWithFile;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.adapter.RecyclerNhsFlightInfoListAdapter;
import kr.go.molit.nhsnes.common.MagicSE_Util;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.model.NhsFlightInfoModel;
import kr.go.molit.nhsnes.net.model.AirChartListItemModel;
import kr.go.molit.nhsnes.net.model.AirChartListModel;
import kr.go.molit.nhsnes.net.model.FlightPlanModel;
import kr.go.molit.nhsnes.net.model.NetSecurityModel;
import kr.go.molit.nhsnes.net.service.FlightInfoService;
import kr.go.molit.nhsnes.net.service.NetConst;
import kr.go.molit.nhsnes.net.service.NetUtil;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kr.go.molit.nhsnes.activity.NhsAipInfoActivity.CALL_CALSS_NAME;
import static kr.go.molit.nhsnes.adapter.RecyclerNhsFlightInfoListAdapter.INTENT_ID;
import static kr.go.molit.nhsnes.adapter.RecyclerNhsFlightInfoListAdapter.VIEWTYPE_TITLE_DATE_DOWNLOAD_DELETE;
import static kr.go.molit.nhsnes.adapter.RecyclerNhsFlightInfoListAdapter.VIEWTYPE_TITLE_DOWNLOAD;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class NhsAirlineChartActivity extends NhsBaseFragmentActivity {
    private RecyclerView rvList;
    private String infoId;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_nhs_airline_chart);
        if (getIntent().getStringExtra(INTENT_ID) != null) {
            infoId = getIntent().getStringExtra(INTENT_ID);
        }
        setLayout();
//        setRecyclerView();
        getData();

    }

    /**
    * 데이터를 가져온다.
    * @author FIESTA
    * @version 1.0.0
    * @since 2017-09-01 오후 10:17
    **/
    private void getData(){

        FlightInfoService service = FlightInfoService.retrofit.create(FlightInfoService.class);
        RequestBody body = NetUtil.mapToJsonBody(this, null);
        // AirChartListModel
        Call<NetSecurityModel> callback = service.searchAirChartList(body);
        callback.enqueue(new Callback<NetSecurityModel>() {
            @Override
            public void onResponse(Call<NetSecurityModel> call, Response<NetSecurityModel> response) {


                if (response.code() == 200) {

                    NetSecurityModel netSecurityModel = response.body();

                    if (netSecurityModel.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {

                        String dec = new MagicSE_Util(getContext()).getDecData(netSecurityModel.getResult_data());

                        AirChartListModel model = new Gson().fromJson(dec, AirChartListModel.class);

                        if (model.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {

                            ArrayList<AirChartListItemModel> netDataList = model.getResult_data();

                            int i = 0;
                            int size = netDataList.size();
                            NhsFlightInfoModel nhsFlightInfoModel = null;
                            AirChartListItemModel airChartListItemModel = null;

                            rvList.setLayoutManager(new LinearLayoutManager(NhsAirlineChartActivity.this));
                            RecyclerNhsFlightInfoListAdapter adapter = new RecyclerNhsFlightInfoListAdapter(NhsAirlineChartActivity.this, VIEWTYPE_TITLE_DATE_DOWNLOAD_DELETE);

                            for (i = 0; i < size; i++) {
                                airChartListItemModel = netDataList.get(i);

                                // AD 아닌것만 조회한다.
                                if (!airChartListItemModel.getAIRCHART_GB().equalsIgnoreCase("AD")) {

                                    nhsFlightInfoModel = new NhsFlightInfoModel();
                                    nhsFlightInfoModel.setAirChartListItemModel(airChartListItemModel);
                                    nhsFlightInfoModel.setSavePath(Environment.getExternalStorageDirectory() + "/ACC_NAVI/Flight_Char/");
                                    adapter.addData(nhsFlightInfoModel);

                                }
                            }

                            rvList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            adapter.setOnFileResultListener(new NetworkProcessWithFile.OnResultListener() {
                                @Override
                                public void onFailure() {
                                    new ToastUtile().showCenterText(NhsAirlineChartActivity.this, "다운로드가 실패했습니다.");
                                }

                                @Override
                                public void onSuccess(File file) {
                                    new ToastUtile().showCenterText(NhsAirlineChartActivity.this, "다운로드 완료");
                                    rvList.getAdapter().notifyDataSetChanged();
//                                Intent intent = new Intent(NhsAirlineChartActivity.this, NhsAipInfoActivity.class);
//                                intent.putExtra(CALL_CALSS_NAME, NhsAirlineChartActivity.class.getName());
//                                startActivity(intent);
                                }

                                @Override
                                public void onStart(String fileName) {

                                }
                            });

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

    /**
    private void setRecyclerView() {
        List<NhsFlightInfoModel> list = new ArrayList<>();


        if (infoId != null) {
            list.add(new NhsFlightInfoModel("ATS ROUTE_8", "12월 07일 18:10", "1"));
            list.add(new NhsFlightInfoModel("ATS ROUTE_7", "12월 07일 18:10", "2"));
            list.add(new NhsFlightInfoModel("ATS ROUTE_6", "12월 07일 18:10", "3"));
            list.add(new NhsFlightInfoModel("ATS ROUTE_5", "12월 07일 18:10", "4"));
            list.add(new NhsFlightInfoModel("ATS ROUTE_4", "12월 07일 18:10", "5"));
            list.add(new NhsFlightInfoModel("ATS ROUTE_3", "12월 07일 18:10", "6"));
            list.add(new NhsFlightInfoModel("ATS ROUTE_2", "12월 07일 18:10", "7"));
            list.add(new NhsFlightInfoModel("ATS ROUTE_1", "12월 07일 18:10", "8"));


            rvList.setAdapter(new RecyclerNhsFlightInfoListAdapter(list, this, RecyclerNhsFlightInfoListAdapter.VIEWTYPE_TITLE_DATE_DELETE));
        } else {
            list.add(new NhsFlightInfoModel("ATS ROUTE", "1"));
            list.add(new NhsFlightInfoModel("FIR", "2"));
            list.add(new NhsFlightInfoModel("TMA", "3"));
            list.add(new NhsFlightInfoModel("P73 시계비행로", "4"));
            list.add(new NhsFlightInfoModel("한강 회랑", "5"));
            list.add(new NhsFlightInfoModel("수색비행장 공항교통구역", "6"));
            rvList.setAdapter(new RecyclerNhsFlightInfoListAdapter(list, this, RecyclerNhsFlightInfoListAdapter.VIEWTYPE_TITLE_DOWNLOAD));
        }
        rvList.setLayoutManager(new LinearLayoutManager(this));
    }
**/
    private void setLayout() {
        rvList = (RecyclerView) findViewById(R.id.rv_info_list);
    }
}
