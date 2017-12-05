package kr.go.molit.nhsnes.model;

/**
 * Created by yeonjukim on 2017. 4. 7..
 */

public class NhsFlightWriteModel {
    private String section;
    private String lcl;
    private String airport;
    private String eet;
    private String eta;
    private String etd;
    private String atd;
    private String ata;
    private boolean isEtdChecked;
    private boolean isEtaChecked;


    public NhsFlightWriteModel(String section, String lcl, String airport, String eet, String eta, String etd, String atd, String ata, boolean isEtaChecked,boolean isEtdChecked) {
        this.section = section;
        this.lcl = lcl;
        this.airport = airport;
        this.eet = eet;
        this.eta = eta;
        this.etd = etd;
        this.atd = atd;
        this.ata = ata;
        this.isEtaChecked = isEtaChecked;
        this.isEtdChecked = isEtdChecked;
    }

    public String getLcl() {
        return lcl;
    }

    public void setLcl(String lcl) {
        this.lcl = lcl;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public String getEet() {
        return eet;
    }

    public void setEet(String eet) {
        this.eet = eet;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }

    public String getAtd() {
        return atd;
    }

    public void setAtd(String atd) {
        this.atd = atd;
    }

    public String getAta() {
        return ata;
    }

    public void setAta(String ata) {
        this.ata = ata;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public boolean isEtdChecked() {
        return isEtdChecked;
    }

    public void setEtdChecked(boolean etdChecked) {
        isEtdChecked = etdChecked;
    }

    public boolean isEtaChecked() {
        return isEtaChecked;
    }

    public void setEtaChecked(boolean etaChecked) {
        isEtaChecked = etaChecked;
    }
}
