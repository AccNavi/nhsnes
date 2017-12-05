package kr.go.molit.nhsnes.net.model;


/**
 * 항공 정보 조회
 * @author FIESTA
 * @version 1.0.0
 * @since 2017-09-01 오후 9:36
 **/
public class PsmHkMessage {

    private String result_code = "";    // 결과코드
    private String result_msg = "";     // 결과메시지

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


}
