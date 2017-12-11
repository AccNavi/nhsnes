package kr.go.molit.nhsnes.activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import kr.go.molit.nhsnes.Network.NetworkProcess;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.NetworkParamUtil;
import kr.go.molit.nhsnes.common.NetworkUrlUtil;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.interfaces.OnGpsListener;
import kr.go.molit.nhsnes.widget.TextViewEx;

import static kr.go.molit.nhsnes.activity.NhsMainActivity.MAP_VERSION_DEM;
import static kr.go.molit.nhsnes.activity.NhsMainActivity.MAP_VERSION_VECTOR;

/**
* 기상정보
* @author FIESTA
* @since  오후 11:58
**/
public class NhsWeatherInfoActivity extends NhsBaseFragmentActivity implements View.OnClickListener{

    private String myLocation = "";
    private TextViewEx tveLocation = null;      // 대구 : 맑음
    private TextViewEx tveWd = null;             // 풍향
    private TextViewEx tveWspd = null;          // 풍속
    private TextViewEx tveVis = null;           // 시정
    private TextViewEx tveTempt = null;         // 기온
    private TextViewEx tveCldalt = null;        // 운고

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_weather_info);

        setLayout();

        // 지역 정보를 가져온다.
        getMyLocation();

    }

    /**
     * 지역정보를 가져온다.
     * @author FIESTA
     * @since  오전 12:23
     **/
    private void getMyLocation(){

        // gps 얻을 동안 프로그래스바를 보여준다.
        displayGpsProgressBar();

        super.setGpsListener(new OnGpsListener() {
            @Override
            public void onLocationChanged(Location location) {

                List<Address> list = null;
                try {


                    Geocoder geocoder = new Geocoder(NhsWeatherInfoActivity.this);

                    list = geocoder.getFromLocation(
                            location.getLatitude(), // 위도
                            location.getLongitude(), // 경도
                            1); // 얻어올 값의 개수
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
                }

                //  gps 프로그래스바를 멈춘다.
                dismissGpsProgress();

                if (list != null) {

                    if (list.size() > 0) {

                        // 지명을 가져온다.
                        myLocation = list.get(0).getLocality();

                        // 날씨를 조회한다.
                        getWeather();

                    } else {

                        // 주소 없음

                    }

                }



            }

            @Override
            public void onLocationFailed(String message) {

            }
        });

        // 위치를 가져온다.
        super.getLocation();

    }


    /**
     * 날씨 정보를 가져온다.
     * @author FIESTA
     * @since  오전 12:23
     **/
    private void getWeather(){

        String endDt = "";
        String startDt = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        endDt = sdf.format(new Date());  // 종료일

        sdf = new SimpleDateFormat("yyyyMMdd");
        startDt = sdf.format(new Date()) + "0000";    // 시작일

        NetworkUrlUtil nuu = new NetworkUrlUtil();
        NetworkParamUtil npu =new NetworkParamUtil();
        StringEntity param = npu.getWeatherPoint(NhsWeatherInfoActivity.this, startDt,endDt);
        new NetworkProcess(NhsWeatherInfoActivity.this, nuu.getWeatherPoint(), param,
                new NetworkProcess.OnResultListener() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        String msg = response.optString("result_msg");
                        String resultCode = response.optString("result_code");

                        if (resultCode.equalsIgnoreCase("Y")) {

                            try {
                                JSONArray list = response.optJSONArray("result_data");

                                int size = list.length();
                                int i = 0;
                                JSONObject data = null;
                                String koName = "";

                                boolean isFound = false;

                                for (i = 0; i < size; i++) {

                                    try {

                                        data = list.getJSONObject(i);
                                        koName = data.optString("POINT_NM_KOR");

                                        // 지명이 같은게 있다면 날씨 파싱해서 보여준다.
                                        if (myLocation.indexOf(koName) >= 0) {
                                            showData(data);
                                            isFound = true;

                                            // 검색 중지
                                            break;
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }

                                if (!isFound) {
                                    new ToastUtile().showCenterText(NhsWeatherInfoActivity.this, "기상 정보가 없습니다.");
                                }
                            }catch (Exception ex) {
                                new ToastUtile().showCenterText(NhsWeatherInfoActivity.this, "기상 정보가 없습니다.");
                            }

                        } else {
                            new ToastUtile().showCenterText(NhsWeatherInfoActivity.this, "기상 정보가 없습니다.");
                        }
                    }
                }, true).sendEmptyMessage(0);
    }

    /**
     * 데이터를 화면에 보여준다.
     * @author FIESTA
     * @since  오전 12:23
     **/
    private void showData(JSONObject data){

        String wd = "";           // 풍향
        String wspd = "";         // 풍속
        String vis = "";          // 시정
        String tempt = "";        // 기온
        String cldalt = "";       // 운고

        // 풍향을 구한다.
        int convertWd = Integer.parseInt(data.optString("WD", "0"))/10;
        switch (convertWd) {

            case 2:
                wd = "북북동";
                break;
            case 5:
                wd = "북동";
                break;
            case 7:
                wd = "동북동";
                break;
            case 9:
                wd = "동";
                break;
            case 11:
                wd = "동남동";
                break;
            case 14:
                wd = "남동";
                break;
            case 16:
                wd = "남남동";
                break;
            case 18:
                wd = "남";
                break;
            case 20:
                wd = "남남서";
                break;
            case 23:
                wd = "남서";
                break;
            case 25:
                wd = "서남서";
                break;
            case 27:
                wd = "서";
                break;
            case 29:
                wd = "서북서";
                break;
            case 32:
                wd = "북서";
                break;
            case 34:
                wd = "북북서";
                break;
            case 36:
                wd = "북";
                break;
            default:
                wd = "정보없음";
                break;
        }


        // 풍속을 구한다.
        wspd = data.optString("WSPD", "0.0") + data.optString("WSPD_UNIT", "0.0");

        // 시정을 구한다.
        vis = (Integer.parseInt(data.optString("VIS", "0"))/(float)1000)+"km";

        // 온도를 구한다.
        tempt = data.optString("TEMPT", "0") + "C";

        // 운고를 구한다.
        if (data.isNull("CLDALT")){
            cldalt = "정보없음";
        } else {
            cldalt = data.optString("CLDALT", "정보없음");
        }

        // 정보를 화면에 뿌린다.
        this.tveLocation.setText("대구 : 맑음");
        this.tveWd.setText(wd);                     // 풍향
        this.tveWspd.setText(wspd);                 // 풍속
        this.tveVis.setText(vis);                   // 시정
        this.tveTempt.setText(tempt);              // 기온
        this.tveCldalt.setText(cldalt);            // 운고

    }

    /**
    * 레이아웃을 설정한다
    * @author FIESTA
    * @since  오전 12:23
    **/
    private void setLayout(){

        findViewById(R.id.ll_go_to_kma).setOnClickListener(this);
        findViewById(R.id.ll_go_to_windy).setOnClickListener(this);

        this.tveLocation = (TextViewEx)findViewById(R.id.tve_location);        // 대구 : 맑음
        this.tveWd = (TextViewEx)findViewById(R.id.tve_wd);;                     // 풍향
        this.tveWspd = (TextViewEx)findViewById(R.id.tve_wspd);;                 // 풍속
        this.tveVis = (TextViewEx)findViewById(R.id.tve_vis);;                   // 시정
        this.tveTempt = (TextViewEx)findViewById(R.id.tve_tempt);;              // 기온
        this.tveCldalt = (TextViewEx)findViewById(R.id.tve_cldalt);;            // 운고

        // 화면에 초기 날짜는 오늘 날짜로 한다.
        TextViewEx tveNowDate = (TextViewEx) findViewById(R.id.tve_now_date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
        String nowDate = sdf.format(new Date());
        tveNowDate.setText(nowDate);

    }

    @Override
    public void onClick(View view) {

        Intent i = new Intent(Intent.ACTION_VIEW);
        Uri u = null;

        switch (view.getId()) {

            case R.id.ll_go_to_kma:
                u = Uri.parse("http://amo.kma.go.kr/new/html/main/main.jsp");
                break;

            case R.id.ll_go_to_windy:
                u = Uri.parse("https://www.windy.com");
                break;

        }

        i.setData(u);
        startActivity(i);

    }

}

