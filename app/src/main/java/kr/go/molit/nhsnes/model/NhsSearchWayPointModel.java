package kr.go.molit.nhsnes.model;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * 좌표검색 저장 데이터 모델
 * Created by shin on 2017. 8. 19..
 */

public class NhsSearchWayPointModel extends RealmObject {

    @PrimaryKey
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


    public static ArrayList<NhsSearchWayPointModel> findAll(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NhsSearchWayPointModel> swModels = realm.where(NhsSearchWayPointModel.class).findAll();

        ArrayList<NhsSearchWayPointModel> nhsWaypoinSearchModels = new ArrayList<NhsSearchWayPointModel>();
        for(int i=(swModels.size()-1); i>=0; i--){
            nhsWaypoinSearchModels.add(swModels.get(i));
        }
        return nhsWaypoinSearchModels;
    }

    public static ArrayList<NhsSearchWayPointModel> addFindAll(NhsSearchWayPointModel data){

        ArrayList<NhsSearchWayPointModel> list = findAll();
        list.add(data);
        return list;
    }
}