package kr.go.molit.nhsnes.model;

import kr.go.molit.nhsnes.net.model.AirChartListItemModel;

/**
 * 항공 정보 모델
 *
 * @author 문종락.
 * @version 1.0, 2017.04.04 최초 작성
 **/

public class NhsFlightInfoModel extends NhsBaseModel {
    private String title;
    private String date;
    private String id;

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    private String savePath = "";
    private AirChartListItemModel airChartListItemModel;

    public NhsFlightInfoModel(){

    }

    public NhsFlightInfoModel(String title, String id) {
        this.title = title;
        this.id = id;
    }

    public NhsFlightInfoModel(String title, String date, String id) {
        this.title = title;
        this.date = date;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AirChartListItemModel getAirChartListItemModel() {
        return airChartListItemModel;
    }

    public void setAirChartListItemModel(AirChartListItemModel airChartListItemModel) {
        this.airChartListItemModel = airChartListItemModel;
    }
}
