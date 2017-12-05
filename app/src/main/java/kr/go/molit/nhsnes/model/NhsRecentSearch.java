package kr.go.molit.nhsnes.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
* 최근검색 모델
* @author FIESTA
* @since  오후 9:59
**/
public class NhsRecentSearch extends RealmObject {

  @PrimaryKey
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
