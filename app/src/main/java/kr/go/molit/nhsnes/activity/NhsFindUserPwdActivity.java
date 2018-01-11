package kr.go.molit.nhsnes.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.regex.Pattern;

import cz.msebera.android.httpclient.entity.StringEntity;
import kr.go.molit.nhsnes.Network.NetworkProcess;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.NetworkParamUtil;
import kr.go.molit.nhsnes.common.NetworkUrlUtil;
import kr.go.molit.nhsnes.common.ToastUtile;

/**
 * 패스워드 찾기
 *
 * @author FIESTA
 * @since 오후 12:14
 **/
public class NhsFindUserPwdActivity extends NhsBaseFragmentActivity implements View.OnClickListener {

  private EditText edId = null;
  private EditText edName = null;
  private EditText edPhoneNumber = null;
  private EditText edAuthCode = null;

  private View vInputInfo = null;
  private View vResultInfo = null;

  private String authCode = "";
  private boolean checkAuthCode = false;
  private boolean isChangePwdMode = false;  // 비밀번호 변경 페이지 여부
  private String userUniqueId = "";           // 회원일련번호;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_find_user_pwd);

    setLayout();

  }

  /**
  * 레이아웃을 설정한다.
  * @author FIESTA
  * @version 1.0.0
  * @since 오후 5:16
  **/
  private void setLayout() {

    this.edId = (EditText) this.findViewById(R.id.ed_id);
    this.edName = (EditText) this.findViewById(R.id.ed_name);
    this.edPhoneNumber = (EditText) this.findViewById(R.id.ed_phone);
    this.edAuthCode = (EditText) this.findViewById(R.id.ed_auth_code);

    //영어, 숫자만 입력 가능
    this.edId.setFilters(new InputFilter[]{filterAlphaNum});

    this.vInputInfo = this.findViewById(R.id.ll_input_info);
    this.vResultInfo = this.findViewById(R.id.ll_result);

    findViewById(R.id.btn_request_auth_code).setOnClickListener(this);
    findViewById(R.id.btn_auth_code).setOnClickListener(this);
    findViewById(R.id.fl_find_user_pwd).setOnClickListener(this);
    findViewById(R.id.btn_request_auth_code).setOnClickListener(this);
    findViewById(R.id.btn_auth_code).setOnClickListener(this);

    EditText edPwd = (EditText)findViewById(R.id.ed_pwd);
    EditText pwdRe = (EditText)findViewById(R.id.ed_pwd_re);

    //영어, 숫자만 입력 가능
    edPwd.setFilters(new InputFilter[]{filterAlphaNumSpecial});
    pwdRe.setFilters(new InputFilter[]{filterAlphaNumSpecial});
  }

  @Override
  public void onClick(View view) {

    String id = this.edId.getText().toString();
    String phoneNumber = this.edPhoneNumber.getText().toString();
    String name = this.edName.getText().toString();
    String authCode = this.edAuthCode.getText().toString();

    switch (view.getId()) {

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
          NetworkProcess networkProcess = new NetworkProcess(NhsFindUserPwdActivity.this,
              networkUrlUtil.getRequestAuthCode(),
              param,
              requestAuthCodeResult, true);
          networkProcess.sendEmptyMessage(0);

        }

        break;

      case R.id.btn_auth_code:

        if (authCode.isEmpty()) {
          Toast.makeText(getContext(), "수신한 인증번호를 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
        } else {
          NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
          NetworkParamUtil networkParamUtil = new NetworkParamUtil();

          // 인증코드 확인 요청
          StringEntity param = networkParamUtil.requestCheckAuthCode(authCode, phoneNumber);
          NetworkProcess networkProcess = new NetworkProcess(NhsFindUserPwdActivity.this,
              networkUrlUtil.getRequestCheckAuthCode(),
              param,
              checkAuthCodeResult, true);
          networkProcess.sendEmptyMessage(0);
        }

        break;

      case R.id.fl_find_user_pwd:

        if (!this.isChangePwdMode) {  // 첫 회원 인증

          if (vResultInfo.getVisibility() == View.VISIBLE) {
            finish();
          } else if (phoneNumber.isEmpty()) {
            Toast.makeText(getContext(), "휴대폰 번호를 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
          } else if (name.isEmpty()) {
            Toast.makeText(getContext(), "이름을 입력해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
          } else if (authCode.isEmpty()) {
            Toast.makeText(getContext(), "인증번호 인증을 해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
          } else if (id.length() < 4){
            Toast.makeText(getContext(), "입력된 ID가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
          } else if (!checkAuthCode) {
            Toast.makeText(getContext(), "인증번호 인증을 해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
          } else {

            NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
            NetworkParamUtil networkParamUtil = new NetworkParamUtil();

            // 비밀번호 찾기 요청
            StringEntity param = networkParamUtil.findPwd(id, name, phoneNumber, authCode);
            NetworkProcess networkProcess = new NetworkProcess(NhsFindUserPwdActivity.this,
                networkUrlUtil.getFindPwd(),
                param,
                findPwdResult, true);
            networkProcess.sendEmptyMessage(0);

          }

        }else { // 비밀번호 변경

          String pwd = ((EditText)findViewById(R.id.ed_pwd)).getText().toString();
          String pwdRe = ((EditText)findViewById(R.id.ed_pwd_re)).getText().toString();

          if (pwd.length() < 9) {
            Toast.makeText(getContext(), "비밀번호를 9자리 이상 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
          } else if (pwdRe.isEmpty()) {
            Toast.makeText(getContext(), "비밀번호 확인을 입력해주세요.", Toast.LENGTH_SHORT).show();
          } else if (!pwd.equals(pwdRe)) {
            Toast.makeText(getContext(), "비밀번호와 비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
          }  else{

            NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
            NetworkParamUtil networkParamUtil = new NetworkParamUtil();

            // 비밀번호 찾기 요청
            StringEntity param = networkParamUtil.changePwd(this.userUniqueId, pwd);
            NetworkProcess networkProcess = new NetworkProcess(NhsFindUserPwdActivity.this,
                networkUrlUtil.getChangePwd(),
                param,
                changePwdResult, true);
            networkProcess.sendEmptyMessage(0);

          }

        }

        break;


    }

  }

  /**
   * 비밀번호 변경 결과
   *
   * @author FIESTA
   * @since 오후 11:04
   **/
  NetworkProcess.OnResultListener changePwdResult = new NetworkProcess.OnResultListener() {
    @Override
    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
      try {

        new ToastUtile().showCenterText(NhsFindUserPwdActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
      try {

        new ToastUtile().showCenterText(NhsFindUserPwdActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

      String msg = response.optString("result_msg");
      String resultCode = response.optString("result_code");

      if (resultCode.equalsIgnoreCase("Y")) {

        finish();

      }

      // 메세지 출력
      new ToastUtile().showCenterText(NhsFindUserPwdActivity.this, msg);


    }


  };

  /**
   * 비밀번호 찾기 결과
   *
   * @author FIESTA
   * @since 오후 11:04
   **/
  NetworkProcess.OnResultListener findPwdResult = new NetworkProcess.OnResultListener() {
    @Override
    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
      try {

        new ToastUtile().showCenterText(NhsFindUserPwdActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
      try {

        new ToastUtile().showCenterText(NhsFindUserPwdActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

      String msg = response.optString("result_msg");
      String resultCode = response.optString("result_code");

      if (resultCode.equalsIgnoreCase("Y")) {

        userUniqueId = response.optString("mbrId", "");

        // test
        //userUniqueId = "MBR_00000000571";


        runOnUiThread(new Runnable() {
          @Override
          public void run() {

            vInputInfo.setVisibility(View.GONE);
            vResultInfo.setVisibility(View.VISIBLE);
            String msgName = String.format(getString(R.string.find_pwd_msg), edName.getText().toString(), edId.getText().toString());

            // 메세지 표시
            ((TextView) findViewById(R.id.tv_pwd_msg)).setText(msgName);

            isChangePwdMode = true;


          }
        });


      }

      // 메세지 출력
      new ToastUtile().showCenterText(NhsFindUserPwdActivity.this, msg);


    }


  };

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

        new ToastUtile().showCenterText(NhsFindUserPwdActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
      try {

        new ToastUtile().showCenterText(NhsFindUserPwdActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

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
      new ToastUtile().showCenterText(NhsFindUserPwdActivity.this, msg);


    }


  };


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

        new ToastUtile().showCenterText(NhsFindUserPwdActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
      try {

        new ToastUtile().showCenterText(NhsFindUserPwdActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

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
      new ToastUtile().showCenterText(NhsFindUserPwdActivity.this, msg + "\n 코드는  : " + authCode);


    }
  };

  /**
  * 영문 + 숫자 만 입력 되도록
  * @author FIESTA
  * @version 1.0.0
  * @since 오후 5:16
  **/
  public InputFilter filterAlphaNum = new InputFilter() {
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
      Pattern ps = Pattern.compile("^[a-zA-Z0-9]*$");
      if (!ps.matcher(source).matches()) {
        return "";
      }
      return null;
    }
  };

  /**
  * 영문 + 숫자 만 입력 되도록
  * @author FIESTA
  * @version 1.0.0
  * @since 오후 5:16
  **/
  public InputFilter filterAlphaNumSpecial = new InputFilter() {
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
      Pattern ps = Pattern.compile("^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`]+$");
      if (!ps.matcher(source).matches()) {
        return "";
      }
      return null;
    }
  };

}

