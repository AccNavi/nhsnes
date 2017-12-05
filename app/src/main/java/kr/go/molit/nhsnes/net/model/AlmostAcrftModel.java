package kr.go.molit.nhsnes.net.model;

/**
* 
* @author FIESTA
* @since  오전 12:49
**/
public class AlmostAcrftModel {

  private String acrftCd = "";      // 항공기ID(CallSign)
  private String planId = "";       // 비행계획서ID
  private String lat = "";          // 위도좌표
  private String lon = "";          // 경도좌표
  private String elev = "";         // 해발고도
  private String speed = "";        // 순항속도
  private String heading = "";      // 해딩방향
  private String msgTxt = "";       // 전파 메시지 내용

  public String getAcrftCd() {
    return acrftCd;
  }

  public void setAcrftCd(String acrftCd) {
    this.acrftCd = acrftCd;
  }

  public String getPlanId() {
    return planId;
  }

  public void setPlanId(String planId) {
    this.planId = planId;
  }

  public String getLat() {
    return lat;
  }

  public void setLat(String lat) {
    this.lat = lat;
  }

  public String getLon() {
    return lon;
  }

  public void setLon(String lon) {
    this.lon = lon;
  }

  public String getElev() {
    return elev;
  }

  public void setElev(String elev) {
    this.elev = elev;
  }

  public String getSpeed() {
    return speed;
  }

  public void setSpeed(String speed) {
    this.speed = speed;
  }

  public String getHeading() {
    return heading;
  }

  public void setHeading(String heading) {
    this.heading = heading;
  }

  public String getMsgTxt() {
    return msgTxt;
  }

  public void setMsgTxt(String msgTxt) {
    this.msgTxt = msgTxt;
  }




}
