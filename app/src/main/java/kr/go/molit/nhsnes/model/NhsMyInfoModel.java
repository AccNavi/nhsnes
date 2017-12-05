package kr.go.molit.nhsnes.model;

/**
* 내 정보
* @author FIESTA
* @since  오후 1:33
**/
public class NhsMyInfoModel {

  private String mbrId = "";      // 회원일련번호(key 값)
  private String mbrNm = "";      // 회원명
  private String afftId = "";     // 소속ID
  private String hpno = "";       // 핸드폰번호
  private String telno = "";      // 전화번호
  private String email = "";      // 이메일
  private String zipCode = "";    // 우편번호
  private String address = "";    // 주소
  private String birday = "";     // 생년월일
  private String loginPw = "";    // 비밀번호
  private String mbrGrade = "";   // 직위
  private String acrftCd = "";    // 등록기호
  private String afftNm = "";

  public String getAfftNm() {
    return afftNm;
  }

  public void setAfftNm(String afftNm) {
    this.afftNm = afftNm;
  }

  public String getMbrId() {
    return mbrId;
  }

  public void setMbrId(String mbrId) {
    this.mbrId = mbrId;
  }

  public String getMbrNm() {
    return mbrNm;
  }

  public void setMbrNm(String mbrNm) {
    this.mbrNm = mbrNm;
  }

  public String getAfftId() {
    return afftId;
  }

  public void setAfftId(String afftId) {
    this.afftId = afftId;
  }

  public String getHpno() {
    return hpno;
  }

  public void setHpno(String hpno) {
    this.hpno = hpno;
  }

  public String getTelno() {
    return telno;
  }

  public void setTelno(String telno) {
    this.telno = telno;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getBirday() {
    return birday;
  }

  public void setBirday(String birday) {
    this.birday = birday;
  }

  public String getLoginPw() {
    return loginPw;
  }

  public void setLoginPw(String loginPw) {
    this.loginPw = loginPw;
  }

  public String getMbrGrade() {
    return mbrGrade;
  }

  public void setMbrGrade(String mbrGrade) {
    this.mbrGrade = mbrGrade;
  }

  public String getAcrftCd() {
    return acrftCd;
  }

  public void setAcrftCd(String acrftCd) {
    this.acrftCd = acrftCd;
  }
}
