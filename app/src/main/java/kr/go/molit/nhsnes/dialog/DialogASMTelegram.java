package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import kr.go.molit.nhsnes.Network.NetworkProcess;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.activity.NhsLoginActivity;
import kr.go.molit.nhsnes.common.NetworkParamUtil;
import kr.go.molit.nhsnes.common.NetworkUrlUtil;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.widget.TextViewEx;

/**
 * Created by jongrakmoon on 2017. 4. 1..
 */

public class DialogASMTelegram extends DialogBase implements View.OnClickListener {

    private TextViewEx tveAsm = null;

    public DialogASMTelegram(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_asm_telegram);
        findViewById(R.id.btn_cancel).setOnClickListener(this);

        // 전문 내용
        this.tveAsm = (TextViewEx) this.findViewById(R.id.tve_content);

        // 노탐 정보를 가져온다.
        getNotam();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    /**
     * 노탐 정보를 가져와서 출력한다.
     *
     * @author FIESTA
     * @since 오전 12:36
     **/
    private void getNotam() {

        NetworkUrlUtil networkUrlUtil = new NetworkUrlUtil();
        NetworkParamUtil networkParamUtil = new NetworkParamUtil();

        StringEntity param = networkParamUtil.notamParam(getContext());
        NetworkProcess networkProcess = new NetworkProcess(getContext(),
                networkUrlUtil.getNotam(),
                param,
                new NetworkProcess.OnResultListener() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        try {

                            new ToastUtile().showCenterText(getContext(), getContext().getString(R.string.error_network) + "(" + statusCode + ")");

                        } catch (Exception ex) {

                        } finally {
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        try {

                            new ToastUtile().showCenterText(getContext(), getContext().getString(R.string.error_network) + "(" + statusCode + ")");

                        } catch (Exception ex) {

                        } finally {
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        String msg = response.optString("result_msg");
                        String resultCode = response.optString("result_code");

                        if (resultCode.equalsIgnoreCase("Y")) {

                            JSONArray resultData = response.optJSONArray("result_data");

                            StringBuilder sb = new StringBuilder();

                            int size = resultData.length();
                            if(size > 0) {
                                int i = 0;

                                for (i = 0; i < size; i++) {

                                    Iterator iterator = resultData.optJSONObject(i).keys();


                                    while (iterator.hasNext()) {

                                        try {

                                            String key = (String) iterator.next();
                                            String value = resultData.getJSONObject(i).get(key).toString();
                                            sb.append(key);
                                            sb.append(" : ");
                                            sb.append(value);
                                            sb.append("\n");

                                        } catch (Exception ex) {

                                        }
                                    }


                                }


                                tveAsm.setText(sb.toString());
                            } else {
                                tveAsm.setText("수신데이터 없습니다.");
                            }
                        }else {
                            tveAsm.setText("수신데이터 없습니다.");
                        }

                    }
                }, true);
        networkProcess.sendEmptyMessage(0);

    }

}
