package kr.go.molit.nhsnes.model;

import java.io.Serializable;

/**
 * Created by jongrakmoon on 2017. 4. 1..
 */

public class NhsWaypoinSearchModel extends NhsBaseModel implements Serializable {

    private long id;
    private String latitude;
    private String longitude;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
       return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
