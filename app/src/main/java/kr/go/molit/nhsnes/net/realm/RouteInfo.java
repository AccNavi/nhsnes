package kr.go.molit.nhsnes.net.realm;

import io.realm.RealmObject;

/**
 * Created by user on 2017-08-18.
 */

public class RouteInfo extends RealmObject {
    private String stepNum;
    private String areaId;
    private String areaNm;
    private String lat;
    private String lon;
    private String elev;
    private String heading;

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
}
