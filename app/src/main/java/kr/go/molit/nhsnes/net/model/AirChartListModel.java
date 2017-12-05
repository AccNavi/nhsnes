package kr.go.molit.nhsnes.net.model;

import java.util.ArrayList;

/**
* 항공Chart정보조회
* @author FIESTA
* @version 1.0.0
* @since 2017-09-01 오후 9:36
**/
public class AirChartListModel {

    private String result_code;
    private ArrayList<AirChartListItemModel> result_data;
    private String result_msg;

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

    public ArrayList<AirChartListItemModel> getResult_data() {
        return result_data;
    }

    public void setResult_data(ArrayList<AirChartListItemModel> result_data) {
        this.result_data = result_data;
    }

}
