package kr.go.molit.nhsnes.common;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.entity.StringEntity;
import kr.go.molit.nhsnes.model.NhsFlightScheduledModel;
import kr.go.molit.nhsnes.model.NhsMyInfoModel;
import kr.go.molit.nhsnes.net.model.FlightPlanModel;

import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_MBR_ID;
import static kr.go.molit.nhsnes.activity.NhsLoginActivity.LOGIN_TOKEN_KEY;

/**
* 네트워크 파라미터 유틸
* @author FIESTA
* @since  오전 12:44
**/
public class NetworkParamUtil {

//  MagicSE_Util magic = new MagicSE_Util(_context);
//  String test = "테스트 abc 123";
//  //암호화
//  String enc = magic.getEncData(test);
//  //복호화
//  String dec = magic.getDecData(enc);
//
//    Log.d("TEST", "원본 : "+test);
//    Log.d("TEST", "암호화 : "+enc);
//    Log.d("TEST", "복호화 : "+dec);

  public StringEntity encData(Context context, String jsonData){

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      String sessionKey = (String)SharedData.getSharedData(context, SharedData.SESSION_KEY);

      TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
      String imei = telephonyManager.getDeviceId();

      // 암홈화
      MagicSE_Util magic = new MagicSE_Util(context);
      String encData = magic.getEncData(jsonData);

      jsonParams.put("sessionId", imei);     // 세션키
      jsonParams.put("data", encData);              // 암호화된 데이터

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }

  /**
  * 비행 계획서 전송 파라미터
  * @author FIESTA
  * @since  오전 1:12
  **/
  public StringEntity writeFlight(NhsFlightScheduledModel model) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      jsonParams.put("planPriority", model.getPlanPriority());
      jsonParams.put("planDoccd", model.getPlanDoccd());
      jsonParams.put("planFltm", model.getPlanFltm());
      jsonParams.put("planRqstdept", model.getPlanRqstdept());
      jsonParams.put("planDate", model.getPlanDate());
      jsonParams.put("messageType", model.getMessageType());
      jsonParams.put("acrftCd", model.getAcrftCd());
      jsonParams.put("flightRule", model.getFlightRule());
      jsonParams.put("flightType", model.getFlightType());
      jsonParams.put("planPurpose", model.getPlanPurpose());
      jsonParams.put("planNumber", model.getPlanNumber());
      jsonParams.put("acrftType", model.getAcrftType());
      jsonParams.put("wakeTurbcat", model.getWakeTurbcat());
      jsonParams.put("planEquipment", model.getPlanEquipment());
      jsonParams.put("planDeparture", model.getPlanDeparture());
      jsonParams.put("planEtd", model.getPlanEtd());
      jsonParams.put("cruisingSpeed", model.getCruisingSpeed());
      jsonParams.put("flightLevel", model.getFlightLevel());
      jsonParams.put("planRoute", model.getPlanRoute());
      jsonParams.put("planArrival", model.getPlanArrival());
      jsonParams.put("planTeet", model.getPlanTeet());
      jsonParams.put("captainPhone", model.getCaptainPhone());
      jsonParams.put("planPresent", model.getPlanPresent());
      jsonParams.put("callsign", model.getCallsign());
      jsonParams.put("planMbrId", model.getPlanMbrId());
      jsonParams.put("oneAltn", model.getOneAltn());
      jsonParams.put("twoAltn", model.getTwoAltn());
      jsonParams.put("otherInfo", model.getOtherInfo());
      jsonParams.put("flightPsbtime", model.getFlightPsbtime());
      jsonParams.put("flightPerson", model.getFlightPerson());
      jsonParams.put("rrUhf", model.getRrUhf());
      jsonParams.put("rrVhf", model.getRrVhf());
      jsonParams.put("rrElt", model.getRrElt());
      jsonParams.put("emgcPolar", model.getEmgcPolar());
      jsonParams.put("emgcDesert", model.getEmgcDesert());
      jsonParams.put("emgcMaritime", model.getEmgcMaritime());
      jsonParams.put("emgcJungle", model.getEmgcJungle());
      jsonParams.put("lifejkLight", model.getLifejkLight());
      jsonParams.put("lifejkFluores", model.getLifejkFluores());
      jsonParams.put("lifejkUhf", model.getLifejkUhf());
      jsonParams.put("lifejkVhf", model.getLifejkVhf());
      jsonParams.put("lifebtNumber", model.getLifebtNumber());
      jsonParams.put("lifebtPerson", model.getLifebtPerson());
      jsonParams.put("lifebtCover", model.getLifebtCover());
      jsonParams.put("lifebtColor", model.getLifebtColor());
      jsonParams.put("acrftColor", model.getAcrftColor());

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }

  /**
   * 회원 탈퇴
   * @author FIESTA
   * @since  오전 12:44
   **/
  public StringEntity getWeatherPoint(Context context, String start_dt, String end_dt) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      jsonParams.put("tokenKey", StorageUtil.getStorageModeEx(context, LOGIN_TOKEN_KEY, ""));     // Token번호
      jsonParams.put("start_dt", start_dt);    // 검색 시작일시
      jsonParams.put("end_dt", end_dt);        // 검색 종료일시

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }

  /**
   * 지점기상정보
   * @author FIESTA
   * @since  오전 12:44
   **/
  public StringEntity getWeatherInfo(Context context, String start_dt, String end_dt) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      jsonParams.put("tokenKey", StorageUtil.getStorageModeEx(context, LOGIN_TOKEN_KEY, ""));     // Token번호
      jsonParams.put("start_dt", start_dt);    // 검색 시작일시
      jsonParams.put("end_dt", end_dt);        // 검색 종료일시

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }

  /**
   * 회원 탈퇴
   * @author FIESTA
   * @since  오전 12:44
   **/
  public StringEntity getLeave(String mbrId, String loginId, String loginPw, String leaveMemo) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      jsonParams.put("mbrId", mbrId);             // 회원일련번호
      jsonParams.put("loginId", loginId);        // 탈퇴사유
      jsonParams.put("loginPw", loginPw);        // 사용자ID
      jsonParams.put("leaveMemo", leaveMemo);    // 비밀번호

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }

  /**
   * 맵 버전
   * @author FIESTA
   * @since  오전 12:44
   **/
  public StringEntity getMapVersion(String sessionId, int mapType) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      jsonParams.put("sessionId", sessionId);     // 세션ID

      /**
       * 요청 지도 종류
       (1:벡터,
       2:위성,
       3:수치표고도,
       4:내비용 벡터,
       5: 내비용 위성지도,
       6:내비용 수치표고도)
       *
       */
      jsonParams.put("map_type", mapType);        // 탈퇴사유

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }

  /**
   * 회원정보 수정
   * @author FIESTA
   * @since  오전 12:44
   **/
  public StringEntity modifyMyInfo(NhsMyInfoModel nhsMyInfoModel) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      jsonParams.put("mbrId", nhsMyInfoModel.getMbrId());
      jsonParams.put("afftId", nhsMyInfoModel.getAfftId());
      jsonParams.put("hpno", nhsMyInfoModel.getHpno());
      jsonParams.put("telno", nhsMyInfoModel.getTelno());
      jsonParams.put("email", nhsMyInfoModel.getEmail());
      jsonParams.put("zipCode", nhsMyInfoModel.getZipCode());
      jsonParams.put("address", nhsMyInfoModel.getAddress());
      jsonParams.put("birday", nhsMyInfoModel.getBirday());
      jsonParams.put("loginPw", nhsMyInfoModel.getLoginPw());
      jsonParams.put("mbrGrade", nhsMyInfoModel.getMbrGrade());
      jsonParams.put("acrftCd", nhsMyInfoModel.getAcrftCd());

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }

  /**
   * 항공기 정보
   * @author FIESTA
   * @since  오전 12:44
   **/
  public StringEntity getAcrftInformation(String acrftCd) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      jsonParams.put("acrftCd", acrftCd);        // 항공 코드

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }

  /**
   * 내 정보
   * @author FIESTA
   * @since  오전 12:44
   **/
  public StringEntity getMyInfo(String mbrId) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      jsonParams.put("mbrId", mbrId);        // 회원일련번호

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }

  /**
   * 비행 계획서 리스트
   * @author FIESTA
   * @since  오전 12:44
   **/
  public StringEntity getFlightPlanList(String planMbrId,
                                        String planDate,
                                        String planStatus,
                                        String planDoccd) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      jsonParams.put("planMbrId",planMbrId);
      jsonParams.put("planDate",planDate);
//        params.put("planDate","20170912");  // test
      jsonParams.put("planStatus",planStatus.trim());
      jsonParams.put("planDoccd",planDoccd);

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }


  /**
   * 회원가입
   * @author FIESTA
   * @since  오전 12:44
   **/
  public StringEntity getRegister(String loginId, String mbrNm, String afftId,
                                  String hpno, String birday, String loginPw, String acrftCd) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      jsonParams.put("loginId", loginId);        // 사용자ID
      jsonParams.put("mbrNm", mbrNm);           // 사용자명
      jsonParams.put("afftId", afftId);        // 소속ID
      jsonParams.put("hpno", hpno);             // 핸드폰번호
      jsonParams.put("birday", birday);        // 생년월일
      jsonParams.put("loginPw", loginPw);      // 비밀번호
      jsonParams.put("acrftCd", acrftCd);      // 등록기호

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }


  /**
   * 아이디 중복 체크
   * @author FIESTA
   * @since  오전 12:44
   **/
  public StringEntity checkUserId(String loginId) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      jsonParams.put("loginId", loginId);        // 사용자ID

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }


  /**
   * 비밀번호 변경 요청
   * @author FIESTA
   * @since  오전 12:44
   **/
  public StringEntity changePwd(String mbrId, String loginPw) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      jsonParams.put("mbrId", mbrId);       // 회원일련번호
      jsonParams.put("loginPw", loginPw);  // 비밀번호

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }

  /**
   * 비밀번호 찾기 요청
   * @author FIESTA
   * @since  오전 12:44
   **/
  public StringEntity findPwd(String loginId, String mbrNm, String hpno, String vrfctNo) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      jsonParams.put("loginId", loginId);       // 사용자ID(key 값)
      jsonParams.put("mbrNm", mbrNm);           // 사용자명(key 값)
      jsonParams.put("hpno", hpno);             // 인증번호
      jsonParams.put("vrfctNo", vrfctNo);      // 핸드폰번호(key 값)

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }

  /**
   * 아이디찾기 요청
   * @author FIESTA
   * @since  오전 12:44
   **/
  public StringEntity findId(String mbrNm, String hpno, String vrfctNo) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      jsonParams.put("mbrNm", mbrNm);        // 사용자명
      jsonParams.put("vrfctNo", vrfctNo);  // 인증번호
      jsonParams.put("hpno", hpno);        // 핸드폰번호

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }

  /**
   * 인증번호 인증 요청
   * @author FIESTA
   * @since  오전 12:44
   **/
  public StringEntity requestCheckAuthCode(String vrfctNo, String hpno) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      jsonParams.put("vrfctNo", vrfctNo);  // 요청시나온값
      jsonParams.put("hpno", hpno);        // 휴대폰 번호

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }

  /**
   * 인증번호 요청
   * @author FIESTA
   * @since  오전 12:44
   **/
  public StringEntity requestAuthCode(String phoneNumber, String name) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      jsonParams.put("hpno", phoneNumber);  // 휴대폰 번호
      jsonParams.put("mbrNm", name);        // 유저 이름

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }

  /**
   * 관측기상(Metar/Speci) 조회
   * @author FIESTA
   * @since  오전 12:44
   **/
  public StringEntity getWeather(String apCd, String tokenKey, String startDate, String endDate, String isu_year) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      jsonParams.put("ap_cd", apCd);
      jsonParams.put("tokenKey", tokenKey);
      jsonParams.put("start_dt", startDate);
      jsonParams.put("end_dt", endDate);
      jsonParams.put("isu_year", isu_year);


      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }


  /**
  * 로그인 요청
  * @author FIESTA
  * @since  오전 12:44
  **/
  public StringEntity requestLogin(String userId, String pwd) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      jsonParams.put("loginId", userId);
      jsonParams.put("loginPw", pwd);

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }

  /**
   * 다운로드 요청
   * @author FIESTA
   * @since  오전 12:44
   **/
  public org.apache.http.entity.StringEntity requestDownload(Context context, String fileNm, String fileOriginNm) {

    JSONObject jsonParams = new JSONObject();
    org.apache.http.entity.StringEntity entity = null;

    try {

      jsonParams.put("tokenKey", StorageUtil.getStorageModeEx(context, LOGIN_TOKEN_KEY, ""));
      jsonParams.put("file_nm", fileNm);
      jsonParams.put("file_origin_nm", fileOriginNm);

      entity = new org.apache.http.entity.StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    return entity;

  }

  /**
   * 다운로드 요청
   * @author FIESTA
   * @since  오전 12:44
   **/
  public org.apache.http.entity.StringEntity requestDownloadDoyup(Context context, String mapType, String fileNm) {

    JSONObject jsonParams = new JSONObject();
    org.apache.http.entity.StringEntity entity = null;

    try {

      jsonParams.put("tokenKey", StorageUtil.getStorageModeEx(context, LOGIN_TOKEN_KEY, ""));
      jsonParams.put("map_type", mapType);
      jsonParams.put("file_nm", fileNm);

      entity = new org.apache.http.entity.StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    return entity;

  }

  /**
   * 로그아웃 요청
   * @author FIESTA
   * @since  오전 12:44
   **/
  public StringEntity requestLogout(Context context) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      jsonParams.put("mbrId", StorageUtil.getStorageModeEx(context, LOGIN_MBR_ID));
      jsonParams.put("tokenKey", StorageUtil.getStorageModeEx(context, LOGIN_TOKEN_KEY));

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }

  /**
  * 노탐
  * @author FIESTA
  * @version 1.0.0
  * @since 2017-10-25 오전 4:34
  **/
  public StringEntity notamParam(Context context) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
      String startDt = sdf.format(new Date());

      jsonParams.put("tokenKey", StorageUtil.getStorageModeEx(context, LOGIN_TOKEN_KEY, ""));
      jsonParams.put("dt_gb", "valid_dt");
      jsonParams.put("start_dt", startDt);
      jsonParams.put("end_dt", startDt);

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }

  /**
   * s노탐
   * @author FIESTA
   * @version 1.0.0
   * @since 2017-10-25 오전 4:34
   **/
  public StringEntity sNotamParam(Context context) {

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
      String startDt = sdf.format(new Date());
      sdf = new SimpleDateFormat("yyyy");
      String year = sdf.format(new Date());

      jsonParams.put("tokenKey", StorageUtil.getStorageModeEx(context, LOGIN_TOKEN_KEY, ""));
      jsonParams.put("ap_cd", "");
      jsonParams.put("isu_year", "2017");
//      jsonParams.put("start_dt", startDt);
//      jsonParams.put("end_dt", startDt);
      jsonParams.put("start_dt", "201701020727");
      jsonParams.put("end_dt", "201702020727");

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }

  //정제영(암호화)
  //서버 인증서 요청
  public StringEntity requestServerCert(Context context) {

    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
    String imei =  telephonyManager.getDeviceId();

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {
      jsonParams.put("sessionId", imei);

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }

  //세션키 전송
  public StringEntity sendSessionKey(Context context) {
    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    String imei = telephonyManager.getDeviceId();
    String sessionKey = (String)SharedData.getSharedData(context, SharedData.SESSION_KEY);

    JSONObject jsonParams = new JSONObject();
    StringEntity entity = null;

    try {

      Log.d("TEST", "sessionId ==> : "+imei);
      Log.d("TEST", "sessionKey ==> : "+sessionKey);
      jsonParams.put("sessionId", imei);
      jsonParams.put("sessionKey", sessionKey);

      Log.d("TEST", "param ==> : "+jsonParams.toString());

      entity = new StringEntity(jsonParams.toString(), "UTF-8");

    } catch (JSONException e) {
      // TODO Auto-generated catch block
    }

    return entity;

  }
}
