package kr.go.molit.nhsnes.activity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.modim.lan.lanandroid.INativeImple;
import com.modim.lan.lanandroid.NativeImplement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import kr.go.molit.nhsnes.Network.NetworkProcessWithFile;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.NetworkParamUtil;
import kr.go.molit.nhsnes.common.NetworkUrlUtil;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.dialog.DialogType1;
import kr.go.molit.nhsnes.widget.TextViewEx;

import static kr.go.molit.nhsnes.activity.NhsMainActivity.MAP_VERSION_DEM;
import static kr.go.molit.nhsnes.activity.NhsMainActivity.MAP_VERSION_VECTOR;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class NhsAppInfoActivity extends NhsBaseFragmentActivity implements View.OnClickListener {

    private DialogType1 newAppDialgo = null;
    private LinearLayout llBgNewApp = null;
    private TextView tvNewApp = null;
    private NativeImplement nativeImplement = null;
    private String vectorVersion = "0";
    private String demVersion = "0";
    private String newestVectorVersion = "";
    private String newestDemVersion = "";

    private TextViewEx tveMapVersion = null;
    private LinkedList<NetworkProcessWithFile> downloadQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_app_info);

        this.nativeImplement = INativeImple.getInstance(getContext());

        // 레이아웃 설정
        setLayout();

        // 맵 버전을 체크 후, 버튼을 활성화 한다.
        try {
            checkMapVersion();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private int downloadStep = 0;

    /**
     * 맵 버전을 체크 후, 버튼을 활성화 한다.
     * @author FIESTA
     * @since  오후 11:40
     **/
    private void checkMapVersion() throws JSONException {

        final TextView tvMapUpdate = (TextView) this.findViewById(R.id.tv_map_update);
        final LinearLayout llMapUpdate = (LinearLayout) this.findViewById(R.id.ll_map_update);

        try {

            this.vectorVersion = StorageUtil.getStorageModeEx(NhsAppInfoActivity.this, NativeImplement.NHS_MAP_VERSION_VECTOR, "0");
            this.demVersion = StorageUtil.getStorageModeEx(NhsAppInfoActivity.this, NativeImplement.NHS_MAP_VERSION_DEM, "0");

            JSONObject newestVectorObject = new JSONObject(StorageUtil.getStorageModeEx(NhsAppInfoActivity.this, MAP_VERSION_VECTOR, "0"));
            JSONObject newestDemObject = new JSONObject(StorageUtil.getStorageModeEx(NhsAppInfoActivity.this, MAP_VERSION_DEM, "0"));

            this.newestVectorVersion = newestVectorObject.optString("MAP_VER");
            this.newestDemVersion = newestDemObject.optString("MAP_VER");

        }catch (Exception ex) {

        }

        String mapVersion = "백터지도 버전: " + vectorVersion + "(최신: " + newestVectorVersion + ")\n"+
                            "수치지도 버전: " + demVersion + "(최신: " + newestDemVersion + ")";

        this.tveMapVersion.setText(mapVersion);

        // 버전이 같지 않으면 지도 업데이트 활성화
//        if ((this.vectorVersion.equals(this.newestVectorVersion) ||
//                this.demVersion.equals(this.newestDemVersion))) {
        if ((!(this.vectorVersion.equals(this.newestVectorVersion)) ||
                !(this.demVersion.equals(this.newestDemVersion)))) {

            showCheckNewAppDialog();

            tvMapUpdate.setTextColor(getResources().getColor(android.R.color.white));
            llMapUpdate.setBackgroundResource(R.drawable.border_corner_white);
            llMapUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    downloadQueue = new LinkedList<>();

                    if (!(vectorVersion.equals(newestVectorVersion))){

                        org.apache.http.entity.StringEntity param = new NetworkParamUtil().requestDownloadMap(NhsAppInfoActivity.this, "4", "MapMain.dat");
                        NetworkProcessWithFile downloadFile = new NetworkProcessWithFile(NhsAppInfoActivity.this, new NetworkUrlUtil().getDownloadMap(),
                                param, Environment.getExternalStorageDirectory().getAbsolutePath() + "/ACC_Navi/Map_Data/",
                                "MapMain.dat", new NetworkProcessWithFile.OnResultListener() {
                            @Override
                            public void onFailure() {

                                if (downloadQueue.size() > downloadStep) {
                                    downloadStep++;
                                    downloadQueue.get(downloadStep).execute();
                                }

                            }

                            @Override
                            public void onSuccess(File file) {
                                if (downloadQueue.size() > downloadStep) {
                                    downloadStep++;
                                    downloadQueue.get(downloadStep).execute();
                                }
                            }
                        }, true);

                        downloadQueue.add(downloadFile);

                    }

                    if (!(demVersion.equals(newestDemVersion))) {

                        org.apache.http.entity.StringEntity param = new NetworkParamUtil().requestDownloadMap(NhsAppInfoActivity.this, "3", "DemMain.dat");
                        NetworkProcessWithFile downloadFile = new NetworkProcessWithFile(NhsAppInfoActivity.this, new NetworkUrlUtil().getDownloadMap(),
                                param, Environment.getExternalStorageDirectory().getAbsolutePath()+"/ACC_Navi/Map_Data/",
                                "DemMain.dat", new NetworkProcessWithFile.OnResultListener() {
                            @Override
                            public void onFailure() {
                                if (downloadQueue.size() > downloadStep) {
                                    downloadStep++;
                                    downloadQueue.get(downloadStep).execute();
                                }
                            }

                            @Override
                            public void onSuccess(File file) {
                                if (downloadQueue.size() > downloadStep) {
                                    downloadStep++;
                                    downloadQueue.get(downloadStep).execute();
                                }
                            }
                        }, true);

                        downloadQueue.add(downloadFile);

                    }


                    org.apache.http.entity.StringEntity param = new NetworkParamUtil().requestDownloadMap(NhsAppInfoActivity.this, "7", "LAN_POI");
                    NetworkProcessWithFile downloadFile = new NetworkProcessWithFile(NhsAppInfoActivity.this, new NetworkUrlUtil().getDownloadMap(),
                            param, Environment.getExternalStorageDirectory().getAbsolutePath()+"/ACC_Navi/Map_Data/",
                            "LAN_POI", new NetworkProcessWithFile.OnResultListener() {
                        @Override
                        public void onFailure() {
                            downloadQueue.clear();
                        }

                        @Override
                        public void onSuccess(File file) {
                            downloadQueue.clear();
                        }
                    }, true);


                    downloadStep = 0;
                    downloadQueue.add(downloadFile);
                    downloadQueue.get(downloadStep).execute();


//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            copyAssets("Dem.dat", Environment.getExternalStorageDirectory() + "/Acc_Navi/Map_Data/Dem/");
//                            copyAssets("MapMain.dat", Environment.getExternalStorageDirectory() + "/Acc_Navi/Map_Data/Vector/");
//                            Toast.makeText(NhsAppInfoActivity.this, "지도 업데이트가 완료되었습니다.", Toast.LENGTH_SHORT).show();
//                            tvMapUpdate.setTextColor(getResources().getColor(R.color.listDivider));
//                            llMapUpdate.setBackgroundResource(R.drawable.border_corner_gray);
//                        }
//                    }, 1000);

                }
            });
        } else {
            tvMapUpdate.setTextColor(getResources().getColor(R.color.listDivider));
            llMapUpdate.setBackgroundResource(R.drawable.border_corner_gray);

        }
    }

    /**
    * 레이아웃 설정
    * @author FIESTA
    * @since  오후 11:52
    **/
    private void setLayout(){

        this.llBgNewApp = (LinearLayout) findViewById(R.id.ll_bg_new_app);
        this.tvNewApp = (TextView) findViewById(R.id.tv_new_app);
        this.tveMapVersion = (TextViewEx) findViewById(R.id.tve_map_version);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            // 앱 마켓 이동
            case R.id.ll_bg_new_app:
                Util.moveToMarket(NhsAppInfoActivity.this);
                break;
        }

    }

    /**
     * 신규 어플리케이션이 존재함을 알리는 다이얼로그를 띄운다.
     * @author FIESTA
     * @since  오후 11:40
     **/
    private void showCheckNewAppDialog(){

        this.newAppDialgo = new DialogType1(getContext(), "업데이트", "신규버전이 있습니다.\n업그레이드 하시겠습니까?", getContext().getString(R.string.btn_confirm), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llBgNewApp.setBackgroundResource(R.drawable.border_corner_white);
                tvNewApp.setTextColor(v.getResources().getColor(R.color.white));
                llBgNewApp.setOnClickListener(NhsAppInfoActivity.this);
                newAppDialgo.hideDialog();


            }
        }, getString(R.string.btn_cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llBgNewApp.setBackgroundResource(R.drawable.border_corner_gray);
                tvNewApp.setTextColor(v.getResources().getColor(R.color.listDivider));
                llBgNewApp.setOnClickListener(null);
                newAppDialgo.hideDialog();

            }
        });

    }

    private void copyAssets(String filename, String savePath) {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) {
            InputStream in = null;
            OutputStream out = null;
            try {

                in = assetManager.open(filename);
                File outFile = new File(savePath, filename);

                if (!outFile.getParentFile().exists()) {
                    outFile.getParentFile().mkdirs();
                }

                out = new FileOutputStream(outFile);
                copyFile(in, out);

            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    } finally {

                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    } finally {

                    }
                }
            }
        }

    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
