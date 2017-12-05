package kr.go.molit.nhsnes.model;

/**
* 비행 계획서 작성 정보 모델
* @author FIESTA
* @since  오전 12:30
**/
public class NhsFlightScheduledModel {

  private String planPriority = "FF";       // 전문우선순위
  private String planDoccd = "00";          // 비행계획서 서류구분
  private String planFltm = "";             // 제출시간
  private String planRqstdept = "1111";   // 의뢰부서
  private String planDate = "";           // 비행계획 일자
  private String messageType = "FPL";     // 전문타입
  private String acrftCd = "";            // 항공기식별부호
  private String flightRule = "";         // 비행규칙
  private String flightType = "";         // 비행종류
  private String planPurpose = "";        // 비행목적
  private String planNumber = "";         // 비행댓수
  private String acrftType = "";          // 항공기형식
  private String wakeTurbcat = "";       // 등급
  private String planEquipment = "";     // 탑재장비
  private String planDeparture = "";    // 출발비행장
  private String planEtd = "";           // 출발예상시간
  private String cruisingSpeed = "";    // 순항속도
  private String flightLevel = "";      // 비행고도
  private String planRoute = "";        // 비행 ROUTE
  private String planArrival = "";      // 목적비행장
  private String planTeet = "";         // 총소요시간
  private String captainPhone = "";     // 기장연락처
  private String planPresent = "";      // 제출자명
  private String callsign = "";         // 콜사인
  private String planMbrId = "";        // 제출자 사용자일련번호
  private String oneAltn = "";          // 주 교체비행장
  private String twoAltn = "";          // 부 교체비행장
  private String otherInfo = "";        // 기타정보
  private String flightPsbtime = "";    // 비행가능시간
  private String flightPerson = "";     // 탑승인원수
  private String rrUhf = "";            // 비상무선탑재장비_UHF
  private String rrVhf = "";            // 비상무선탑재장비_VHF
  private String rrElt = "";            // 비상무선탑재장비_ELT
  private String emgcPolar = "";        // 구급용구_Polar
  private String emgcDesert = "";       // 구급용구_Desert
  private String emgcMaritime = "";     // 구급용구_Maritime
  private String emgcJungle = "";       // 구급영구_Jungle
  private String lifejkLight = "";      // 구명동의_Light
  private String lifejkFluores = "";    // 구명동의_Fluores
  private String lifejkUhf = "";        // 구명동의_UHF
  private String lifejkVhf = "";        // 구명동의_VHF
  private String lifebtNumber = "";     // 구명보트 수량
  private String lifebtPerson = "";     // 구명보트 수용인원
  private String lifebtCover = "";      // 구명보트 덮개
  private String lifebtColor = "";      // 구명보트 색상
  private String acrftColor = "";       // 항공기색상

  public String getPlanPriority() {
    return planPriority;
  }

  public String getPlanDoccd() {
    return planDoccd;
  }

  public String getPlanRqstdept() {
    return planRqstdept;
  }

  public void setPlanDate(String planDate) {
    this.planDate = planDate;
  }

  public String getPlanDate() {
    return planDate;
  }

  public String getMessageType() {
    return messageType;
  }

  public String getPlanFltm() {
    return planFltm;
  }

  public void setPlanFltm(String planFltm) {
    this.planFltm = planFltm;
  }

  public String getAcrftCd() {
    return acrftCd;
  }

  public void setAcrftCd(String acrftCd) {
    this.acrftCd = acrftCd;
  }

  public String getFlightRule() {
    return flightRule;
  }

  public void setFlightRule(String flightRule) {
    this.flightRule = flightRule;
  }

  public String getFlightType() {
    return flightType;
  }

  public void setFlightType(String flightType) {
    this.flightType = flightType;
  }

  public String getPlanPurpose() {
    return planPurpose;
  }

  public void setPlanPurpose(String planPurpose) {
    this.planPurpose = planPurpose;
  }

  public String getPlanNumber() {
    return planNumber;
  }

  public void setPlanNumber(String planNumber) {
    this.planNumber = planNumber;
  }

  public String getAcrftType() {
    return acrftType;
  }

  public void setAcrftType(String acrftType) {
    this.acrftType = acrftType;
  }

  public String getWakeTurbcat() {
    return wakeTurbcat;
  }

  public void setWakeTurbcat(String wakeTurbcat) {
    this.wakeTurbcat = wakeTurbcat;
  }

  public String getPlanEquipment() {
    return planEquipment;
  }

  public void setPlanEquipment(String planEquipment) {
    this.planEquipment = planEquipment;
  }

  public String getPlanDeparture() {
    return planDeparture;
  }

  public void setPlanDeparture(String planDeparture) {
    this.planDeparture = planDeparture;
  }

  public String getPlanEtd() {
    return planEtd;
  }

  public void setPlanEtd(String planEtd) {
    this.planEtd = planEtd;
  }

  public String getCruisingSpeed() {
    return cruisingSpeed;
  }

  public void setCruisingSpeed(String cruisingSpeed) {
    this.cruisingSpeed = cruisingSpeed;
  }

  public String getFlightLevel() {
    return flightLevel;
  }

  public void setFlightLevel(String flightLevel) {
    this.flightLevel = flightLevel;
  }

  public String getPlanRoute() {
    return planRoute;
  }

  public void setPlanRoute(String planRoute) {
    this.planRoute = planRoute;
  }

  public String getPlanArrival() {
    return planArrival;
  }

  public void setPlanArrival(String planArrival) {
    this.planArrival = planArrival;
  }

  public String getPlanTeet() {
    return planTeet;
  }

  public void setPlanTeet(String planTeet) {
    this.planTeet = planTeet;
  }

  public String getCaptainPhone() {
    return captainPhone;
  }

  public void setCaptainPhone(String captainPhone) {
    this.captainPhone = captainPhone;
  }

  public String getPlanPresent() {
    return planPresent;
  }

  public void setPlanPresent(String planPresent) {
    this.planPresent = planPresent;
  }

  public String getCallsign() {
    return callsign;
  }

  public void setCallsign(String callsign) {
    this.callsign = callsign;
  }

  public String getPlanMbrId() {
    return planMbrId;
  }

  public void setPlanMbrId(String planMbrId) {
    this.planMbrId = planMbrId;
  }

  public String getOneAltn() {
    return oneAltn;
  }

  public void setOneAltn(String oneAltn) {
    this.oneAltn = oneAltn;
  }

  public String getTwoAltn() {
    return twoAltn;
  }

  public void setTwoAltn(String twoAltn) {
    this.twoAltn = twoAltn;
  }

  public String getOtherInfo() {
    return otherInfo;
  }

  public void setOtherInfo(String otherInfo) {
    this.otherInfo = otherInfo;
  }

  public String getFlightPsbtime() {
    return flightPsbtime;
  }

  public void setFlightPsbtime(String flightPsbtime) {
    this.flightPsbtime = flightPsbtime;
  }

  public String getFlightPerson() {
    return flightPerson;
  }

  public void setFlightPerson(String flightPerson) {
    this.flightPerson = flightPerson;
  }

  public String getRrUhf() {
    return rrUhf;
  }

  public void setRrUhf(String rrUhf) {
    this.rrUhf = rrUhf;
  }

  public String getRrVhf() {
    return rrVhf;
  }

  public void setRrVhf(String rrVhf) {
    this.rrVhf = rrVhf;
  }

  public String getRrElt() {
    return rrElt;
  }

  public void setRrElt(String rrElt) {
    this.rrElt = rrElt;
  }

  public String getEmgcPolar() {
    return emgcPolar;
  }

  public void setEmgcPolar(String emgcPolar) {
    this.emgcPolar = emgcPolar;
  }

  public String getEmgcDesert() {
    return emgcDesert;
  }

  public void setEmgcDesert(String emgcDesert) {
    this.emgcDesert = emgcDesert;
  }

  public String getEmgcMaritime() {
    return emgcMaritime;
  }

  public void setEmgcMaritime(String emgcMaritime) {
    this.emgcMaritime = emgcMaritime;
  }

  public String getEmgcJungle() {
    return emgcJungle;
  }

  public void setEmgcJungle(String emgcJungle) {
    this.emgcJungle = emgcJungle;
  }

  public String getLifejkLight() {
    return lifejkLight;
  }

  public void setLifejkLight(String lifejkLight) {
    this.lifejkLight = lifejkLight;
  }

  public String getLifejkFluores() {
    return lifejkFluores;
  }

  public void setLifejkFluores(String lifejkFluores) {
    this.lifejkFluores = lifejkFluores;
  }

  public String getLifejkUhf() {
    return lifejkUhf;
  }

  public void setLifejkUhf(String lifejkUhf) {
    this.lifejkUhf = lifejkUhf;
  }

  public String getLifejkVhf() {
    return lifejkVhf;
  }

  public void setLifejkVhf(String lifejkVhf) {
    this.lifejkVhf = lifejkVhf;
  }

  public String getLifebtNumber() {
    return lifebtNumber;
  }

  public void setLifebtNumber(String lifebtNumber) {
    this.lifebtNumber = lifebtNumber;
  }

  public String getLifebtPerson() {
    return lifebtPerson;
  }

  public void setLifebtPerson(String lifebtPerson) {
    this.lifebtPerson = lifebtPerson;
  }

  public String getLifebtCover() {
    return lifebtCover;
  }

  public void setLifebtCover(String lifebtCover) {
    this.lifebtCover = lifebtCover;
  }

  public String getLifebtColor() {
    return lifebtColor;
  }

  public void setLifebtColor(String lifebtColor) {
    this.lifebtColor = lifebtColor;
  }

  public String getAcrftColor() {
    return acrftColor;
  }

  public void setAcrftColor(String acrftColor) {
    this.acrftColor = acrftColor;
  }

}
