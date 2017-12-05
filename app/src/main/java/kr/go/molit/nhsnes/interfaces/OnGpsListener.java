package kr.go.molit.nhsnes.interfaces;

import android.location.Location;

/**
 * gps 관련 인터페이스
 *
 * @author FIESTA
 * @version 1.0.0
 * @since 2017-06-21 오전 9:18
 **/
public interface OnGpsListener {

  /**
   * gps 위치가 바뀌면 호출되는 메소드
   *
   * @param location 검색된 위치 정보
   * @author 임성진
   * @version 1.0.0
   * @since 2017-06-21 오전 9:23
   **/
  public void onLocationChanged(Location location);

  /**
   * gps 위치를 가져오지 못하면 발생하는 메소드
   *
   * @param message 실패 원인 메세지
   * @author 임성진
   * @version 1.0.0
   * @since 2017-06-21 오전 9:23
   **/
  public void onLocationFailed(String message);


}
