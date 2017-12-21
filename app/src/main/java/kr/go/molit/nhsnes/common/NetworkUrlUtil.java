package kr.go.molit.nhsnes.common;

/**
* 네트워크 통신 유틸
* @author FIESTA
* @since  오전 12:41
**/
public class NetworkUrlUtil {

    private final static String BASE_URL = "http://59.23.225.177:8080"; //  암호화된 서버
//  private final static String BASE_URL = "http://211.107.29.58:8181"; //  암호화 안된 서버


  private String userLogin = BASE_URL + "/NIF/user/login/userLogin.do";                                        // 로그인 요청
  private String requestAuthCode = BASE_URL + "/NIF/user/certification/userCertificationRequest.do";       // 인증번호요청
  private String requestCheckAuthCode = BASE_URL + "/NIF/user/certification/userCertificationConfirm.do";       // 인증번호확인요청
  private String findId = BASE_URL + "/NIF/user/find/userIdFind.do";       // 아이디 찾기 요청
  private String findPwd = BASE_URL + "/NIF/user/find/userPassFind.do";   // 비밀번호 찾기 요청
  private String changePwd = BASE_URL + "/NIF/user/pwModify/userPwModifyPage.do";   // 비밀번호수정페이지
  private String logout = BASE_URL + "/NIF/user/logout/userLogout.do";   // 로그아웃
  private String checkUserId = BASE_URL + "/NIF/user/idCheck/userIdCheck.do";   // 유저 아이디 중복 검사
  private String companyList = BASE_URL + "/NIF/user/inst/userInst.do";   // 소속 정보를 받아온다.
  private String register = BASE_URL + "/NIF/user/join/userJoin.do";   // 회원가입
  private String myInfo = BASE_URL + "/NIF/user/myPage/userMyPage.do";   // 내정보
  private String modifyMyInfo = BASE_URL + "/NIF/user/modify/userModify.do";   // 내정보 수정
  private String leave = BASE_URL + "/NIF/user/leave/userLeave.do";   // 회원 탈퇴
  private String writeFlight = BASE_URL + "/NIF/fpm/fpl/fpmFpl.do";   // 비행계획서 제출요청 + 경로정보등록
  private String requestAgreement = BASE_URL + "/NIF/user/clause/userClause.do";   // 이용약관
  private String requestDownload = BASE_URL + "/NIF/nis/file/downFileAirChart.do";   // 다운로드
  private String weatherMetar = BASE_URL + "/NIF/nis/list/weatherMetar.do";   // 관측기상(Metar/Speci) 조회
  private String notam = BASE_URL + "/NIF/nis/list/notamList.do";   // 항공고시보(NOTAM) 조회
  private String snotam = BASE_URL + "/NIF/nis/list/snowtamList.do";   // 항공고시보(SNOWTAM) 조회
  private String mapVersion = BASE_URL + "/NIF/gis/list/mapVersion.do";   // 맵 버전
  private String fpmList = BASE_URL+"/NIF/fpm/list/fpmList.do";   // 비행계획서 리스트
  private String downloadMap = BASE_URL+"/NIF/gis/file/downloadMapData.do";   // 맵 데이터 다운로드
  private String acrftInformation = BASE_URL+"/NIF/user/acrft/AcrftInformation.do";   // 항공기정보
  private String weatherTaf = BASE_URL+"/NIF/nis/list/weatherTaf.do";   // 기상 taf
  private String weatherWrng = BASE_URL+"/NIF/nis/list/weatherWrng.do";   // 공항경보(Wrng) 조회
  private String weatherAirmet = BASE_URL+"/NIF/nis/list/weatherAirmet.do";   // 저고도 위험기상(Airmet) 조회
  private String weatherSigmet = BASE_URL+"/NIF/nis/list/weatherSigmet.do";   // 고고도 위험기상(Sigmet) 조회
  private String weatherPoint = BASE_URL+"/NIF/nis/list/weatherPoint.do";   // 지점기상정보

  //정제영(암호화)
  private String requestServerCert = BASE_URL+"/NIF/msg/cert/svrCert.do";   // 서버 인증서 가져오기
  //private String sendSessionKey = BASE_URL+"/NIF/msg/cert/setSession.do";   // 세션키 전송
  private String sendSessionKey = BASE_URL+"/NIF/msg/cert/svrSetting.do";   // 세션키 전송

  /**
   * 예전 주소
   */
//  private final static String BASE_URL = "http://211.249.63.76:8080";
//  private final static String IBRA_URL = "http://211.107.29.58:8181";
//
//  private String userLogin = BASE_URL + "/ws/user/login/userLogin.do";                                        // 로그인 요청
//  private String requestAuthCode = BASE_URL + "/ws/user/certification/userCertificationRequest.do";       // 인증번호요청
//  private String requestCheckAuthCode = BASE_URL + "/ws/user/certification/userCertificationConfirm.do";       // 인증번호확인요청
//  private String findId = BASE_URL + "/ws/user/find/userIdFind.do";       // 아이디 찾기 요청
//  private String findPwd = BASE_URL + "/ws/user/find/userPassFind.do";   // 비밀번호 찾기 요청
//  private String changePwd = BASE_URL + "/ws/user/pwModify/userPwModifyPage.do";   // 비밀번호수정페이지
//  private String logout = BASE_URL + "/ws/user/logout/userLogout.do";   // 로그아웃
//  private String checkUserId = BASE_URL + "/ws/user/idCheck/userIdCheck.do";   // 유저 아이디 중복 검사
//  private String companyList = BASE_URL + "/ws/user/inst/userInst.do";   // 소속 정보를 받아온다.
//  private String register = BASE_URL + "/ws/user/join/userJoin.do";   // 회원가입
//  private String myInfo = BASE_URL + "/ws/user/myPage/userMyPage.do";   // 내정보
//  private String modifyMyInfo = BASE_URL + "/ws/user/modify/userModify.do";   // 내정보 수정
//  private String leave = BASE_URL + "/ws/user/leave/userLeave.do";   // 회원 탈퇴
//  private String writeFlight = BASE_URL + "/ws/fpm/fpl/fpmFpl.do";   // 비행계획서 제출요청 + 경로정보등록
//  private String requestAgreement = BASE_URL + "/ws/user/clause/userClause.do";   // 이용약관
//  private String requestDownload = IBRA_URL + "/NIF/nis/file/downFileAirChart.do";   // 다운로드
//  private String weatherMetar = IBRA_URL + "/NIF/nis/list/weatherMetar.do";   // 관측기상(Metar/Speci) 조회
//  private String notam = IBRA_URL + "/NIF/nis/list/notamList.do";   // 항공고시보(NOTAM) 조회
//  private String mapVersion = IBRA_URL + "/NIF/gis/list/mapVersion.do";   // 맵 버전

//  //정제영(암호화)
//  private String requestServerCert = IBRA_URL+"/NIF/msg/cert/svrCert.do";   // 서버 인증서 가져오기
//  //private String sendSessionKey = IBRA_URL+"/NIF/msg/cert/setSession.do";   // 세션키 전송
//  private String sendSessionKey = IBRA_URL+"/NIF/msg/cert/svrSetting.do";   // 세션키 전송

  /**
   * 기상 taf
   * @author FIESTA
   * @version 1.0.0
   **/
  public String getWeatherTaf() {
    return weatherTaf;
  }

  /**
   * 공항경보(Wrng) 조회
   * @author FIESTA
   * @version 1.0.0
   **/
  public String getWeatherWrng() {
    return weatherWrng;
  }

  /**
   * 저고도 위험기상(Airmet) 조회
   * @author FIESTA
   * @version 1.0.0
   **/
  public String getWeatherAirmet() {
    return weatherAirmet;
  }

  /**
   * 고고도 위험기상(Sigmet) 조회
   * @author FIESTA
   * @version 1.0.0
   **/
  public String getWeatherSigmet() {
    return weatherSigmet;
  }

  /**
   * 항공기정보
   * @author FIESTA
   * @version 1.0.0
   **/
  public String getAcrftInformation() {
    return acrftInformation;
  }

  /**
   * 도엽 파일 다운로드
   * @author FIESTA
   * @version 1.0.0
   * @since 2017-09-19 오후 10:32
   **/
  public String getSnotam() {
    return snotam;
  }


  /**
   * 도엽 파일 다운로드
   * @author FIESTA
   * @version 1.0.0
   * @since 2017-09-19 오후 10:32
   **/
  public String getDownloadMap() {
    return downloadMap;
  }


  /**
   * 지점기상정보
   * @author FIESTA
   * @version 1.0.0
   * @since 2017-09-19 오후 10:32
   **/
  public String getWeatherPoint() {
    return weatherPoint;
  }

  /**
   * 비행 계획서 리스트
   * @author FIESTA
   * @version 1.0.0
   * @since 2017-09-19 오후 10:32
   **/
  public String getFpmList() {
    return fpmList;
  }


  /**
   *  맵 버전 정보
   * @author FIESTA
   * @version 1.0.0
   * @since 2017-09-19 오후 10:32
   **/
  public String getMapVersion() {
    return mapVersion;
  }

  /**
   *  항공고시보(NOTAM) 조회
   * @author FIESTA
   * @version 1.0.0
   * @since 2017-09-19 오후 10:32
   **/
  public String getNotam() {
    return notam;
  }

  /**
  * 관측기상(Metar/Speci) 조회
  * @author FIESTA
  * @version 1.0.0
  * @since 2017-09-19 오후 10:32
  **/
  public String getWeatherMetar() {
    return weatherMetar;
  }


  /**
  * 항공Chart정보다운로드
  * @author FIESTA
  * @version 1.0.0
  * @since 2017-09-07 오후 1:16
  **/  
  public String getRequestDownload() {
    return requestDownload;
  }

  /**
   * 이용약관을 요창한다
   * @author FIESTA
   * @since  오후 11:12
   **/
  public String getRequestAgreement() {
    return requestAgreement;
  }

  /**
   * 비행계획서 제출요청 + 경로정보등록
   * @author FIESTA
   * @since  오후 11:12
   **/
  public String getWriteFlight() {
    return writeFlight;
  }

  /**
   * 회원탈퇴
   * @author FIESTA
   * @since  오후 11:12
   **/
  public String getLeave() {
    return leave;
  }


  /**
   * 내 정보 수정
   * @author FIESTA
   * @since  오후 11:12
   **/
  public String getModifyMyInfo() {
    return modifyMyInfo;
  }

  /**
   * 내 정보
   * @author FIESTA
   * @since  오후 11:12
   **/
  public String getMyInfo() {
    return myInfo;
  }


  /**
   * 회원가입
   * @author FIESTA
   * @since  오후 11:12
   **/
  public String getRegister() {
    return register;
  }

  /**
   * 소속 정보를 받아온다.
   * @author FIESTA
   * @since  오후 11:12
   **/
  public String getCompanyList() {
    return companyList;
  }

  /**
   * 유저 아이디 중복 검사
   * @author FIESTA
   * @since  오후 11:12
   **/
  public String getCheckUserId() {
    return checkUserId;
  }

  /**
   * 로그아웃
   * @author FIESTA
   * @since  오후 11:12
   **/
  public String getLogout() {
    return logout;
  }

  /**
  * 로그인
  * @author FIESTA
  * @since  오후 11:12
  **/
  public String getUserLogin() {
    return userLogin;
  }

  /**
  * 인증 번호 요청
  * @author FIESTA
  * @since  오후 11:08
  **/
  public String getRequestAuthCode() {
    return requestAuthCode;
  }

  /**
   * 인증 번호 확인 요청
   * @author FIESTA
   * @since  오후 11:08
   **/
  public String getRequestCheckAuthCode() {
    return requestCheckAuthCode;
  }

  /**
   * 아이디찾기 요청
   * @author FIESTA
   * @since  오후 11:08
   **/
  public String getFindId() {
    return findId;
  }

  /**
   * 비밀번호 찾기 요청
   * @author FIESTA
   * @since  오후 11:08
   **/
  public String getFindPwd() {
    return findPwd;
  }

  /**
   * 비밀번호수정페이지 요청
   * @author FIESTA
   * @since  오후 11:08
   **/
  public String getChangePwd() {
    return changePwd;
  }

  //정제영(암호화)
  //서버 인증서 요청
  public String getRequestServerCert() {
    return requestServerCert;
  }

  //세션키 전송
  public String getSendSesstionKey() {
    return sendSessionKey;
  }

  public static String getBaseUrl() {
    return BASE_URL;
  }

}
