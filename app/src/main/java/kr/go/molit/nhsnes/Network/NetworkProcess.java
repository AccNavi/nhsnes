package kr.go.molit.nhsnes.Network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.entity.StringEntity;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.MagicSE_Util;
import kr.go.molit.nhsnes.common.NetworkParamUtil;

/**
 * 네트워크 통신 모듈
 *
 * @author FIESTA
 * @since 오전 12:40
 **/
public class NetworkProcess extends Handler {

    public interface OnResultListener {

        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable);

        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse);

        public void onSuccess(int statusCode, Header[] headers, JSONObject response);

    }

    private ProgressDialog mProgressDialog;
    private Context context;
    private String direction;
    private StringEntity param;
    private OnResultListener onResult;
    private boolean showProgressBar = true;
    private HashMap<String, String> customHeader = null;  // 헤더를 커스텀 한다면 해당 변수로.
    private String contentType = "application/json";    // 기본은 application/json 이다.
    private int timeout = 60000;                           // 기본 60초
    private MagicSE_Util magic = null;                     // 암호화/복호화 객체
    private boolean isSecurity = true;                   // 암호화 전송 여부


    public NetworkProcess(Context context, String direction,
                          StringEntity param, OnResultListener onResult, boolean showProgressBar) {

        this.direction = direction;
        this.param = param;
        this.context = context;
        this.onResult = onResult;
        this.showProgressBar = showProgressBar;
        this.magic = new MagicSE_Util(context);
    }

    public NetworkProcess(Context context, String direction,
                          StringEntity param, OnResultListener onResult, boolean isSecurity, boolean showProgressBar) {

        this.direction = direction;
        this.param = param;
        this.context = context;
        this.onResult = onResult;
        this.showProgressBar = showProgressBar;
        this.isSecurity = isSecurity;
        this.magic = new MagicSE_Util(context);

    }

    public NetworkProcess(Context context, String direction,
                          StringEntity param, OnResultListener onResult) {

        this.direction = direction;
        this.param = param;
        this.context = context;
        this.onResult = onResult;
        this.magic = new MagicSE_Util(context);

    }


    @Override
    public void handleMessage(Message msg) {
        // TODO Auto-generated method stub
        super.handleMessage(msg);

        try {

            try {
                if (showProgressBar) {
                    this.mProgressDialog = new ProgressDialog(this.context, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    this.mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    this.mProgressDialog.setTitle("");
                    this.mProgressDialog.setMessage(this.context.getString(R.string.wait_message));
                    this.mProgressDialog.setIndeterminate(true);
                    this.mProgressDialog.setCancelable(false);
                    this.mProgressDialog.show();
                }
            } catch (Exception ex) {

                showProgressBar = false;
                this.mProgressDialog = null;
            }

            AsyncHttpClient client = new AsyncHttpClient();

            // 커스텀 해더가 없으면
            if (this.customHeader == null) {

                client.addHeader("Accept", "application/json");
                client.addHeader("Content-type", "application/json");

            } else {  // 커스텀 해더가 있으면

                for (String key : this.customHeader.keySet()) {
                    client.addHeader(key, this.customHeader.get(key));
                }

            }

            // 타임아웃 설정
            client.setTimeout(this.timeout);

            if (this.isSecurity) {
                if (this.param != null) {

                    // 파라미터를 가져온다.
                    String data = new String(inputStreamToByteArray(this.param.getContent()));

                    // 암호화 인코딩한다.
                    this.param = new NetworkParamUtil().encData(this.context, data);

                }
            }

            client.post(this.context, this.direction, this.param, this.contentType, new JsonHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      String responseString, Throwable throwable) {
                    // TODO Auto-generated method stub
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (onResult != null) {
                        onResult.onFailure(statusCode, headers, responseString, throwable);
                    }

                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }

                }

                public void onFailure(int statusCode, Header[] headers,
                                      Throwable throwable, JSONObject errorResponse) {
                    // TODO Auto-generated method stub
                    super.onFailure(statusCode, headers, throwable, errorResponse);

                    if (onResult != null) {
                        onResult.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      JSONObject response) {

                    // 보안 적용 여부
                    if (isSecurity) {

                        // TODO Auto-generated method stub
                        if (response != null) {

                            String resultCode = response.optString("result_code", "N");

                            if (resultCode.equalsIgnoreCase("Y")) {

                                boolean isNull = response.isNull("result_data");

                                if (!isNull) {

                                    String resultData = response.optString("result_data", "");

                                    try {

                                        // 복호화한다.
                                        resultData = magic.getDecData(resultData);

                                        // 복호화하 내용을 json으로 변환한다.
                                        response = new JSONObject(resultData);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }

                        }
                    }

                    super.onSuccess(statusCode, headers, response);

                    try {
                        if (onResult != null) {
                            onResult.onSuccess(statusCode, headers, response);
                        }
                    } catch (Exception ex) {

                    }

                    try {
                        if (showProgressBar) {
                            mProgressDialog.dismiss();
                        }
                    } catch (Exception ex) {

                    }

                }

            });

        } catch (Exception ex) {

        }

    }

    public static byte[] inputStreamToByteArray(InputStream is) {

        byte[] resBytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int read = -1;
        try {
            while ((read = is.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
            }

            resBytes = bos.toByteArray();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resBytes;
    }

    public HashMap<String, String> getCustomHeader() {
        return customHeader;
    }

    public void setCustomHeader(HashMap<String, String> customHeader) {
        this.customHeader = customHeader;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

}
