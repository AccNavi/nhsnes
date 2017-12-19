package kr.go.molit.nhsnes.activity;

import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.modim.lan.lanandroid.INativeImple;
import com.modim.lan.lanandroid.NativeImplement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.dialog.DialogType1;
import kr.go.molit.nhsnes.widget.TextViewEx;

import static kr.go.molit.nhsnes.activity.NhsMainActivity.MAP_VERSION_DEM;
import static kr.go.molit.nhsnes.activity.NhsMainActivity.MAP_VERSION_VECTOR;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class NhsAppInfoActivity extends NhsBaseFragmentActivity implements View.OnClickListener {

    private final static String DEFAULT_DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ACC_Navi/Map_Data/";

    // DEM 저장 경로
    private final static String DEM_DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ACC_Navi/Map_Data/DEM/";

    // 실제 맵 경로
    private final static String REAL_MAP_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LANMap/";

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
    private ProgressDialog progressDialog;
    private boolean isDownloadDem = false;
    private boolean isDownloadVector = false;
    private boolean isDownloadPOI = false;

    private int currentProgress = 0;

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
     *
     * @author FIESTA
     * @since 오후 11:40
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

        } catch (Exception ex) {

        }

        String mapVersion = "백터지도 버전: " + vectorVersion + "(최신: " + newestVectorVersion + ")\n" +
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

                    if (downloadQueue != null) {
                        downloadQueue.clear();
                    }

                    if (!(vectorVersion.equals(newestVectorVersion))) {

                        // 벡터 데이터를 큐에 넣는다.
                        addDownloadQueue("4", DEFAULT_DOWNLOAD_PATH, "MapMain.dat");
                        isDownloadVector = true;

                    }

                    // dem 데이터를 다운로드 큐에 넣는다.
                    if (!(demVersion.equals(newestDemVersion))) {

                        addDownloadQueue("6", DEM_DOWNLOAD_PATH, "DEM2-1.bin");
                        addDownloadQueue("6", DEM_DOWNLOAD_PATH, "DEM2-2.bin");
                        addDownloadQueue("6", DEM_DOWNLOAD_PATH, "DEM3.bin");
                        addDownloadQueue("6", DEM_DOWNLOAD_PATH, "DEM4.bin");
                        addDownloadQueue("6", DEM_DOWNLOAD_PATH, "DEM5.bin");
                        addDownloadQueue("6", DEM_DOWNLOAD_PATH, "DEM6.bin");
                        addDownloadQueue("6", DEM_DOWNLOAD_PATH, "DEM7.bin");
                        addDownloadQueue("6", DEM_DOWNLOAD_PATH, "DEM8.bin");

                        isDownloadDem = true;

                    }

                    // LAN_POI를 다운로드 큐에 넣는다.
                    addDownloadQueue("7", DEFAULT_DOWNLOAD_PATH, "LANPOI.dat");
                    isDownloadPOI = true;

                    // 다운로드를 시작한다.
                    startDownloadQueue();

                }
            });
        } else {
            tvMapUpdate.setTextColor(getResources().getColor(R.color.listDivider));
            llMapUpdate.setBackgroundResource(R.drawable.border_corner_gray);

        }
    }

    /**
     * 다운로드 큐를 시작한다.
     *
     * @author FIESTA
     * @since 오후 11:52
     **/
    private void startDownloadQueue() {


        if (downloadQueue != null) {

            // 프로그래스바를 보여준다.
            showProgress(this.downloadQueue.size() - 1, "다운로드 중입니다..");

            // 처음부터 큐 스탭을 시작한다.
            this.downloadStep = 0;

            // 다운로드 시작
            this.downloadQueue.get(this.downloadStep).execute();

        }
    }

    /**
     * 다운로드 큐에 삽입한다.
     *
     * @author FIESTA
     * @since 오후 11:52
     **/
    private void addDownloadQueue(String mapType, String downloadPath, final String fileName) {

        if (downloadQueue == null) {
            downloadQueue = new LinkedList<>();
        }

        org.apache.http.entity.StringEntity param = new NetworkParamUtil().requestDownloadMap(NhsAppInfoActivity.this, mapType, fileName);
        final NetworkProcessWithFile downloadFile = new NetworkProcessWithFile(NhsAppInfoActivity.this, new NetworkUrlUtil().getDownloadMap(),
                param, downloadPath,
                fileName, new NetworkProcessWithFile.OnResultListener() {
            @Override
            public void onFailure() {

                dismissProgress();
                new ToastUtile().showCenterText(NhsAppInfoActivity.this, "다운로드 실패했습니다.\n[파일 이름: " + fileName + "]\n다시 시도해주세요.");

            }

            @Override
            public void onSuccess(File file) {
                if (downloadQueue.size() - 1 > downloadStep) {
                    downloadStep++;
                    downloadQueue.get(downloadStep).execute();
                } else {
                    NhsAppInfoActivity.this.donwloadComplate();
                }
            }

            @Override
            public void onStart(String fileName) {
                updateProgress(fileName + "을 다운로드 중입니다.");
            }
        }, false);

        downloadQueue.add(downloadFile);

    }

    /**
     * 레이아웃 설정
     *
     * @author FIESTA
     * @since 오후 11:52
     **/
    private void setLayout() {

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
     *
     * @author FIESTA
     * @since 오후 11:40
     **/
    private void showCheckNewAppDialog() {

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

                NhsAppInfoActivity.this.finish();

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
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    /**
     * 프로그래스바를 보여준다.
     *
     * @author FIESTA
     * @since 오전 3:50
     **/
    private void showProgress(final int maxSize, final String msg) {
        this.currentProgress = 0;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                progressDialog = new ProgressDialog(NhsAppInfoActivity.this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setTitle("다운로드 중..");
                progressDialog.setMax(maxSize);
                progressDialog.setMessage(msg);
                progressDialog.setCancelable(false);
                progressDialog.show();

            }
        });

    }


    /**
     * 프로그래스바 업데이트한다.
     *
     * @author FIESTA
     * @since 오전 3:50
     **/
    private void updateProgress(final String msg) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {

                    if (progressDialog.isShowing()) {

                        Log.d("downloadtest", msg);
                        progressDialog.setMessage(msg);

                        if (currentProgress == 0) {
                            currentProgress = progressDialog.getProgress();
                        }

                        progressDialog.setProgress(currentProgress);

                        currentProgress += 1;

                    }

                }
            }
        });


    }

    /**
     * 프로그래스바를 종료한다.
     *
     * @author FIESTA
     * @since 오전 11:21
     **/
    private void dismissProgress() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        });

    }

    /**
     * DEM 파일들을 합친다.
     *
     * @author FIESTA
     * @since 오전 11:21
     **/
    private boolean combineFileDem(String saveFileName, String filePath, String createPth) {

        boolean isComplate = false;
        updateProgress("DEM 파일을 합치는 중입니다.");

        try {

            File createFile = new File(createPth);

            // 경로가 없으면 만든다..
            if (!createFile.exists()) {
                createFile.mkdirs();
            }

            FileOutputStream nFo = new FileOutputStream(createPth + saveFileName);

            FileInputStream nFi1 = new FileInputStream(filePath + "DEM2-1.bin");
            FileInputStream nFi2 = new FileInputStream(filePath + "DEM2-2.bin");


            byte[] buf = new byte[2048];

            int readCnt = 0;

            Log.d("downloadtest", "DEM2-1.bin 합치는 중");
            updateProgress("DEM2-1.bin 합치는 중");

            // DEM2-1 합치기
            while ((readCnt = nFi1.read(buf)) > -1) {

                nFo.write(buf, 0, readCnt);

            }

            // 삭제
//            new File(filePath + "DEM2-1.bin").delete();

            Log.d("downloadtest", "DEM2-1.bin 합치기 완료");
            Log.d("downloadtest", "DEM2-2.bin 합치는 중");

            updateProgress("DEM2-2.bin 합치는 중");

            // DEM2-2 합치기
            while ((readCnt = nFi2.read(buf)) > -1) {

                nFo.write(buf, 0, readCnt);

            }

            // 삭제
//            new File(filePath + "DEM2-2.bin").delete();

            Log.d("downloadtest", "DEM2-2.bin 합치기 완료");

            int i = 3;
            int size = 8;

            for (i = 3; i <= size; i++) {

                nFi2 = new FileInputStream(filePath + "DEM" + i + ".bin");

                Log.d("downloadtest", "DEM" + i + ".bin 합치기 중");
                updateProgress("DEM" + i + ".bin 합치기 중");

                // 합치기
                while ((readCnt = nFi2.read(buf)) > -1) {

                    nFo.write(buf, 0, readCnt);

                }

                // 삭제
//                new File(filePath + "DEM" + i + ".bin").delete();

                Log.d("downloadtest", "DEM" + i + ".bin 합치기 완료");
            }


            nFo.flush();
            nFo.close();

            isComplate = true;

        } catch (Exception ex) {
            isComplate = false;
        }

        return isComplate;
    }

    /**
     * 파일 이동
     *
     * @author FIESTA
     * @since 오전 11:19
     **/
    private boolean moveFile(String inputPath, String inputName, String reName, String outputPath) {

        boolean isSucc = false;

        updateProgress(reName + " 파일을 이동중입니다.");

        InputStream in = null;
        OutputStream out = null;

        try {

            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputName);
            out = new FileOutputStream(outputPath + reName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath + inputName).delete();

            isSucc = true;

        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
            isSucc = false;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
            isSucc = false;
        } finally {
            return isSucc;
        }

    }

    /**
     * 다운로드 완료
     *
     * @author FIESTA
     * @since 오전 11:19
     **/
    public void donwloadComplate() {

        dismissProgress();


        new AsyncTask<Void, Void, Void>() {

            private boolean isComplate = false;
            String failStateMsg = "";

            @Override
            protected void onPreExecute() {

                int count = 0;

                if (isDownloadDem) {
                    count += 9;
                }

                if (isDownloadPOI) {
                    count += 1;
                }

                if (isDownloadPOI) {
                    count += 1;
                }

                NhsAppInfoActivity.this.showProgress(count, "파일 병합 및 파일 이동을 합니다.");
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {

                // dem 다운로드가 되었다면
                if (isDownloadDem) {

                    // DEM2-1.bin, DEM2-2.bin 을 합친다.
                    isComplate = combineFileDem("DemMain.dat", DEM_DOWNLOAD_PATH, DEM_DOWNLOAD_PATH);

                    // DemMain.dat을 맵 폴더로 옮긴다.
                    if (isComplate) {

                        // DemMain을 옮긴다.
                        isComplate = moveFile(DEM_DOWNLOAD_PATH, "DemMain.dat", "DemMain.dat", REAL_MAP_PATH);

                        if (!isComplate) {
                            failStateMsg = "DemMain.dat 옮기던 중, 실패했습니다.";
                            return null;
                        }

                    } else {
                        failStateMsg = "DEM2-1.bin, DEM2-2.bin 을 합치던 중, 실패했습니다.";
                        return null;
                    }

                }

                if (isDownloadVector) {

                    // DemMain을 옮긴다.
                    isComplate = moveFile(DEFAULT_DOWNLOAD_PATH, "MapMain.dat", "MapMain.dat", REAL_MAP_PATH);

                    if (!isComplate) {
                        failStateMsg = "MapMain.dat 옮기던 중, 실패했습니다.";
                        return null;
                    }

                }

                if (isDownloadPOI) {

                    // DemMain을 옮긴다.
                    isComplate = moveFile(DEFAULT_DOWNLOAD_PATH, "LANPOI.dat", "LANPOI.dat", REAL_MAP_PATH);

                    if (!isComplate) {
                        failStateMsg = "LANPOI.dat 옮기던 중, 실패했습니다.";
                        return null;
                    }

                }

                // 임시 DEM 폴더의 파일들을 삭제
                removeAllFile(DEM_DOWNLOAD_PATH);

                // 임시 DEFAULT DOWNLOAD 폴더의 파일들을 삭제
                removeAllFile(DEFAULT_DOWNLOAD_PATH);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (isComplate) {

                    new ToastUtile().showCenterText(NhsAppInfoActivity.this, "지도 업데이트가 완료되었습니다.");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            dismissProgress();
                            finish();

                        }
                    }, 2000);

                } else {

                    dismissProgress();
                    new ToastUtile().showCenterText(NhsAppInfoActivity.this, failStateMsg);


                }


            }
        }.execute();


    }

    /**
     * 임시 저장 폴더에 있는 파일들을 모두 삭제한다.
     *
     * @author FIESTA
     * @since 오전 10:14
     **/
    private void removeAllFile(String directoryPath) {

        try {

            File file = new File(directoryPath);
            File lists[] = file.listFiles();

            int size = lists.length;
            int i = 0;

            // 파일 모두 삭제
            for (i = 0; i < size; i++) {
                lists[i].delete();
            }

        } catch (Exception ex) {

        } finally {


        }

    }

}
