package kr.go.molit.nhsnes.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

/**
 * Created by jongrakmoon on 2017. 5. 29..
 */

public class StorageUtil {
    public static final int PERMISSION_STORAGE_REQUEST_CODE = 100;

    private static final String TAG = "Storage";
    private static final String FLAG_SAVE_MODE = "SaveMode";

    private static final String DIR_REALM_SAVE = "Realm";

    public enum Storage {
        LOCAL("local"), EXTERNAL("external");

        String value;

        Storage(String value) {
            this.value = value;
        }

        public static Storage parse(String value) {
            if (LOCAL.value.equals(value)) {
                return LOCAL;
            } else if (EXTERNAL.value.equals(value)) {
                return EXTERNAL;
            } else {
                return null;
            }
        }
    }

    public static File getSaveStorage(Context context) {
        Storage storage = getStorageMode(context);

        File saveDir;

        if (storage == Storage.LOCAL || storage == null) {
            saveDir = new File(context.getFilesDir(), DIR_REALM_SAVE);
        } else {
            saveDir = new File(context.getExternalFilesDir(null), DIR_REALM_SAVE);
        }

        if (!saveDir.exists()) {
            saveDir.mkdir();
        }

        return saveDir;
    }

    public static boolean isAvailableExternalStorage() {
        //String state = Environment.getExternalStorageState();
        //return state.equals(Environment.MEDIA_MOUNTED);
        String dir = Util.getMicroSDCardDirectory();
        Log.d("JeLib","dir::"+dir);
        if(dir!=null && dir.length() > 0)
        {
            return true;
        }
        return false;
    }

    public static void setStorageMode(Context context, Storage storage) {
        context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
                .edit().putString(FLAG_SAVE_MODE, storage.value)
                .commit();
    }

    public static void setStorageMode(Context context, String key, String value) {
        context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            .edit().putString(key, value)
            .commit();
    }

    public static void setStorageMode(Context context, String key, boolean value) {
        context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            .edit().putBoolean(key, value)
            .commit();
    }


    public static Storage getStorageMode(Context context) {
        return Storage.parse(context.getSharedPreferences(TAG, Context.MODE_PRIVATE).getString(FLAG_SAVE_MODE, null));
    }

    public static boolean getStorageMode(Context context, String key) {
        return context.getSharedPreferences(TAG, Context.MODE_PRIVATE).getBoolean(key, false);
    }

    public static boolean getStorageModeWithDefaultValue(Context context, String key, boolean defaultValue) {
        return context.getSharedPreferences(TAG, Context.MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    public static String getStorageModeEx(Context context, String key) {
        return context.getSharedPreferences(TAG, Context.MODE_PRIVATE).getString(key,"");
    }

    public static String getStorageModeEx(Context context, String key, String defaultValue) {
        return context.getSharedPreferences(TAG, Context.MODE_PRIVATE).getString(key, defaultValue);
    }

    public static boolean checkStoragePermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {


                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(activity, "외부저장소에 접근하기 위해 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STORAGE_REQUEST_CODE);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

}
