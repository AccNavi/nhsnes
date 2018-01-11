package kr.go.molit.nhsnes.common;

import static android.provider.Settings.System.SCREEN_BRIGHTNESS;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.util.Base64;

import com.modim.lan.lanandroid.AirPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kisa.SEEDCBC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 자주 사용하는 함수들 모음
 *
 * @author 정제영
 * @version 1.0, 2017.03.06 최초 작성
 **/

public class Util {

    public static byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    public static String aesKey = "1234567890123456";

    /**
     * 테스트 설정
     */
    public static boolean isTest = false;

    /**
     * 로그
     *
     * @author 정제영
     * @version 1.0, 2017.03.04 최초 작성
     **/
    public static void Log(String msg) {
        if (isTest)
            android.util.Log.d("NHS", msg);
    }

    /**
     * 도분초를 좌표로 변환
     *
     * @author FIESTA
     * @since 오후 11:47
     **/
    public static double getDoToDgree(String convertString) {

        int size = convertString.length();
        int xdo = Integer.parseInt(convertString.substring(0, size-4));
        int xmn = Integer.parseInt(convertString.substring(size-4, size-2));
        int xsec = Integer.parseInt(convertString.substring(size-2, size));

        return (double) xdo + ((double) xmn / 60.0) + ((double) xsec / 3600.0);

    }

    /**
     * 진행 방향각을 구한다,.
     *
     * @author FIESTA
     * @since 오후 11:47
     **/
    public static float bearingP1toP2(double P1_latitude, double P1_longitude, double P2_latitude, double P2_longitude)
    {
        // 현재 위치 : 위도나 경도는 지구 중심을 기반으로 하는 각도이기 때문에 라디안 각도로 변환한다.
        double Cur_Lat_radian = P1_latitude * (3.141592 / 180);
        double Cur_Lon_radian = P1_longitude * (3.141592 / 180);


        // 목표 위치 : 위도나 경도는 지구 중심을 기반으로 하는 각도이기 때문에 라디안 각도로 변환한다.
        double Dest_Lat_radian = P2_latitude * (3.141592 / 180);
        double Dest_Lon_radian = P2_longitude * (3.141592 / 180);

        // radian distance
        double radian_distance = 0;
        radian_distance = Math.acos(Math.sin(Cur_Lat_radian) * Math.sin(Dest_Lat_radian) + Math.cos(Cur_Lat_radian) * Math.cos(Dest_Lat_radian) * Math.cos(Cur_Lon_radian - Dest_Lon_radian));

        // 목적지 이동 방향을 구한다.(현재 좌표에서 다음 좌표로 이동하기 위해서는 방향을 설정해야 한다. 라디안값이다.
        double radian_bearing = Math.acos((Math.sin(Dest_Lat_radian) - Math.sin(Cur_Lat_radian) * Math.cos(radian_distance)) / (Math.cos(Cur_Lat_radian) * Math.sin(radian_distance)));        // acos의 인수로 주어지는 x는 360분법의 각도가 아닌 radian(호도)값이다.

        double true_bearing = 0;
        if (Math.sin(Dest_Lon_radian - Cur_Lon_radian) < 0)
        {
            true_bearing = radian_bearing * (180 / 3.141592);
            true_bearing = 360 - true_bearing;
        }
        else
        {
            true_bearing = radian_bearing * (180 / 3.141592);
        }

        return (float)true_bearing;
    }

    /**
     * string null 체크
     *
     * @param str 입력 받는 값
     * @param def null일 경우 리턴 값
     * @author 정제영
     * @version 1.0, 2017.03.04 최초 작성
     **/
    public static String NullString(String str, String def) {
        if (str == null || str.length() <= 0) return def;
        return str;
    }

    public static String getMimeType(String url) {
        String type = "";
        String extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(url)).toString());
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }

        if (type == null) {
            type = "";
        }
        return type;
    }

    /**
     * 도분초를 좌표로 환산한다.
     *
     * @author FIESTA
     * @since 오후 11:47
     **/
    public static double convertLocation(float fDo, float fBun, float fCho) {

        double cal = fDo + ((fBun / 60) + (fCho / 3600));
        String str = String.format("%.6f", cal);

        return Double.parseDouble(str);
    }

    /**
     * 맵 좌표를 우리가 보는 좌표로 바꾼다
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-26 오후 10:59
     **/
    public static int convertValue(String value) {

        String tenoValue = value.replaceAll("\\.", "");
        int len = tenoValue.length();
        int temp = 9 - len;
        int itemp = Integer.parseInt(tenoValue);
        int i = 0;

        if (temp > 0) {

            for (i = 0; i < temp; i++) {
                itemp = itemp * 10;
            }

        }


        return itemp;

    }

    /**
     * string 자르기
     *
     * @param result 자를 string
     * @param cStr   구분자
     * @return string 배열
     * @author 정제영
     * @version 1.0, 2017.03.04 최초 작성
     **/
    public static String[] Split(String result, String cStr) {

        int nowIndex = 0;
        int firstIndex = 0;
        String[] buffer = null;
        Vector vector = new Vector();

        while (true) {
            nowIndex = result.indexOf(cStr, firstIndex);
            if (nowIndex == -1) {
                if (result.length() >= firstIndex) {
                    vector.addElement(result.substring(firstIndex, result.length()));
                }
                break;
            } else {
                vector.addElement(result.substring(firstIndex, nowIndex));
                firstIndex = nowIndex + 1;
            }
        }
        if (!vector.isEmpty()) {
            buffer = new String[vector.size()];
            Enumeration enu = vector.elements();
            for (int i = 0; enu.hasMoreElements(); i++) {
                buffer[i] = (String) enu.nextElement();
            }
            vector.removeAllElements();
        }
        vector = null;
        return buffer;
    }

    /**
     * 앱 버전 가져오기
     *
     * @return app version
     * @author 정제영
     * @version 1.0, 2017.03.04 최초 작성
     **/
    public static String GetAppVerName(Context context) {
        String version = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            //  PackageInfo infor = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            PackageInfo infor = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            version = infor.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Util.Log("NameNotFoundException");
        }
        return version;
    }

    /**
     * string을 integer로 변환
     *
     * @return integer로 변환된 num
     * @author 정제영
     * @version 1.0, 2017.03.04 최초 작성
     **/
    public static int StringToInt(String str) {
        int num = 0;
        try {
            num = Integer.parseInt(str);
        } catch (Exception e) {
            num = 0;
        }
        return num;
    }

    /**
     * 항공 마켓으로 이동한다
     *
     * @author FIESTA
     * @since 오후 11:58
     **/
    public static void moveToMarket(Context context) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
        context.startActivity(intent);

    }

    /**
     * 추가 적인 외부 확장 SDCard path 얻기.
     * 조건으로 걸러진 최종 String List size가 1이 아니면 null 리턴
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getMicroSDCardDirectory() {
        List<String> mMounts = readMountsFile();
        List<String> mVold = readVoldFile();

        Log.d("JeLib", "mVold.size:" + mVold.size());

        for (int i = 0; i < mMounts.size(); i++) {
            String mount = mMounts.get(i);
            Log.d("JeLib", "mount:" + mount);
            if (mVold != null && mVold.size() > 0) {
                if (!mVold.contains(mount)) {
                    mMounts.remove(i--);
                    Log.d("JeLib", "======================1");
                    continue;
                }

                File root = new File(mount);
                if (!root.exists() || !root.isDirectory()) {
                    mMounts.remove(i--);
                    Log.d("JeLib", "======================2");
                    continue;
                }

                if (!isAvailableFileSystem(mount)) {
                    mMounts.remove(i--);
                    Log.d("JeLib", "======================3");
                    continue;
                }

                if (!checkMicroSDCard(mount)) {
                    mMounts.remove(i--);
                    Log.d("JeLib", "======================5");
                }
            }
        }
        Log.d("JeLib", "mMounts.size:" + mMounts.size());
        if (mMounts.size() >= 1) {
            Log.d("JeLib", "======================");
            return mMounts.get(0);
        }

        return null;
    }

    public static final byte[] key = {
            0x01, 0x02, 0x03, 0x04,
            0x01, 0x02, 0x03, 0x04,
            0x01, 0x02, 0x03, 0x04,
            0x01, 0x02, 0x03, 0x04,
    };

    public static final byte[] iv = {
            0x01, 0x02, 0x03, 0x04,
            0x01, 0x02, 0x03, 0x04,
            0x01, 0x02, 0x03, 0x04,
            0x01, 0x02, 0x03, 0x04,
    };

    /**
     * 복호화 seed
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-12 오후 3:07
     **/
    public static String decryption(String strText) {
        if (strText == null || strText.equals("")) return "";

        byte[] cipherText = new java.math.BigInteger(strText.trim(), 16).toByteArray();
        byte[] plainText = new byte[144];
        int outputTextLen = 0;

        SEEDCBC seed = new SEEDCBC();
        seed.init(SEEDCBC.DEC, key, iv);

        outputTextLen = seed.process(
                cipherText, 0,
                cipherText.length,
                plainText, 0);

        seed.close(plainText, outputTextLen);

        strText = new String(plainText).trim();

        return strText;
    }

    /**
     * 암호화 seed
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-12 오후 3:07
     **/
    public static String encryption(String strText) {
        byte[] plainText = strText.getBytes();
        byte[] cipherText = new byte[plainText.length + 16];
        int outputTextLen = 0;

        SEEDCBC seed = new SEEDCBC();
        seed.init(SEEDCBC.ENC, key, iv);

        outputTextLen = seed.process(
                plainText, 0,
                plainText.length,
                cipherText, 0);

        seed.close(cipherText, outputTextLen);
        String hexText = new java.math.BigInteger(cipherText).toString(16);

        strText = new String(hexText);
        return strText.trim();
    }

    /**
     * 파일을 쓴다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-12 오후 2:35
     **/
    public static void writeStringAsFile(String filePath, String fileName, String content) {

        FileWriter out = null;

        try {

            File file = new File(filePath, fileName);

            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }

            out = new FileWriter(file);
            out.write(content);

        } catch (Exception e) {


        } finally {

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    /**
     * 파일을 쓴다. (파일이 있으면 이어 쓴다)
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-12 오후 2:35
     **/
    public static void appendStringAsFile(String filePath, String fileName, String content) {

        BufferedWriter bw = null;

        try {
            File file = new File(filePath, fileName);

            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }

            bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(content);
            bw.newLine();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<String> readMountsFile() {
        /**
         * Scan the /proc/mounts file and look for lines like this:
         * /dev/block/vold/179:1 /mnt/sdcard vfat rw,dirsync,nosuid,nodev,noexec,relatime,uid=1000,gid=1015,fmask=0602,dmask=0602,allow_utime=0020,codepage=cp437,iocharset=iso8859-1,shortname=mixed,utf8,errors=remount-ro 0 0
         *
         * When one is found, split it into its elements
         * and then pull out the path to the that mount point
         * and add it to the arraylist
         */
        List<String> mMounts = new ArrayList<String>();

        try {
            Scanner scanner = new Scanner(new File("/proc/mounts"));

            while (scanner.hasNext()) {
                String line = scanner.nextLine();

                if (line.startsWith("/dev/block/vold/")) {
                    String[] lineElements = line.split("[ \t]+");
                    String element = lineElements[1];

                    mMounts.add(element);
                }
            }
        } catch (Exception e) {
            // Auto-generated catch block
            e.printStackTrace();
        }

        return mMounts;
    }

    private static List<String> readVoldFile() {
        /**
         * Scan the /system/etc/vold.fstab file and look for lines like this:
         * dev_mount sdcard /mnt/sdcard 1 /devices/platform/s3c-sdhci.0/mmc_host/mmc0
         *
         * When one is found, split it into its elements
         * and then pull out the path to the that mount point
         * and add it to the arraylist
         */

        List<String> mVold = new ArrayList<String>();

        try {
            Scanner scanner = new Scanner(new File("/system/etc/vold.fstab"));

            while (scanner.hasNext()) {
                String line = scanner.nextLine();

                if (line.startsWith("dev_mount")) {
                    String[] lineElements = line.split("[ \t]+");
                    String element = lineElements[2];

                    if (element.contains(":")) {
                        element = element.substring(0, element.indexOf(":"));
                    }

                    mVold.add(element);
                }
            }
        } catch (Exception e) {
            // Auto-generated catch block
            e.printStackTrace();
        }

        return mVold;
    }

    /**
    * MicroSDCard 검사
    * @author FIESTA
    * @version 1.0.0
    * @since 오후 5:33
    **/
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static boolean checkMicroSDCard(String fileSystemName) {
        StatFs statFs = new StatFs(fileSystemName);
        Log.d("JeLib", "statFs.getTotalBytes:" + statFs.getTotalBytes());

        long totalSize = (long) statFs.getBlockSizeLong() * statFs.getBlockCountLong();

        if (totalSize < 1024 * 1024 * 1024) {
            return false;
        }

        return true;
    }

    private static boolean isAvailableFileSystem(String fileSystemName) {
        final String[] unAvailableFileSystemList = {"/dev", "/mnt/asec", "/mnt/obb", "/system", "/data", "/cache", "/efs", "/firmware"};   // 알려진 File System List입니다.

        for (String name : unAvailableFileSystemList) {
            if (fileSystemName.contains(name) == true) {
                return false;
            }
        }

        if (Environment.getExternalStorageDirectory().getAbsolutePath().equals(fileSystemName) == true) {
            /** 안드로이드에서 제공되는 getExternalStorageDirectory() 경로와 같은 경로일 경우에는 추가로 삽입된 SDCard가 아니라고 판단하였습니다. **/
            return false;
        }

        return true;
    }

    /**
     * string 날짜를 date로 만들어서 반환한다.
     *
     * @param strDate   string 날짜
     * @param strFormat date의 포맷형식
     * @return Date      변환된 date 객체
     * @author FIESTA
     * @since 오후 11:34
     **/
    public static Date convertStringToDate(String strDate, String strFormat) {

        SimpleDateFormat format = new SimpleDateFormat(strFormat);
        Date date = null;

        try {
            date = format.parse(strDate);
        } catch (ParseException e) {

        } finally {
            return date;
        }

    }

    /**
     * 자동 밝기 설정
     *
     * @author FIESTA
     * @since 오후 5:01
     **/
    public static void setAutoBrightness(Activity act, boolean value) {
        if (value) {
            Settings.System.putInt(act.getContentResolver(), SCREEN_BRIGHTNESS_MODE, SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        } else {
            Settings.System.putInt(act.getContentResolver(), SCREEN_BRIGHTNESS_MODE, SCREEN_BRIGHTNESS_MODE_MANUAL);
        }

//        if (isChecked) {
//            refreshBrightness(-1);
//        } else {
//            refreshBrightness(getBrightnessLevel());
//        }
    }

    /**
     * 화면 밝기를 조절한다.
     *
     * @author FIESTA
     * @since 오후 5:02
     **/
    public static void refreshBrightness(Activity act, float brightness) {
        WindowManager.LayoutParams lp = act.getWindow().getAttributes();
        if (brightness < 0) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = brightness;
        }
        act.getWindow().setAttributes(lp);
    }

    /**
     * 밝기 값을 가져온다.
     *
     * @author FIESTA
     * @since 오후 5:02
     **/
    public static int getBrightnessValue(Activity act) {
        try {
            int value;
            value = Settings.System.getInt(act.getContentResolver(), SCREEN_BRIGHTNESS);
            // convert brightness level to range 0..1
//            value = value / 255;
            return value;
        } catch (Settings.SettingNotFoundException e) {
            return 0;
        }
    }

    /**
     * text 파일을 읽어서 jsonArray로 반환한다.
     * 해당 기능은 비행 종료시 보내는 데이터용으로 쓰인다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-13 오후 6:34
     **/
    public static JSONArray readFileTextToJsonArray(String path, String name) throws IOException {

        File file = new File(path, name);
        JSONArray resultList = new JSONArray();

        // 파일이 없으면 빈 값을 보낸다.
        if (!file.exists()) {

            return resultList;

        } else {
            BufferedReader br = null;

            try {

                br = new BufferedReader(new FileReader(file));

                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                JSONObject jsonData = null;

                while (line != null) {

                    try {

                        jsonData = new JSONObject(line);
                        resultList.put(jsonData);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    line = br.readLine();

                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                br.close();

                return resultList;

            }

        }

    }

    /**
     * text 파일을 읽어서 jsonArray로 반환한다.
     * 해당 기능은 비행 종료시 보내는 데이터용으로 쓰인다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-13 오후 6:34
     **/
    public static String[] readFileTexts(String path, String name, String splitTxt) throws IOException {

        File file = new File(path, name);
        StringBuilder sb = new StringBuilder();

        // 파일이 없으면 빈 값을 보낸다.
        if (!file.exists()) {

            return new String[0];

        } else {
            BufferedReader br = null;

            try {

                br = new BufferedReader(new FileReader(file));

                String line = br.readLine();
                JSONObject jsonData = null;

                while (line != null) {

                    sb.append(line + splitTxt);

                    line = br.readLine();

                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                br.close();

                return sb.toString().split(splitTxt);

            }

        }

    }

    /**
     * text 파일을 읽어서 jsonArray로 반환한다.
     * 해당 기능은 비행 종료시 보내는 데이터용으로 쓰인다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-13 오후 6:34
     **/
    public static String[] readFileTexts(String path, String name, String splitTxt, boolean isForward) throws IOException {

        File file = new File(path, name);
        StringBuilder sb = new StringBuilder();

        // 파일이 없으면 빈 값을 보낸다.
        if (!file.exists()) {

            return new String[0];

        } else {
            BufferedReader br = null;

            try {

                br = new BufferedReader(new FileReader(file));

                String line = br.readLine();
                JSONObject jsonData = null;

                while (line != null) {

                    if (isForward) {    // 정방향
                        sb.append(line + splitTxt);
                    } else { // 역방향
                        sb.insert(0, line + splitTxt); // 역방향
                    }

                    line = br.readLine();

                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                br.close();

                return sb.toString().split(splitTxt);

            }

        }

    }


    /**
     * text 파일을 읽어서 문자들로 반환한다.
     * 해당 기능은 비행 종료시 보내는 데이터용으로 쓰인다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-13 오후 6:34
     **/
    public static String[] readAssetsToStrings(Context context, String fileName) throws IOException {

        InputStream is = context.getResources().getAssets().open(fileName);

        Writer writer = new StringWriter();

        char[] buffer = new char[2048];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        return writer.toString().split("\n");

    }

    /**
     * 파일을 복사한다
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-20 오전 1:10
     **/
    public static void copyFile(File src, File dst) throws IOException {

        if (!dst.getParentFile().exists())
            dst.getParentFile().mkdirs();

        InputStream in = null;
        OutputStream out = null;

        try {

            in = new FileInputStream(src);
            out = new FileOutputStream(dst);


            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

        } finally {
            in.close();
            out.close();
        }
    }

    /**
     * 파일을 이동시킨다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-15 오후 12:17
     **/
    public static void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            // write the output file
            out.flush();

            // delete the original file
            new File(inputPath + inputFile).delete();


        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 모바일 네트워크를 체크한다.
     *
     * @param context
     * @return 모바일 네트워크 통신 가능 여부
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-06-07 오전 8:56
     **/
    public static boolean isMobileDataEnabled(Context context) {
        Object connectivityService = context.getSystemService(context.CONNECTIVITY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) connectivityService;

        try {
            Class<?> c = Class.forName(cm.getClass().getName());
            Method m = c.getDeclaredMethod("getMobileDataEnabled");
            m.setAccessible(true);
            return (Boolean) m.invoke(cm);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * wifi 연결 상태를 체크한다
     *
     * @param context
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-06-07 오전 8:56
     **/
    public static boolean isWifiConnected(Context context) {

        boolean isConnected = false;

        try {

            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mWifi.isConnected()) {

                isConnected = true;

            } else {

                isConnected = false;

            }

        } catch (Exception ex) {

            isConnected = true;

        } finally {

            return isConnected;

        }

    }

    /**
     * bit를 byte로 변환한다
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-18 오후 3:36
     **/
    public static byte[] encodeToByteArray(Integer[] bits) {
        byte[] results = new byte[(bits.length + 7) / 8];
        int byteValue = 0;
        int index;
        for (index = 0; index < bits.length; index++) {

            byteValue = (byteValue << 1) | bits[index];

            if (index % 8 == 7) {
                results[index / 8] = (byte) byteValue;
            }
        }

        if (index % 8 != 0) {
            results[index / 8] = (byte) ((byte) byteValue << (8 - (index % 8)));
        }

        return results;
    }

    /**
     * byte를 int로
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-18 오후 3:39
     **/
    public static int byteToint(byte[] arr) {
        return (arr[0] & 0xff) << 24 | (arr[1] & 0xff) << 16 | (arr[2] & 0xff) << 8 | (arr[3] & 0xff);
    }


    /**
     * AES 암호화 엔코딩
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-20 오후 10:57
     **/
    public static String aesEncode(String str, String key)
            throws java.io.UnsupportedEncodingException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {

        byte[] textBytes = str.getBytes("UTF-8");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        return Base64.encodeToString(cipher.doFinal(textBytes), Base64.DEFAULT);
    }


    /**
     * AES 암호화 디코딩
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-20 오후 10:56
     **/
    public static String aesDecode(String str, String key)
            throws java.io.UnsupportedEncodingException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {

        byte[] textBytes = Base64.decode(str, Base64.DEFAULT);
        // byte[] textBytes = str.getBytes("UTF-8");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return new String(cipher.doFinal(textBytes), "UTF-8");
    }


}