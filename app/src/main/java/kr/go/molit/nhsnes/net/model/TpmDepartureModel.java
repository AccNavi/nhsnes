package kr.go.molit.nhsnes.net.model;

/**
* 비행계획서 출발 요청
* @author FIESTA
* @since  오전 12:49
**/
public class TpmDepartureModel {

  private String result_msg;
  private String result_code;
  private String planId;
  private String planSn;
  private int flgtIdx;

  public String getResult_msg() {
    return result_msg;
  }

  public void setResult_msg(String result_msg) {
    this.result_msg = result_msg;
  }

  public String getResult_code() {
    return result_code;
  }

  public void setResult_code(String result_code) {
    this.result_code = result_code;
  }

  public String getPlanId() {
    return planId;
  }

  public void setPlanId(String planId) {
    this.planId = planId;
  }

  public String getPlanSn() {
    return planSn;
  }

  public void setPlanSn(String planSn) {
    this.planSn = planSn;
  }

  public int getFlgtIdx() {
    return flgtIdx;
  }

  public void setFlgtIdx(int flgtIdx) {
    this.flgtIdx = flgtIdx;
  }


}
