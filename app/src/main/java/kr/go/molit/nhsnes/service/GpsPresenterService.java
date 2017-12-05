package kr.go.molit.nhsnes.service;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;

import com.yayandroid.locationmanager.constants.FailType;
import com.yayandroid.locationmanager.constants.ProcessType;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.interfaces.OnGpsListener;

/**
 * Gps 서비스
 *
 * @author FIESTA
 * @version 1.0.0
 * @since 2017-06-20 오후 5:14
 **/
public class GpsPresenterService {

  private GpsPresenter gpsPresenter = null;
  private OnGpsListener onGpsListener = null;     // gps 결과를 전달할 리스너
  private Context context = null;

  public GpsPresenterService(Context context, GpsPresenter gpsPresenter) {
    this.gpsPresenter = gpsPresenter;
    this.context = context;
  }

  public void destroy() {
    this.gpsPresenter = null;
    this.onGpsListener = null;
  }

  /**
   * 위치가 갱신되면 호출되는 메소드
   *
   * @param location 위치정보
   * @author FIESTA
   * @version 1.0.0
   * @since 2017-06-21 오전 9:40
   **/
  public void onLocationChanged(Location location) {
    this.gpsPresenter.dismissGpsProgress();
    setText(location);

    // 전달할 리스너가 있다면, 위치를 전달한다
    if (this.onGpsListener != null) {
      this.onGpsListener.onLocationChanged(location);
    }
  }

  /**
   * 위치 정보를 가져오기가 실패할때 발생하는 메소드
   *
   * @param failType 오류 원인 코드
   * @author FIESTA
   * @version 1.0.0
   * @see FailType
   * @since 2017-06-21 오전 9:25
   **/
  public void onLocationFailed(@FailType int failType) {
    this.gpsPresenter.dismissGpsProgress();

    String message = "";

    switch (failType) {
      case FailType.TIMEOUT: {
        message = context.getString(R.string.GPS_TIMEOUT);
        break;
      }
      case FailType.PERMISSION_DENIED: {
        message = context.getString(R.string.GPS_PERMISSION_DENIED);
        break;
      }
      case FailType.NETWORK_NOT_AVAILABLE: {
        message = context.getString(R.string.GPS_NETWORK_NOT_AVAILABLE);
        break;
      }
      case FailType.GOOGLE_PLAY_SERVICES_NOT_AVAILABLE: {
        message = context.getString(R.string.GPS_GOOGLE_PLAY_SERVICES_NOT_AVAILABLE);
        break;
      }
      case FailType.GOOGLE_PLAY_SERVICES_CONNECTION_FAIL: {
        message = context.getString(R.string.GPS_GOOGLE_PLAY_SERVICES_CONNECTION_FAIL);
        break;
      }
      case FailType.GOOGLE_PLAY_SERVICES_SETTINGS_DIALOG: {
        message = context.getString(R.string.GPS_GOOGLE_PLAY_SERVICES_SETTINGS_DIALOG);
        break;
      }
      case FailType.GOOGLE_PLAY_SERVICES_SETTINGS_DENIED: {
        message = context.getString(R.string.GPS_GOOGLE_PLAY_SERVICES_SETTINGS_DENIED);
        break;
      }
      case FailType.VIEW_DETACHED: {
        message = context.getString(R.string.GPS_VIEW_DETACHED);
        break;
      }
      case FailType.VIEW_NOT_REQUIRED_TYPE: {
        message = context.getString(R.string.GPS_VIEW_NOT_REQUIRED_TYPE);
        break;
      }
      case FailType.UNKNOWN: {
        message = context.getString(R.string.GPS_UNKNOWN);
        break;
      }
    }

    this.gpsPresenter.setGpsStateText(message);

    // 전달할 리스너가 있다면, 실패 원인을 전달한다.
    if (this.onGpsListener != null) {
      this.onGpsListener.onLocationFailed(message);
    }

  }

  /**
   * 프로그래스바에 진행 메세지를 보여주기 위한 메소드
   *
   * @param newProcess 프로그래스 진행 상태 코드
   * @author FIESTA
   * @version 1.0.0
   * @see ProcessType
   * @since 2017-06-21 오전 9:26
   **/
  public void onProcessTypeChanged(@ProcessType int newProcess) {
    switch (newProcess) {
      case ProcessType.GETTING_LOCATION_FROM_GOOGLE_PLAY_SERVICES: {
        this.gpsPresenter.updateGpsProgress(this.context.getString(R.string.GPS_GETTING_LOCATION_FROM_GOOGLE_PLAY_SERVICES));
        break;
      }
      case ProcessType.GETTING_LOCATION_FROM_GPS_PROVIDER: {
        this.gpsPresenter.updateGpsProgress(this.context.getString(R.string.GPS_GETTING_LOCATION_FROM_GPS_PROVIDER));
        break;
      }
      case ProcessType.GETTING_LOCATION_FROM_NETWORK_PROVIDER: {
        this.gpsPresenter.updateGpsProgress(this.context.getString(R.string.GPS_GETTING_LOCATION_FROM_NETWORK_PROVIDER));
        break;
      }
      case ProcessType.ASKING_PERMISSIONS:
      case ProcessType.GETTING_LOCATION_FROM_CUSTOM_PROVIDER:
        // Ignored
        break;
    }
  }

  public OnGpsListener getOnGpsListener() {
    return onGpsListener;
  }

  public void setOnGpsListener(OnGpsListener onGpsListener) {
    this.onGpsListener = onGpsListener;
  }

  private void setText(Location location) {
    String appendValue = location.getLatitude() + ", " + location.getLongitude() + "\n";
    String newValue;
    CharSequence current = this.gpsPresenter.getGpsStateText();

    if (!TextUtils.isEmpty(current)) {
      newValue = current + appendValue;
    } else {
      newValue = appendValue;
    }

    this.gpsPresenter.setGpsStateText(newValue);
  }

  public interface GpsPresenter {

    /**
     * gps 상태 text를 가져온다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-06-21 오전 8:27
     **/
    String getGpsStateText();

    /**
     * gps 상태 text를 보낸다
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-06-21 오전 8:28
     **/
    void setGpsStateText(String text);

    /**
     * gps를 위한 프로그래스바 메세지를 보낸다
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-06-21 오전 8:28
     **/
    void updateGpsProgress(String text);

    /**
     * gps를 위한 프로그래스바를 끝낸다
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-06-21 오전 8:29
     **/
    void dismissGpsProgress();

  }

}