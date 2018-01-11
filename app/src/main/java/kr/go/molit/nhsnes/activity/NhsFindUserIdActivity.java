package kr.go.molit.nhsnes.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import cz.msebera.android.httpclient.entity.StringEntity;
import kr.go.molit.nhsnes.Network.NetworkProcess;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.NetworkParamUtil;
import kr.go.molit.nhsnes.common.NetworkUrlUtil;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.widget.TextViewEx;

/**
* 아이디 찾기
* @author FIESTA
* @since  오후 12:14
**/
public class NhsFindUserIdActivity extends NhsBaseFragmentActivity implements View.OnClickListener {

    private EditText edName = null;
    private EditText edPhoneNumber = null;
    private EditText edAuthCode = null;

    private View vInputInfo = null;
    private View vResultInfo = null;

    private String authCode = "";
    private boolean checkAuthCode = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_find_user_id);

        setLayout();

    }

    /**
    * 레이아웃을 설정한다.
    * @author FIESTA
    * @version 1.0.0
    * @since 오후 5:15
    **/
    private void setLayout(){

        this.edName = (EditText) this.findViewById(R.id.ed_name);
        this.edPhoneNumber = (EditText) this.findViewById(R.id.ed_phone);
        this.edAuthCode = (EditText) this.findViewById(R.id.ed_auth_code);

        this.vInputInfo = this.findViewById(R.id.ll_input_info);
        this.vResultInfo = this.findViewById(R.id.ll_result);

        findViewById(R.id.btn_request_auth_code).setOnClickListener(this);
        findViewById(R.id.btn_auth_code).setOnClickListener(this);
        findViewById(R.id.fl_find_user_id).setOnClickListener(this);
        findViewById(R.id.btn_request_auth_code).setOnClickListener(this);
        findViewById(R.id.btn_auth_code).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        String phoneNumber = this.edPhoneNumber.getText().toString();
        String name =this.edName.getText().toString();
        String authCode = this.edAuthCode.getText().toString();

        switch (view.getId()) {

            case R.id.btn_request_auth_code:

                if (phoneNumber.isEmpty()){
                    Toast.makeText(getContext(), "휴대폰 번호를 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                } else if (name.isEmpty()){
                    Toast.makeText(getContext(), "이름을 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                } else {

                    NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
                    NetworkParamUtil networkParamUtil = new NetworkParamUtil();

                    // 인증코드 요청
                    StringEntity param = networkParamUtil.requestAuthCode(phoneNumber, name);
                    NetworkProcess networkProcess = new NetworkProcess(NhsFindUserIdActivity.this,
                        networkUrlUtil.getRequestAuthCode(),
                        param,
                        requestAuthCodeResult, true);
                    networkProcess.sendEmptyMessage(0);

                }

                break;

            case R.id.btn_auth_code:

                if (authCode.isEmpty()) {
                    Toast.makeText(getContext(), "수신한 인증번호를 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                }else {

                    NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
                    NetworkParamUtil networkParamUtil = new NetworkParamUtil();

                    // 인증코드 확인 요청
                    StringEntity param = networkParamUtil.requestCheckAuthCode(authCode, phoneNumber);
                    NetworkProcess networkProcess = new NetworkProcess(NhsFindUserIdActivity.this,
                        networkUrlUtil.getRequestCheckAuthCode(),
                        param,
                        checkAuthCodeResult, true);
                    networkProcess.sendEmptyMessage(0);

                }

                break;

            case R.id.fl_find_user_id:

                if (vResultInfo.getVisibility() == View.VISIBLE) {
                    finish();
                }else if (phoneNumber.isEmpty()){
                    Toast.makeText(getContext(), "휴대폰 번호를 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                } else if (name.isEmpty()){
                    Toast.makeText(getContext(), "이름을 입력해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                } else if (!checkAuthCode){
                    Toast.makeText(getContext(), "인증번호 인증을 해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                } else {

                    NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
                    NetworkParamUtil networkParamUtil = new NetworkParamUtil();

                    // 아이디 찾기 요청
                    StringEntity param = networkParamUtil.findId(name, phoneNumber, authCode);
                    NetworkProcess networkProcess = new NetworkProcess(NhsFindUserIdActivity.this,
                        networkUrlUtil.getFindId(),
                        param,
                        findIdResult, true);
                    networkProcess.sendEmptyMessage(0);

                }

                break;


        }

    }

    /**
     * 아이디 찾기 결과
     * @author FIESTA
     * @since  오후 11:04
     **/
    NetworkProcess.OnResultListener findIdResult = new NetworkProcess.OnResultListener() {
        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
            try {

                new ToastUtile().showCenterText(NhsFindUserIdActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

            } catch (Exception ex) {

            } finally {
            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
            try {

                new ToastUtile().showCenterText(NhsFindUserIdActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

            } catch (Exception ex) {

            } finally {
            }
        }

        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

            String msg = response.optString("result_msg");
            String resultCode = response.optString("result_code");

            if (resultCode.equalsIgnoreCase("Y")) {

               final String resultId = response.optString("loginId","검색된 결과가 없습니다.");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        vInputInfo.setVisibility(View.GONE);
                        vResultInfo.setVisibility(View.VISIBLE);
                        String msgName = String.format(getString(R.string.find_id_name), edName.getText().toString());

                        // 아이디 표시
                        ((TextView)findViewById(R.id.tv_result_id)).setText(resultId);

                        // 메세지 표시
                        ((TextView)findViewById(R.id.tv_find_id_msg)).setText(msgName);

                        ((TextViewEx)findViewById(R.id.tv_find_user_id)).setText("확인");

                    }
                });


            }

            // 메세지 출력
            new ToastUtile().showCenterText(NhsFindUserIdActivity.this, msg);


        }


    };

    /**
     * 인증번호 체크 결과
     * @author FIESTA
     * @since  오후 11:04
     **/
    NetworkProcess.OnResultListener checkAuthCodeResult = new NetworkProcess.OnResultListener() {
        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
            try {

                new ToastUtile().showCenterText(NhsFindUserIdActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

            } catch (Exception ex) {

            } finally {
            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
            try {

                new ToastUtile().showCenterText(NhsFindUserIdActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

            } catch (Exception ex) {

            } finally {
            }
        }

        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

            String msg = response.optString("result_msg");
            String resultCode = response.optString("result_code");

            if (resultCode.equalsIgnoreCase("Y")) {

                checkAuthCode = true;

            }

            // 메세지 출력
            new ToastUtile().showCenterText(NhsFindUserIdActivity.this, msg);


        }


    };



   /**
   * 인증번호 요청 결과
   * @author FIESTA
   * @since  오후 11:04
   **/
    NetworkProcess.OnResultListener requestAuthCodeResult = new NetworkProcess.OnResultListener() {
        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
            try {

                new ToastUtile().showCenterText(NhsFindUserIdActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

            } catch (Exception ex) {

            } finally {
            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
            try {

                new ToastUtile().showCenterText(NhsFindUserIdActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

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
            new ToastUtile().showCenterText(NhsFindUserIdActivity.this, msg + "\n 코드는  : " + authCode);


        }


    };
}

