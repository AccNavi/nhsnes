package kr.go.molit.nhsnes.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import kr.go.molit.nhsnes.Network.NetworkProcessWithFile;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.adapter.RecyclerNhsFlightInfoListAdapter;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.model.NhsFlightWeatherMetarModel;
import kr.go.molit.nhsnes.net.model.AirChartListItemModel;
import kr.go.molit.nhsnes.net.model.AirChartListModel;
import kr.go.molit.nhsnes.net.service.FlightInfoService;
import kr.go.molit.nhsnes.net.service.NetConst;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kr.go.molit.nhsnes.adapter.RecyclerNhsFlightInfoListAdapter.VIEWTYPE_TITLE_DATE_DOWNLOAD_DELETE;

/**
* Airline weather 화면
* @author FIESTA
* @version 1.0.0
* @since 오후 4:47
**/
public class NhsAirlineWeatherActivity extends NhsBaseFragmentActivity {

    private RecyclerView rvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_nhs_weather);

        setLayout();
        getWeaherData();
    }


    /**
    * 레이아웃을 설정한다.
    * @author FIESTA
    * @version 1.0.0
    * @since 오후 4:47
    **/
    private void setLayout() {
        rvList = (RecyclerView) findViewById(R.id.rv_info_list);
        rvList.setLayoutManager(new LinearLayoutManager(NhsAirlineWeatherActivity.this));
    }

    /**
     * AIP 정보 조회 데이터를 가져온다.
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-01 오후 10:17
     **/
    private void getWeaherData(){

        NhsFlightWeatherMetarModel nhsFlightWeatherMetar = null;

        rvList.setLayoutManager(new LinearLayoutManager(NhsAirlineWeatherActivity.this));
        RecyclerNhsFlightInfoListAdapter adapter = new RecyclerNhsFlightInfoListAdapter(NhsAirlineWeatherActivity.this, VIEWTYPE_TITLE_DATE_DOWNLOAD_DELETE);

        nhsFlightWeatherMetar = new NhsFlightWeatherMetarModel();
        nhsFlightWeatherMetar.setFileName("AirWeather.dat");
        nhsFlightWeatherMetar.setSavePath(Environment.getExternalStorageDirectory() + "/ACC_NAVI/Flight_Weather/");
        adapter.addData(nhsFlightWeatherMetar);

        rvList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnFileResultListener(new NetworkProcessWithFile.OnResultListener() {
            @Override
            public void onFailure() {
                new ToastUtile().showCenterText(NhsAirlineWeatherActivity.this, "다운로드가 실패했습니다.");
            }

            @Override
            public void onSuccess(File file) {
                new ToastUtile().showCenterText(NhsAirlineWeatherActivity.this, "다운로드 완료");
                rvList.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onStart(String fileName) {

            }
        });

    }
}
