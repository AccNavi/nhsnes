package kr.go.molit.nhsnes.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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

import cz.msebera.android.httpclient.entity.StringEntity;
import kr.go.molit.nhsnes.Network.NetworkProcess;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.NetworkParamUtil;
import kr.go.molit.nhsnes.common.NetworkUrlUtil;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.model.NhsCompanyModel;
import kr.go.molit.nhsnes.model.NhsMyInfoModel;

import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_ID;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_MBR_ID;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_NAME;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_PWD;

/**
 * 마이페이지 수정
 *
 * @author FIESTA
 * @since 오후 12:14
 **/
public class NhsMyPageModifyActivity extends NhsBaseFragmentActivity implements View.OnClickListener {

  private TextView tvId = null;
  private TextView tvCompany = null;
  private TextView tvName = null;
  private TextView tvAcrftcd = null;
  private EditText edEmail = null;
  private EditText edPhoneNumber = null;
  private EditText edAddress = null;

  private PopupWindow popupWindow = null;

  private NhsCompanyModel selectedCompany = null;
  private NhsMyInfoModel nhsMyInfoModel = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_my_page_modify);

    // 레이아웃 설정
    setLayout();

    // 소속기관을 가져온다.
    getComapnyList();

  }

  private void setLayout() {

    this.tvId = (TextView) findViewById(R.id.tv_id);
    this.tvCompany = (TextView) findViewById(R.id.tv_company);
    this.tvName = (TextView) findViewById(R.id.tv_name);
    this.tvAcrftcd = (TextView)findViewById(R.id.tv_acrftcd);
    this.edEmail = (EditText) findViewById(R.id.ed_email);
    this.edPhoneNumber = (EditText) findViewById(R.id.ed_phone_number);
    this.edAddress = (EditText) findViewById(R.id.ed_address);

    this.tvCompany = (TextView) this.findViewById(R.id.tv_company);

    findViewById(R.id.fl_modify).setOnClickListener(this);
    findViewById(R.id.tv_company).setOnClickListener(this);

  }

  /**
   * 소소기관 리스틀르 받아온다.
   *
   * @author FIESTA
   * @since 오후 12:00
   **/
  private void getComapnyList() {

    NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();

    // 인증코드 확인 요청
    NetworkProcess networkProcess = new NetworkProcess(NhsMyPageModifyActivity.this,
        networkUrlUtil.getCompanyList(),
        null,
        getCompanyResult, true);
    networkProcess.sendEmptyMessage(0);

  }

  @Override
  public void onClick(View view) {

    switch (view.getId()) {

      case R.id.tv_company_name:

        this.selectedCompany = (NhsCompanyModel) view.getTag();
        this.tvCompany.setText(selectedCompany.getAfftNm());
        this.popupWindow.dismiss();

        break;

      case R.id.fl_modify:

        this.nhsMyInfoModel.setAfftId(this.selectedCompany.getAfftId());
        this.nhsMyInfoModel.setEmail(this.edEmail.getText().toString());
        this.nhsMyInfoModel.setHpno(this.edPhoneNumber.getText().toString());
        this.nhsMyInfoModel.setAddress(this.edAddress.getText().toString());

        NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
        NetworkParamUtil networkParamUtil = new NetworkParamUtil();

        // 수정 요청
        StringEntity param = networkParamUtil.modifyMyInfo(this.nhsMyInfoModel);
        NetworkProcess networkProcess = new NetworkProcess(NhsMyPageModifyActivity.this,
            networkUrlUtil.getModifyMyInfo(),
            param,
            modifyMyInfoResult, true);
        networkProcess.sendEmptyMessage(0);


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

      for (i = 0; i < size; i++) {

        // 레이어 추가
        childView = getLayoutInflater().inflate(R.layout.view_company, vgParent);

        nhsCompanyModel = dataList.get(i);
        addView = ((LinearLayout) childView).getChildAt(((LinearLayout) childView).getChildCount() - 1);
        ((TextView) addView).setText(nhsCompanyModel.getAfftNm());
        addView.setTag(nhsCompanyModel);
        addView.setOnClickListener(this);
        ;

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
   * 회원정보 수정 결과
   *
   * @author FIESTA
   * @since 오후 11:04
   **/
  NetworkProcess.OnResultListener modifyMyInfoResult = new NetworkProcess.OnResultListener() {
    @Override
    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
      try {

        new ToastUtile().showCenterText(NhsMyPageModifyActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
      try {

        new ToastUtile().showCenterText(NhsMyPageModifyActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

      try {

        String msg = response.optString("result_msg");
        String resultCode = response.optString("result_code");

        if (resultCode.equalsIgnoreCase("Y")) {

          StorageUtil.setStorageMode(NhsMyPageModifyActivity.this, LOGIN_MBR_ID, response.optString("mbrId"));
          setResult(RESULT_OK);
          finish();

        }

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

      } catch (Exception ex) {

      } finally {


      }

    }


  };

  /**
   * 소속기관 리스트 결과
   *
   * @author FIESTA
   * @since 오후 11:04
   **/
  NetworkProcess.OnResultListener getCompanyResult = new NetworkProcess.OnResultListener() {
    @Override
    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
      try {

        new ToastUtile().showCenterText(NhsMyPageModifyActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
      try {

        new ToastUtile().showCenterText(NhsMyPageModifyActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

      try {
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

          for (i = 0; i < size; i++) {

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
        new ToastUtile().showCenterText(NhsMyPageModifyActivity.this, msg);

      } catch (Exception ex) {

      } finally {

        // 내 정보를 받아온다.
        getMyInfo();

      }

    }


  };


  /**
   * 내 정보를 받아온다.
   * @author FIESTA
   * @since  오후 12:00
   **/
  private void getMyInfo(){

    NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
    NetworkParamUtil networkParamUtil = new NetworkParamUtil();

    String mbrId = StorageUtil.getStorageModeEx(NhsMyPageModifyActivity.this, LOGIN_MBR_ID);

    // 내 정보를 요청
    StringEntity param = networkParamUtil.getMyInfo(mbrId);
    NetworkProcess networkProcess = new NetworkProcess(NhsMyPageModifyActivity.this,
        networkUrlUtil.getMyInfo(),
        param,
        myInfoResult, true);
    networkProcess.sendEmptyMessage(0);

  }

  /**
   * 회원정보를 받아온 결과
   *
   * @author FIESTA
   * @since 오후 11:04
   **/
  NetworkProcess.OnResultListener myInfoResult = new NetworkProcess.OnResultListener() {
    @Override
    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
      try {

        new ToastUtile().showCenterText(NhsMyPageModifyActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
      try {

        new ToastUtile().showCenterText(NhsMyPageModifyActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

      } catch (Exception ex) {

      } finally {
      }
    }

    @Override
    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

      String msg = response.optString("result_msg");
      String resultCode = response.optString("result_code");

      if (resultCode.equalsIgnoreCase("Y")) {

        nhsMyInfoModel = new NhsMyInfoModel();

        String mbrNm = response.optString("mbrNm", "");
        String acrftCd = response.optString("acrftCd", "");
        String afftId = response.optString("afftId", "");
        String hpno = response.optString("hpno", "");
        String telno = response.optString("telno", "");
        String email = response.optString("email", "");
        String zipCode = response.optString("zipCode", "");
        String address = response.optString("address", "");
        String mbrGrade = response.optString("mbrGrade", "");
        String birday = response.optString("birday", "19700101");
        String afftNm = response.optString("afftNm", "");

        String id = StorageUtil.getStorageModeEx(NhsMyPageModifyActivity.this, LOGIN_ID);
        String mbrId = StorageUtil.getStorageModeEx(NhsMyPageModifyActivity.this, LOGIN_MBR_ID);
        String pwd = StorageUtil.getStorageModeEx(NhsMyPageModifyActivity.this, LOGIN_PWD);
        String name = StorageUtil.getStorageModeEx(NhsMyPageModifyActivity.this, LOGIN_NAME);

        if (!name.equals("null")) {
          nhsMyInfoModel.setMbrNm(name);
        }

        if (!mbrId.equals("null")) {
          nhsMyInfoModel.setMbrId(mbrId);
        }

        if (!afftId.equals("null")) {
          nhsMyInfoModel.setAfftId(afftId);
        }

        if (!hpno.equals("null")) {
          nhsMyInfoModel.setHpno(hpno);
        }

        if (!telno.equals("null")) {
          nhsMyInfoModel.setTelno(telno);
        }

        if (!email.equals("null")) {
          nhsMyInfoModel.setEmail(email);
        }

        if (!zipCode.equals("null")) {
          nhsMyInfoModel.setZipCode(zipCode);
        }

        if (!address.equals("null")) {
          nhsMyInfoModel.setAddress(address);
        }

        if (!birday.equals("null")) {
          nhsMyInfoModel.setBirday(birday);
        }

        if (!mbrGrade.equals("null")) {
          nhsMyInfoModel.setMbrGrade(mbrGrade);
        }

        if (!acrftCd.equals("null")) {
          nhsMyInfoModel.setAcrftCd(acrftCd);
        }

        if (!pwd.equals("null")) {
          nhsMyInfoModel.setLoginPw(pwd);
        }

        if (!afftNm.equals("null")) {
          nhsMyInfoModel.setAfftNm(afftNm);
        }

        tvId.setText(id);
        tvName.setText(nhsMyInfoModel.getMbrNm());
        tvCompany.setText(nhsMyInfoModel.getAfftNm());
        edEmail.setText(nhsMyInfoModel.getEmail());
        edPhoneNumber.setText(nhsMyInfoModel.getHpno());
        edAddress.setText(nhsMyInfoModel.getAddress());
        tvAcrftcd.setText(nhsMyInfoModel.getAcrftCd());
        //tvRegisterDate.setText(address);

        selectedCompany = new NhsCompanyModel();
        selectedCompany.setAfftNm(nhsMyInfoModel.getAfftNm());
        selectedCompany.setAfftId(nhsMyInfoModel.getAfftId());

      }


      // 메세지 출력
      new ToastUtile().showCenterText(NhsMyPageModifyActivity.this, msg);


    }


  };

}


