package kr.go.molit.nhsnes.model;

import java.io.Serializable;

/**
* 항공 route
* @author FIESTA
* @version 1.0.0
* @since 2017-09-18 오후 3:50
**/
public class FlightRouteModel implements Serializable{

    public String stepNum = "";
    private String areaId = "";
    private String areaNm = "";
    private String lat = "";
    private String lon = "";
    private String elev = "";
    private String heading = "";
    private String rType = "";

    public String getStepNum() {
        return stepNum;
    }

    public void setStepNum(String stepNum) {
        this.stepNum = stepNum;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaNm() {
        return areaNm;
    }

    public void setAreaNm(String areaNm) {
        this.areaNm = areaNm;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getElev() {
        return elev;
    }

    public void setElev(String elev) {
        this.elev = elev;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }


    public String getrType() {
        return rType;
    }

    public void setrType(String rType) {
        this.rType = rType;
    }
}
