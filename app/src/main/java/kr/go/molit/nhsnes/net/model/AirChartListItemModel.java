package kr.go.molit.nhsnes.net.model;


/**
* 항공Chart정보조회 아이템
* @author FIESTA
* @version 1.0.0
* @since 2017-09-01 오후 9:40
**/
public class AirChartListItemModel {

    private long AIRCHART_SN;            // 항공Chart 정보 일련번호
    private String AIRCHART_GB;            // 항공Chart 종류
    private String AIRCHART_GB_DETAIL;     // 항공Chart 종류 상세
    private String AIRCHART_VER;           // 항공Chart 버전
    private long FILE_SN;                // 항공Chart 파일 일련번호
    private String FILE_ORIGIN_NM;         // 파일의 물리적 명칭
    private String FILE_NM;                // 파일의 실제 명칭

    public long getAIRCHART_SN() {
        return AIRCHART_SN;
    }

    public void setAIRCHART_SN(long AIRCHART_SN) {
        this.AIRCHART_SN = AIRCHART_SN;
    }

    public String getAIRCHART_GB() {
        return AIRCHART_GB;
    }

    public void setAIRCHART_GB(String AIRCHART_GB) {
        this.AIRCHART_GB = AIRCHART_GB;
    }

    public String getAIRCHART_GB_DETAIL() {
        return AIRCHART_GB_DETAIL;
    }

    public void setAIRCHART_GB_DETAIL(String AIRCHART_GB_DETAIL) {
        this.AIRCHART_GB_DETAIL = AIRCHART_GB_DETAIL;
    }

    public String getAIRCHART_VER() {
        return AIRCHART_VER;
    }

    public void setAIRCHART_VER(String AIRCHART_VER) {
        this.AIRCHART_VER = AIRCHART_VER;
    }

    public long getFILE_SN() {
        return FILE_SN;
    }

    public void setFILE_SN(long FILE_SN) {
        this.FILE_SN = FILE_SN;
    }

    public String getFILE_ORIGIN_NM() {
        return FILE_ORIGIN_NM;
    }

    public void setFILE_ORIGIN_NM(String FILE_ORIGIN_NM) {
        this.FILE_ORIGIN_NM = FILE_ORIGIN_NM;
    }

    public String getFILE_NM() {
        return FILE_NM;
    }

    public void setFILE_NM(String FILE_NM) {
        this.FILE_NM = FILE_NM;
    }


}
