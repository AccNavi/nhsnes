package kr.go.molit.nhsnes.model;

import java.io.File;

/**
* 항공 차트 리스트 삭제 모델
* @author FIESTA
* @version 1.0.0
* @since 2017-09-07 오후 5:09
**/
public class NhsFlightDeleteModel extends NhsFlightInfoModel{

    private String fileName = "";
    private String date = "";
    private File file = null;

    public NhsFlightDeleteModel(){
        super("","");
    }
    public NhsFlightDeleteModel(String title, String id) {
        super(title, id);
    }

    public NhsFlightDeleteModel(String title, String date, String id) {
        super(title, date, id);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }


}
