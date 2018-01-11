package kr.go.molit.nhsnes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import kr.go.molit.nhsnes.Network.NetworkProcess;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.NetworkUrlUtil;
import kr.go.molit.nhsnes.common.ToastUtile;

/**
 * 이용약관 동의
 *
 * @author FIESTA
 * @since 오후 12:14
 **/
public class NhsAgreermentActivity extends NhsBaseFragmentActivity implements View.OnClickListener {

  private CheckBox cbAgree1 = null;
  private CheckBox cbAgree2 = null;
  private CheckBox cbAgree3 = null;

  private TextView tvAgreement1 = null;
  private TextView tvAgreement2 = null;
  private TextView tvAgreement3 = null;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_agreement);

    setLayout();

    // 이용약관 요청
    requestData();

  }

  /**
  * 이용야관 요청
  * @author FIESTA
  * @version 1.0.0
  * @since 오후 4:45
  **/
  private void requestData() {

    NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();

    // 이용약관 요청
    NetworkProcess networkProcess = new NetworkProcess(NhsAgreermentActivity.this,
        networkUrlUtil.getRequestAgreement(),
        null,
        textResult, true);
    networkProcess.sendEmptyMessage(0);

  }

  /**
  * 레이아웃 설정
  * @author FIESTA
  * @version 1.0.0
  * @since 오후 4:45
  **/
  private void setLayout() {

    this.cbAgree1 = (CheckBox) this.findViewById(R.id.cb_check1);
    this.cbAgree2 = (CheckBox) this.findViewById(R.id.cb_check2);
    this.cbAgree3 = (CheckBox) this.findViewById(R.id.cb_check3);

    this.tvAgreement1 = (TextView) this.findViewById(R.id.tv_agreement_1);
    this.tvAgreement2 = (TextView) this.findViewById(R.id.tv_agreement_2);
    this.tvAgreement3 = (TextView) this.findViewById(R.id.tv_agreement_3);

    findViewById(R.id.fl_agree).setOnClickListener(this);

  }

  @Override
  public void onClick(View view) {

    if (!this.cbAgree1.isChecked()) {

      Toast.makeText(getContext(), "회원가입약관을 확인하고 체크하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();

    } else if (!this.cbAgree2.isChecked()) {

      Toast.makeText(getContext(), "개인정보 수집 이용 동의를 확인하고 체크하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();

    } else if (!this.cbAgree3.isChecked()) {

      Toast.makeText(getContext(), "위치기반 서비스 이용약관을 확인하고 체크하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();

    } else {

      Intent registerIntent = new Intent(NhsAgreermentActivity.this, NhsRegisterActivity.class);
      startActivity(registerIntent);
      finish();

    }

  }

  /**
   * 로그인 결과 리스너
   *
   * @author FIESTA
   * @since 오전 12:36
   **/
  NetworkProcess.OnResultListener textResult = new NetworkProcess.OnResultListener() {
    @Override
    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
      try {

        new ToastUtile().showCenterText(NhsAgreermentActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
      try {

        new ToastUtile().showCenterText(NhsAgreermentActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

      String msg = response.optString("result_msg");
      String resultCode = response.optString("result_code");

      // 메세지 출력
      new ToastUtile().showCenterText(NhsAgreermentActivity.this, msg);

      if (resultCode.equalsIgnoreCase("Y")) {

        String join = response.optString("joinCause");
        String personal = response.optString("personalCause");
        String location = response.optString("locationClause");

        tvAgreement1.setText(join);
        tvAgreement2.setText(personal);
        tvAgreement3.setText(location);

      }

    }


  };

}


