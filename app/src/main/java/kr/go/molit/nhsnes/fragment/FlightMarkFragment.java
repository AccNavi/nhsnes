package kr.go.molit.nhsnes.fragment;

import static com.modim.lan.lanandroid.Constants.NAVI_MAP_DEM;
import static com.modim.lan.lanandroid.Constants.NAVI_MAP_SATELLITE;
import static com.modim.lan.lanandroid.Constants.NAVI_MAP_VECTOR;
import static kr.go.molit.nhsnes.fragment.SystemSettingFragment.SCREEN_VALUE;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.modim.lan.lanandroid.NhsLanView;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.widget.TextToggleButton;
import kr.go.molit.nhsnes.widget.TextViewEx;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class FlightMarkFragment extends BaseFragment implements View.OnClickListener, TextToggleButton.OnCheckedChangeListener {

  public final static String ISL_LAYOUT_AIRCRAFTSPEED = "isLayoutAircraftSpeed";      // 속도 표시
  public final static String IS_LAYOUT_DISTANCE = "isLayoutDistance";                 // 거리 표시
  public final static String IS_LAYOUT_DIRECTION = "isLayoutDirection";              // 방향 표시
  public final static String IS_LAYOUT_ALTITUDE = "isLayoutAltitude";                // 고도 표시

  public final static String IS_LAYOUT_NW = "isLayoutnw";                             // 가상 정보 레이어
  public final static String AIRCRAFT_POSITIONING_LAYER = "Aircraft Positioning Layer";  // 항공기 위치 표시 레이어
  public final static String NOTAM_INFO_LAYER = "NOTAM_Info_layer";  // NOTAM 정보 레이어
  public final static String WEATHER_INFORMATION = "Weather_information";  // 기상정보
  public final static String AIRSPACE_INFORMATION_LAYER = "Airspace_information_layer";  // 공역_정보_레이어
  public final static String OBSTACLE_INFORMATION_LAYER = "Obstacle_information_layer";  // 장애물_정보_레이어
  public final static String LANDING_INFORMATION_LAYER = "Landing _ information layer";  // 이착륙장_정보_레이어
  public final static String MAP_LAYER = "Map layer";  // 지도 레이어
  public final static String EXPRESSWAY_DISPLAY_LAYER = "Expressway_display_layer";  // 고속도로_표시_레이어
  public final static String RIVER_INFORMATION_LAYER = "River information layer";  // 강 정보 레이어
  public final static String MOUNTAIN_INFORMATION_LAYER = "Mountain_information_layer";  // 산악_정보_레이어
  public final static String KML_DATA = "KML_Data";  // KML_데이터


  public final static String MAP_TYPE = "map_type";                                  // 백터, 위성, 디지털 지도 등등

  private ViewGroup mLayout2D, mLayout3D, mLayoutMap1, mLayoutMap2, mLayoutMap3;
  private ImageView mImageView2D, mImageView3D, mImageViewMap1, mImageViewMap2, mImageViewMap3;
  private TextViewEx mTextViewEx2D, mTextViewEx3D, mTextViewExMap1, mTextViewExMap2, mTextViewExMap3;

  private TextToggleButton mTtbAircraftSpeed, mTtbDistance, mTtbDirection, mTtbAltitude, mTtbnw;
  private TextToggleButton ttbAircraftPositioningLayer;
  private TextToggleButton ttbNotamInfoLayer;
  private TextToggleButton ttbAirspaceInformationLayer ;
  private TextToggleButton ttbObstacleInformationLayer ;
  private TextToggleButton ttbLandingInformationLayer ;
  private TextToggleButton ttbMapLayer;
  private TextToggleButton ttbExpresswayDisplayLayer;
  private TextToggleButton ttbRiverInformationLayer;
  private TextToggleButton ttbMountainInformationLayer;
  private TextToggleButton ttbKmlData;
  private TextToggleButton ttbWatherInfoLayer;


  private View rootView;

  private int selectedColor;
  private int unselectedColor;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    if (rootView == null) {
      rootView = LayoutInflater.from(getContext()).inflate(R.layout.frg_flight_mark_setting, container, false);

      selectedColor = Color.WHITE;
      unselectedColor = getResources().getColor(R.color.textColorTransparentWhite);

      mLayout2D = (ViewGroup) rootView.findViewById(R.id.layout_2d);
      mLayout3D = (ViewGroup) rootView.findViewById(R.id.layout_3d);
      mLayoutMap1 = (ViewGroup) rootView.findViewById(R.id.layout_map1);
      mLayoutMap2 = (ViewGroup) rootView.findViewById(R.id.layout_map2);
      mLayoutMap3 = (ViewGroup) rootView.findViewById(R.id.layout_map3);

      mLayout2D.setOnClickListener(this);
      mLayout3D.setOnClickListener(this);
      mLayoutMap1.setOnClickListener(this);
      mLayoutMap2.setOnClickListener(this);
      mLayoutMap3.setOnClickListener(this);

      mImageView2D = (ImageView) rootView.findViewById(R.id.iv_2d);
      mImageView3D = (ImageView) rootView.findViewById(R.id.iv_3d);
      mImageViewMap1 = (ImageView) rootView.findViewById(R.id.iv_map1);
      mImageViewMap2 = (ImageView) rootView.findViewById(R.id.iv_map2);
      mImageViewMap3 = (ImageView) rootView.findViewById(R.id.iv_map3);

      mTextViewEx2D = (TextViewEx) rootView.findViewById(R.id.tv_2d);
      mTextViewEx3D = (TextViewEx) rootView.findViewById(R.id.tv_3d);
      mTextViewExMap1 = (TextViewEx) rootView.findViewById(R.id.tv_map1);
      mTextViewExMap2 = (TextViewEx) rootView.findViewById(R.id.tv_map2);
      mTextViewExMap3 = (TextViewEx) rootView.findViewById(R.id.tv_map3);

      this.mTtbAircraftSpeed = ((TextToggleButton) rootView.findViewById(R.id.ttb_display_aircraft_speed));
      this.mTtbDistance = ((TextToggleButton) rootView.findViewById(R.id.ttb_display_distance));
      this.mTtbDirection = ((TextToggleButton) rootView.findViewById(R.id.ttb_display_direction));
      this.mTtbAltitude = ((TextToggleButton) rootView.findViewById(R.id.ttb_display_altitude));
      this.mTtbnw = ((TextToggleButton) rootView.findViewById(R.id.ttb_display_nw));


      this.ttbAircraftPositioningLayer = ((TextToggleButton) rootView.findViewById(R.id.ttb_aircraft_positioning_layer));
      this.ttbNotamInfoLayer = ((TextToggleButton) rootView.findViewById(R.id.ttb_notam_info_layer));
      this.ttbAirspaceInformationLayer = ((TextToggleButton) rootView.findViewById(R.id.ttb_airspace_information_layer));
      this.ttbObstacleInformationLayer = ((TextToggleButton) rootView.findViewById(R.id.ttb_obstacle_information_layer));
      this.ttbLandingInformationLayer = ((TextToggleButton) rootView.findViewById(R.id.ttb_landing_information_layer));
      this.ttbMapLayer = ((TextToggleButton) rootView.findViewById(R.id.ttb_map_layer));
      this.ttbExpresswayDisplayLayer = ((TextToggleButton) rootView.findViewById(R.id.ttb_expressway_display_layer));
      this.ttbRiverInformationLayer = ((TextToggleButton) rootView.findViewById(R.id.ttb_river_information_layer));
      this.ttbMountainInformationLayer = ((TextToggleButton) rootView.findViewById(R.id.ttb_mountain_information_layer));
      this.ttbKmlData = ((TextToggleButton) rootView.findViewById(R.id.ttb_kml_data));
      this.ttbWatherInfoLayer = ((TextToggleButton) rootView.findViewById(R.id.ttb_wather_info_layer));



      // 지도 레이어는 항상 true 이다
      this.ttbMapLayer.setCheck(true);
      this.ttbMapLayer.setEnabled(false);


      this.ttbAircraftPositioningLayer.setOnCheckedChangeListener(this);
      this.mTtbAircraftSpeed.setOnCheckedChangeListener(this);
      this.mTtbDistance.setOnCheckedChangeListener(this);
      this.mTtbDirection.setOnCheckedChangeListener(this);
      this.mTtbAltitude.setOnCheckedChangeListener(this);
      this.mTtbnw.setOnCheckedChangeListener(this);

      this.ttbNotamInfoLayer.setOnCheckedChangeListener(this);
      this.ttbAirspaceInformationLayer.setOnCheckedChangeListener(this);
      this.ttbObstacleInformationLayer.setOnCheckedChangeListener(this);
      this.ttbLandingInformationLayer.setOnCheckedChangeListener(this);
//      this.ttbMapLayer.setOnCheckedChangeListener(this);
      this.ttbExpresswayDisplayLayer.setOnCheckedChangeListener(this);
      this.ttbRiverInformationLayer.setOnCheckedChangeListener(this);
      this.ttbMountainInformationLayer.setOnCheckedChangeListener(this);
      this.ttbKmlData.setOnCheckedChangeListener(this);
      this.ttbWatherInfoLayer.setOnCheckedChangeListener(this);


      this.mTtbDirection = ((TextToggleButton) rootView.findViewById(R.id.ttb_display_distance));
      this.mTtbDirection = ((TextToggleButton) rootView.findViewById(R.id.ttb_display_direction));
      this.mTtbAltitude = ((TextToggleButton) rootView.findViewById(R.id.ttb_display_altitude));
      this.mTtbnw = ((TextToggleButton) rootView.findViewById(R.id.ttb_display_nw));



      this.mTtbAircraftSpeed.setCheck(StorageUtil.getStorageMode(getContext(), ISL_LAYOUT_AIRCRAFTSPEED));
      this.mTtbDistance.setCheck(StorageUtil.getStorageMode(getContext(), IS_LAYOUT_DISTANCE));
      this.mTtbDirection.setCheck(StorageUtil.getStorageMode(getContext(), IS_LAYOUT_DIRECTION));
      this.mTtbAltitude.setCheck(StorageUtil.getStorageMode(getContext(), IS_LAYOUT_ALTITUDE));
      this.mTtbnw.setCheck(StorageUtil.getStorageMode(getContext(), IS_LAYOUT_NW));


      this.ttbNotamInfoLayer.setCheck(StorageUtil.getStorageMode(getContext(), NOTAM_INFO_LAYER));
      this.ttbAirspaceInformationLayer.setCheck(StorageUtil.getStorageMode(getContext(), AIRSPACE_INFORMATION_LAYER));
      this.ttbObstacleInformationLayer.setCheck(StorageUtil.getStorageMode(getContext(), OBSTACLE_INFORMATION_LAYER));
      this.ttbLandingInformationLayer.setCheck(StorageUtil.getStorageMode(getContext(), LANDING_INFORMATION_LAYER));
//      this.ttbMapLayer.setCheck(StorageUtil.getStorageMode(getContext(), MAP_LAYER ));
      this.ttbExpresswayDisplayLayer.setCheck(StorageUtil.getStorageMode(getContext(), EXPRESSWAY_DISPLAY_LAYER));
      this.ttbRiverInformationLayer.setCheck(StorageUtil.getStorageMode(getContext(), RIVER_INFORMATION_LAYER));
      this.ttbMountainInformationLayer.setCheck(StorageUtil.getStorageMode(getContext(), MOUNTAIN_INFORMATION_LAYER));
      this.ttbKmlData.setCheck(StorageUtil.getStorageMode(getContext(), KML_DATA));
      this.ttbAircraftPositioningLayer.setCheck(StorageUtil.getStorageMode(getContext(), AIRCRAFT_POSITIONING_LAYER));
      this.ttbWatherInfoLayer.setCheck(StorageUtil.getStorageMode(getContext(), WEATHER_INFORMATION));

      String mapType =StorageUtil.getStorageModeEx(getContext(), MAP_TYPE, NAVI_MAP_VECTOR+"");

      if (mapType.equalsIgnoreCase(String.valueOf(NAVI_MAP_VECTOR)) ||
          mapType.equalsIgnoreCase(String.valueOf(NAVI_MAP_SATELLITE)) ) {
        select2D();
      } else {
        select3D();
      }
      selectMap(Integer.parseInt(StorageUtil.getStorageModeEx(getContext(), MAP_TYPE, NAVI_MAP_VECTOR+"")));

      // 화면 밝기에 맞게 설정한다.
      int brightness = Integer.parseInt(StorageUtil.getStorageModeEx(getActivity(), SCREEN_VALUE, "0"));

      float calValue = (brightness * 1) / (float)100;

      // 발기를 조절한다.
      Util.refreshBrightness(getActivity(), calValue);

    }

    return rootView;
  }


  @Override
  public void onClick(View v) {

    switch (v.getId()) {
      case R.id.layout_2d:
        selectMap(NAVI_MAP_VECTOR);
        StorageUtil.setStorageMode(getContext(), MAP_TYPE, NAVI_MAP_VECTOR+"");
        select2D();
        break;
      case R.id.layout_3d:
        selectMap(NAVI_MAP_DEM);
        StorageUtil.setStorageMode(getContext(), MAP_TYPE, NAVI_MAP_DEM+"");
        select3D();
        break;
      case R.id.layout_map1:
        StorageUtil.setStorageMode(getContext(), MAP_TYPE, NAVI_MAP_VECTOR+"");
        selectMap(NAVI_MAP_VECTOR);
        break;
      case R.id.layout_map2:
        StorageUtil.setStorageMode(getContext(), MAP_TYPE, NAVI_MAP_SATELLITE+"");
        selectMap(NAVI_MAP_SATELLITE);
        break;
      case R.id.layout_map3:
        StorageUtil.setStorageMode(getContext(), MAP_TYPE, NAVI_MAP_DEM+"");
        selectMap(NAVI_MAP_DEM);
        break;
    }

  }

  private void select2D() {
    mLayout2D.setBackgroundResource(R.drawable.btn_type8_nor);
    mImageView2D.setImageResource(R.drawable.icon_2dmap_nor);
    mTextViewEx2D.setTextColor(selectedColor);
    mLayout3D.setBackgroundResource(R.color.transparent);
    mImageView3D.setImageResource(R.drawable.icon_3dmap_dis);
    mTextViewEx3D.setTextColor(unselectedColor);


    mLayoutMap1.setEnabled(true);
    mLayoutMap2.setEnabled(true);
    mLayoutMap3.setEnabled(false);

  }

  private void select3D() {
    mLayout2D.setBackgroundResource(R.color.transparent);
    mImageView2D.setImageResource(R.drawable.icon_2dmap_dis);
    mTextViewEx2D.setTextColor(unselectedColor);
    mLayout3D.setBackgroundResource(R.drawable.btn_type8_nor);
    mImageView3D.setImageResource(R.drawable.icon_3dmap_nor);
    mTextViewEx3D.setTextColor(selectedColor);


    mLayoutMap1.setEnabled(false);
    mLayoutMap2.setEnabled(false);
    mLayoutMap3.setEnabled(true);

  }

  private void selectMap(int type) {
    mLayoutMap1.setBackgroundResource(R.color.transparent);
    mLayoutMap2.setBackgroundResource(R.color.transparent);
    mLayoutMap3.setBackgroundResource(R.color.transparent);

    mImageViewMap1.setBackgroundResource(R.drawable.icon_map1_dis);
    mImageViewMap2.setBackgroundResource(R.drawable.icon_map2_dis);
    mImageViewMap3.setBackgroundResource(R.drawable.icon_map3_dis);

    mTextViewExMap1.setTextColor(unselectedColor);
    mTextViewExMap2.setTextColor(unselectedColor);
    mTextViewExMap3.setTextColor(unselectedColor);


    switch (type) {
      case NAVI_MAP_VECTOR:
        mLayoutMap1.setBackgroundResource(R.drawable.btn_type8_nor);
        mImageViewMap1.setBackgroundResource(R.drawable.icon_map1_nor);
        mTextViewExMap1.setTextColor(selectedColor);
        break;
      case NAVI_MAP_SATELLITE:
        mLayoutMap2.setBackgroundResource(R.drawable.btn_type8_nor);
        mImageViewMap2.setBackgroundResource(R.drawable.icon_map2_nor);
        mTextViewExMap2.setTextColor(selectedColor);
        break;
      case NAVI_MAP_DEM:
        mLayoutMap3.setBackgroundResource(R.drawable.btn_type8_nor);
        mImageViewMap3.setBackgroundResource(R.drawable.icon_map3_nor);
        mTextViewExMap3.setTextColor(selectedColor);
        break;
    }


  }

  @Override
  public void onCheckedChanged(int id, boolean isChecked) {

    Log.d("JeLib", "--------------");

    switch (id) {

      case R.id.ttb_display_aircraft_speed:
        StorageUtil.setStorageMode(getContext(), ISL_LAYOUT_AIRCRAFTSPEED, isChecked);
        Log.d("check", "ttb_display_aircraft_speed " + isChecked);
        break;
      case R.id.ttb_display_distance:
        StorageUtil.setStorageMode(getContext(), IS_LAYOUT_DISTANCE, isChecked);
        Log.d("check", "ttb_display_distance " + isChecked);
        break;
      case R.id.ttb_display_direction:
        StorageUtil.setStorageMode(getContext(), IS_LAYOUT_DIRECTION, isChecked);
        Log.d("check", "ttb_display_direction " + isChecked);
        break;
      case R.id.ttb_display_altitude:
        StorageUtil.setStorageMode(getContext(), IS_LAYOUT_ALTITUDE, isChecked);
        Log.d("check", "ttb_display_altitude " + isChecked);
        break;
      case R.id.ttb_display_nw:
        StorageUtil.setStorageMode(getContext(), IS_LAYOUT_NW, isChecked);
        Log.d("check", "ttb_display_nw " + isChecked);
        break;


      case R.id.ttb_notam_info_layer:
        StorageUtil.setStorageMode(getContext(), NOTAM_INFO_LAYER , isChecked);
        Log.d("check", "ttb_notam_info_layer " + isChecked);
        break;
      case R.id.ttb_airspace_information_layer:
        StorageUtil.setStorageMode(getContext(), AIRSPACE_INFORMATION_LAYER, isChecked);
        Log.d("check", "ttb_airspace_information_layer " + isChecked);
        break;
      case R.id.ttb_obstacle_information_layer:
        StorageUtil.setStorageMode(getContext(), OBSTACLE_INFORMATION_LAYER, isChecked);
        Log.d("check", "ttb_obstacle_information_layer " + isChecked);
        break;
      case R.id.ttb_landing_information_layer:
        StorageUtil.setStorageMode(getContext(), LANDING_INFORMATION_LAYER, isChecked);
        Log.d("check", "ttb_landing_information_layer " + isChecked);
        break;
      case R.id.ttb_expressway_display_layer:
        StorageUtil.setStorageMode(getContext(), EXPRESSWAY_DISPLAY_LAYER, isChecked);
        Log.d("check", "ttb_expressway_display_layer " + isChecked);
        break;
      case R.id.ttb_river_information_layer:
        StorageUtil.setStorageMode(getContext(), RIVER_INFORMATION_LAYER, isChecked);
        Log.d("check", "ttb_river_information_layer " + isChecked);
        break;
      case R.id.ttb_mountain_information_layer:
        StorageUtil.setStorageMode(getContext(), MOUNTAIN_INFORMATION_LAYER, isChecked);
        Log.d("check", "ttb_mountain_information_layer " + isChecked);
        break;
      case R.id.ttb_kml_data:
        StorageUtil.setStorageMode(getContext(), KML_DATA, isChecked);
        Log.d("check", "ttb_kml_data " + isChecked);
        break;
      case R.id.ttb_map_layer:
        StorageUtil.setStorageMode(getContext(), MAP_LAYER, isChecked);
        Log.d("check", "ttb_map_layer " + isChecked);
        break;
      case R.id.ttb_aircraft_positioning_layer:
        StorageUtil.setStorageMode(getContext(), AIRCRAFT_POSITIONING_LAYER, isChecked);
        Log.d("check", "ttb_aircraft_positioning_layer " + isChecked);
        break;
      case R.id.ttb_wather_info_layer:
        StorageUtil.setStorageMode(getContext(), WEATHER_INFORMATION, isChecked);
        Log.d("check", "ttb_wather_information " + isChecked);
        break;

      default:
        break;

    }

  }
}

