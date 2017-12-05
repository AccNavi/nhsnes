package kr.go.molit.nhsnes.fragment;

import static kr.go.molit.nhsnes.fragment.SystemSettingFragment.SCREEN_VALUE;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.modim.lan.lanandroid.AirSystemParametersInfo;
import com.modim.lan.lanandroid.INativeImple;
import com.modim.lan.lanandroid.NativeImplement;
import com.modim.lan.lanandroid.NhsLanView;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.widget.SelectButton;
import kr.go.molit.nhsnes.widget.TextToggleButton;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class FlightInfoFragment extends BaseFragment implements TextToggleButton.OnCheckedChangeListener, SelectButton.OnSelectListener{

    public final static String PATH_EXIT_BUFFER_ZONE = "Path exit buffer zone"; // 경로이탈 버퍼존
    public final static String PRESERVATION_OF_OBSTACLES = "Preservation of obstacles"; // 장애물 경보존
    public final static String SPACE_CONSERVATION = "Space conservation"; // 공역 경보존
    public final static String FLYING_DISTANCE = "Flying distance"; // 비행금지구역 접근가능 거리
    public final static String HIGHEST_FLIGHT_ALTITUDE = "Highest flight altitude"; // 최고 비행 고도
    public final static String LOWEST_FLIGHT_ALTITUDE = "Lowest flight altitude"; // 최저비행 고도
    public final static String SET_THE_MAP_DOWNLOAD_SCOPE = "Set the map download scope"; // 지도 다운로드 범위 설정

    public final static String EXPLORING_PATH_EXITS= "Exploring path exits"; //  경로이탈 재검색
    public final static String ALERT_MESSAGE_SET = "Alert message_set"; // 경보설정 설정
    private final static String ALARM_MESSAGE_VOICE = "Alarm message_voice"; // 경보 설정 음성
    public final static String AUTOZOOM_LEVEL_SETTING = "Auto zoom level setting"; // 줌 레벨 자동 설정

    private View rootView;

    private SelectButton sbPathExitBufferZone = null;
    private SelectButton sbPreservationOfObstacles = null;
    private SelectButton sbSpaceConservation = null;
    private SelectButton sbFlyingDistance = null;
    private SelectButton sbHighestFlightAltitude = null;
    private SelectButton sbLowestFlightAltitude = null;
    private SelectButton sbSetTheMapDownloadScope = null;
    private TextToggleButton ttbExploringPathExits = null;
    private TextToggleButton ttbAlertMessageSet = null;
    private TextToggleButton ttbAlarmMessageVoice = null;
    private TextToggleButton ttbAutozoomLevelSetting = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = LayoutInflater.from(getContext()).inflate(R.layout.frg_flight_info_setting, container, false);
        }

        setLayout();

        return rootView;
    }

    private void setLayout(){

        this.sbPathExitBufferZone = (SelectButton) this.rootView.findViewById(R.id.sb_path_exit_buffer_zone);
        this.sbPreservationOfObstacles = (SelectButton) this.rootView.findViewById(R.id.sb_preservation_of_obstacles);
        this.sbSpaceConservation = (SelectButton) this.rootView.findViewById(R.id.sb_space_conservation);
        this.sbFlyingDistance = (SelectButton) this.rootView.findViewById(R.id.sb_flying_distance);
        this.sbHighestFlightAltitude = (SelectButton) this.rootView.findViewById(R.id.sb_highest_flight_altitude);
        this.sbLowestFlightAltitude = (SelectButton) this.rootView.findViewById(R.id.sb_lowest_flight_altitude);
        this.sbSetTheMapDownloadScope = (SelectButton) this.rootView.findViewById(R.id.sb_set_the_map_download_scope);

        this.ttbExploringPathExits = (TextToggleButton) this.rootView.findViewById(R.id.ttb_exploring_path_exits);
        this.ttbAlertMessageSet = (TextToggleButton) this.rootView.findViewById(R.id.ttb_alert_message_set);
        this.ttbAlarmMessageVoice = (TextToggleButton) this.rootView.findViewById(R.id.ttb_alarm_message_voice);
        this.ttbAutozoomLevelSetting = (TextToggleButton) this.rootView.findViewById(R.id.ttb_autozoom_level_setting);


        this.sbPathExitBufferZone.setSelectIndex(Integer.parseInt(StorageUtil.getStorageModeEx(getContext(), PATH_EXIT_BUFFER_ZONE, "0")));
        this.sbPreservationOfObstacles.setSelectIndex(Integer.parseInt(StorageUtil.getStorageModeEx(getContext(), PRESERVATION_OF_OBSTACLES, "0")));
        this.sbSpaceConservation.setSelectIndex(Integer.parseInt(StorageUtil.getStorageModeEx(getContext(), SPACE_CONSERVATION, "0")));
        this.sbFlyingDistance.setSelectIndex(Integer.parseInt(StorageUtil.getStorageModeEx(getContext(), FLYING_DISTANCE, "0")));
        this.sbHighestFlightAltitude.setSelectIndex(Integer.parseInt(StorageUtil.getStorageModeEx(getContext(), HIGHEST_FLIGHT_ALTITUDE, "0")));
        this.sbLowestFlightAltitude.setSelectIndex(Integer.parseInt(StorageUtil.getStorageModeEx(getContext(), LOWEST_FLIGHT_ALTITUDE, "0")));
        this.sbSetTheMapDownloadScope.setSelectIndex(Integer.parseInt(StorageUtil.getStorageModeEx(getContext(), SET_THE_MAP_DOWNLOAD_SCOPE, "0")));

        this.ttbExploringPathExits.setCheck(StorageUtil.getStorageMode(getContext(), EXPLORING_PATH_EXITS));
        this.ttbAlertMessageSet.setCheck(StorageUtil.getStorageMode(getContext(), ALERT_MESSAGE_SET));
        this.ttbAlarmMessageVoice.setCheck(StorageUtil.getStorageMode(getContext(), ALARM_MESSAGE_VOICE));
        this.ttbAutozoomLevelSetting.setCheck(StorageUtil.getStorageMode(getContext(), AUTOZOOM_LEVEL_SETTING));

        sbPathExitBufferZone.setUnit("m");
        sbPathExitBufferZone.setSelect0("100");
        sbPathExitBufferZone.setSelect1("200");
        sbPathExitBufferZone.setSelect2("500");

        sbPreservationOfObstacles.setUnit("nm");
        sbPreservationOfObstacles.setSelect0("2");
        sbPreservationOfObstacles.setSelect1("3");
        sbPreservationOfObstacles.setSelect2("5");

        sbSpaceConservation.setUnit("nm");
        sbSpaceConservation.setSelect0("2");
        sbSpaceConservation.setSelect1("3");
        sbSpaceConservation.setSelect2("5");

        sbFlyingDistance.setUnit("nm");
        sbFlyingDistance.setSelect0("2");
        sbFlyingDistance.setSelect1("3");
        sbFlyingDistance.setSelect2("5");

        sbHighestFlightAltitude.setUnit("ft");
        sbHighestFlightAltitude.setSelect0("4500");
        sbHighestFlightAltitude.setSelect1("4800");
        sbHighestFlightAltitude.setSelect2("5000");

        sbLowestFlightAltitude.setUnit("ft");
        sbLowestFlightAltitude.setSelect0("500");
        sbLowestFlightAltitude.setSelect1("600");
        sbLowestFlightAltitude.setSelect2("700");

        sbSetTheMapDownloadScope.setUnit("nm");
        sbSetTheMapDownloadScope.setSelect0("3");
        sbSetTheMapDownloadScope.setSelect1("4");
        sbSetTheMapDownloadScope.setSelect2("5");


        this.ttbExploringPathExits.setOnCheckedChangeListener(this);
        this.ttbAlertMessageSet.setOnCheckedChangeListener(this);
        this.ttbAlarmMessageVoice.setOnCheckedChangeListener(this);
        this.ttbAutozoomLevelSetting.setOnCheckedChangeListener(this);

        this.sbPathExitBufferZone.setOnSelectListener(this);
        this.sbPreservationOfObstacles.setOnSelectListener(this);
        this.sbSpaceConservation.setOnSelectListener(this);
        this.sbFlyingDistance.setOnSelectListener(this);
        this.sbHighestFlightAltitude.setOnSelectListener(this);
        this.sbLowestFlightAltitude.setOnSelectListener(this);
        this.sbSetTheMapDownloadScope.setOnSelectListener(this);


        // 화면 밝기에 맞게 설정한다.
        int brightness = Integer.parseInt(StorageUtil.getStorageModeEx(getActivity(), SCREEN_VALUE, "0"));

        float calValue = (brightness * 1) / (float)100;

        // 발기를 조절한다.
        Util.refreshBrightness(getActivity(), calValue);

        // 시스템 설정을 변경
        setSystemParametersInfo();

    }

    @Override
    public void onCheckedChanged(int id, boolean isChecked) {

        switch (id) {

            case R.id.ttb_exploring_path_exits:
                StorageUtil.setStorageMode(getContext(), EXPLORING_PATH_EXITS, isChecked);
                Log.d("check", "ttb_exploring_path_exits " + isChecked);
                break;

            case R.id.ttb_alert_message_set:
                StorageUtil.setStorageMode(getContext(), ALERT_MESSAGE_SET, isChecked);
                Log.d("check", "ttb_alert_message_set " + isChecked);
                break;

            case R.id.ttb_alarm_message_voice:
                StorageUtil.setStorageMode(getContext(), ALARM_MESSAGE_VOICE, isChecked);
                Log.d("check", "ttb_alarm_message_voice " + isChecked);
                break;

            case R.id.ttb_autozoom_level_setting:
                StorageUtil.setStorageMode(getContext(), AUTOZOOM_LEVEL_SETTING, isChecked);
                Log.d("check", "ttb_autozoom_level_setting " + isChecked);
                break;

            default:
                break;

        }

        // 시스템 설정을 변경
        setSystemParametersInfo();

    }

    @Override
    public void onSelected(View v, int index, String data) {

        switch (v.getId()) {

            case R.id.sb_path_exit_buffer_zone:
                StorageUtil.setStorageMode(getContext(), PATH_EXIT_BUFFER_ZONE, index+"");
                break;
            case R.id.sb_preservation_of_obstacles:
            case R.id.sb_space_conservation:
            case R.id.sb_flying_distance:
                sbPreservationOfObstacles.setOnSelectListener(null);
                sbSpaceConservation.setOnSelectListener(null);
                sbFlyingDistance.setOnSelectListener(null);

                sbPreservationOfObstacles.setSelectIndex(index);
                sbSpaceConservation.setSelectIndex(index);
                sbFlyingDistance.setSelectIndex(index);

                StorageUtil.setStorageMode(getContext(), PRESERVATION_OF_OBSTACLES, index+"");
                StorageUtil.setStorageMode(getContext(), SPACE_CONSERVATION, index+"");
                StorageUtil.setStorageMode(getContext(), FLYING_DISTANCE, index+"");

                sbPreservationOfObstacles.setOnSelectListener(this);
                sbSpaceConservation.setOnSelectListener(this);
                sbFlyingDistance.setOnSelectListener(this);

                break;
            case R.id.sb_highest_flight_altitude:
                StorageUtil.setStorageMode(getContext(), HIGHEST_FLIGHT_ALTITUDE, index+"");
                break;
            case R.id.sb_lowest_flight_altitude:
                StorageUtil.setStorageMode(getContext(), LOWEST_FLIGHT_ALTITUDE, index+"");
                break;
            case R.id.sb_set_the_map_download_scope:
                StorageUtil.setStorageMode(getContext(), SET_THE_MAP_DOWNLOAD_SCOPE, index+"");
                break;

        }

        // 시스템 설정을 변경
        setSystemParametersInfo();

    }

    /**
     * 시스템 설정을 변경
     * @author FIESTA
     * @since  오전 12:39
     **/
    private void setSystemParametersInfo(){
        NativeImplement nativeImplement = INativeImple.getInstance(getContext());

        AirSystemParametersInfo airSystemParametersInfo = new AirSystemParametersInfo();

        int pathExitBufferZone = Integer.parseInt(sbPathExitBufferZone.getSelectedData());
        int preservationOfObstacles = Integer.parseInt(sbPreservationOfObstacles.getSelectedData());
        int paceConservation = Integer.parseInt(sbSpaceConservation.getSelectedData());
        int flyingDistance = Integer.parseInt(sbFlyingDistance.getSelectedData());
        int highestFlightAltitude = Integer.parseInt(sbHighestFlightAltitude.getSelectedData());
        int lowestFlightAltitude = Integer.parseInt(sbLowestFlightAltitude.getSelectedData());
        int setTheMapDownloadScope = Integer.parseInt(sbSetTheMapDownloadScope.getSelectedData());
        boolean isDeviation = ttbExploringPathExits.isCheck();
        boolean isAlarm = ttbAlertMessageSet.isCheck();

        // 사용할 마스크 설정
        airSystemParametersInfo.setuParamMask(AirSystemParametersInfo.SYSP_BUFAIRCRAFT1 |
        AirSystemParametersInfo.SYSP_BUFOBSTACLE1 |
                AirSystemParametersInfo.SYSP_BUFAIRTERRAIN1 |
                AirSystemParametersInfo.SYSP_BUFAIRCRAFT1 |
                AirSystemParametersInfo.SYSP_BUFMINALTITUDE |
                AirSystemParametersInfo.SYSP_BUFMAPDOWNRANGE |
                AirSystemParametersInfo.SYSP_DEVIATION |
                AirSystemParametersInfo.SYSP_ALARM |
                AirSystemParametersInfo.SYSP_SHOWALTITUDE);

        airSystemParametersInfo.setuBufDeviation1(pathExitBufferZone);          // 경로 이탈 버퍼존
        airSystemParametersInfo.setuBufObstacle1(preservationOfObstacles);      // 장애물/공역/비행금지구역 버퍼존
        airSystemParametersInfo.setuBufAirCraft1(400);                          // 지형 버퍼존
        airSystemParametersInfo.setuBufAirCraft1(pathExitBufferZone);           // 인접항공기
        airSystemParametersInfo.setuBufMinAltitude(lowestFlightAltitude);       // 비행 안전 고도 (최저)
        airSystemParametersInfo.setuBufMaxAltitude(highestFlightAltitude);      // 비행 안전 고도 (최고)
        airSystemParametersInfo.setuBufMapDownloadRange(setTheMapDownloadScope);      // 맵 다운로드 범위
        airSystemParametersInfo.setbDeviation(isDeviation); //  경로이탈 재탐색 여부
        airSystemParametersInfo.setbAlarm(isAlarm);         //  경보 설정 여부
        airSystemParametersInfo.setbShowAltitude(true);    //   측면고도를 표출 여부 설정

        nativeImplement.lanSystemParametersInfo(airSystemParametersInfo);

    }
}
