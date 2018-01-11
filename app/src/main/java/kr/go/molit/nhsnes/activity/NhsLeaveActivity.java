package kr.go.molit.nhsnes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.entity.StringEntity;
import kr.go.molit.nhsnes.Network.NetworkProcess;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.NetworkParamUtil;
import kr.go.molit.nhsnes.common.NetworkUrlUtil;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.dialog.DialogType1;

import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_ID;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_MBR_ID;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_NAME;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_PWD;

/**
 * 회원탈퇴
 *
 * @author FIESTA
 * @since 오후 12:14
 **/
public class NhsLeaveActivity extends NhsBaseFragmentActivity implements View.OnClickListener {

    private EditText edPhoneNumber = null;
    private EditText edAuthCode = null;
    private EditText edId = null;
    private EditText edName = null;
    private EditText edPwd = null;
    private EditText edWhy = null;
    private TextView tvCompany = null;

    private DialogType1 mPopup1 = null;
    private PopupWindow popupWindow = null;

    private boolean isCheckedAuthCode = false;

    private String authCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_leave);

        // 레이아웃 설정
        setLayout();

    }

    /**
    * 레이아웃 설정
    * @author FIESTA
    * @version 1.0.0
    * @since 오후 5:20
    **/
    private void setLayout() {

        this.edPhoneNumber = (EditText) this.findViewById(R.id.ed_phone);
        this.edAuthCode = (EditText) this.findViewById(R.id.ed_auth_code);
        this.edId = (EditText) this.findViewById(R.id.ed_id);
        this.edName = (EditText) this.findViewById(R.id.ed_name);
        this.edPwd = (EditText) this.findViewById(R.id.ed_pwd);
        this.edWhy = (EditText) this.findViewById(R.id.ed_why);
        this.tvCompany = (TextView) this.findViewById(R.id.tv_company);

        findViewById(R.id.fl_leave).setOnClickListener(this);
        findViewById(R.id.btn_request_auth_code).setOnClickListener(this);
        findViewById(R.id.btn_auth_code).setOnClickListener(this);

        String myId = StorageUtil.getStorageModeEx(NhsLeaveActivity.this, LOGIN_ID);
        String myName = StorageUtil.getStorageModeEx(NhsLeaveActivity.this, LOGIN_NAME);

        this.edId.setText(myId);
        this.edName.setText(myName);

    }

    /**
     * 유저 아이디 중복 체크 다이얼로그
     *
     * @author FIESTA
     * @since 오후 10:46
     **/
    private void checkUserIdDialog() {
        mPopup1 = new DialogType1(getContext(), "중복확인", "사용자 아이디 중복 확인을 해주셔야 합니다.", getString(R.string.btn_agree), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopup1.hideDialog();
            }
        }, "", null);
    }

    @Override
    public void onClick(View view) {

        String phoneNumber = this.edPhoneNumber.getText().toString();
        String name = this.edName.getText().toString();
        String authCode = this.edAuthCode.getText().toString();
        String id = this.edId.getText().toString();
        String pwd = this.edPwd.getText().toString();
        String why = this.edWhy.getText().toString();

        switch (view.getId()) {


            case R.id.btn_auth_code:

                if (authCode.isEmpty()) {
                    Toast.makeText(getContext(), "수신한 인증번호를 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                } else {

                    NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
                    NetworkParamUtil networkParamUtil = new NetworkParamUtil();

                    // 인증코드 확인 요청
                    StringEntity param = networkParamUtil.requestCheckAuthCode(authCode, phoneNumber);
                    NetworkProcess networkProcess = new NetworkProcess(NhsLeaveActivity.this,
                            networkUrlUtil.getRequestCheckAuthCode(),
                            param,
                            checkAuthCodeResult, true);
                    networkProcess.sendEmptyMessage(0);

                }

                break;
            case R.id.btn_request_auth_code:

                if (phoneNumber.isEmpty()) {
                    Toast.makeText(getContext(), "휴대폰 번호를 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                } else if (name.isEmpty()) {
                    Toast.makeText(getContext(), "이름을 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                } else {
                    NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
                    NetworkParamUtil networkParamUtil = new NetworkParamUtil();

                    // 인증코드 요청
                    StringEntity param = networkParamUtil.requestAuthCode(phoneNumber, name);
                    NetworkProcess networkProcess = new NetworkProcess(NhsLeaveActivity.this,
                            networkUrlUtil.getRequestAuthCode(),
                            param,
                            requestAuthCodeResult, true);
                    networkProcess.sendEmptyMessage(0);
                }

                break;


            // 회원가입
            case R.id.fl_leave:

                if (phoneNumber.isEmpty()) {
                    Toast.makeText(getContext(), "휴대폰 번호을 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                }
//        else if (!isCheckedAuthCode) {
//          Toast.makeText(getContext(), "인증번호 확인을 진행해 주십시오.", Toast.LENGTH_SHORT).show();
//        }
                else if (id.isEmpty()) {
                    Toast.makeText(getContext(), "아이디을 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                } else if (name.isEmpty()) {
                    Toast.makeText(getContext(), "이름을 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                } else if (pwd.isEmpty()) {
                    Toast.makeText(getContext(), "비밀번호을 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                } else if (why.isEmpty()) {
                    Toast.makeText(getContext(), "탈퇴 사유를 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                } else {

                    String mbrId = StorageUtil.getStorageModeEx(NhsLeaveActivity.this, LOGIN_MBR_ID);

                    NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
                    NetworkParamUtil networkParamUtil = new NetworkParamUtil();

                    // 회원탈퇴 요청
                    StringEntity param = networkParamUtil.getLeave(mbrId, id, pwd, why);
                    NetworkProcess networkProcess = new NetworkProcess(NhsLeaveActivity.this,
                            networkUrlUtil.getLeave(),
                            param,
                            leaveResult, true);
                    networkProcess.sendEmptyMessage(0);

                }


                break;

            case R.id.tv_company:

                togglePopup();

                break;


        }

    }


    /**
     * 소속사 팝업창 토글
     *
     * @author FIESTA
     * @since 오전 2:41
     **/
    private void togglePopup() {

        try {

            if (this.popupWindow != null) {


                if (this.popupWindow.isShowing()) {
                    this.popupWindow.dismiss();                 // 숨긴다
                } else {

                    this.popupWindow.showAsDropDown(findViewById(R.id.tv_company), 0, 0);   // 화면에 출력한다.

                }

            }

        } catch (Exception ex) {

        }


    }

    /**
     * 인증번호 체크 결과
     *
     * @author FIESTA
     * @since 오후 11:04
     **/
    NetworkProcess.OnResultListener checkAuthCodeResult = new NetworkProcess.OnResultListener() {
        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
            try {

                new ToastUtile().showCenterText(NhsLeaveActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

            } catch (Exception ex) {

            } finally {
            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
            try {

                new ToastUtile().showCenterText(NhsLeaveActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

            } catch (Exception ex) {

            } finally {
            }
        }

        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

            String msg = response.optString("result_msg");
            String resultCode = response.optString("result_code");

            if (resultCode.equalsIgnoreCase("Y")) {

                isCheckedAuthCode = true;

            }

            // 메세지 출력
            new ToastUtile().showCenterText(NhsLeaveActivity.this, msg);


        }


    };

    /**
     * 회원탈퇴 결과
     *
     * @author FIESTA
     * @since 오후 11:04
     **/
    NetworkProcess.OnResultListener leaveResult = new NetworkProcess.OnResultListener() {
        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
            try {

                new ToastUtile().showCenterText(NhsLeaveActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

            } catch (Exception ex) {

            } finally {
            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
            try {

                new ToastUtile().showCenterText(NhsLeaveActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

            } catch (Exception ex) {

            } finally {
            }
        }

        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

            String msg = response.optString("result_msg");
            String resultCode = response.optString("result_code");

            if (resultCode.equalsIgnoreCase("Y")) {

                int size = activityArrayList.size();
                int i = 0;

                for (i = 0; i < size; i++) {
                    try {
                        activityArrayList.get(i).finish();
                    } catch (Exception ex) {

                    }
                }


                // 동의 파일들을 모두 삭제한다.
                deleteAgreeFIles();

                Intent intent = new Intent(NhsLeaveActivity.this, NhsLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }

            // 메세지 출력
            new ToastUtile().showCenterText(NhsLeaveActivity.this, msg);


        }


    };

    /**
     * 동의 기록들을 삭제한다.
     *
     * @author FIESTA
     * @since 오후 11:04
     **/
    private void deleteAgreeFIles() {

        File file = null;

        file = new File(Environment.getExternalStorageDirectory() + "/ACC_Navi/private_agree_info.dat");

        if (file.exists()) {
            file.delete();
        }


        file = new File(Environment.getExternalStorageDirectory() + "/ACC_Navi/IDWithFlyInfoMapping.dat");

        if (file.exists()) {
            file.delete();
        }

        file = new File(Environment.getExternalStorageDirectory() + "/ACC_Navi/gps_agree_info.dat");

        if (file.exists()) {
            file.delete();
        }

        file = new File(Environment.getExternalStorageDirectory() + "/ACC_Navi/refer_agree_info.dat");

        if (file.exists()) {
            file.delete();
        }

        file = new File(Environment.getExternalStorageDirectory() + "/ACC_Navi/pay_agree_info.dat");

        if (file.exists()) {
            file.delete();
        }


    }

    /**
     * 인증번호 요청 결과
     *
     * @author FIESTA
     * @since 오후 11:04
     **/
    NetworkProcess.OnResultListener requestAuthCodeResult = new NetworkProcess.OnResultListener() {
        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
            try {

                new ToastUtile().showCenterText(NhsLeaveActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

            } catch (Exception ex) {

            } finally {
            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
            try {

                new ToastUtile().showCenterText(NhsLeaveActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

            } catch (Exception ex) {

            } finally {
            }
        }

        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

            String msg = response.optString("result_msg");
            String resultCode = response.optString("result_code");

            if (resultCode.equalsIgnoreCase("Y")) {

                authCode = response.optString("vrfctNo", "");
                edAuthCode.setText(authCode);

            }

            // 메세지 출력
            new ToastUtile().showCenterText(NhsLeaveActivity.this, msg + "\n 코드는  : " + authCode);


        }


    };

}

