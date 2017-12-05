package kr.go.molit.nhsnes.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import kr.go.molit.nhsnes.Network.NetworkProcess;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.MagicSE_Util;
import kr.go.molit.nhsnes.common.NetworkParamUtil;
import kr.go.molit.nhsnes.common.NetworkUrlUtil;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.dialog.DialogType1;
import kr.go.molit.nhsnes.widget.EditTextEx;
import kr.go.molit.nhsnes.widget.KeypadScroll;
import kr.go.molit.nhsnes.widget.TextViewEx;

import static kr.go.molit.nhsnes.activity.NhsMainActivity.MAP_VERSION_VECTOR;
import static kr.go.molit.nhsnes.activity.NhsMainActivity.MAP_VERSION_DEM;

/**
 * 로그인
 *
 * @author 정제영
 * @version 1.0, 2017.03.13 최초 작성
 **/

public class NhsLoginActivity extends NhsBaseFragmentActivity implements CompoundButton.OnCheckedChangeListener{

  public final static String LOGIN_MBR_ID = "mbrId";
  public final static String LOGIN_TOKEN_KEY = "tokenKey";
  public final static String LOGIN_NAME = "mbrNm";
  public final static String LOGIN_ID = "loginId";
  public final static String LOGIN_PWD = "loginPw";
  public final static String LOGIN_AUTO = "loginAuto";  // 자동 로그인
  public final static String LOGIN_ACRFTCD="acrftCd";

  Context _context;
  EditTextEx et_id, et_pass;
  TextViewEx tv_error;
  CheckBox cb_auto;
  //로그인 후 데이터요금 안내 팝업을 위해 로그인 팝업을 없애주기 위해 레이아웃으로 묶는다.
  FrameLayout fl_login;

  //버튼 두개짜리 팝업(데이터요금 안내 팝업)
  DialogType1 mPopup1;
  DialogType1 mPopup2;

  @RequiresApi(api = Build.VERSION_CODES.M)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_login);

    _context = NhsLoginActivity.this;


    /**
    //정제영
    //암복호화 예제
    MagicSE_Util magic = new MagicSE_Util(_context);
    String test = "테스트 abc 123";
    //암호화
    String enc = magic.getEncData(test);
    //복호화
    String dec = magic.getDecData(enc);

    Log.d("TEST", "원본 : "+test);
    Log.d("TEST", "암호화 : "+enc);
    Log.d("TEST", "복호화 : "+dec);
  **/
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    KeypadScroll.assistActivity(this);

    //에러 메시지
    tv_error = (TextViewEx) findViewById(R.id.tv_error);
    tv_error.setText(Html.fromHtml(getString(R.string.error_login)));

    fl_login = (FrameLayout) findViewById(R.id.fl_login);
    //아이디
    et_id = (EditTextEx) findViewById(R.id.et_id);
    //패스워드
    et_pass = (EditTextEx) findViewById(R.id.et_pass);
    //체크박스
    cb_auto = (CheckBox) findViewById(R.id.cb_auto);

    findViewById(R.id.bt_find_id).setOnClickListener(clickListener);
    findViewById(R.id.bt_find_pwd).setOnClickListener(clickListener);
    findViewById(R.id.bt_login).setOnClickListener(clickListener);
    findViewById(R.id.bt_signin).setOnClickListener(clickListener);

    cb_auto.setOnCheckedChangeListener(this);
  }

  View.OnClickListener clickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        //아이디 찾기
        case R.id.bt_find_id:
//                    tv_error.setVisibility(View.VISIBLE);
          Intent findIdIntent = new Intent(NhsLoginActivity.this, NhsFindUserIdActivity.class);
          startActivity(findIdIntent);
          break;
        //비밀번호 찾기
        case R.id.bt_find_pwd:
//                    tv_error.setVisibility(View.VISIBLE);
          Intent findPwdIntent = new Intent(NhsLoginActivity.this, NhsFindUserPwdActivity.class);
          startActivity(findPwdIntent);
          break;
        //로그인
        case R.id.bt_login:

          NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
          NetworkParamUtil networkParamUtil = new NetworkParamUtil();

          // 로그인 요청
          StringEntity param = networkParamUtil.requestLogin(et_id.getText().toString(), et_pass.getText().toString());
          NetworkProcess networkProcess = new NetworkProcess(NhsLoginActivity.this,
              networkUrlUtil.getUserLogin(),
              param,
              loginResult, true);
          networkProcess.sendEmptyMessage(0);


          break;
        //회원가입
        case R.id.bt_signin:
          tv_error.setVisibility(View.GONE);
          Intent registerIntent = new Intent(NhsLoginActivity.this, NhsAgreermentActivity.class);
          startActivity(registerIntent);
          break;
      }
    }
  };

  //데이터 요금 안내
  private void dataDialog() {

    ConnectivityManager connect = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

    if (connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

      syncAgreeDialog();

    } else {
      mPopup1 = new DialogType1(_context, getString(R.string.dialog_data_notice_title), getString(R.string.dialog_data_notice_msg), getString(R.string.btn_agree), new View.OnClickListener() {
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
    mPopup2 = new DialogType1(_context, "아이디와 비행정보 연동 동의", "내비게이션은 사용자의  ID와 비행계획서로 제출한 비행정보를  연동하여  단말기와  비행계획서를 관리하는  시스템에서 활용하도록  동의 합니다.", getString(R.string.btn_confirm), new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String strNowTime = sdf.format(new Date());

        // 개인정보 활용 동의
        String agreeDate = "agree_dat= " +
                            strNowTime +
                            "|agree_content=private_agree_info" +
                            "|userID=" + StorageUtil.getStorageModeEx(NhsLoginActivity.this, LOGIN_MBR_ID) +
                            "|acrftCd=" + StorageUtil.getStorageModeEx(NhsLoginActivity.this, LOGIN_ACRFTCD);

        // 아이디와 비행정보 연동 동의
        String idFlightDate = "agree_dat= " +
                strNowTime +
                "|agree_content=IDWithFlyInfoMapping" +
                "|userID=" + StorageUtil.getStorageModeEx(NhsLoginActivity.this, LOGIN_MBR_ID) +
                "|acrftCd=" + StorageUtil.getStorageModeEx(NhsLoginActivity.this, LOGIN_ACRFTCD);

        String tokenKey = StorageUtil.getStorageModeEx(NhsLoginActivity.this, LOGIN_TOKEN_KEY, "");
        String saveUserValue = et_id.getText().toString() + "\n" + et_pass.getText().toString() + "\n" + tokenKey;


        // 암호화 한다.
        try {
          agreeDate = Util.aesEncode(agreeDate, Util.aesKey);
          idFlightDate = Util.aesEncode(agreeDate, Util.aesKey);
          saveUserValue = Util.aesEncode(saveUserValue, Util.aesKey);
        }catch (Exception ex) {

        }


        // 저장한다.
        Util.writeStringAsFile(Environment.getExternalStorageDirectory() + "/ACC_Navi", "private_agree_info.dat", agreeDate);
        Util.writeStringAsFile(Environment.getExternalStorageDirectory() + "/ACC_Navi", "IDWithFlyInfoMapping.dat", idFlightDate);
        Util.writeStringAsFile(Environment.getExternalStorageDirectory() + "/ACC_Navi", "session.dat", saveUserValue);


        mPopup2.hideDialog();
        Intent intent = new Intent(NhsLoginActivity.this, NhsMainActivity.class);
        startActivity(intent);
        finish();
      }
    }, null, null);
  }

  //날씨 정보
  private void weatherDialog() {
//        mPopup2 = new DialogType2(_context, getString(R.string.dialog_weather_title), getString(R.string.dialog_weather_msg), getString(R.string.btn_confirm), new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPopup2.hideDialog();
//                Intent intent = new Intent(NhsLoginActivity.this, NhsMainActivity.class);
//                startActivity(intent);
//            }
//        });
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

        new ToastUtile().showCenterText(NhsLoginActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
      try {

        new ToastUtile().showCenterText(NhsLoginActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

      String msg = response.optString("result_msg");
      String resultCode = response.optString("result_code");

      // 메세지 출력
      new ToastUtile().showCenterText(NhsLoginActivity.this, msg);

      if (resultCode.equalsIgnoreCase("Y")) {

        // 로그아웃 혹은 기타 등등에 사용할 저장값 저장
        StorageUtil.setStorageMode(NhsLoginActivity.this, LOGIN_MBR_ID, response.optString("mbrId"));
        StorageUtil.setStorageMode(NhsLoginActivity.this, LOGIN_TOKEN_KEY, response.optString("tokenKey"));
        StorageUtil.setStorageMode(NhsLoginActivity.this, LOGIN_NAME, response.optString("mbrNm"));
        StorageUtil.setStorageMode(NhsLoginActivity.this, LOGIN_ID, response.optString("loginId"));
        StorageUtil.setStorageMode(NhsLoginActivity.this, LOGIN_PWD, et_pass.getText().toString());
        StorageUtil.setStorageMode(NhsLoginActivity.this, LOGIN_ACRFTCD, response.optString("acrftCd"));

        // 맵 정보를 받는다.
        getMapVersion(response.optString("tokenKey"), 1, MAP_VERSION_VECTOR);
        getMapVersion(response.optString("tokenKey"), 3, MAP_VERSION_DEM);

        Log.d("JeLib","response::"+response);
        fl_login.setVisibility(View.GONE);
        dataDialog();

      } else {
        tv_error.setVisibility(View.VISIBLE);
      }

    }


  };

  @Override
  public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    switch (compoundButton.getId()) {

      // 자동 로그인
      case R.id.cb_auto:

        // 자동 로그인 여부 저장
        StorageUtil.setStorageMode(NhsLoginActivity.this, LOGIN_AUTO, b);

        break;

    }

  }

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

    NetworkProcess networkProcess = new NetworkProcess(NhsLoginActivity.this,
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

                  StorageUtil.setStorageMode(NhsLoginActivity.this, saveKey, response.optJSONObject("result_data").toString());

                }
              }
            }, false);
    networkProcess.sendEmptyMessage(0);
  }
}

