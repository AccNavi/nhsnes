package kr.go.molit.nhsnes.activity;

import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_AUTO;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_ID;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_MBR_ID;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_NAME;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_PWD;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_TOKEN_KEY;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_ACRFTCD;
import static kr.go.molit.nhsnes.activity.NhsMainActivity.MAP_VERSION_VECTOR;
import static kr.go.molit.nhsnes.activity.NhsMainActivity.MAP_VERSION_DEM;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.dreamsecurity.e2e.MagicSE2;
import com.modim.lan.lanandroid.INativeImple;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import kr.go.molit.nhsnes.Network.NetworkProcess;
import kr.go.molit.nhsnes.NhsApplication;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.NetworkParamUtil;
import kr.go.molit.nhsnes.common.NetworkUrlUtil;
import kr.go.molit.nhsnes.common.SharedData;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.dialog.DialogSelectStorage;
import kr.go.molit.nhsnes.dialog.DialogType1;
import kr.go.molit.nhsnes.dialog.DialogType2;

/**
 * 인트로
 *
 * @author 정제영
 * @version 1.0, 2017.03.06 최초 작성
 **/

public class NhsIntroActivity extends NhsBaseFragmentActivity {
    byte[]	byRootCert = {
            (byte)0x30,(byte)0x82,(byte)0x03,(byte)0x5B,(byte)0x30,(byte)0x82,(byte)0x02,(byte)0x43,(byte)0xA0,(byte)0x03,(byte)0x02,(byte)0x01,(byte)0x02,(byte)0x02,(byte)0x01,(byte)0x01,
            (byte)0x30,(byte)0x0D,(byte)0x06,(byte)0x09,(byte)0x2A,(byte)0x86,(byte)0x48,(byte)0x86,(byte)0xF7,(byte)0x0D,(byte)0x01,(byte)0x01,(byte)0x0B,(byte)0x05,(byte)0x00,(byte)0x30,
            (byte)0x5D,(byte)0x31,(byte)0x0B,(byte)0x30,(byte)0x09,(byte)0x06,(byte)0x03,(byte)0x55,(byte)0x04,(byte)0x06,(byte)0x13,(byte)0x02,(byte)0x4B,(byte)0x52,(byte)0x31,(byte)0x16,
            (byte)0x30,(byte)0x14,(byte)0x06,(byte)0x03,(byte)0x55,(byte)0x04,(byte)0x0A,(byte)0x0C,(byte)0x0D,(byte)0x44,(byte)0x72,(byte)0x65,(byte)0x61,(byte)0x6D,(byte)0x53,(byte)0x65,
            (byte)0x63,(byte)0x75,(byte)0x72,(byte)0x69,(byte)0x74,(byte)0x79,(byte)0x31,(byte)0x12,(byte)0x30,(byte)0x10,(byte)0x06,(byte)0x03,(byte)0x55,(byte)0x04,(byte)0x0B,(byte)0x0C,
            (byte)0x09,(byte)0x4D,(byte)0x61,(byte)0x67,(byte)0x69,(byte)0x63,(byte)0x53,(byte)0x45,(byte)0x76,(byte)0x32,(byte)0x31,(byte)0x22,(byte)0x30,(byte)0x20,(byte)0x06,(byte)0x03,
            (byte)0x55,(byte)0x04,(byte)0x03,(byte)0x0C,(byte)0x19,(byte)0x44,(byte)0x72,(byte)0x65,(byte)0x61,(byte)0x6D,(byte)0x53,(byte)0x65,(byte)0x63,(byte)0x75,(byte)0x72,(byte)0x69,
            (byte)0x74,(byte)0x79,(byte)0x20,(byte)0x72,(byte)0x6F,(byte)0x6F,(byte)0x74,(byte)0x43,(byte)0x41,(byte)0x20,(byte)0x32,(byte)0x30,(byte)0x34,(byte)0x38,(byte)0x30,(byte)0x1E,
            (byte)0x17,(byte)0x0D,(byte)0x31,(byte)0x31,(byte)0x30,(byte)0x38,(byte)0x32,(byte)0x39,(byte)0x30,(byte)0x32,(byte)0x33,(byte)0x31,(byte)0x31,(byte)0x30,(byte)0x5A,(byte)0x17,
            (byte)0x0D,(byte)0x33,(byte)0x36,(byte)0x30,(byte)0x38,(byte)0x32,(byte)0x39,(byte)0x30,(byte)0x32,(byte)0x33,(byte)0x31,(byte)0x31,(byte)0x30,(byte)0x5A,(byte)0x30,(byte)0x5D,
            (byte)0x31,(byte)0x0B,(byte)0x30,(byte)0x09,(byte)0x06,(byte)0x03,(byte)0x55,(byte)0x04,(byte)0x06,(byte)0x13,(byte)0x02,(byte)0x4B,(byte)0x52,(byte)0x31,(byte)0x16,(byte)0x30,
            (byte)0x14,(byte)0x06,(byte)0x03,(byte)0x55,(byte)0x04,(byte)0x0A,(byte)0x0C,(byte)0x0D,(byte)0x44,(byte)0x72,(byte)0x65,(byte)0x61,(byte)0x6D,(byte)0x53,(byte)0x65,(byte)0x63,
            (byte)0x75,(byte)0x72,(byte)0x69,(byte)0x74,(byte)0x79,(byte)0x31,(byte)0x12,(byte)0x30,(byte)0x10,(byte)0x06,(byte)0x03,(byte)0x55,(byte)0x04,(byte)0x0B,(byte)0x0C,(byte)0x09,
            (byte)0x4D,(byte)0x61,(byte)0x67,(byte)0x69,(byte)0x63,(byte)0x53,(byte)0x45,(byte)0x76,(byte)0x32,(byte)0x31,(byte)0x22,(byte)0x30,(byte)0x20,(byte)0x06,(byte)0x03,(byte)0x55,
            (byte)0x04,(byte)0x03,(byte)0x0C,(byte)0x19,(byte)0x44,(byte)0x72,(byte)0x65,(byte)0x61,(byte)0x6D,(byte)0x53,(byte)0x65,(byte)0x63,(byte)0x75,(byte)0x72,(byte)0x69,(byte)0x74,
            (byte)0x79,(byte)0x20,(byte)0x72,(byte)0x6F,(byte)0x6F,(byte)0x74,(byte)0x43,(byte)0x41,(byte)0x20,(byte)0x32,(byte)0x30,(byte)0x34,(byte)0x38,(byte)0x30,(byte)0x82,(byte)0x01,
            (byte)0x22,(byte)0x30,(byte)0x0D,(byte)0x06,(byte)0x09,(byte)0x2A,(byte)0x86,(byte)0x48,(byte)0x86,(byte)0xF7,(byte)0x0D,(byte)0x01,(byte)0x01,(byte)0x01,(byte)0x05,(byte)0x00,
            (byte)0x03,(byte)0x82,(byte)0x01,(byte)0x0F,(byte)0x00,(byte)0x30,(byte)0x82,(byte)0x01,(byte)0x0A,(byte)0x02,(byte)0x82,(byte)0x01,(byte)0x01,(byte)0x00,(byte)0xD3,(byte)0xB7,
            (byte)0x6D,(byte)0x74,(byte)0x8F,(byte)0x09,(byte)0x63,(byte)0x3D,(byte)0xD7,(byte)0x8B,(byte)0x51,(byte)0x29,(byte)0xE1,(byte)0x16,(byte)0x8D,(byte)0x86,(byte)0x21,(byte)0x1F,
            (byte)0x13,(byte)0x95,(byte)0x7B,(byte)0x3E,(byte)0x9B,(byte)0xC1,(byte)0xE8,(byte)0xC4,(byte)0x7D,(byte)0xD1,(byte)0x64,(byte)0xE1,(byte)0x88,(byte)0x66,(byte)0x3C,(byte)0xA6,
            (byte)0x14,(byte)0xA9,(byte)0xCA,(byte)0xF4,(byte)0xE6,(byte)0x88,(byte)0xB9,(byte)0xC2,(byte)0x98,(byte)0x84,(byte)0x4D,(byte)0xD4,(byte)0xA9,(byte)0x4E,(byte)0xBD,(byte)0x07,
            (byte)0x82,(byte)0x2D,(byte)0xE4,(byte)0x3F,(byte)0x0D,(byte)0x02,(byte)0x07,(byte)0x7E,(byte)0x43,(byte)0xBB,(byte)0x7F,(byte)0x59,(byte)0xBC,(byte)0x2C,(byte)0xC0,(byte)0x6F,
            (byte)0x6F,(byte)0xF6,(byte)0x9E,(byte)0xBF,(byte)0xD9,(byte)0x57,(byte)0x22,(byte)0x38,(byte)0x6E,(byte)0x94,(byte)0x1A,(byte)0x55,(byte)0xE4,(byte)0x95,(byte)0x68,(byte)0xB6,
            (byte)0x0A,(byte)0xDF,(byte)0x60,(byte)0xA7,(byte)0xEF,(byte)0x1C,(byte)0x79,(byte)0x90,(byte)0xE0,(byte)0xDB,(byte)0x8B,(byte)0x2B,(byte)0x1E,(byte)0xC6,(byte)0x4E,(byte)0x01,
            (byte)0xC9,(byte)0xEF,(byte)0xBF,(byte)0x08,(byte)0xB4,(byte)0x74,(byte)0xDE,(byte)0xBE,(byte)0x73,(byte)0xCD,(byte)0x09,(byte)0xB3,(byte)0x2B,(byte)0x4E,(byte)0xB7,(byte)0x5C,
            (byte)0x95,(byte)0xBF,(byte)0xC1,(byte)0x58,(byte)0xFD,(byte)0x73,(byte)0xAF,(byte)0x61,(byte)0x44,(byte)0x8C,(byte)0x5B,(byte)0x42,(byte)0xBA,(byte)0x21,(byte)0x84,(byte)0x00,
            (byte)0xBC,(byte)0x77,(byte)0x42,(byte)0xF3,(byte)0xFA,(byte)0xDF,(byte)0xA1,(byte)0xD1,(byte)0xAA,(byte)0x38,(byte)0x3C,(byte)0x0F,(byte)0xC8,(byte)0x89,(byte)0xF4,(byte)0xAE,
            (byte)0x84,(byte)0xE2,(byte)0xF8,(byte)0x0C,(byte)0xC1,(byte)0x7C,(byte)0x2F,(byte)0x7C,(byte)0x43,(byte)0xFD,(byte)0x1C,(byte)0xF2,(byte)0x53,(byte)0x9D,(byte)0x6A,(byte)0xBB,
            (byte)0x0B,(byte)0xC1,(byte)0xDA,(byte)0x37,(byte)0xF0,(byte)0x36,(byte)0xBB,(byte)0x73,(byte)0x1D,(byte)0x7A,(byte)0x73,(byte)0x61,(byte)0x6C,(byte)0x95,(byte)0x4D,(byte)0xA7,
            (byte)0xF3,(byte)0xA3,(byte)0xE7,(byte)0x2E,(byte)0xEB,(byte)0x35,(byte)0x88,(byte)0x96,(byte)0xDE,(byte)0xA3,(byte)0x34,(byte)0x62,(byte)0xD4,(byte)0x8D,(byte)0x6D,(byte)0x6C,
            (byte)0x1D,(byte)0x38,(byte)0x6C,(byte)0x54,(byte)0xB0,(byte)0x5F,(byte)0xF6,(byte)0x99,(byte)0xB9,(byte)0x63,(byte)0x2E,(byte)0x15,(byte)0x3D,(byte)0xB5,(byte)0x9B,(byte)0x98,
            (byte)0xF3,(byte)0xC0,(byte)0x37,(byte)0x7D,(byte)0xFF,(byte)0x61,(byte)0x19,(byte)0x20,(byte)0xF1,(byte)0x04,(byte)0xA3,(byte)0x0A,(byte)0xA9,(byte)0x24,(byte)0xBF,(byte)0xDE,
            (byte)0x46,(byte)0xA5,(byte)0xFD,(byte)0x54,(byte)0x49,(byte)0xF7,(byte)0x1A,(byte)0x13,(byte)0xBD,(byte)0xEB,(byte)0xF9,(byte)0x97,(byte)0xD6,(byte)0x2F,(byte)0xE5,(byte)0x01,
            (byte)0x3B,(byte)0x4D,(byte)0x27,(byte)0x84,(byte)0x40,(byte)0x1F,(byte)0xB1,(byte)0xE6,(byte)0xF6,(byte)0x75,(byte)0x6A,(byte)0xCC,(byte)0x2F,(byte)0x29,(byte)0x02,(byte)0x03,
            (byte)0x01,(byte)0x00,(byte)0x01,(byte)0xA3,(byte)0x26,(byte)0x30,(byte)0x24,(byte)0x30,(byte)0x12,(byte)0x06,(byte)0x03,(byte)0x55,(byte)0x1D,(byte)0x13,(byte)0x01,(byte)0x01,
            (byte)0xFF,(byte)0x04,(byte)0x08,(byte)0x30,(byte)0x06,(byte)0x01,(byte)0x01,(byte)0xFF,(byte)0x02,(byte)0x01,(byte)0x00,(byte)0x30,(byte)0x0E,(byte)0x06,(byte)0x03,(byte)0x55,
            (byte)0x1D,(byte)0x0F,(byte)0x01,(byte)0x01,(byte)0xFF,(byte)0x04,(byte)0x04,(byte)0x03,(byte)0x02,(byte)0x02,(byte)0x04,(byte)0x30,(byte)0x0D,(byte)0x06,(byte)0x09,(byte)0x2A,
            (byte)0x86,(byte)0x48,(byte)0x86,(byte)0xF7,(byte)0x0D,(byte)0x01,(byte)0x01,(byte)0x0B,(byte)0x05,(byte)0x00,(byte)0x03,(byte)0x82,(byte)0x01,(byte)0x01,(byte)0x00,(byte)0x33,
            (byte)0xFC,(byte)0xD1,(byte)0x52,(byte)0x03,(byte)0xBB,(byte)0x09,(byte)0xA0,(byte)0xF9,(byte)0x5C,(byte)0xCD,(byte)0x97,(byte)0x03,(byte)0x21,(byte)0xD7,(byte)0xB5,(byte)0x1C,
            (byte)0x52,(byte)0xFC,(byte)0x50,(byte)0x71,(byte)0x9B,(byte)0x01,(byte)0xD4,(byte)0xDC,(byte)0x96,(byte)0xF0,(byte)0x86,(byte)0x21,(byte)0x60,(byte)0x0F,(byte)0x61,(byte)0x46,
            (byte)0x00,(byte)0x85,(byte)0x0C,(byte)0x7E,(byte)0x18,(byte)0xAF,(byte)0x51,(byte)0x3E,(byte)0x7C,(byte)0xC6,(byte)0x06,(byte)0x24,(byte)0x8F,(byte)0x60,(byte)0x0A,(byte)0x6B,
            (byte)0xC9,(byte)0x87,(byte)0x48,(byte)0x34,(byte)0x6F,(byte)0xD6,(byte)0xAF,(byte)0x1E,(byte)0xA2,(byte)0xAB,(byte)0x5D,(byte)0x7C,(byte)0xD8,(byte)0xFD,(byte)0x73,(byte)0x87,
            (byte)0x68,(byte)0xB8,(byte)0x05,(byte)0xB9,(byte)0x4F,(byte)0x19,(byte)0xF5,(byte)0x12,(byte)0x04,(byte)0xF8,(byte)0xD4,(byte)0xBA,(byte)0xAB,(byte)0xD3,(byte)0xAA,(byte)0x84,
            (byte)0xEF,(byte)0xB1,(byte)0xA5,(byte)0x17,(byte)0xE5,(byte)0xF9,(byte)0xE4,(byte)0xAF,(byte)0x7B,(byte)0x0C,(byte)0x63,(byte)0x98,(byte)0xF1,(byte)0x40,(byte)0xA5,(byte)0x9D,
            (byte)0x8A,(byte)0x24,(byte)0x73,(byte)0xA9,(byte)0x87,(byte)0xEE,(byte)0xCD,(byte)0x9C,(byte)0x12,(byte)0x22,(byte)0x59,(byte)0xC5,(byte)0xE8,(byte)0x16,(byte)0xD9,(byte)0x6C,
            (byte)0xA3,(byte)0x57,(byte)0x00,(byte)0x50,(byte)0x10,(byte)0x3F,(byte)0x7F,(byte)0x7A,(byte)0xB0,(byte)0xA2,(byte)0xE7,(byte)0x09,(byte)0xCE,(byte)0xC5,(byte)0x9A,(byte)0xD1,
            (byte)0x3F,(byte)0xF5,(byte)0x06,(byte)0x0F,(byte)0x84,(byte)0xA4,(byte)0xE5,(byte)0xC2,(byte)0xF2,(byte)0x6E,(byte)0xA4,(byte)0x2D,(byte)0x9D,(byte)0x2E,(byte)0x5A,(byte)0xE0,
            (byte)0x00,(byte)0xED,(byte)0xC2,(byte)0x4B,(byte)0x43,(byte)0x27,(byte)0xC1,(byte)0x11,(byte)0x85,(byte)0x12,(byte)0xD2,(byte)0x6E,(byte)0xAE,(byte)0xC2,(byte)0xFB,(byte)0x13,
            (byte)0x8C,(byte)0x01,(byte)0x07,(byte)0xC0,(byte)0x4A,(byte)0xCE,(byte)0x90,(byte)0x39,(byte)0x87,(byte)0x1D,(byte)0x27,(byte)0xAB,(byte)0xC3,(byte)0x53,(byte)0x69,(byte)0x4C,
            (byte)0x43,(byte)0x2C,(byte)0xE2,(byte)0x2B,(byte)0x37,(byte)0x63,(byte)0x46,(byte)0x1F,(byte)0xF1,(byte)0xE0,(byte)0x20,(byte)0x05,(byte)0x21,(byte)0xCF,(byte)0xC1,(byte)0x1F,
            (byte)0x1F,(byte)0x56,(byte)0xD4,(byte)0x60,(byte)0x73,(byte)0x09,(byte)0x77,(byte)0x5A,(byte)0x54,(byte)0xE1,(byte)0x4F,(byte)0x54,(byte)0x21,(byte)0xD4,(byte)0xD3,(byte)0x23,
            (byte)0x3D,(byte)0xEC,(byte)0x75,(byte)0x29,(byte)0x91,(byte)0xCF,(byte)0xFA,(byte)0xA0,(byte)0xD9,(byte)0x59,(byte)0x31,(byte)0x69,(byte)0xA9,(byte)0x3E,(byte)0xD6,(byte)0x99,
            (byte)0xD2,(byte)0xF4,(byte)0x7F,(byte)0x37,(byte)0x87,(byte)0x34,(byte)0x62,(byte)0x99,(byte)0x9F,(byte)0x7C,(byte)0x47,(byte)0x36,(byte)0x64,(byte)0xBE,(byte)0xB9,(byte)0x02,
            (byte)0x6C,(byte)0x10,(byte)0xD6,(byte)0xEA,(byte)0x92,(byte)0x36,(byte)0xCC,(byte)0xD6,(byte)0xE2,(byte)0x24,(byte)0xC7,(byte)0x04,(byte)0x6A,(byte)0xC5,(byte)0x59
    };

    private static final int PERMISSION_REQUEST_READ = 1000;
    private static final int CODE_WRITE_SETTINGS_PERMISSION = 2010;

    Context mContext;

    //버튼 두개짜리 팝업(개인정보 수집, 이용동의, GPS 오차범위 확인 창)
    DialogType2 mAgreePopup;
    DialogSelectStorage mStoragePopup;
    private DialogType1 mPopup;

    //버튼 두개짜리 팝업(데이터요금 안내 팝업)
    DialogType1 mPopup1;
    DialogType1 mPopup2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_intro);

        mContext = NhsIntroActivity.this;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestPermission();

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        if (requestCode == 100) {
            if(grantResults.length == 6
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED
                && grantResults[3] == PackageManager.PERMISSION_GRANTED
                && grantResults[4] == PackageManager.PERMISSION_GRANTED
                && grantResults[5] == PackageManager.PERMISSION_GRANTED) {

                boolean isNext = checkSettingPermissionCode(NhsIntroActivity.this);

                if (isNext) {
                    showLoadingBar(mContext);
                    startInit();
                }
            } else {
                Toast.makeText(mContext, "[설정]->[권한]에서 모든 권한을 활성화해주세요. ", Toast.LENGTH_LONG).show();
                // Permission was denied or request was cancelled
                finish();
            }
        }
    }

    @TargetApi(23)
    private void requestPermission() {
        // Permission#asArray는 내부 arrayOf 함수가 있으므로 사용하지않음
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            // only for gingerbread and newer versions
            if ( checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                )
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_SMS}, 100);
            } else {

                boolean isNext = checkSettingPermissionCode(NhsIntroActivity.this);

                if (isNext) {
                    showLoadingBar(mContext);
                    startInit();
                }
            }
        } else {
            showLoadingBar(mContext);
            startInit();
        }

    }

    private void startInit() {
        IntroTimer timer = new IntroTimer(1000, 1);
        timer.start();
    }

    public class IntroTimer extends CountDownTimer {

        public IntroTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            dismissLoadingBar(mContext);

//            agreeDialog();
            requestServerCert();
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }

    //개인정보 수집, 이용동의 팝업
    private void agreeDialog() {
        mPopup = new DialogType1(mContext, getString(R.string.dialog_agree_title), getString(R.string.dialog_agree_msg), getString(R.string.btn_confirm), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopup.hideDialog();
                gpsDialog();
            }
        }, getString(R.string.btn_cancel), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mPopup.hideDialog();
                finish();
            }
        });
    }

    //GPS 오차범위 안내
    private void gpsDialog() {

        mAgreePopup = new DialogType2(mContext, getString(R.string.dialog_gps_notice_title), getString(R.string.dialog_gps_notice_msg), getString(R.string.btn_confirm), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAgreePopup.hideDialog();
                selectStorageDialog();
            }
        });


    }

    private void selectStorageDialog() {
        /*
        if (!StorageUtil.isAvailableExternalStorage()) {
            StorageUtil.setStorageMode(getContext(), StorageUtil.Storage.LOCAL);
        }
        */

        if (StorageUtil.getStorageMode(getContext()) == null) {
            mStoragePopup = new DialogSelectStorage(this, getString(R.string.dialog_select_storage_title), getString(R.string.btn_confirm), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StorageUtil.Storage saveStorageType = mStoragePopup.getSaveType();
                    if (saveStorageType != StorageUtil.Storage.LOCAL && saveStorageType != StorageUtil.Storage.EXTERNAL) {
                        Toast.makeText(mContext, "저장 매체를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    StorageUtil.setStorageMode(getContext(), saveStorageType);
                    ((NhsApplication) getApplication()).realmSetUp();
                    mStoragePopup.hideDialog();

                    boolean isAutoLogin = autoLogin();

                    if (!isAutoLogin) {
                        Intent intent = new Intent(mContext, NhsLoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }, getString(R.string.btn_cancel), new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mStoragePopup.hideDialog();
                    finish();
                }
            });
        } else {

            boolean isAutoLogin = autoLogin();

            if (!isAutoLogin) {
                Intent intent = new Intent(mContext, NhsLoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    /**
    * 자동 로그인
    * @author FIESTA
    * @since  오후 11:11
    **/
    private boolean autoLogin(){

        boolean isAutoLogin = StorageUtil.getStorageMode(NhsIntroActivity.this, LOGIN_AUTO);

        if (isAutoLogin) {

            String id = StorageUtil.getStorageModeEx(NhsIntroActivity.this, LOGIN_ID);
            String pwd = StorageUtil.getStorageModeEx(NhsIntroActivity.this, LOGIN_PWD);

            NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
            NetworkParamUtil networkParamUtil = new NetworkParamUtil();

            // 로그인 요청
            StringEntity param = networkParamUtil.requestLogin(id, pwd);
            NetworkProcess networkProcess = new NetworkProcess(NhsIntroActivity.this,
                networkUrlUtil.getUserLogin(),
                param,
                loginResult, true);
            networkProcess.sendEmptyMessage(0);

        }

        return isAutoLogin;

    }

    /**
     * 로그인
     *
     * @author FIESTA
     * @since 오전 12:36
     **/
    NetworkProcess.OnResultListener loginResult = new NetworkProcess.OnResultListener() {
        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
            try {

                new ToastUtile().showCenterText(NhsIntroActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

            } catch (Exception ex) {

            } finally {

                Intent intent = new Intent(NhsIntroActivity.this, NhsLoginActivity.class);
                startActivity(intent);
                finish();

            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
            try {

                new ToastUtile().showCenterText(NhsIntroActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

            } catch (Exception ex) {

            } finally {

                Intent intent = new Intent(NhsIntroActivity.this, NhsLoginActivity.class);
                startActivity(intent);
                finish();

            }
        }

        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

            String msg = response.optString("result_msg");
            String resultCode = response.optString("result_code");

            // 메세지 출력
            new ToastUtile().showCenterText(NhsIntroActivity.this, msg);

            if (resultCode.equalsIgnoreCase("Y")) {

                // 로그아웃 혹은 기타 등등에 사용할 저장값 저장
                StorageUtil.setStorageMode(NhsIntroActivity.this, LOGIN_MBR_ID, response.optString("mbrId"));
                StorageUtil.setStorageMode(NhsIntroActivity.this, LOGIN_TOKEN_KEY, response.optString("tokenKey"));
                StorageUtil.setStorageMode(NhsIntroActivity.this, LOGIN_NAME, response.optString("mbrNm"));
                StorageUtil.setStorageMode(NhsIntroActivity.this, LOGIN_ID, response.optString("loginId"));
                StorageUtil.setStorageMode(NhsIntroActivity.this, LOGIN_ACRFTCD, response.optString("acrftCd"));

                // 맵 정보를 받는다.
                getMapVersion(response.optString("tokenKey"), 1, MAP_VERSION_VECTOR);
                getMapVersion(response.optString("tokenKey"), 3, MAP_VERSION_DEM);

                dataDialog();

            } else {

                Intent intent = new Intent(NhsIntroActivity.this, NhsLoginActivity.class);
                startActivity(intent);
                finish();

            }

        }


    };


    /**
     * 맵 버전을 조회해서 저장한다.
     *
     * @author FIESTA
     * @since 오전 12:36
     **/
    private void getMapVersion(String sessionId, int mapType, final String saveKey){

        NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
        NetworkParamUtil networkParamUtil = new NetworkParamUtil();
        StringEntity param = networkParamUtil.getMapVersion(sessionId, mapType);

        NetworkProcess networkProcess = new NetworkProcess(NhsIntroActivity.this,
                networkUrlUtil.getMapVersion(),
                param,
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
                                StorageUtil.setStorageMode(NhsIntroActivity.this, saveKey, response.optJSONArray("result_data").get(0).toString());
                            }catch (Exception ex) {

                            }
                        }
                    }
                }, false);
        networkProcess.sendEmptyMessage(0);
    }

    //데이터 요금 안내
    private void dataDialog() {

        ConnectivityManager connect = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        if (connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            syncAgreeDialog();

        } else {
            mPopup1 = new DialogType1(mContext, getString(R.string.dialog_data_notice_title), getString(R.string.dialog_data_notice_msg), getString(R.string.btn_agree), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopup1.hideDialog();
                    syncAgreeDialog();
                }
            }, getString(R.string.btn_cancel), new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mPopup1.hideDialog();
                    finish();
                }
            });
        }
    }

    //아이디와 비행정보 연동 정보
    private void syncAgreeDialog() {
        mPopup2 = new DialogType1(mContext, "아이디와 비행정보 연동 동의", "내비게이션은 사용자의  ID와 비행계획서로 제출한 비행정보를  연동하여  단말기와  비행계획서를 관리하는  시스템에서 활용하도록  동의 합니다.", getString(R.string.btn_confirm), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopup2.hideDialog();
                Intent intent = new Intent(NhsIntroActivity.this, NhsMainActivity.class);
                startActivity(intent);
                finish();
            }
        }, null, null);
    }

    public boolean checkSettingPermissionCode(Activity context){
        boolean permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = Settings.System.canWrite(context);
        } else {
            permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }
        if (permission) {
            return true;
        }  else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivityForResult(intent, CODE_WRITE_SETTINGS_PERMISSION);
                new ToastUtile().showCenterText(NhsIntroActivity.this, "시스템 설정을 위한 필수 권한입니다. 체크해주세요");
            } else {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_SETTINGS}, CODE_WRITE_SETTINGS_PERMISSION);
            }
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_WRITE_SETTINGS_PERMISSION && Settings.System.canWrite(this)){
            Log.d("TAG", "CODE_WRITE_SETTINGS_PERMISSION success");
            showLoadingBar(mContext);
            startInit();
        }
    }

    /**
     * 정제영(암호화)
     * 서버 인증서 요청
     **/
    private void requestServerCert(){
        NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
        NetworkParamUtil networkParamUtil = new NetworkParamUtil();

        StringEntity param = networkParamUtil.requestServerCert(mContext);
        NetworkProcess networkProcess = new NetworkProcess(mContext, networkUrlUtil.getRequestServerCert(), param, serverCertResult, false, true);
        networkProcess.sendEmptyMessage(0);
    }

    NetworkProcess.OnResultListener serverCertResult = new NetworkProcess.OnResultListener() {
        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
            try {
                new ToastUtile().showCenterText(mContext, getString(R.string.error_network) + "(" + statusCode + ")");
                finish();
            } catch (Exception ex) {

            } finally {
            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
            try {
                new ToastUtile().showCenterText(mContext, getString(R.string.error_network) + "(" + statusCode + ")");
                finish();
            } catch (Exception ex) {
            } finally {
            }
        }

        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

            String msg = response.optString("result_msg");
            String resultCode = response.optString("result_code");

            if (resultCode.equalsIgnoreCase("Y")) {
                MagicSE2 magicSE = null;
                String sessionKey = null;
                String encData = null;
                try {
                    magicSE = new MagicSE2(mContext);
                    sessionKey = magicSE.MagicSE_MakeSessionKey( response.optString("server_cert"), null, byRootCert );
                    //세션키 저장
                    SharedData.saveSharedData(mContext, SharedData.SESSION_KEY, sessionKey);
                    //서버 오류가 나서 적용 못함
                    agreeDialog();
//                    sendSessionKey();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {  //실패
                new ToastUtile().showCenterText(mContext, msg);
                finish();
            }

        }
    };

    /**
     * 정제영(암호화)
     * 세션키 전송
     **/
    private void sendSessionKey(){
        NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
        NetworkParamUtil networkParamUtil = new NetworkParamUtil();

        StringEntity param = networkParamUtil.sendSessionKey(mContext);
        NetworkProcess networkProcess = new NetworkProcess(mContext, networkUrlUtil.getSendSesstionKey(), param, sendSessionKeyResult, true);
        networkProcess.sendEmptyMessage(0);
    }

    /**
     * 세션키 전송
     **/
    NetworkProcess.OnResultListener sendSessionKeyResult = new NetworkProcess.OnResultListener() {
        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
            Log.d("TEST", "responseString ==> : "+responseString);
            try {
                new ToastUtile().showCenterText(mContext, getString(R.string.error_network) + "(" + statusCode + ")");
                finish();
            } catch (Exception ex) {

            } finally {
            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
            try {
                new ToastUtile().showCenterText(mContext, getString(R.string.error_network) + "(" + statusCode + ")");
                finish();
            } catch (Exception ex) {

            } finally {
            }
        }

        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
            String msg = response.optString("result_msg");
            String resultCode = response.optString("result_code");

            if (resultCode.equalsIgnoreCase("Y")) {
                //개인정보 수집, 이용동의 팝업창 호출
                agreeDialog();
            } else {  //실패
                new ToastUtile().showCenterText(mContext, msg);
                finish();
            }

        }
    };
}
