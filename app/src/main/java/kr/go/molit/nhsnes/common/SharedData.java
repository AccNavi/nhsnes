package kr.go.molit.nhsnes.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jung on 2017-11-09.
 */

public class SharedData {
    public static final String SHARED_NAME = "NHS";

    public static final String SESSION_KEY = "session_key";

    public static boolean saveSharedData(Context context, String strKey, Object objData){
        return saveSharedData(context,SHARED_NAME,strKey,objData);
    }

    public static boolean saveSharedData(Context context, String strPrefName, String strKey, Object objData){
        SharedPreferences prefs = context.getSharedPreferences(strPrefName, Activity.MODE_PRIVATE);

        if(prefs == null || strKey == null || objData == null){
            return false;
        }

        SharedPreferences.Editor ed = prefs.edit();
        if(Boolean.class == objData.getClass()){
            ed.putBoolean(strKey, (Boolean)objData);
        }
        else if(Integer.class == objData.getClass()){
            ed.putInt(strKey, (Integer)objData);
        }
        else if(Long.class == objData.getClass()){
            ed.putLong(strKey, (Long)objData);
        }
        else if(Float.class == objData.getClass()){
            ed.putFloat(strKey, (Float)objData);
        }
        else if(String.class == objData.getClass()){
            ed.putString(strKey, (String)objData);
        }
        else{
            return false;
        }

        ed.commit();

        return true;
    }

    /**
    * shareed 정보 반환
    * @author FIESTA
    * @version 1.0.0
    * @since 오후 5:31
    **/
    public static Object getSharedData(Context context, String strKey){
        return getSharedData(context,SHARED_NAME,strKey,new String());
    }

    /**
     * shareed 정보 반환
     * @author FIESTA
     * @version 1.0.0
     * @since 오후 5:31
     **/
    public static Object getSharedData(Context context, String strKey, Object objData){
        return getSharedData(context,SHARED_NAME,strKey, objData);
    }

    /**
     * shareed 정보 반환
     * @author FIESTA
     * @version 1.0.0
     * @since 오후 5:31
     **/
    public static Object getSharedData(Context context, String strPrefName, String strKey, Object objData){
        SharedPreferences prefs = context.getSharedPreferences(strPrefName, Activity.MODE_PRIVATE);

        if(prefs == null || strKey == null){
            return null;
        }

        Object objReturn = null;

        if(Boolean.class == objData.getClass()){
            objReturn = new Boolean(prefs.getBoolean(strKey, (Boolean)objData));
        }
        else if(Integer.class == objData.getClass()){
            objReturn = new Integer(prefs.getInt(strKey, (Integer)objData));
        }
        else if(Long.class == objData.getClass()){
            objReturn = new Long(prefs.getLong(strKey, (Long)objData));
        }
        else if(Float.class == objData.getClass()){
            objReturn = new Float(prefs.getFloat(strKey, (Float)objData));
        }
        else if(String.class == objData.getClass()){
            objReturn = prefs.getString(strKey, (String)objData);
        }
        else{
            return null;
        }

        return objReturn;
    }
}
