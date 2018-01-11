package kr.go.molit.nhsnes.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import kr.go.molit.nhsnes.Network.NetworkProcess;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.NetworkParamUtil;
import kr.go.molit.nhsnes.common.NetworkUrlUtil;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.dialog.DialogType1;
import kr.go.molit.nhsnes.model.NhsCompanyModel;

/**
 * 회원가입
 *
 * @author FIESTA
 * @since 오후 12:14
 **/
public class NhsRegisterActivity extends NhsBaseFragmentActivity implements View.OnClickListener {

  private EditText edPhoneNumber = null;
  private EditText edAuthCode = null;
  private EditText edId = null;
  private EditText edName = null;
  private EditText edPwd = null;
  private EditText edPwdRe = null;
  private EditText edAcrfcd = null;
  private TextView tvCompany = null;

  private DialogType1 mPopup1 = null;
  private PopupWindow popupWindow = null;

  private boolean isCheckUserId = false;
  private boolean isCheckedAuthCode = false;

  private String authCode = "";
  private NhsCompanyModel selectedCompany = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_register);

    // 레이아웃 설정
    setLayout();

    // 소속기관을 가져온다.
    getComapnyList();

  }

  /**
  * 레이아웃 설정
  * @author FIESTA
  * @version 1.0.0
  * @since 오후 5:24
  **/
  private void setLayout() {

    this.edPhoneNumber = (EditText) this.findViewById(R.id.ed_phone);
    this.edAuthCode = (EditText) this.findViewById(R.id.ed_auth_code);
    this.edId = (EditText) this.findViewById(R.id.ed_id);
    this.edName = (EditText) this.findViewById(R.id.ed_name);
    this.edPwd = (EditText) this.findViewById(R.id.ed_pwd);
    this.edPwdRe = (EditText) this.findViewById(R.id.ed_pwd_re);
    this.edAcrfcd =  (EditText) this.findViewById(R.id.ed_acrfcd);
    this.tvCompany = (TextView) this.findViewById(R.id.tv_company);

    //영어, 숫자만 입력 가능
    edId.setFilters(new InputFilter[]{filterAlphaNum});
    edPwd.setFilters(new InputFilter[]{filterAlphaNumSpecial});
    edPwdRe.setFilters(new InputFilter[]{filterAlphaNumSpecial});

    findViewById(R.id.tv_find_user_id).setOnClickListener(this);
    findViewById(R.id.fl_register).setOnClickListener(this);
    findViewById(R.id.btn_request_auth_code).setOnClickListener(this);
    findViewById(R.id.btn_check_auth_code).setOnClickListener(this);
    findViewById(R.id.btn_check_id).setOnClickListener(this);
    findViewById(R.id.tv_company).setOnClickListener(this);


  }

  /**
  * 소소기관 리스틀르 받아온다.
  * @author FIESTA
  * @since  오후 12:00
  **/
  private void getComapnyList(){

    NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();

    // 인증코드 확인 요청
    NetworkProcess networkProcess = new NetworkProcess(NhsRegisterActivity.this,
        networkUrlUtil.getCompanyList(),
        null,
        getCompanyResult, true);
    networkProcess.sendEmptyMessage(0);

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

    final String phoneNumber = this.edPhoneNumber.getText().toString();
    final String name = this.edName.getText().toString();
    String authCode = this.edAuthCode.getText().toString();
    final String id = this.edId.getText().toString();
    final String pwd = this.edPwd.getText().toString();
    String pwdRe = this.edPwdRe.getText().toString();
    final String acrftcd = this.edAcrfcd.getText().toString();

    switch (view.getId()) {

      case R.id.tv_company_name:

        this.selectedCompany = (NhsCompanyModel)view.getTag();
        this.tvCompany.setText(selectedCompany.getAfftNm());
        this.popupWindow.dismiss();

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
          NetworkProcess networkProcess = new NetworkProcess(NhsRegisterActivity.this,
              networkUrlUtil.getRequestAuthCode(),
              param,
              requestAuthCodeResult, true);
          networkProcess.sendEmptyMessage(0);

        }

        break;

      case R.id.btn_check_auth_code:

        if (authCode.isEmpty()) {
          Toast.makeText(getContext(), "수신한 인증번호를 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
        } else {
          NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
          NetworkParamUtil networkParamUtil = new NetworkParamUtil();

          // 인증코드 확인 요청
          StringEntity param = networkParamUtil.requestCheckAuthCode(authCode, phoneNumber);
          NetworkProcess networkProcess = new NetworkProcess(NhsRegisterActivity.this,
              networkUrlUtil.getRequestCheckAuthCode(),
              param,
              checkAuthCodeResult, true);
          networkProcess.sendEmptyMessage(0);
        }

        break;

      case R.id.btn_check_id:

        if(id.length() < 4){
          Toast.makeText(getContext(), "입력된 ID가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
        }else {

          NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
          NetworkParamUtil networkParamUtil = new NetworkParamUtil();

          // 아이디 중복검사 요청
          StringEntity param = networkParamUtil.checkUserId(id);
          NetworkProcess networkProcess = new NetworkProcess(NhsRegisterActivity.this,
              networkUrlUtil.getCheckUserId(),
              param,
              checkIdResult, true);
          networkProcess.sendEmptyMessage(0);

        }
        break;

      // 회원가입
      case R.id.fl_register:

        if (phoneNumber.isEmpty()) {
          Toast.makeText(getContext(), "휴대폰 번호을 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
        } else if (!isCheckedAuthCode) {
          Toast.makeText(getContext(), "인증번호 확인을 진행해 주십시오.", Toast.LENGTH_SHORT).show();
        } else if (id.isEmpty()) {
          Toast.makeText(getContext(), "아이디을 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
        } else if (this.selectedCompany == null) {
          Toast.makeText(getContext(), "소속사를 선택하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
        } else if (name.isEmpty()) {
          Toast.makeText(getContext(), "이름을 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
        } else if (pwd.length() < 9) {
          Toast.makeText(getContext(), "비밀번호를 9자리 이상 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
        } else if (pwdRe.isEmpty()) {
          Toast.makeText(getContext(), "비밀번호 확인을 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
        } else if (!pwd.equals(pwdRe)) {
          Toast.makeText(getContext(), "비밀번호와 비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        } else if (acrftcd.isEmpty()) {
          Toast.makeText(getContext(), "등록기호를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }else if (!this.isCheckUserId) {

          checkUserIdDialog();

        } else {

          NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
          NetworkParamUtil networkParamUtil = new NetworkParamUtil();

          // 항공 코드 검사
          StringEntity param = networkParamUtil.getAcrftInformation(acrftcd);
          NetworkProcess networkProcess = new NetworkProcess(NhsRegisterActivity.this,
                  networkUrlUtil.getAcrftInformation(),
                  param,
                  new NetworkProcess.OnResultListener() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                      new ToastUtile().showCenterText(NhsRegisterActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                      new ToastUtile().showCenterText(NhsRegisterActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                      String msg = response.optString("result_msg");
                      String resultCode = response.optString("result_code");

                      if (resultCode.equalsIgnoreCase("Y")) {

                        NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
                        NetworkParamUtil networkParamUtil = new NetworkParamUtil();

                        // 회원가입 요청
                        StringEntity param = networkParamUtil.getRegister(id, name, selectedCompany.getAfftId()
                                , phoneNumber, "19700101", pwd, acrftcd);
                        NetworkProcess networkProcess = new NetworkProcess(NhsRegisterActivity.this,
                                networkUrlUtil.getRegister(),
                                param,
                                registerResult, true);
                        networkProcess.sendEmptyMessage(0);

                      } else {

                        // 메세지 출력
                        new ToastUtile().showCenterText(NhsRegisterActivity.this, "조회된 항공기 정보가 없습니다.");

                      }



                    }
                  }, true);
          networkProcess.sendEmptyMessage(0);




        }


        break;

      case R.id.tv_company:

        togglePopup();

        break;


    }

  }

  /**
   * 소속사 팝업창 설정
   *
   * @author FIESTA
   * @since 오전 2:42
   **/
  private void setupPopupMenu(ArrayList<NhsCompanyModel> dataList) {

    if (this.popupWindow == null) {

      View popupView = getLayoutInflater().inflate(R.layout.popup_company, null);
      this.popupWindow = new PopupWindow(popupView, findViewById(R.id.tv_company).getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
      this.popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), android.R.color.transparent)));
      this.popupWindow.setOutsideTouchable(true);

      ViewGroup vgParent = (ViewGroup) popupView.findViewById(R.id.ll_parent);

      int i = 0;
      int size = dataList.size();
      View childView = null;
      NhsCompanyModel nhsCompanyModel = null;
      View addView = null;

      for (i=0; i<size; i++) {

        // 레이어 추가
        childView = getLayoutInflater().inflate(R.layout.view_company, vgParent);

        nhsCompanyModel = dataList.get(i);
        addView = ((LinearLayout)childView).getChildAt(((LinearLayout) childView).getChildCount()-1);
        ((TextView)addView).setText(nhsCompanyModel.getAfftNm());
        addView.setTag(nhsCompanyModel);
        addView.setOnClickListener(this);

      }

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
   * 소속기관 리스트 결과
   *
   * @author FIESTA
   * @since 오후 11:04
   **/
  NetworkProcess.OnResultListener getCompanyResult = new NetworkProcess.OnResultListener() {
    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
      try {

        new ToastUtile().showCenterText(NhsRegisterActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
      try {

        new ToastUtile().showCenterText(NhsRegisterActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

      String msg = response.optString("result_msg");
      String resultCode = response.optString("result_code");
      ArrayList<NhsCompanyModel> dataList = new ArrayList<>();

      if (resultCode.equalsIgnoreCase("Y")) {

          JSONArray companyList = response.optJSONArray("attf");
          JSONObject data = null;
          NhsCompanyModel nhsCompanyModel = null;

          int i = 0;
          int size = companyList.length();
          String afftid = "";
          String afftName = "";

          for (i=0; i<size; i++) {

            data = companyList.optJSONObject(i);
            afftid = data.optString("afftId", "");
            afftName = data.optString("afftNm", "");

            nhsCompanyModel = new NhsCompanyModel();
            nhsCompanyModel.setAfftId(afftid);
            nhsCompanyModel.setAfftNm(afftName);

            dataList.add(nhsCompanyModel);

          }

      }

      // 소소기관 리스트 설정
      setupPopupMenu(dataList);

      // 메세지 출력
      new ToastUtile().showCenterText(NhsRegisterActivity.this, msg);


    }


  };

  /**
   * 아이디 찾기 결과
   *
   * @author FIESTA
   * @since 오후 11:04
   **/
  NetworkProcess.OnResultListener checkIdResult = new NetworkProcess.OnResultListener() {
    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
      try {

        new ToastUtile().showCenterText(NhsRegisterActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
      try {

        new ToastUtile().showCenterText(NhsRegisterActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

      String msg = response.optString("result_msg");
      String resultCode = response.optString("result_code");

      if (resultCode.equalsIgnoreCase("Y")) {

        isCheckUserId = true;

      }

      // 메세지 출력
      new ToastUtile().showCenterText(NhsRegisterActivity.this, msg);


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
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
      try {

        new ToastUtile().showCenterText(NhsRegisterActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
      try {

        new ToastUtile().showCenterText(NhsRegisterActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

      String msg = response.optString("result_msg");
      String resultCode = response.optString("result_code");

      if (resultCode.equalsIgnoreCase("Y")) {

        isCheckedAuthCode = true;

      }

      // 메세지 출력
      new ToastUtile().showCenterText(NhsRegisterActivity.this, msg);


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
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
      try {

        new ToastUtile().showCenterText(NhsRegisterActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
      try {

        new ToastUtile().showCenterText(NhsRegisterActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

      String msg = response.optString("result_msg");
      String resultCode = response.optString("result_code");

      if (resultCode.equalsIgnoreCase("Y")) {

        authCode = response.optString("vrfctNo", "");
        edAuthCode.setText(authCode);

      }

      // 메세지 출력
      new ToastUtile().showCenterText(NhsRegisterActivity.this, msg + "\n 코드는  : " + authCode);


    }


  };


  /**
   * 회원가입 결과
   *
   * @author FIESTA
   * @since 오후 11:04
   **/
  NetworkProcess.OnResultListener registerResult = new NetworkProcess.OnResultListener() {
    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
      try {

        new ToastUtile().showCenterText(NhsRegisterActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
      try {

        new ToastUtile().showCenterText(NhsRegisterActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

      String msg = response.optString("result_msg");
      String resultCode = response.optString("result_code");

      if (resultCode.equalsIgnoreCase("Y")) {

        finish();

      }

      // 메세지 출력
      new ToastUtile().showCenterText(NhsRegisterActivity.this, msg);
    }
  };

  /**
  * 영문 + 숫자 만 입력 되도록
  * @author FIESTA
  * @version 1.0.0
  * @since 오후 5:24
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
  * @since 오후 5:24
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

