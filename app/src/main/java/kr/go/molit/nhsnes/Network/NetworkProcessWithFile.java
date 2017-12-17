package kr.go.molit.nhsnes.Network;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Entity;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.Header;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.NetworkParamUtil;

public class NetworkProcessWithFile extends AsyncTask<Void, Integer, Void> {

    public interface OnResultListener {

        public void onFailure();

        public void onSuccess(File file);

        public void onStart(String fileName);

    }

    private ProgressDialog mProgressDialog;
    private Context context;
    private String direction;
    private StringEntity param;
    private OnResultListener onResult;
    private boolean showProgressBar = true;
    private File downlaodFile = null;
    private String downloadPath = "";
    private String fileName = "";
    private boolean isSucc = false;

    public NetworkProcessWithFile(Context context, String direction,
                                  StringEntity param, String downloadPath, String fileName, OnResultListener onResult, boolean showProgressBar) {

        this.direction = direction;
        this.param = param;
        this.context = context;
        this.onResult = onResult;
        this.showProgressBar = showProgressBar;
        this.downloadPath = downloadPath;
        this.fileName = fileName;

    }


    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        try {
            if (this.showProgressBar) {

                this.mProgressDialog = new ProgressDialog(this.context, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
                this.mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                this.mProgressDialog.setTitle("");
                this.mProgressDialog.setMessage(this.context.getString(R.string.wait_message));
                this.mProgressDialog.setIndeterminate(true);
                this.mProgressDialog.setCancelable(false);
                this.mProgressDialog.show();

            }
        } catch(Exception e){

        }
        if (onResult != null) {
            onResult.onStart(fileName);
        }

    }

    @Override
    protected Void doInBackground(Void... params) {
        // TODO Auto-generated method stub

        FileOutputStream fileOutput = null;
        InputStream is = null;

        try {

            HttpEntity resEntity;

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(this.direction);

            post.addHeader("Accept", "application/json");
            post.addHeader("Content-type", "application/json");

            String data = " ";

            if (this.param != null) {

                // 파라미터를 가져온다.
                data = new String(inputStreamToByteArray(this.param.getContent()));
            }

            // 암호화 인코딩한다.
            this.param = new NetworkParamUtil().encDataDonwload(this.context, data);

            post.setEntity(this.param);

            HttpResponse response = client.execute(post);
            resEntity = response.getEntity();

            is = resEntity.getContent();

            this.downlaodFile = new File(this.downloadPath, this.fileName);

            if (!this.downlaodFile.exists()) {
                this.downlaodFile.getParentFile().mkdirs();
            }

            fileOutput = new FileOutputStream(this.downlaodFile);

            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ((bufferLength = is.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                isSucc = true;
            }
        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {
            try {
                is.close();
                fileOutput.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        if (this.onResult != null) {

            if (this.downlaodFile != null && isSucc) {
                onResult.onSuccess(this.downlaodFile);
            } else {
                onResult.onFailure();
            }
        }

        if (this.showProgressBar) {
            try {
                mProgressDialog.dismiss();
            } catch (Exception e){

            }
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

}
