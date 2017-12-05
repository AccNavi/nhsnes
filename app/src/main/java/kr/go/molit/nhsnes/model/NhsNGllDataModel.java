package kr.go.molit.nhsnes.model;

import java.io.File;

/**
* 국토 데이터 모델
* @author FIESTA
* @version 1.0.0
* @since 2017-09-20 오전 12:54
**/
public class NhsNGllDataModel extends NhsFlightInfoModel{

    private String title = "";
    private String resultUrl = "";
    private String fileName = "";
    private String savePath = "";
    private File file = null;

    public NhsNGllDataModel(){
        super("","");
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    public String getResultUrl() {
        return resultUrl;
    }

    public void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getSavePath() {
        return savePath;
    }

    @Override
    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }


}
