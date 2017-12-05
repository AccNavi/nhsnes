package kr.go.molit.nhsnes.common;

import android.content.Context;

import com.dreamsecurity.e2e.MagicSE2;

/**
 * Created by Jung on 2017-11-09.
 */

public class MagicSE_Util {
    Context mContext;

    public MagicSE_Util(Context context){
        mContext = context;
    }

    //암호화된 데이터
    public String getEncData(String data){
        MagicSE2 magicSE = null;
        String sessionKey = null;
        String result = null;

        try {
            magicSE = new MagicSE2(mContext);
            sessionKey = (String)SharedData.getSharedData(mContext, SharedData.SESSION_KEY);
            result = magicSE.MagicSE_EncData(sessionKey, data.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        sessionKey = (String)SharedData.getSharedData(mContext, SharedData.SESSION_KEY);

        return result;
    }

    //복호화된 데이터
    public String getDecData(String data){
        MagicSE2 magicSE = null;
        String sessionKey = null;
        byte[] result = null;

        try {
            magicSE = new MagicSE2(mContext);
            sessionKey = (String)SharedData.getSharedData(mContext, SharedData.SESSION_KEY);
            result = magicSE.MagicSE_DecData(sessionKey, data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String(result);
    }
}
