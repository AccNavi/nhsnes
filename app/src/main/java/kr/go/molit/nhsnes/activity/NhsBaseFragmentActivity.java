package kr.go.molit.nhsnes.activity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.yayandroid.locationmanager.base.LocationBaseActivity;
import com.yayandroid.locationmanager.configuration.DefaultProviderConfiguration;
import com.yayandroid.locationmanager.configuration.LocationConfiguration;
import com.yayandroid.locationmanager.configuration.PermissionConfiguration;
import com.yayandroid.locationmanager.constants.FailType;
import com.yayandroid.locationmanager.constants.ProcessType;
import com.yayandroid.locationmanager.constants.ProviderType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.ListIterator;

import kr.go.molit.nhsnes.NhsApplication;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.dialog.LoadingDialog;
import kr.go.molit.nhsnes.interfaces.OnGpsListener;
import kr.go.molit.nhsnes.receiver.UsbReceiver;
import kr.go.molit.nhsnes.service.GpsPresenterService;
import kr.go.molit.nhsnes.util.serial.FTDriver;

import static kr.go.molit.nhsnes.fragment.SystemSettingFragment.AUTO_SCREEN_BRIGHTNESS;
import static kr.go.molit.nhsnes.fragment.SystemSettingFragment.SCREEN_VALUE;

/**
 * 모든 activity에서 사용될 공통 class
 *
 * @author 정제영
 * @version 1.0, 2017.03.04 최초 작성
 **/

public class NhsBaseFragmentActivity extends LocationBaseActivity implements GpsPresenterService.GpsPresenter {

    protected static LinkedList<Activity> activityArrayList = new LinkedList<>();
    private boolean shortPress = false;         //롱프레스 플래그

    //USB Serial
    private FTDriver mSerial;
    private UsbReceiver mUsbReceiver;
    private int mBaudrate;
    private static final String ACTION_USB_PERMISSION = "kr.go.molit.nhsnes.USB_PERMISSION";


//    static {
//        System.loadLibrary("KISACrypto");
//    }

    LoadingDialog sLoadingDialog;
    private GpsPresenterService gpsPresenterService;    // gps 서비스
    private ProgressDialog gpsProgressBar;              // gps 진행 프로그래스바
    private OnGpsListener gpsListener = null;
    protected Location currentLocation = null;    // 현재 gps

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.gpsPresenterService = new GpsPresenterService(this, this);   // gps 서비스

        String dialogName = getClass().getSimpleName();
        Log.d("###", "Open Activity Name:" + dialogName);

        if (sLoadingDialog != null && !sLoadingDialog.isShowing() || sLoadingDialog == null) {
            //sLoadingDialog = LoadingDialog.create(NhsBaseFragmentActivity.this, null, null);
        }

        // 화면 꺼짐 방지
        boolean isKeepScreen = StorageUtil.getStorageMode(getContext(), "keepScreen");

        if (isKeepScreen) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }


        // 탭용 usb 리시버를 설정한다.
        setUsbReceiver();

        activityArrayList.add(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setUnUsbRecevier();

        try {
            if (this.activityArrayList != null) {
                if (this.activityArrayList.size() > 0) {
                    this.activityArrayList.remove(this);
                }
            }
        } catch (Exception ex) {

        }
    }

    /**
     * 로딩 dialog 시작
     *
     * @author 정제영
     * @version 1.0, 2017.03.04 최초 작성
     **/

    public void showLoadingBar(Context context) {

        if (context != null && sLoadingDialog != null && !sLoadingDialog.isShowing()) {
            try {
                sLoadingDialog.show();
            } catch (Exception e) {
                Util.Log(e.toString());
            }
        }
    }

    /**
     * 로딩 dialog 멈춤
     *
     * @author 정제영
     * @version 1.0, 2017.03.04 최초 작성
     **/
    public void dismissLoadingBar(Context context) {
        if (context != null && sLoadingDialog != null && sLoadingDialog.isShowing()) {
            try {
                sLoadingDialog.dismiss();
            } catch (Exception e) {
                Util.Log(e.toString());
            }
        }
    }

    /**
     * fragment 가져오기
     *
     * @author 정제영
     * @version 1.0, 2017.03.04 최초 작성
     **/
    public Fragment getContent(String tag) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment BaseFragment = (Fragment) fm.findFragmentByTag(tag);
        return BaseFragment;
    }

    /**
     * fragment 교체
     *
     * @author 정제영
     * @version 1.0, 2017.03.04 최초 작성
     **/
    public void replaceContent(Fragment content, String tag, int resContent) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        clearAllContent();
        ft.replace(resContent, content);
        ft.commitAllowingStateLoss();
    }

    /**
     * stack에서 모든 fragment 삭제
     *
     * @author 정제영
     * @version 1.0, 2017.03.04 최초 작성
     **/
    public void clearAllContent() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
            String backTag = fm.getBackStackEntryAt(i).getName();
            fm.popBackStack(backTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean iskeepScreen = StorageUtil.getStorageMode(this, "keepScreen");

        if (iskeepScreen) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }


        boolean checkBrightness = checkSettingPermissionCode(this);

        if (checkBrightness) {

            boolean isAutoScreenBrightness = StorageUtil.getStorageModeWithDefaultValue(getContext(), AUTO_SCREEN_BRIGHTNESS, true);

            if (isAutoScreenBrightness) {

                // 자동 밝기 설정
                Util.setAutoBrightness(this, true);

                // 발기를 조절한다.
                Util.refreshBrightness(this, -1);

            } else {

                // 수동 밝기 설정
                Util.setAutoBrightness(this, false);

                // 화면 밝기에 맞게 설정한다.
                int brightness = Integer.parseInt(StorageUtil.getStorageModeEx(this, SCREEN_VALUE, "0"));

                float calValue = (brightness * 1) / (float) 100;

                // 발기를 조절한다.
                Util.refreshBrightness(this, calValue);

            }
        }

    }

    public boolean checkSettingPermissionCode(Activity context) {
        boolean permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = Settings.System.canWrite(context);
        } else {
            permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }
        if (permission) {
            return true;
        } else {
            return false;
        }
    }

    protected Context getContext() {
        return this;
    }

    public void onDialogDissmiss() {
        onResume();
    }

    @Override
    public LocationConfiguration getLocationConfiguration() {
        return new LocationConfiguration.Builder()
                .keepTracking(false)
                .askForPermission(new PermissionConfiguration.Builder()
                        .rationaleMessage(getString(R.string.GPS_PERMISSION_ERROR))
                        .requiredPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                        .build())
                .useDefaultProviders(new DefaultProviderConfiguration.Builder()
                        .requiredTimeInterval(10000)
                        .requiredDistanceInterval(0)
                        .acceptableAccuracy(5000.0f)
                        .acceptableTimePeriod(1000)
                        .gpsMessage(getString(R.string.GPS_TURN_ON))
                        .setWaitPeriod(ProviderType.GPS, 500)
                        .build())
                .build();

    }

    /**
     * gps 위치가 바뀌면 gps 서비스에게 전달한다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-06-21 오전 8:31
     **/
    @Override
    public void onLocationChanged(Location location) {
        this.currentLocation = location;

        Log.d("gpstest", this.currentLocation.getLatitude() + " "  + this.currentLocation.getLongitude());
        if (this.gpsListener != null) {
            this.gpsListener.onLocationChanged(location);
        }
        this.gpsPresenterService.onLocationChanged(location);
    }


    /**
     * gps 실패 정보를 gps 서비스에 전달한다
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-06-21 오전 8:31
     **/
    @Override
    public void onLocationFailed(@FailType int failType) {
        this.gpsPresenterService.onLocationFailed(failType);
    }


    /**
     * gps 상태를 gps 서비스에 전달한다
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-06-21 오전 8:31
     **/
    @Override
    public void onProcessTypeChanged(@ProcessType int processType) {
        this.gpsPresenterService.onProcessTypeChanged(processType);
    }

    /**
     * gps 프로그래스바를 보여준다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-06-21 오전 8:30
     **/
    public void displayGpsProgressBar() {
        if (this.gpsProgressBar == null) {
            this.gpsProgressBar = new ProgressDialog(this);
            this.gpsProgressBar.getWindow().addFlags(Window.FEATURE_NO_TITLE);
            this.gpsProgressBar.setMessage(getString(R.string.GPS_GETTING_LOCATION));
            this.gpsProgressBar.setCancelable(false);
        }

        if (!this.gpsProgressBar.isShowing()) {
            this.gpsProgressBar.show();
        }
    }

    /**
     * gps 상태 text를 가져온다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-06-21 오전 8:27
     **/
    @Override
    public String getGpsStateText() {
        return "";
    }

    /**
     * gps 상태 text를 보낸다
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-06-21 오전 8:28
     **/
    @Override
    public void setGpsStateText(String text) {

        Log.d("test", text);
//        locationText.setGpsStateText(text);
    }

    /**
     * gps를 위한 프로그래스바 메세지를 보낸다
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-06-21 오전 8:28
     **/
    @Override
    public void updateGpsProgress(String text) {
        if (this.gpsProgressBar != null && this.gpsProgressBar.isShowing()) {
            this.gpsProgressBar.setMessage(text);
        }
    }

    /**
     * gps를 위한 프로그래스바를 끝낸다
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-06-21 오전 8:29
     **/
    @Override
    public void dismissGpsProgress() {
        if (this.gpsProgressBar != null && this.gpsProgressBar.isShowing()) {
            this.gpsProgressBar.dismiss();
        }
    }


    public OnGpsListener getGpsListener() {
        return gpsListener;
    }

    public void setGpsListener(OnGpsListener gpsListener) {
        this.gpsListener = gpsListener;
    }

    /**
     * USB 리시버 설정 (갤럭시 탭에 있는 키보드 연결)
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-10-30 오전 10:25
     **/
    private void setUsbReceiver()
    {
        try {
            mSerial = new FTDriver((UsbManager) getSystemService(Context.USB_SERVICE));
            mUsbReceiver = new UsbReceiver(this, mSerial);

            IntentFilter filter = new IntentFilter();
            filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
            filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
            registerReceiver(mUsbReceiver, filter);

            // load default baud rate
            mBaudrate = mUsbReceiver.loadDefaultBaudrate();

            // for requesting permission
            // setPermissionIntent() before begin()
            PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
            mSerial.setPermissionIntent(permissionIntent);
            Log.d("usb", "FTDriver beginning");

            if (mSerial.begin(mBaudrate)) {
                Log.d("usb", "FTDriver began");
                mUsbReceiver.loadDefaultSettingValues();
                mUsbReceiver.mainloop();
            } else {
                Log.d("usb", "FTDriver no connection");
//            Toast.makeText(this, "no connection", Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e){

        }
    }

    /**
     * USB 리시버 설정 (갤럭시 탭에 있는 키보드 해제)
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-10-30 오전 10:25
     **/
    private void setUnUsbRecevier(){
        try {
            if (this.mUsbReceiver != null) {
                unregisterReceiver(this.mUsbReceiver);
            }
        }catch(Exception e){

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyboard, KeyEvent event)
    {


        if (event.getAction() == KeyEvent.ACTION_DOWN)
        {

            switch (keyboard)
            {
                case KeyEvent.KEYCODE_VOLUME_DOWN:

                    event.startTracking();

                    if(event.getRepeatCount() == 0) {
                        shortPress = true;
                    }

                    return true;
                case KeyEvent.KEYCODE_F2:

                    event.startTracking();

                    if(event.getRepeatCount() == 0) {
                        shortPress = true;
                    }
//                    showMessage("F2 Touch!");
                    return true;
                case KeyEvent.KEYCODE_F3:

                    event.startTracking();

                    if(event.getRepeatCount() == 0) {
                        shortPress = true;
                    }
//                    showMessage("F3 Touch!");
                    return true;
                case KeyEvent.KEYCODE_F4:

                    event.startTracking();

                    if(event.getRepeatCount() == 0) {
                        shortPress = true;
                    }
//                    showMessage("F4 Touch!");
                    return true;
                case KeyEvent.KEYCODE_F5:

                    event.startTracking();

                    if(event.getRepeatCount() == 0) {
                        shortPress = true;
                    }
//                    showMessage("F5 Touch!");
                    return true;
                case KeyEvent.KEYCODE_F6:

                    event.startTracking();

                    if(event.getRepeatCount() == 0) {
                        shortPress = true;
                    }
//                    showMessage("F6 Touch!");
                    return true;
                case KeyEvent.KEYCODE_F7:

                    event.startTracking();

                    if(event.getRepeatCount() == 0) {
                        shortPress = true;
                    }
//                    showMessage("F7 Touch!");
                    return true;
                case KeyEvent.KEYCODE_F8:

                    event.startTracking();

                    if(event.getRepeatCount() == 0) {
                        shortPress = true;
                    }
//                    showMessage("F8 Touch!");
                    return true;
                case KeyEvent.KEYCODE_ALT_LEFT:

                    event.startTracking();

                    if(event.getRepeatCount() == 0) {
                        shortPress = true;
                    }
//                    showMessage("alt left Touch!");
                    return true;
                case KeyEvent.KEYCODE_ALT_RIGHT:

                    event.startTracking();

                    if(event.getRepeatCount() == 0) {
                        shortPress = true;
                    }
//                    showMessage("alt right Touch!");
                    return true;
                case KeyEvent.KEYCODE_SPACE:

                    event.startTracking();

                    if(event.getRepeatCount() == 0) {
                        shortPress = true;
                    }
//                    showMessage("space Touch!");
                    return true;
                case KeyEvent.KEYCODE_ENTER:

                    event.startTracking();

                    if(event.getRepeatCount() == 0) {
                        shortPress = true;
                    }
//                    showMessage("enter Touch!");
                    return true;
                case KeyEvent.KEYCODE_CTRL_LEFT:

                    event.startTracking();

                    if(event.getRepeatCount() == 0) {
                        shortPress = true;
                    }
//                    showMessage("ctrl left Touch!");
                    return true;
                case KeyEvent.KEYCODE_CTRL_RIGHT:

                    event.startTracking();

                    if(event.getRepeatCount() == 0) {
                        shortPress = true;
                    }
//                    showMessage("ctrl right Touch!");
                    return true;

            }
        }


        return super.onKeyDown(keyboard, event);

    }

    /**
     * 단축키를 길게 눌렀을 때 호출되는 콜백함수로,
     * 해당 단축키의 키코드를 확인하여 코딩 할 것.
     *
     * @param keyCode 입력한 키코드
     * @param event 발생 이벤트 정보
     * @return  super.onKeyLongPress
     */
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {   // 홈 길게 눌렀을떄 설정 화면으로

            shortPress = false;

            if (!this.getClass().getName().equals(NhsSettingtActivity.class.getName())) {
                Intent intent = new Intent(this, NhsSettingtActivity.class);
                startActivity(intent);
            }

            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }


    /**
     * 단축키를 떼었을 때 발생하는 콜백함수.
     *
     * onKeyDown 콜백함수에서 설정한 플래그값을 확인한 후 로직을 짜면 된다.
     * shortPress가 true면 shortPress 로직,
     * shortPress가 false면 LongPress 로직,
     *
     * @param keyCode 입력한 단축키의 keyCode
     * @param event 발생한 이벤트 정보
     * @return super.onKeyUp(keyCode, event);
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        switch (keyCode)
        {
            case KeyEvent.KEYCODE_VOLUME_DOWN:

                if(shortPress){

                    if (!this.getClass().getName().equals(NhsMainActivity.class.getName()) &&
                            !this.getClass().getName().equals(NhsIntroActivity.class.getName()) &&
                            !this.getClass().getName().equals(NhsLoginActivity.class.getName())) {
                        finish();
                        try {
                            Intent homeIntent = new Intent(this, NhsMainActivity.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(homeIntent);
                        } catch (Exception e){
                            
                        }
                        return false;
                    }

                }

                break;
            case KeyEvent.KEYCODE_F2:
//                    showMessage("F2 Touch!");
                return true;
            case KeyEvent.KEYCODE_F3:
//                    showMessage("F3 Touch!");
                return true;
            case KeyEvent.KEYCODE_F4:
//                    showMessage("F4 Touch!");
                return true;
            case KeyEvent.KEYCODE_F5:
//                    showMessage("F5 Touch!");
                return true;
            case KeyEvent.KEYCODE_F6:
//                    showMessage("F6 Touch!");
                return true;
            case KeyEvent.KEYCODE_F7:
//                    showMessage("F7 Touch!");
                return true;
            case KeyEvent.KEYCODE_F8:
//                    showMessage("F8 Touch!");
                return true;
            case KeyEvent.KEYCODE_ALT_LEFT:
//                    showMessage("alt left Touch!");
                return true;
            case KeyEvent.KEYCODE_ALT_RIGHT:
//                    showMessage("alt right Touch!");
                return true;
            case KeyEvent.KEYCODE_SPACE:
//                    showMessage("space Touch!");
                return true;
            case KeyEvent.KEYCODE_ENTER:
//                    showMessage("enter Touch!");
                return true;
            case KeyEvent.KEYCODE_CTRL_LEFT:
//                    showMessage("ctrl left Touch!");
                return true;
            case KeyEvent.KEYCODE_CTRL_RIGHT:
//                    showMessage("ctrl right Touch!");
                return true;
        }


        shortPress = false;

//        return true;
        return super.onKeyUp(keyCode, event);
    }

    public void showMessage(String strMsg) {
        // 메시지 텍스트를 핸들러에 전달
        Message msg = Message.obtain(mHandler2, 0, strMsg);
        mHandler2.sendMessage(msg);
        Log.d("tag1", strMsg);
    }

    /**
     * USB Serial Toast Handler
     */
    Handler mHandler2 = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String strMsg = (String)msg.obj;
                Toast.makeText(NhsBaseFragmentActivity.this, strMsg, Toast.LENGTH_SHORT).show();
                Log.v("usb", strMsg);
                if (strMsg != null)
                {
                }
            }
        }
    };


    /**
     * 모든 엑티비티 종료 결과
     *
     * @author FIESTA
     * @since 오전 18:18
     **/
    protected void allFlinishAllActivity(){

        if (this.activityArrayList != null){

            if (this.activityArrayList.size() > 0) {

                ListIterator<Activity> listIterator = this.activityArrayList.listIterator();
                while (listIterator.hasNext()) {
                    listIterator.next().finish();
                    listIterator.remove();
                }

            }

        }


    }
}
