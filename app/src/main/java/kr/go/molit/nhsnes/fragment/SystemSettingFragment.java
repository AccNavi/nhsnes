package kr.go.molit.nhsnes.fragment;

import static android.provider.Settings.System.SCREEN_BRIGHTNESS;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.widget.SettingSeekBar;
import kr.go.molit.nhsnes.widget.TextToggleButton;
import kr.go.molit.nhsnes.widget.TextViewEx;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class SystemSettingFragment extends BaseFragment implements View.OnClickListener, TextToggleButton.OnCheckedChangeListener {

    public final static String IS_TTS_SOUND = "is_tts_sound";
    public final static String IS_USE_UTC = "is_use_utc";

    public final static String TTS_VALUE = "TTS_VALUE"; // 음성 볼륨
    public final static String SCREEN_VALUE = "SCREEN_VALUE"; // 화면 설정 설정


    public final static String PREVENT_SCREEN_OFF = "Prevent screen_off"; // 화면_꺼짐_방지
    public final static String AUTO_SCREEN_BRIGHTNESS = "Auto screen brightness"; // 자동 화면 밝기
    public final static String CHANGE_COLOR_AUTOMATICALLY = "Change color automatically"; // 색상 자동 변경
    public final static String IS_NIGHTTIME = "IS_Nighttime"; // 야간 여부
    public final static String ADS_INTERLOCK = "ADS_ interlock"; // ADS 연동
    public final static String ADS_LINK_WIFI = "ADS_link_WIFI"; // ADS_연동_WIFI
    public final static String ADS_LINK_BLUETOOTH = "ADS_link _BLUETOOTH"; // ADS_연동_BLUETOOTH
    public final static String ADS_INFO_EXPRESSION = "ADS_Info_Expression"; // ADS_정보_표출
    public final static String ADS_AROUND_AIRCRAFT_INFO_EXPRESSION = "ADS_Around_Aircraft_Info_Expression"; // ADS_주변_항공기_정보_표출
    public final static String SEND_AND_RECEIVE_FLIGHT_INFORMATION = "Send and receive flight information"; // 비행정보 송수신
    public final static String CONNECT_WITH_SYSTEM = "Connect with system"; // 시스템과 연계
    public final static String PROVIDE_AIRCRAFT_LOCATION = "Provide aircraft location"; // 항공기 위치 제공
    public final static String AUTO_DELETE = "Auto delete"; // 자동 삭제
    public final static String SYNC_DATE = "sync_date"; // 동기화 시간

    private View rootView;

    private ViewGroup mLayoutWeekly, mLayoutNightly, mLayoutLocal, mLayoutExt;
    private ImageView mImageViewWeekly, mImageViewNightly, mImageViewLocal, mImageViewExt;
    private TextViewEx mTextViewExWeekly, mTextViewExNightly, mTextViewExLocal, mTextViewExExt;


    private int selectedColor;
    private int unselectedColor;
    private TextToggleButton mTtbSound;
    private TextToggleButton mTtbUtc;
    private TextToggleButton ttbAutoScreenBrightness;
    private TextToggleButton ttbChangeColorAutomatically;
    private TextToggleButton ttbAdsInterlock;
    private TextToggleButton ttbAdsLinkWifi;
    private TextToggleButton ttbAdsLinkBluetooth;
    private TextToggleButton ttbAdsInfoExpression;
    private TextToggleButton ttbAdsAround_aircraftInfoExpression;
    private TextToggleButton ttbSendAndReceiveFlightInformation;
    private TextToggleButton ttbConnectWithSystem;
    private TextToggleButton ttbProvideAircraftLocation;
    private TextToggleButton ttbAutoDelete;

    private SettingSeekBar ssbTtsValue;
    private SettingSeekBar ssbScreenValue;

    private AudioManager am = null;
    private Button btnSync = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.am = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        if (rootView == null) {

            rootView = LayoutInflater.from(getContext()).inflate(R.layout.frg_system_setting, container, false);

            selectedColor = Color.WHITE;
            unselectedColor = getResources().getColor(R.color.textColorTransparentWhite);

            mLayoutWeekly = (ViewGroup) rootView.findViewById(R.id.layout_weekly);
            mLayoutNightly = (ViewGroup) rootView.findViewById(R.id.layout_nightly);
            mLayoutLocal = (ViewGroup) rootView.findViewById(R.id.layout_local);
            mLayoutExt = (ViewGroup) rootView.findViewById(R.id.layout_ext);

            mTtbSound = (TextToggleButton) rootView.findViewById(R.id.ttb_tts_sound);
            mTtbUtc = (TextToggleButton) rootView.findViewById(R.id.ttb_use_utc);
            ttbAutoScreenBrightness = (TextToggleButton) rootView.findViewById(R.id.ttb_auto_screen_brightness);
            ttbChangeColorAutomatically = (TextToggleButton) rootView.findViewById(R.id.ttb_change_color_automatically);
            ttbAdsInterlock = (TextToggleButton) rootView.findViewById(R.id.ttb_ads_interlock);
            ttbAdsLinkWifi = (TextToggleButton) rootView.findViewById(R.id.ttb_ads_link_wifi);
            ttbAdsLinkBluetooth = (TextToggleButton) rootView.findViewById(R.id.ttb_ads_link_bluetooth);
            ttbAdsInfoExpression = (TextToggleButton) rootView.findViewById(R.id.ttb_ads_info_expression);
            ttbAdsAround_aircraftInfoExpression = (TextToggleButton) rootView.findViewById(R.id.ttb_ads_around_aircraft_info_expression);
            ttbSendAndReceiveFlightInformation = (TextToggleButton) rootView.findViewById(R.id.ttb_send_and_receive_flight_information);
            ttbConnectWithSystem = (TextToggleButton) rootView.findViewById(R.id.ttb_connect_with_system);
            ttbProvideAircraftLocation = (TextToggleButton) rootView.findViewById(R.id.ttb_provide_aircraft_location);
            ttbAutoDelete = (TextToggleButton) rootView.findViewById(R.id.ttb_auto_delete);

            ssbTtsValue = (SettingSeekBar) rootView.findViewById(R.id.ssb_tts_value);
            ssbScreenValue = (SettingSeekBar) rootView.findViewById(R.id.ssb_screen_value);

            btnSync = (Button) rootView.findViewById(R.id.bt_sync);

            ttbAutoScreenBrightness.setOnCheckedChangeListener(this);
            ttbChangeColorAutomatically.setOnCheckedChangeListener(this);
            ttbAdsInterlock.setOnCheckedChangeListener(this);
            ttbAdsLinkWifi.setOnCheckedChangeListener(this);
            ttbAdsLinkBluetooth.setOnCheckedChangeListener(this);
            ttbAdsInfoExpression.setOnCheckedChangeListener(this);
            ttbAdsAround_aircraftInfoExpression.setOnCheckedChangeListener(this);
            ttbSendAndReceiveFlightInformation.setOnCheckedChangeListener(this);
            ttbConnectWithSystem.setOnCheckedChangeListener(this);
            ttbProvideAircraftLocation.setOnCheckedChangeListener(this);
            ttbAutoDelete.setOnCheckedChangeListener(this);

            btnSync.setOnClickListener(this);

            mLayoutWeekly.setOnClickListener(this);
            mLayoutNightly.setOnClickListener(this);
            mLayoutLocal.setOnClickListener(this);
            mLayoutExt.setOnClickListener(this);

            mTextViewExWeekly = (TextViewEx) rootView.findViewById(R.id.tv_weekly);
            mTextViewExNightly = (TextViewEx) rootView.findViewById(R.id.tv_nightly);
            mTextViewExLocal = (TextViewEx) rootView.findViewById(R.id.tv_local);
            mTextViewExExt = (TextViewEx) rootView.findViewById(R.id.tv_ext);

            // 화며 꺼짐 방지
            settingKeepScreen();

            ttbAutoScreenBrightness.setCheck(StorageUtil.getStorageModeWithDefaultValue(getContext(), AUTO_SCREEN_BRIGHTNESS, true));
            ttbChangeColorAutomatically.setCheck(StorageUtil.getStorageMode(getContext(), CHANGE_COLOR_AUTOMATICALLY));
            ttbAdsInterlock.setCheck(StorageUtil.getStorageMode(getContext(), ADS_INTERLOCK));
            ttbAdsLinkWifi.setCheck(StorageUtil.getStorageMode(getContext(), ADS_LINK_WIFI));
            ttbAdsLinkBluetooth.setCheck(StorageUtil.getStorageMode(getContext(), ADS_LINK_BLUETOOTH));
            ttbAdsInfoExpression.setCheck(StorageUtil.getStorageMode(getContext(), ADS_INFO_EXPRESSION));
            ttbAdsAround_aircraftInfoExpression.setCheck(StorageUtil.getStorageMode(getContext(), ADS_AROUND_AIRCRAFT_INFO_EXPRESSION));
            ttbSendAndReceiveFlightInformation.setCheck(StorageUtil.getStorageMode(getContext(), SEND_AND_RECEIVE_FLIGHT_INFORMATION));
            ttbConnectWithSystem.setCheck(StorageUtil.getStorageMode(getContext(), CONNECT_WITH_SYSTEM));
            ttbProvideAircraftLocation.setCheck(StorageUtil.getStorageMode(getContext(), PROVIDE_AIRCRAFT_LOCATION));
            ttbAutoDelete.setCheck(StorageUtil.getStorageMode(getContext(), AUTO_DELETE));


            SimpleDateFormat sdf = new SimpleDateFormat("MM월 dd일 HH:mm");
            TextView tvSyncTime = ((TextViewEx)rootView.findViewById(R.id.tve_sync_time));
            tvSyncTime.setText(StorageUtil.getStorageModeEx(getContext(), SYNC_DATE, sdf.format(new Date())));

            if (StorageUtil.getStorageMode(getContext(), IS_TTS_SOUND)) {

                int volume = this.am.getStreamVolume(AudioManager.STREAM_MUSIC);
                volume = Math.round(((float)volume / (float)15) * 100);
                ssbTtsValue.setProgress(volume);

            } else {
                StorageUtil.setStorageMode(getContext(), TTS_VALUE, 0+"");
            }

            ssbScreenValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                    float calValue = (i * 1) / (float)100;

                    // auto 모드이면 저장만한다.
                    if (!StorageUtil.getStorageMode(getContext(), AUTO_SCREEN_BRIGHTNESS)) {

                        // 발기를 조절한다.
                        Util.refreshBrightness(getActivity(), calValue);

                    }
                    // 데이터 값 저장
                    StorageUtil.setStorageMode(getActivity(), SCREEN_VALUE, i+"");

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            }) ;

            // 화면 밝기에 맞게 프로그래스바를 설정한다.
            int brightness = Integer.parseInt(StorageUtil.getStorageModeEx(getActivity(), SCREEN_VALUE, "0"));
            ssbScreenValue.setProgress(brightness);

            ssbTtsValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                    ssbTtsValue.setOnSeekBarChangeListener(null);

                    // 볼륨을 계산한다.
                    int value = Math.round((15 * i) / 100);
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, value, AudioManager.FLAG_PLAY_SOUND);

                    // 90이라는 값을 계산이 안된다.
                    // 예를 들어 13.5 가 90인데 int 값으로 들어가기 떄문에, 13혹은 14가 들어간다. 그러므로
                    // 90이 나올 수가 없다. 그래서, 얼추 비슷한 값으로 다시 셋팅한다.
                    value = Math.round(((float)value / (float)15) * 100);
                    ssbTtsValue.setProgress(value);

                    StorageUtil.setStorageMode(getContext(), TTS_VALUE, i+"");

                    if (value > 0) {
                        mTtbSound.setCheck(true);
                    } else {
                        mTtbSound.setCheck(false);
                    }

                    ssbTtsValue.setOnSeekBarChangeListener(this);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            }) ;
        }

        if (StorageUtil.getStorageMode(getContext(), IS_NIGHTTIME)) {
            selectNightly();
        } else {
            selectWeekly();
        }

        mTtbSound.setCheck(StorageUtil.getStorageMode(getContext(), IS_TTS_SOUND));
        mTtbUtc.setCheck(StorageUtil.getStorageMode(getContext(), IS_USE_UTC));
        mTtbSound.setOnCheckedChangeListener(this);
        mTtbUtc.setOnCheckedChangeListener(this);

        setupSaveLocation();

        return rootView;
    }

    /**
    * 저장된 위치를 지정한다.
    * @author FIESTA
    * @since  오전 12:32
    **/
    private void setupSaveLocation(){

        if (!StorageUtil.isAvailableExternalStorage()) {

            mLayoutLocal.setOnClickListener(null);
            mLayoutExt.setOnClickListener(null);

            selectLocal();

        } else {

            if (StorageUtil.getStorageMode(getContext()) == StorageUtil.Storage.LOCAL) {

                selectLocal();

            } else {

                selectExt();

            }

        }

    }

    /**
    * 화면 꺼짐 방지
    * @author FIESTA
    * @since  오전 3:11
    **/
    private void settingKeepScreen(){

        // 화면 꺼짐 방지
        TextToggleButton ttbScreen = (TextToggleButton)rootView.findViewById(R.id.ttb_screen);

        // 화면 꺼짐 방지
        boolean isKeepScreen = StorageUtil.getStorageMode(getContext(), "keepScreen");
        ((ToggleButton)ttbScreen.findViewById(R.id.tb_switch)).setChecked(isKeepScreen);

        ((ToggleButton)ttbScreen.findViewById(R.id.tb_switch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                StorageUtil.setStorageMode(getActivity(), "keepScreen", b);

                if (b){
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else {
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_weekly:
                selectWeekly();
                break;
            case R.id.layout_nightly:
                selectNightly();
                break;
            case R.id.layout_local:
                selectLocal();
                break;
            case R.id.layout_ext:
                selectExt();
                break;

            // 동기화
            case R.id.bt_sync:
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        new ToastUtile().showCenterText(getActivity(), "동기화를 완료하였습니다.");


                        // 동기화된 시간을 표시한다.
                        SimpleDateFormat sdf = new SimpleDateFormat("MM월 dd일 HH:mm");
                        ((TextViewEx)rootView.findViewById(R.id.tve_sync_time)).setText(sdf.format(new Date()));

                        // 동기화 시간 저장
                        StorageUtil.setStorageMode(getContext(), SYNC_DATE, sdf.format(new Date()));

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                v.startAnimation(animation);

                break;
        }

    }

    private void selectWeekly() {
        mLayoutWeekly.setBackgroundResource(R.drawable.btn_type7_nor);
        if(mImageViewWeekly!=null)mImageViewWeekly.setImageResource(R.drawable.icon_brightness);
        mTextViewExWeekly.setTextColor(selectedColor);
        mLayoutNightly.setBackgroundResource(R.color.transparent);
        if(mImageViewNightly!=null)mImageViewNightly.setImageResource(R.drawable.icon_night_dis);
        mTextViewExNightly.setTextColor(unselectedColor);
        StorageUtil.setStorageMode(getContext(), IS_NIGHTTIME, false);
    }

    private void selectNightly() {
        mLayoutWeekly.setBackgroundResource(R.color.transparent);
        if(mImageViewWeekly!=null)mImageViewWeekly.setImageResource(R.drawable.icon_brightness_dis);
        mTextViewExWeekly.setTextColor(unselectedColor);
        mLayoutNightly.setBackgroundResource(R.drawable.btn_type7_nor);
        if(mImageViewNightly!=null)mImageViewNightly.setImageResource(R.drawable.icon_night_nor);
        mTextViewExNightly.setTextColor(selectedColor);
        StorageUtil.setStorageMode(getContext(), IS_NIGHTTIME, true);
    }

    private void selectLocal() {
        mLayoutLocal.setBackgroundResource(R.drawable.btn_type7_nor);
        if(mImageViewLocal!=null)mImageViewLocal.setImageResource(R.drawable.icon_local_nor);
        mTextViewExLocal.setTextColor(selectedColor);
        mLayoutExt.setBackgroundResource(R.color.transparent);
        if(mImageViewExt!=null)mImageViewExt.setImageResource(R.drawable.icon_memory2_dis);
        mTextViewExExt.setTextColor(unselectedColor);
        StorageUtil.setStorageMode(getContext(), StorageUtil.Storage.LOCAL);
    }

    private void selectExt() {
        mLayoutLocal.setBackgroundResource(R.color.transparent);
        if(mImageViewLocal!=null)mImageViewLocal.setImageResource(R.drawable.icon_local_dis);
        mTextViewExLocal.setTextColor(unselectedColor);
        mLayoutExt.setBackgroundResource(R.drawable.btn_type7_nor);
        if(mImageViewExt!=null)mImageViewExt.setImageResource(R.drawable.icon_memory2_nor);
        mTextViewExExt.setTextColor(selectedColor);
        StorageUtil.setStorageMode(getContext(), StorageUtil.Storage.EXTERNAL);
    }

    @Override
    public void onCheckedChanged(int id, boolean isChecked) {

        switch (id) {

            case R.id.ttb_tts_sound:
                StorageUtil.setStorageMode(getContext(), IS_TTS_SOUND, isChecked);

                if (!isChecked) {

                    ssbTtsValue.setProgress(0);

                }

                Log.d("check", "ttb_tts_sound " + isChecked);
                break;

            case R.id.ttb_use_utc:
                StorageUtil.setStorageMode(getContext(), IS_USE_UTC, isChecked);
                Log.d("check", "IS_USE_UTC " + isChecked);
                break;

            case R.id.ttb_auto_screen_brightness:

                StorageUtil.setStorageMode(getContext(), AUTO_SCREEN_BRIGHTNESS, isChecked);

                if (isChecked) {
                    // 자동 밝기 설정
                    Util.setAutoBrightness(getActivity(), isChecked);

                    // 발기를 조절한다.
                    Util.refreshBrightness(getActivity(), -1);
                } else {

                    int brightness = Integer.parseInt(StorageUtil.getStorageModeEx(getActivity(), SCREEN_VALUE, "0"));
                    float calValue = (brightness * 1) / (float)100;

                    // 발기를 조절한다.
                    Util.refreshBrightness(getActivity(), calValue);

                }


                Log.d("check", "ttb_auto_screen_brightness " + isChecked);
                break;
            case R.id.ttb_change_color_automatically:
                StorageUtil.setStorageMode(getContext(), CHANGE_COLOR_AUTOMATICALLY, isChecked);
                Log.d("check", "ttb_change_color_automatically " + isChecked);
                break;

            case R.id.ttb_ads_interlock:
                StorageUtil.setStorageMode(getContext(), ADS_INTERLOCK, isChecked);
                Log.d("check", "ttb_ads_interlock " + isChecked);
                break;
            case R.id.ttb_ads_link_wifi:
                StorageUtil.setStorageMode(getContext(), ADS_LINK_WIFI, isChecked);
                Log.d("check", "ttb_ads_link_wifi " + isChecked);
                break;
            case R.id.ttb_ads_link_bluetooth:
                StorageUtil.setStorageMode(getContext(), ADS_LINK_BLUETOOTH, isChecked);
                Log.d("check", "ttb_ads_link_bluetooth " + isChecked);
                break;
            case R.id.ttb_ads_info_expression:
                StorageUtil.setStorageMode(getContext(), ADS_INFO_EXPRESSION, isChecked);
                Log.d("check", "ttb_ads_info_expression " + isChecked);
                break;
            case R.id.ttb_ads_around_aircraft_info_expression:
                StorageUtil.setStorageMode(getContext(), ADS_AROUND_AIRCRAFT_INFO_EXPRESSION, isChecked);
                Log.d("check", "ttb_ads_around_aircraft_info_expression " + isChecked);
                break;
            case R.id.ttb_send_and_receive_flight_information:
                StorageUtil.setStorageMode(getContext(), SEND_AND_RECEIVE_FLIGHT_INFORMATION, isChecked);
                Log.d("check", "ttb_send_and_receive_flight_information " + isChecked);
                break;
            case R.id.ttb_connect_with_system:
                StorageUtil.setStorageMode(getContext(), CONNECT_WITH_SYSTEM, isChecked);
                Log.d("check", "ttb_connect_with_system " + isChecked);
                break;
            case R.id.ttb_provide_aircraft_location:
                StorageUtil.setStorageMode(getContext(), PROVIDE_AIRCRAFT_LOCATION, isChecked);
                Log.d("check", "ttb_provide_aircraft_location " + isChecked);
                break;
            case R.id.ttb_auto_delete:
                StorageUtil.setStorageMode(getContext(), AUTO_DELETE, isChecked);
                Log.d("check", "ttb_auto_delete " + isChecked);
                break;



        }

    }
    


}


