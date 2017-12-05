package kr.go.molit.nhsnes.activity;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.modim.lan.lanandroid.INativeImple;
import com.modim.lan.lanandroid.NativeImplement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.dialog.DialogType1;

import static com.modim.lan.lanandroid.NativeImplement.lanVersion;
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
    private int vectorVersion = 0;
    private int demVersion = 0;
    private String newstVectorVersion = "";
    private String newestDemVersion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_app_info);
        showCheckNewAppDialog();

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
    /**
     * 맵 버전을 체크 후, 버튼을 활성화 한다.
     * @author FIESTA
     * @since  오후 11:40
     **/
    private void checkMapVersion() throws JSONException {

        final TextView tvMapUpdate = (TextView) this.findViewById(R.id.tv_map_update);
        final LinearLayout llMapUpdate = (LinearLayout) this.findViewById(R.id.ll_map_update);

        try {

            this.vectorVersion = lanVersion(1);
            this.demVersion = lanVersion(2);

            JSONObject newestVectorObject = new JSONObject(StorageUtil.getStorageModeEx(NhsAppInfoActivity.this, MAP_VERSION_VECTOR, ""));
            JSONObject newestDemObject = new JSONObject(StorageUtil.getStorageModeEx(NhsAppInfoActivity.this, MAP_VERSION_DEM, ""));

            this.newstVectorVersion = newestVectorObject.optString("MAP_VER");
            this.newestDemVersion = newestDemObject.optString("MAP_VER");

             // TODO: 2017-11-01 추후 int를 형식에 맞게 string으로 변경하는 작업이 필요하다.
            String strVectorVersion = this.vectorVersion + "";
            String strDemVersion = this.demVersion + "";

        }catch (Exception ex) {

        }
        // 버전이 같지 않으면 지도 업데이트 활성화
//        if (!(strVectorVersion.equals(newstVectorVersion) &&
//                strDemVersion.equals(newestDemVersion))) {

            tvMapUpdate.setTextColor(getResources().getColor(android.R.color.white));
            llMapUpdate.setBackgroundResource(R.drawable.border_corner_white);
            llMapUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(NhsAppInfoActivity.this, "지도 업데이트 시작되었습니다. \n 잠시만 기다려주새요", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            copyAssets("Dem.dat", Environment.getExternalStorageDirectory() + "/Acc_Navi/Map_Data/Dem/");
                            copyAssets("MapMain.dat", Environment.getExternalStorageDirectory() + "/Acc_Navi/Map_Data/Vector/");
                            Toast.makeText(NhsAppInfoActivity.this, "지도 업데이트가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            tvMapUpdate.setTextColor(getResources().getColor(R.color.listDivider));
                            llMapUpdate.setBackgroundResource(R.drawable.border_corner_gray);
                        }
                    }, 1000);

                }
            });
//        }
    }

    /**
    * 레이아웃 설정
    * @author FIESTA
    * @since  오후 11:52
    **/
    private void setLayout(){

        this.llBgNewApp = (LinearLayout) findViewById(R.id.ll_bg_new_app);
        this.tvNewApp = (TextView) findViewById(R.id.tv_new_app);

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
