package kr.go.molit.nhsnes.model;

/**
 * Created by jongrakmoon on 2017. 4. 4..
 */

public class NhsDestinationSearchModel extends NhsBaseModel {

    private String destinationName;
    private long date;
    private String destinationDetail;
    private String latitude;
    private String longitude;

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

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDestinationDetail() {
        return destinationDetail;
    }

    public void setDestinationDetail(String destinationDetail) {
        this.destinationDetail = destinationDetail;
    }

}
