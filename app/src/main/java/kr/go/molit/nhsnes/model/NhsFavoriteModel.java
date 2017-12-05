package kr.go.molit.nhsnes.model;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jongrakmoon on 2017. 4. 1..
 */

public class NhsFavoriteModel extends RealmObject {

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


    public static ArrayList<NhsFavoriteModel> findAll(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NhsFavoriteModel> favoriteModels = realm.where(NhsFavoriteModel.class).findAll();

        ArrayList<NhsFavoriteModel> nhsWaypoinSearchModels = new ArrayList<NhsFavoriteModel>();
        for(int i=(favoriteModels.size() - 1) ; i >= 0 ;i--){
            nhsWaypoinSearchModels.add(favoriteModels.get(i));
        }
//        for(NhsFavoriteModel model : favoriteModels){
//            nhsWaypoinSearchModels.add(model);
//        }
        return nhsWaypoinSearchModels;
    }

    public static ArrayList<NhsFavoriteModel> addFindAll(NhsFavoriteModel data){

        ArrayList<NhsFavoriteModel> list = findAll();
        list.add(data);
        return list;
    }

    /**
    * 삭제한다.
    * @author FIESTA
    * @version 1.0.0
    * @since 2017-09-14 오후 5:29
    *
     * @param deleteModel*/
    public static void delete(final NhsMapModel deleteModel){
        Realm realm = Realm.getDefaultInstance();

        // 데이터에 대한 모든 변경은 트랜잭션에서 이루어져야 합니다
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<NhsFavoriteModel> result = realm.where(NhsFavoriteModel.class).equalTo("id",deleteModel.getId()).findAll();
                result.deleteAllFromRealm();
            }
        });

    }
}
