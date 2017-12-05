package kr.go.molit.nhsnes.model;

import java.io.File;

import kr.go.molit.nhsnes.net.model.AirChartListItemModel;

/**
* 항공 조회
* @author FIESTA
* @version 1.0.0
* @since 2017-09-08 오후 1:27
**/
public class NhsAirListModel extends NhsFlightInfoModel {

    private String title = "";
    private String resultUrl = "";
    private String dataVer = "";
    private String fileName = "";
    private String savePath = "";
    private File file = null;

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResultUrl() {
        return resultUrl;
    }

    public void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl;
    }

    public String getDataVer() {
        return dataVer;
    }

    public void setDataVer(String dataVer) {
        this.dataVer = dataVer;
    }

}
