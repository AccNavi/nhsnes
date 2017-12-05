package kr.go.molit.nhsnes.interfaces;

import com.modim.lan.lanandroid.AirPoint;

/**
* 맵 화면의 옵션 메뉴 클릭 인터페이스
* @author FIESTA
* @since  오후 7:51
**/
public interface OnClickOptionMapMenu {

  /**
  * 클릭 발생
  * @author FIESTA
  * @since  오전 3:49
  **/
  public void onClick(AirPoint curPos);

  /**
  * 시작지 추가
  * @author FIESTA
  * @since  오후 7:58
  **/
  public void setStart(AirPoint curPos);
  
  /**
  * 도착지 추가
  * @author FIESTA
  * @since  오후 7:55
  **/
  public void setGoal(AirPoint curPos);
  
  /**
  * 웨이 포인트 추가
  * @author FIESTA
  * @since  오후 7:55
  **/
  public void setWaypoint(AirPoint curPos);
  
  /**
  * 
  * @author FIESTA
  * @since  오후 7:55
  **/
  public void OnSearch();

  /**
  * 전체 리셋 버튼
  * @author FIESTA
  * @since  오후 7:55
  **/
  public void OnReset();


}
