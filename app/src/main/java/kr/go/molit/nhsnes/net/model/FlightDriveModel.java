package kr.go.molit.nhsnes.net.model;

import android.util.Log;

import java.util.List;

/**
* 주행 정보
* @author FIESTA
* @since  오전 12:47
**/
public class FlightDriveModel {

  private String result_code;
  private String result_msg;
  private String msgsendSn;
  private String msgsendContent;
  private String eegcmsgIdx;
  private String eegcmsgMemo;
  private List<AlmostAcrftModel> list;

  public String getMsgsendSn() {
    return msgsendSn;
  }

  public void setMsgsendSn(String msgsendSn) {
    this.msgsendSn = msgsendSn;
  }

  public String getMsgsendContent() {
    return msgsendContent;
  }

  public void setMsgsendContent(String msgsendContent) {
    this.msgsendContent = msgsendContent;
  }

  public String getEegcmsgIdx() {
    return eegcmsgIdx;
  }

  public void setEegcmsgIdx(String eegcmsgIdx) {
    this.eegcmsgIdx = eegcmsgIdx;
  }

  public String getEegcmsgMemo() {
    return eegcmsgMemo;
  }

  public void setEegcmsgMemo(String eegcmsgMemo) {
    this.eegcmsgMemo = eegcmsgMemo;
  }

  public String getResult_code() {
    return result_code;
  }

  public void setResult_code(String result_code) {
    this.result_code = result_code;
  }

  public String getResult_msg() {
    return result_msg;
  }

  public void setResult_msg(String result_msg) {
    this.result_msg = result_msg;
  }

  public List<AlmostAcrftModel> getList() {
    if(list.getClass().equals(String.class)){
      Log.d("JeLib","--------String--------");
      Log.d("JeLib","--------String--------");
      Log.d("JeLib","--------String--------");
      return null;
    }
    return list;
  }

  public void setList(List<AlmostAcrftModel> list) {
    this.list = list;
  }




}
