package kr.go.molit.nhsnes.model;

/**
 * 공용 리스트로 사용하기 위한 모델
 * Created by shin on 2017. 8. 19..
 */

public class NhsMapModel {


    public static final int TYPE_FAVORITE = 0;              // 즐겨찾기 데이터
   public static final int TYPE_SEARCH_WAY_POINT = 1;      // 좌표검색 데이터

    private int mType = TYPE_FAVORITE;

    private long id;
    private String name;

    private String startData = "";                    // 이미 선택된 지정된 출발 좌표
    private String endData = "";                      // 이미 선택된 지정된 도착 좌표
    private String routeData = "";                   // 이미 선택된 지정된 경유지 좌표

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getStartData() {
        return startData;
    }

    public void setStartData(String startData) {
        this.startData = startData;
    }

    public String getEndData() {
        return endData;
    }

    public void setEndData(String endData) {
        this.endData = endData;
    }

    public String getRouteData() {
        return routeData;
    }

    public void setRouteData(String routeData) {
        this.routeData = routeData;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }


    public void setFavoriteData(NhsFavoriteModel data){
        this.mType = TYPE_FAVORITE;
        id = data.getId();
        name = data.getName();
        startData = data.getStartData();
        endData = data.getEndData();
        routeData = data.getRouteData();
    }

    public NhsFavoriteModel getFavoriteData(){
        NhsFavoriteModel data = new NhsFavoriteModel();
        data.setId(getId());
        data.setName(getName());
        data.setStartData(getStartData());
        data.setEndData(getEndData());
        data.setRouteData(getRouteData());
        return data;
    }


    public void setSearchWayPointData(NhsSearchWayPointModel data){
        this.mType = TYPE_SEARCH_WAY_POINT;
        id = data.getId();
        name = data.getName();
        startData = data.getStartData();
        endData = data.getEndData();
        routeData = data.getRouteData();
    }

    public NhsSearchWayPointModel getSearchWayPointData(){
        NhsSearchWayPointModel data = new NhsSearchWayPointModel();
        data.setId(getId());
        data.setName(getName());
        data.setStartData(getStartData());
        data.setEndData(getEndData());
        data.setRouteData(getRouteData());
        return data;
    }
}
