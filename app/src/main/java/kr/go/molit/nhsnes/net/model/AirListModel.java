package kr.go.molit.nhsnes.net.model;

/**
* 항공 정보 조회
* @author FIESTA
* @version 1.0.0
* @since 2017-09-01 오후 9:36
**/
public class AirListModel {

    private String result_code;
    private String result_msg;
    private String result_url;

    public String getResult_data() {
        return result_data;
    }

    public void setResult_data(String result_data) {
        this.result_data = result_data;
    }

    private String result_data;
    private String data_ver;

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

    public String getResult_url() {
        return result_url;
    }

    public void setResult_url(String result_url) {
        this.result_url = result_url;
    }

    public String getData_ver() {
        return data_ver;
    }

    public void setData_ver(String data_ver) {
        this.data_ver = data_ver;
    }


}
