package kr.go.molit.nhsnes.model;


/**
* 기사 정보 모델
* @author FIESTA
* @version 1.0.0
* @since 2017-09-19 오후 10:19
**/
public class NhsFlightWeatherMetarModel extends NhsFlightInfoModel  {

    private String fileName = "";
    private String downloadUrl = "";
    private String savePath = "";

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }


}
