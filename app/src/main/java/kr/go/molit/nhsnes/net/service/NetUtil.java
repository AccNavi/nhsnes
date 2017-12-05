package kr.go.molit.nhsnes.net.service;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.entity.StringEntity;
import kr.go.molit.nhsnes.common.NetworkParamUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static kr.go.molit.nhsnes.Network.NetworkProcess.inputStreamToByteArray;

/**
 * Created by user on 2017-08-17.
 */

public class NetUtil {

    public static RequestBody mapToJsonBody(Context context, Map<String,Object> params){
        JSONObject jsonObj = new JSONObject();

        try {
            Iterator<String> keys = params.keySet().iterator();
            while( keys.hasNext() ){
                String key = keys.next();
                if(params.get(key).getClass().getSimpleName().equals("HashMap")){
                    JSONObject subJsonObj = new JSONObject();
                    Map<String,Object> subMap = (HashMap<String,Object>) params.get(key);
                    Log.d("JeLib","subMap:"+subMap);
                    Iterator<String> subkeys = subMap.keySet().iterator();
                    while( subkeys.hasNext() ) {
                        String skey = subkeys.next();
                        Log.d("JeLib","skey:"+skey);
                        subJsonObj.put(skey, subMap.get(skey));
                    }
                    Log.d("JeLib","key:"+key+" subJsonObj:"+subJsonObj);
                    jsonObj.put(key, subJsonObj);
                } else if(params.get(key).getClass().getSimpleName().equals("ArrayList")){
                    JSONArray jsonArr = new JSONArray();
                    ArrayList subList = (ArrayList) params.get(key);
                    for(int i = 0 ; i < subList.size() ; i++){

                        JSONObject subJsonObj = new JSONObject();
                        HashMap subMap = (HashMap) subList.get(i);
                        Iterator<String> subkeys = subMap.keySet().iterator();
                        while( subkeys.hasNext() ) {
                            String skey = subkeys.next();
                            subJsonObj.put(skey, subMap.get(skey));
                        }
                        jsonArr.put(subJsonObj);
                    }
                    jsonObj.put(key, jsonArr);
                } else {
                    jsonObj.put(key, params.get(key));
                }
                Log.d("JeLib", String.format("키 : %s, 값 : %s", key, params.get(key)));
            }
        } catch (JSONException e) {
        }

        String json = jsonObj.toString();

        // 암호화 인코딩한다.
        StringEntity param = new NetworkParamUtil().encData(context, json);

        // 파라미터를 가져온다.
        try {
            json = new String(inputStreamToByteArray(param.getContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return RequestBody.create(MediaType.parse("application/json"), json);
    }
}
