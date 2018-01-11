package kr.go.molit.nhsnes.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.ImageView;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.widget.TextViewEx;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class NhsGpsInfoActivity extends NhsBaseFragmentActivity implements CompoundButton.OnCheckedChangeListener {

  private final static String GPS_CONTROL = "gps_control";

  private SwitchCompat scBluetooth = null;        //  블루투스 스위치
  private SwitchCompat scGps = null;              //  gps 스위치
  private TextViewEx tveGpsState = null;            // gps 상태 메세지

  private int beforeWifiLevel = -1;               // 이전 wifi 수신 레벨

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_gps_info);

    // 레이아웃 설정
    setLayout();

  }

  /**
  * 레이아웃을 설정한다.
  * @author FIESTA
  * @version 1.0.0
  * @since 오후 5:19
  **/
  private void setLayout() {

    this.scBluetooth = (SwitchCompat) this.findViewById(R.id.sc_bluetooth);
    this.scGps = (SwitchCompat) this.findViewById(R.id.sc_gps);
    this.tveGpsState = (TextViewEx) this.findViewById(R.id.tve_gps_state);

    // 저장된 gps 토글 상태
    boolean gpsState = StorageUtil.getStorageModeWithDefaultValue(NhsGpsInfoActivity.this, GPS_CONTROL, false);
    scGps.setChecked(gpsState);

    // wifi 상태에 따라서 이미지를 변경한다.
    changeWifiState();

    // gps 상테에 따라서 이미지를 변경한다.
    changeGpsState();

    // 블루투스가 연결되어있는지 확인 후, 레이아웃을 업데이트한다.
    changeBluetoothState();

    this.scBluetooth.setOnCheckedChangeListener(this);
    this.scGps.setOnCheckedChangeListener(this);

  }

  /**
   * wifi 상태에 따라서 이미지를 변경한다.
   *
   * @author FIESTA
   * @since 오전 12:02
   **/
  private void changeWifiState() {

    ConnectivityManager connect = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    ImageView ivWifi = (ImageView) this.findViewById(R.id.iv_wifi_state);

    if (connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
      ivWifi.setImageResource(R.drawable.img_gps_signal_1);
    } else {
      ivWifi.setImageResource(R.drawable.img_gps_signal_5);
    }


  }

  /**
   * GPS 상태에 따라서 이미지를 변경한다.
   *
   * @author FIESTA
   * @since 오후 11:56
   **/
  private void changeGpsState() {

    ImageView ivGpsState = (ImageView) this.findViewById(R.id.iv_gps_state);

    // gps가 연결되어있는지 확인한다.
    boolean isGpsOn = isGpsOn();

    // gps 연결여부에 따라서 이미지를 바꾼다.
    if (isGpsOn) {
      ivGpsState.setImageResource(R.drawable.img_gps_good);
      this.tveGpsState.setText("GPS 통신 상태 좋음");
    } else {
      ivGpsState.setImageResource(R.drawable.img_gps_bad);
      this.tveGpsState.setText("GPS 통신 상태 나쁨");
    }

  }

  /**
   * GPS 활성화 여부 체크
   *
   * @author FIESTA
   * @since 오후 11:52
   **/
  private boolean isGpsOn() {

    LocationManager myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    return myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

  }

  /**
   * 블루투스가 연결되어있는지 확인 후, 레이아웃을 업데이트한다.
   *
   * @author FIESTA
   * @since 오후 11:39
   **/
  private void changeBluetoothState() {

    try {

      BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

      if (adapter.getState() == BluetoothAdapter.STATE_TURNING_ON ||
          adapter.getState() == BluetoothAdapter.STATE_ON) {
        this.scBluetooth.setChecked(true);
      } else {
        this.scBluetooth.setChecked(false);
      }

    } catch (Exception ex) {

    }

  }

  /**
   * 블루투스를 on / off 한다
   *
   * @author FIESTA
   * @since 오후 11:43
   **/
  private void toggleBluetooth(boolean enable) {

    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

    if (enable) {
      adapter.enable();     // Bluetooth On
    } else {
      adapter.disable();   // Bluetooth Off
    }

  }

  @Override
  public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    switch (compoundButton.getId()) {

      // 블루투스 on / off
      case R.id.sc_bluetooth:
        toggleBluetooth(b); // 블루투스 상태 변경
        break;

      // gps
      case R.id.sc_gps:

        //  gps 토글 상태 저장
        StorageUtil.setStorageMode(NhsGpsInfoActivity.this, GPS_CONTROL, b);
        break;

    }


  }

  @Override
  protected void onResume() {
    super.onResume();

    // wifi 수신감도 체크 및 wifi 연결 정보 리시버 등록
    IntentFilter networkChangeFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
    networkChangeFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
    networkChangeFilter.addAction("android.location.PROVIDERS_CHANGED");    // gps
    this.registerReceiver(wifiStateReceiver, networkChangeFilter);

//    WifiManager wifiMan = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    WifiManager wifiMan = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    wifiMan.startScan();

  }

  @Override
  public void onPause() {
    super.onPause();

    // wifi 수신감도 체크 및 wifi 연결 정보 리시버 삭제
    this.unregisterReceiver(wifiStateReceiver);
  }


  /**
   * wifi 수신감도 체크 및 wifi 연결 정보 리시버
   *
   * @author FIESTA
   * @since 오전 12:35
   **/
  private BroadcastReceiver wifiStateReceiver
      = new BroadcastReceiver() {
    @Override
    public void onReceive(Context arg0, Intent arg1) {

      String action = arg1.getAction();

      // wifi 수신 감도 액션
      if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {

//        WifiManager wifiMan = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiManager wifiMan = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiMan.startScan();
        int numberOfLevels = 5;
        WifiInfo wifiInfo = wifiMan.getConnectionInfo();
        final int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);


        if (beforeWifiLevel != level) {

          runOnUiThread(new Runnable() {
            @Override
            public void run() {

              ImageView ivWifi = (ImageView) findViewById(R.id.iv_wifi_state);

              switch (level) {

                case 0:
                  ivWifi.setImageResource(R.drawable.img_gps_signal_5);
                  break;
                case 1:
                  ivWifi.setImageResource(R.drawable.img_gps_signal_4);
                  break;
                case 2:
                  ivWifi.setImageResource(R.drawable.img_gps_signal_3);
                  break;
                case 3:
                  ivWifi.setImageResource(R.drawable.img_gps_signal_2);
                  break;
                case 4:
                  ivWifi.setImageResource(R.drawable.img_gps_signal_1);
                  break;
                default:
                  break;


              }


            }
          });

        }

        beforeWifiLevel = level;

      } else if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {    // wifi 연결 정보 액션

        ConnectivityManager connect = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

          beforeWifiLevel = -1;

          runOnUiThread(new Runnable() {
            @Override
            public void run() {

              ImageView ivWifi = (ImageView) findViewById(R.id.iv_wifi_state);
              ivWifi.setImageResource(R.drawable.img_gps_signal_1);

            }

          });

          // 연결되었으니 rssi 스캔 시작
          WifiManager wifiMan = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
          wifiMan.startScan();


        } else if (connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED){

          // 연결 끊김으로 변환
          runOnUiThread(new Runnable() {
            @Override
            public void run() {

              ImageView ivWifi = (ImageView) findViewById(R.id.iv_wifi_state);
              ivWifi.setImageResource(R.drawable.img_gps_signal_5);

            }

          });

        }

      } else if (action.matches("android.location.PROVIDERS_CHANGED")) {
        changeGpsState();
      }

    }
  };
}
