package kr.go.molit.nhsnes.dialog;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import kr.go.molit.nhsnes.activity.NhsBaseFragmentActivity;
import kr.go.molit.nhsnes.activity.NhsIntroActivity;
import kr.go.molit.nhsnes.activity.NhsLoginActivity;
import kr.go.molit.nhsnes.activity.NhsMainActivity;
import kr.go.molit.nhsnes.activity.NhsSettingtActivity;
import kr.go.molit.nhsnes.receiver.UsbReceiver;
import kr.go.molit.nhsnes.util.serial.FTDriver;

/**
 * Created by jongrakmoon on 2017. 5. 4..
 */

public class DialogBase extends AppCompatDialog {

    //USB Serial
    private FTDriver mSerial;
    private UsbReceiver mUsbReceiver;
    private int mBaudrate;
    private static final String ACTION_USB_PERMISSION = "kr.go.molit.nhsnes.USB_PERMISSION";

    protected boolean isPushButton = false;   // ok 버튼 클릭 여부
    private boolean shortPress = false;         //롱프레스 플래그


    public DialogBase(Context context) {
        this(context, 0);
    }

    public DialogBase(Context context, int theme) {
        super(context, theme);
        String dialogName = getClass().getSimpleName();
        Log.d("###", "Open Dialog Name:" + dialogName);

        // 탭용 usb 리시버를 설정한다.
        setUsbReceiver();

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
            mSerial = new FTDriver((UsbManager) getContext().getSystemService(Context.USB_SERVICE));
            mUsbReceiver = new UsbReceiver(getContext(), mSerial);

            try {
                IntentFilter filter = new IntentFilter();
                filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
                filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
                getContext().registerReceiver(mUsbReceiver, filter);
            } catch (Exception ex) {

            }
            // load default baud rate
            mBaudrate = mUsbReceiver.loadDefaultBaudrate();

            // for requesting permission
            // setPermissionIntent() before begin()
            PendingIntent permissionIntent = PendingIntent.getBroadcast(getContext(), 0, new Intent(ACTION_USB_PERMISSION), 0);
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
        }catch (Exception e){

        }
    }

    @Override
    public void dismiss() {
        setUnUsbRecevier();
        super.dismiss();
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
                try {
                    getContext().unregisterReceiver(this.mUsbReceiver);
                } catch (Exception ex) {

                }
            }
        } catch(Exception e){

        }
    }



    @Override
    public boolean onKeyDown(int keyboard, KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
        {
            switch (keyboard)
            {
                case KeyEvent.KEYCODE_F1:

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
        if (keyCode == KeyEvent.KEYCODE_F1) {   // 홈 길게 눌렀을떄 설정 화면으로
            shortPress = false;

            if (!this.getClass().getName().equals(NhsSettingtActivity.class.getName())) {
                Intent intent = new Intent(getContext(), NhsSettingtActivity.class);
                getContext().startActivity(intent);
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
            case KeyEvent.KEYCODE_F1:
                if(shortPress) {
                    //showMessage("F1 Touch!" + getClass().getSimpleName());
                    if (this.getClass().getName().equals(DialogSearchWaypoint.class.getName())) {
                        dismiss();
                        NhsBaseFragmentActivity.goMain();
                    }
                }
                return true;
            case KeyEvent.KEYCODE_F2:
//                    showMessage("F2 Touch!");
                return true;
            case KeyEvent.KEYCODE_F3:
//                    showMessage("F3 Touch!");
                return true;
            case KeyEvent.KEYCODE_F4:   // ok 버튼

                if (shortPress) {
                    isPushButton = true;
                    dismiss();
                }
                return false;
//                    showMessage("F4 Touch!");
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
                Toast.makeText(getContext(), strMsg, Toast.LENGTH_SHORT).show();
                Log.v("usb", strMsg);
                if (strMsg != null)
                {
                }
            }
        }
    };

}
