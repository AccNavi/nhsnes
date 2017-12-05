package kr.go.molit.nhsnes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import cz.msebera.android.httpclient.entity.StringEntity;
import kr.go.molit.nhsnes.Network.NetworkProcess;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.NetworkParamUtil;
import kr.go.molit.nhsnes.common.NetworkUrlUtil;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.ToastUtile;

import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_ID;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_MBR_ID;

/**
 * 마이 페이지
 *
 * @author FIESTA
 * @since 오후 12:14
 **/
public class NhsMyPageActivity extends NhsBaseFragmentActivity implements View.OnClickListener {

    private TextView tvId = null;
    private TextView tvCompany = null;
    private TextView tvName = null;
    private TextView tvEmail = null;
    private TextView tvPhoneNumber = null;
    private TextView tvAddress = null;
    private TextView tvAcrftcd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_my_page);

        setLayout();

        // 내 정보를 받아온다.
        getMyInfo();


    }

    private void setLayout() {

        this.tvId = (TextView) findViewById(R.id.tv_id);
        this.tvCompany = (TextView) findViewById(R.id.tv_company);
        this.tvName = (TextView) findViewById(R.id.tv_name);
        this.tvEmail = (TextView) findViewById(R.id.tv_email);
        this.tvPhoneNumber = (TextView) findViewById(R.id.tv_phone_number);
        this.tvAddress = (TextView) findViewById(R.id.tv_address);
        this.tvAcrftcd = (TextView) findViewById(R.id.tv_acrftcd);

        findViewById(R.id.fl_modify).setOnClickListener(this);
        findViewById(R.id.fl_leave).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.fl_modify:

                Intent modifyIntent = new Intent(NhsMyPageActivity.this, NhsMyPageModifyActivity.class);
                startActivityForResult(modifyIntent, 100);

                break;

            case R.id.fl_leave:
                Intent leaveIntent = new Intent(NhsMyPageActivity.this, NhsLeaveActivity.class);
                startActivity(leaveIntent);
                break;

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                // 내 정보를 받아온다.
                getMyInfo();
            }
        }
    }

    /**
     * 내 정보를 받아온다.
     *
     * @author FIESTA
     * @since 오후 12:00
     **/
    private void getMyInfo() {

        NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
        NetworkParamUtil networkParamUtil = new NetworkParamUtil();

        String mbrId = StorageUtil.getStorageModeEx(NhsMyPageActivity.this, LOGIN_MBR_ID);

        // 내 정보를 요청
        StringEntity param = networkParamUtil.getMyInfo(mbrId);
        NetworkProcess networkProcess = new NetworkProcess(NhsMyPageActivity.this,
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

                new ToastUtile().showCenterText(NhsMyPageActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

            } catch (Exception ex) {

            } finally {
            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
            try {

                new ToastUtile().showCenterText(NhsMyPageActivity.this, getString(R.string.error_network) + "(" + statusCode + ")");

            } catch (Exception ex) {

            } finally {
            }
        }

        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

            String msg = response.optString("result_msg");
            String resultCode = response.optString("result_code");

            if (resultCode.equalsIgnoreCase("Y")) {

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

                String id = StorageUtil.getStorageModeEx(NhsMyPageActivity.this, LOGIN_ID);

                if (!id.equals("null")) {
                    tvId.setText(id);
                } else {
                    tvId.setText("");
                }

                if (!mbrNm.equals("null")) {
                    tvName.setText(mbrNm);
                } else {
                    tvName.setText("");
                }

                if (!afftNm.equals("null")) {
                    tvCompany.setText(afftNm);
                } else {
                    tvCompany.setText("");
                }

                if (!email.equals("null")) {
                    tvEmail.setText(email);
                } else {
                    tvEmail.setText("");
                }

                if (!hpno.equals("null")) {
                    tvPhoneNumber.setText(hpno);
                } else {
                    tvPhoneNumber.setText("");
                }

                if (!address.equals("null")) {
                    tvAddress.setText(address);
                } else {
                    tvAddress.setText("");
                }

                if (!acrftCd.equals("null")) {
                    tvAcrftcd.setText(acrftCd);
                } else {
                    tvAcrftcd.setText("");
                }

                //tvRegisterDate.setText(address);

            }


            // 메세지 출력
            new ToastUtile().showCenterText(NhsMyPageActivity.this, msg);


        }


    };


}


