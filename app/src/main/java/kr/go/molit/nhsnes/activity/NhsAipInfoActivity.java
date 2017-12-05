package kr.go.molit.nhsnes.activity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.modim.lan.lanandroid.AirDoyupList;
import com.modim.lan.lanandroid.Constants;
import com.modim.lan.lanandroid.INativeImple;
import com.modim.lan.lanandroid.NativeImplement;
import com.modim.lan.lanandroid.RpOption;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kr.go.molit.nhsnes.Network.NetworkProcessWithFile;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.adapter.RecyclerNhsFlightInfoListAdapter;
import kr.go.molit.nhsnes.common.MagicSE_Util;
import kr.go.molit.nhsnes.common.NetworkParamUtil;
import kr.go.molit.nhsnes.common.NetworkUrlUtil;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.model.FlightRouteModel;
import kr.go.molit.nhsnes.model.NhsAirListModel;
import kr.go.molit.nhsnes.model.NhsFlightDeleteModel;
import kr.go.molit.nhsnes.model.NhsFlightInfoModel;
import kr.go.molit.nhsnes.model.NhsNGllDataModel;
import kr.go.molit.nhsnes.net.model.AirChartListItemModel;
import kr.go.molit.nhsnes.net.model.AirChartListModel;
import kr.go.molit.nhsnes.net.model.AirListModel;
import kr.go.molit.nhsnes.net.model.FlightPlanModel;
import kr.go.molit.nhsnes.net.model.NetSecurityModel;
import kr.go.molit.nhsnes.net.service.FlightInfoService;
import kr.go.molit.nhsnes.net.service.NetConst;
import kr.go.molit.nhsnes.net.service.NetUtil;
import kr.go.molit.nhsnes.widget.ActionBarEx;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.modim.lan.lanandroid.NativeImplement.lanGetRouteDoyupList;
import static com.modim.lan.lanandroid.NativeImplement.lanGetRouteDoyupListCount;
import static kr.go.molit.nhsnes.activity.NhsAirlineInquiryActivity.AIRFIELD_LIST;
import static kr.go.molit.nhsnes.activity.NhsAirlineInquiryActivity.AIRPORT_LIST;
import static kr.go.molit.nhsnes.activity.NhsAirlineInquiryActivity.AIRSPACE_LIST;
import static kr.go.molit.nhsnes.activity.NhsAirlineInquiryActivity.FLIGHT_PATH_LIST;
import static kr.go.molit.nhsnes.activity.NhsAirlineInquiryActivity.HELIPORT_LIST;
import static kr.go.molit.nhsnes.activity.NhsAirlineInquiryActivity.NOTAM_LIST;
import static kr.go.molit.nhsnes.activity.NhsAirlineInquiryActivity.OBSTACLE_LIST;
import static kr.go.molit.nhsnes.activity.NhsAirlineInquiryActivity.PYLON_LIST;
import static kr.go.molit.nhsnes.activity.NhsAirlineInquiryActivity.ROUTE_LIST;
import static kr.go.molit.nhsnes.activity.NhsAirlineInquiryActivity.RUNWAY_LIST;
import static kr.go.molit.nhsnes.activity.NhsAirlineInquiryActivity.VFR_PATH;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_TOKEN_KEY;
import static kr.go.molit.nhsnes.adapter.RecyclerNhsFlightInfoListAdapter.VIEWTYPE_TITLE_DATE_DOWNLOAD_DELETE;

/**
 * z
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class NhsAipInfoActivity extends NhsBaseFragmentActivity {

    public static final String CALL_CALSS_NAME = "className";       // 호출한 클래스 이름
    public static final String SEARCH_TYPE = "searchType";           // 조회 타입
    public static final String DATA = "data";                           // 데이터


    private RecyclerView rvList;
    private String callCalssName = "";           // 호출한 클래스 이름
    private ActionBarEx abeTitme = null;    // 타이틀
    private String searchType = "";         // 조회 정보
    private NativeImplement nativeImplement;

    private AirDoyupList doyupList = null; // 도엽리스트
    private int allDownLoadStep = 0;
    private ProgressDialog mProgressDialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_nhs_aip_info);

        this.nativeImplement = INativeImple.getInstance(getContext());

        Bundle data = getIntent().getExtras();
        if (data != null) {
            this.callCalssName = data.getString(CALL_CALSS_NAME, "");
            this.searchType = data.getString(SEARCH_TYPE, "");
            this.abeTitme = (ActionBarEx) findViewById(R.id.abe_title);
        }

        setLayout();
//        setRecyclerView();
        getList();

    }

    /**
     * 리스트를 조회한다.
     *
     * @author 임성진
     * @version 1.0.0
     * @since 2017-09-08 오전 11:34
     **/
    private void getList() {

        if (this.callCalssName.equalsIgnoreCase(NhsAirlineChartActivity.class.getName())) {

            findViewById(R.id.nlv_view).setVisibility(View.GONE);

            this.abeTitme.setTitleText("AIP 정보");
            String path = Environment.getExternalStorageDirectory().toString() + "/ACC_NAVI/Flight_Char";

            File directory = new File(path);
            File[] files = directory.listFiles();

            if (files != null) {
                NhsFlightDeleteModel airChartDeleteModel = null;
                String fileDate = "";
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                RecyclerNhsFlightInfoListAdapter adapter = new RecyclerNhsFlightInfoListAdapter(this, RecyclerNhsFlightInfoListAdapter.VIEWTYPE_TITLE_DATE_DELETE);

                for (int i = 0; i < files.length; i++) {

                    date.setTime(files[i].lastModified());
                    fileDate = sdf.format(date);

                    airChartDeleteModel = new NhsFlightDeleteModel();
                    airChartDeleteModel.setFileName(files[i].getName());
                    airChartDeleteModel.setDate(fileDate);
                    airChartDeleteModel.setFile(files[i]);

                    adapter.addData(airChartDeleteModel);

                }

                rvList.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }


        } else if (this.callCalssName.equalsIgnoreCase(NhsNGIIDataActivity.class.getName())) {

            this.abeTitme.setTitleText("국토지리정보원 데이터");

            // 일괄 다운로드
            findViewById(R.id.btn_all_download).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_all_download).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    allDownload();

                }
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    ArrayList<FlightRouteModel> route = (ArrayList<FlightRouteModel>) getIntent().getExtras().getSerializable(DATA);

                    int size = route.size();
                    int i = 0;

                    nativeImplement.lanClearRoutePosition();

                    // 시작 위치
                    nativeImplement.lanSetRoutePosition(Constants.NAVI_SETPOSITION_START, Double.parseDouble(route.get(0).getLon()), Double.parseDouble(route.get(0).getLat()),
                            "start", 0);

                    // 종료 위치
                    nativeImplement.lanSetRoutePosition(Constants.NAVI_SETPOSITION_GOAL, Double.parseDouble(route.get(route.size() - 1).getLon()), Double.parseDouble(route.get(route.size() - 1).getLat()),
                            "end", 0);


                    // 경유지 등록
                    for (i = 1; i < size - 1; i++) {

                        nativeImplement.lanSetRoutePosition(Constants.NAVI_SETPOSITION_WAYPOINT, Double.parseDouble(route.get(i).getLon()), Double.parseDouble(route.get(i).getLat()),
                                "way" + i, 0);

                    }

                    int result = nativeImplement.lanExecuteRP(new RpOption(0, 0));

                    if (result != -1) {

                        // 경로에 따라 위성사진 다운로드할 path 및 파일이름이 나온다.
                        doyupList = lanGetRouteDoyupList();

                        findViewById(R.id.nlv_view).setVisibility(View.GONE);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadDoyupList();
                            }
                        });

                    }

                }
            }, 1000);


        } else if (this.callCalssName.equalsIgnoreCase(NhsAirlineInquiryActivity.class.getName())) {

            findViewById(R.id.nlv_view).setVisibility(View.GONE);

            this.abeTitme.setTitleText("항공정보");

            // 리스트에 표시될 제목
            String title = "";
            String saveFileName = "";  // 저장할 파일 이름
            File savedFile = null;          // 저장된 파일

            // 파라미터를 만든다.
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("tokenKey", StorageUtil.getStorageModeEx(NhsAipInfoActivity.this, LOGIN_TOKEN_KEY, ""));

            RequestBody body = NetUtil.mapToJsonBody(NhsAipInfoActivity.this, params);

            // 호출할 서비스를 만든다.
            FlightInfoService service = FlightInfoService.retrofit.create(FlightInfoService.class);

            // AirListModel
            Call<NetSecurityModel> callback = null;

            switch (this.searchType) {

                // 공역
                case AIRSPACE_LIST:
                    title += "공역";
                    saveFileName = "AirSpace.dat";
                    callback = service.airspaceList(body);
                    break;

                // 장애물
                case OBSTACLE_LIST:
                    title += "장애물";
                    saveFileName = "AirObstacle.dat";
                    callback = service.obstacleList(body);
                    break;

                // 이착륙장
                case AIRFIELD_LIST:
                    title += "이착륙장";
                    saveFileName = "AirLanding.dat";
                    callback = service.airfieldList(body);
                    break;

                // 공항조회
                case AIRPORT_LIST:
                    title += "공항";
                    saveFileName = "AirPort.dat";
                    callback = service.airportList(body);
                    break;

                // 비행경로조회
                case FLIGHT_PATH_LIST:
                    title += "비행경로";
                    saveFileName = "AirPath.dat";
                    callback = service.flightPathList(body);
                    break;

                // 항로조회
                case ROUTE_LIST:
                    title += "항로";
                    saveFileName = "AirCourse.dat";
                    callback = service.routeList(body);
                    break;

                // 시계비행로
                case VFR_PATH:
                    title += "시계비행로";
                    saveFileName = "AirVfrPath.dat";
                    callback = service.vfrPath(body);
                    break;

                // 철탑
                case PYLON_LIST:
                    title += "철탑";
                    saveFileName = "AirPylon.dat";
                    callback = service.pylonList(body);
                    break;

                // 활주로 조회
                case RUNWAY_LIST:
                    title += "활주로";
                    saveFileName = "AirStrip.dat";
                    callback = service.runwayList(body);
                    break;

                // 헬기장 조회
                case HELIPORT_LIST:
                    title += "헬기장";
                    saveFileName = "AirHeliport.dat";
                    callback = service.heliportList(body);
                    break;
                // 항공시보
                case NOTAM_LIST:


                    break;


            }

            if (callback != null) {

                final String finalTitle = title;
                final String finalSaveFileName = saveFileName;

                // AirListModel
                callback.enqueue(new Callback<NetSecurityModel>() {
                    @Override
                    public void onResponse(Call<NetSecurityModel> call, Response<NetSecurityModel> response) {

                        if (response.code() == 200) {

                            NetSecurityModel netSecurityModel = response.body();

                            if (netSecurityModel.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {

                                String dec = new MagicSE_Util(getContext()).getDecData(netSecurityModel.getResult_data());

                                AirListModel model = new Gson().fromJson(dec, AirListModel.class);

                                if (model.getResult_code().trim().equals(NetConst.RESPONSE_SUCCESS)) {

                                    final NhsAirListModel nhsAirListModel = new NhsAirListModel();
                                    nhsAirListModel.setTitle(finalTitle + " " + model.getData_ver());
                                    nhsAirListModel.setResultUrl(model.getResult_url());
                                    nhsAirListModel.setDataVer(model.getData_ver());
                                    nhsAirListModel.setFileName(finalSaveFileName);

                                    // 파일이 있든 없든 경로로 파일이 있다고 가정하에 파일 오브젝트를 미리 만들어 놓는다.
                                    nhsAirListModel.setFile(new File(Environment.getExternalStorageDirectory() + "/ACC_NAVI/Flight_Info/" + finalSaveFileName));

                                    rvList.setLayoutManager(new LinearLayoutManager(NhsAipInfoActivity.this));
                                    RecyclerNhsFlightInfoListAdapter adapter = new RecyclerNhsFlightInfoListAdapter(NhsAipInfoActivity.this, VIEWTYPE_TITLE_DATE_DOWNLOAD_DELETE);
                                    nhsAirListModel.setSavePath(Environment.getExternalStorageDirectory() + "/ACC_NAVI/Flight_Info/");
                                    adapter.addData(nhsAirListModel);

                                    rvList.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();

                                    adapter.setOnFileResultListener(new NetworkProcessWithFile.OnResultListener() {
                                        @Override
                                        public void onFailure() {
                                            new ToastUtile().showCenterText(NhsAipInfoActivity.this, "다운로드 실패");
                                        }

                                        @Override
                                        public void onSuccess(File file) {
                                            nhsAirListModel.setFile(file);
                                            new ToastUtile().showCenterText(NhsAipInfoActivity.this, "다운로드 완료");
                                            rvList.getAdapter().notifyDataSetChanged();
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


        } else if (this.callCalssName.equalsIgnoreCase(NhsAipInfoActivity.class.getName())) {

            findViewById(R.id.nlv_view).setVisibility(View.GONE);

            // AIP 정보 조회 데이터를 가져온다.
            getAIPInfoData();
        }

    }


    /**
     * 도협을 모두 다운로드한다.
     *
     * @author FIESTA
     * @version 1.0.0
     **/
    synchronized private void allDownload(){

        RecyclerView.Adapter adapter = rvList.getAdapter();

        if (adapter !=null) {


            RecyclerNhsFlightInfoListAdapter recyclerNhsFlightInfoListAdapter = (RecyclerNhsFlightInfoListAdapter)adapter;

            ArrayList<Object> list = recyclerNhsFlightInfoListAdapter.getAllData();

            if (allDownLoadStep == 0) {
                this.mProgressDialog = new ProgressDialog(NhsAipInfoActivity.this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
                this.mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                this.mProgressDialog.setTitle("다운로드 중...");
                this.mProgressDialog.setMax(list.size());
                this.mProgressDialog.setMessage(NhsAipInfoActivity.this.getString(R.string.wait_message));
                this.mProgressDialog.setCancelable(false);
                this.mProgressDialog.show();
            }


            if (this.allDownLoadStep < list.size()) {

                NhsNGllDataModel nhsNGllDataModel = (NhsNGllDataModel) list.get(allDownLoadStep);

                this.allDownLoadStep += 1;

                org.apache.http.entity.StringEntity param = new NetworkParamUtil().requestDownloadDoyup(NhsAipInfoActivity.this, "5", nhsNGllDataModel.getResultUrl());
                NetworkProcessWithFile downloadFile = new NetworkProcessWithFile(NhsAipInfoActivity.this, new NetworkUrlUtil().getDownloadDoyup(), param, nhsNGllDataModel.getSavePath(), nhsNGllDataModel.getFileName(), new NetworkProcessWithFile.OnResultListener() {
                    @Override
                    public void onFailure() {
                        allDownload();
                    }

                    @Override
                    public void onSuccess(File file) {
                        allDownload();
                    }

                }, false);

                downloadFile.execute();



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog.setProgress(allDownLoadStep);
                    }
                });


            } else {

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                this.allDownLoadStep = 0;
                loadDoyupList();
                Toast.makeText(NhsAipInfoActivity.this, "다운로드가 완료되었습니다.", Toast.LENGTH_LONG).show();
            }

        }

    }

    /**
     * AIP 정보 조회 데이터를 가져온다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-01 오후 10:17
     **/
    private void getAIPInfoData() {

        FlightInfoService service = FlightInfoService.retrofit.create(FlightInfoService.class);

        // AirChartListModel
        Call<NetSecurityModel> callback = service.searchAirChartList();
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

                            rvList.setLayoutManager(new LinearLayoutManager(NhsAipInfoActivity.this));
                            RecyclerNhsFlightInfoListAdapter adapter = new RecyclerNhsFlightInfoListAdapter(NhsAipInfoActivity.this, VIEWTYPE_TITLE_DATE_DOWNLOAD_DELETE);

                            for (i = 0; i < size; i++) {

                                airChartListItemModel = netDataList.get(i);

                                // AD 인것만 조회한다.
                                if (airChartListItemModel.getAIRCHART_GB().equalsIgnoreCase("AD")) {
                                    nhsFlightInfoModel = new NhsFlightInfoModel();
                                    nhsFlightInfoModel.setAirChartListItemModel(airChartListItemModel);
                                    nhsFlightInfoModel.setSavePath(Environment.getExternalStorageDirectory() + "/ACC_NAVI/AIP/");
                                    adapter.addData(nhsFlightInfoModel);
                                }

                            }


                            rvList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            adapter.setOnFileResultListener(new NetworkProcessWithFile.OnResultListener() {
                                @Override
                                public void onFailure() {
                                    new ToastUtile().showCenterText(NhsAipInfoActivity.this, "다운로드가 실패했습니다.");
                                }

                                @Override
                                public void onSuccess(File file) {
                                    new ToastUtile().showCenterText(NhsAipInfoActivity.this, "다운로드 완료");
                                    rvList.getAdapter().notifyDataSetChanged();
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
     * 도협 리스트를 보여준다.
     *
     * @author FIESTA
     * @version 1.0.0
     **/
    private void loadDoyupList() {

        int i = 0;

        RecyclerNhsFlightInfoListAdapter recyclerNhsFlightInfoListAdapter = new RecyclerNhsFlightInfoListAdapter(NhsAipInfoActivity.this, RecyclerNhsFlightInfoListAdapter.VIEWTYPE_TITLE_DATE_DOWNLOAD_DELETE);

        NhsNGllDataModel nhsNGllDataModel;

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
                String[] names = doyupList.lists[i].path.split("/");

                int z = 0;
                StringBuilder sb = new StringBuilder();

                try {
                    for (z = 0; z < names.length - 1; z++) {
                        sb.append(names[z]);
                        sb.append("/");
                    }
                } catch (Exception ex) {
                    Log.d("test", "ERROR ARRAY : " + i + ", " + z);
                }

                nhsNGllDataModel = new NhsNGllDataModel();
                nhsNGllDataModel.setTitle(doyupList.lists[i].path);
                nhsNGllDataModel.setResultUrl(doyupList.lists[i].path.replaceFirst("/", ""));
                nhsNGllDataModel.setFileName(names[names.length - 1]);

                // 파일이 있든 없든 경로로 파일이 있다고 가정하에 파일 오브젝트를 미리 만들어 놓는다.
                nhsNGllDataModel.setFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/LANMap" + doyupList.lists[i].path));

                nhsNGllDataModel.setSavePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/LANMap" + sb.toString());
                recyclerNhsFlightInfoListAdapter.addData(nhsNGllDataModel);

            }

            recyclerNhsFlightInfoListAdapter.setOnFileResultListener(new NetworkProcessWithFile.OnResultListener() {
                @Override
                public void onFailure() {
                    new ToastUtile().showCenterText(NhsAipInfoActivity.this, "다운로드 실패");
                }

                @Override
                public void onSuccess(File file) {
                    loadDoyupList();
                    new ToastUtile().showCenterText(NhsAipInfoActivity.this, "다운로드 완료");
                }
            });


//            rvList.bringToFront();
            rvList.setLayoutManager(new LinearLayoutManager(NhsAipInfoActivity.this));
            rvList.setAdapter(recyclerNhsFlightInfoListAdapter);
            recyclerNhsFlightInfoListAdapter.notifyDataSetChanged();


        }

    }

    private void setLayout() {
        rvList = (RecyclerView) findViewById(R.id.rv_info_list);
        rvList.setLayoutManager(new LinearLayoutManager(NhsAipInfoActivity.this));
    }
}
