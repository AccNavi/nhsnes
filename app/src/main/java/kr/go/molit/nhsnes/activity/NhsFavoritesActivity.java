package kr.go.molit.nhsnes.activity;

import static com.modim.lan.lanandroid.LanStorage.mNative;
import static com.modim.lan.lanandroid.NativeImplement.lanSetDisplayLayer;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_END;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_ROUTE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_START;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.KEY_MODE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.MODE_ROUTE_SEARCH;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.MODE_SEARCH_IN_ROUTE_SEARCH;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.AIRCRAFT_POSITIONING_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.AIRSPACE_INFORMATION_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.EXPRESSWAY_DISPLAY_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.IS_LAYOUT_NW;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.LANDING_INFORMATION_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.MOUNTAIN_INFORMATION_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.NOTAM_INFO_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.OBSTACLE_INFORMATION_LAYER;
import static kr.go.molit.nhsnes.fragment.FlightMarkFragment.RIVER_INFORMATION_LAYER;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.modim.lan.lanandroid.AirPoint;
import com.modim.lan.lanandroid.Constants;
import com.modim.lan.lanandroid.NhsLanView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.adapter.RecyclerWaypointAdapter;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.dialog.DialogAddRoute;
import kr.go.molit.nhsnes.dialog.DialogFavorites;
import kr.go.molit.nhsnes.fragment.FlightMarkFragment;
import kr.go.molit.nhsnes.interfaces.OnClickOptionMapMenu;
import kr.go.molit.nhsnes.interfaces.OnLongClickOptionMapMenu;
import kr.go.molit.nhsnes.interfaces.OnSearchWayPointListener;
import kr.go.molit.nhsnes.model.NhsFavoriteModel;
import kr.go.molit.nhsnes.model.NhsWaypoinSearchModel;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class NhsFavoritesActivity extends NhsBaseFragmentActivity implements RecyclerWaypointAdapter.OnClickListener, OnSearchWayPointListener, OnClickOptionMapMenu, OnLongClickOptionMapMenu {

    private RecyclerView mRecyclerViewFavorites;
    private RecyclerWaypointAdapter mRecyclerFavorites;

    private int mode = NhsSelectPointActivity.MODE_NONE;
    private NhsFavoriteModel selectedModel = null;
    private LinkedList<NhsWaypoinSearchModel> selectedList = new LinkedList<>();
    private NhsLanView mNlvView = null;

    private String startData = "";
    private String endData = "";
    private String routeData = "";

    private DialogAddRoute dialogAddRoute = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_favorites);

        setLayout();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (mNlvView != null) {

                    String type = (String) StorageUtil.getStorageModeEx(NhsFavoritesActivity.this, FlightMarkFragment.MAP_TYPE);

                    if (type.equals("1") || type.isEmpty()) {
                        mNlvView.setMapKind(Constants.NAVI_MAP_VECTOR);
                    }

                    setMapLayout();

                    mNlvView.setOnClickOptionMapMenu(NhsFavoritesActivity.this);
                    mNlvView.setOnLongClickOptionMapMenu(NhsFavoritesActivity.this);
                    mNlvView.showPopup = true;

                }
            }
        }, 1000);
    }

    private void setLayout() {

        Bundle data = getIntent().getExtras();

        if (data != null) {

            this.mode = data.getInt(NhsSelectPointActivity.KEY_MODE, NhsSelectPointActivity.MODE_NONE);

        }

        this.mNlvView = (NhsLanView) findViewById(R.id.nlv_view);
        this.mNlvView.showPopup = false;

        mRecyclerViewFavorites = (RecyclerView) findViewById(R.id.rv_favorites);
        mRecyclerViewFavorites.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerFavorites = new RecyclerWaypointAdapter(Color.WHITE, makeFakeData());
        mRecyclerFavorites.setOnClickListener(this);
        mRecyclerViewFavorites.setAdapter(mRecyclerFavorites);

    }

    public NhsFavoriteModel getSelectedModel() {
        return selectedModel;
    }


    @Override
    public void onComplate(int type, Object model) {

    }

    @Override
    public void onComplate() {

        Intent intent = new Intent();
        intent.putExtra(NhsSelectPointActivity.DATA, (Serializable) mRecyclerFavorites.getSelectedListObj());
        intent.putExtra(NhsSelectPointActivity.KEY_MODE, mode);
        setResult(RESULT_OK, intent);
        finish();



    }

    @Override
    public void onCancel() {


//        mRecyclerFavorites.remoteAllSelectedList();

    }

    @Override
    public void onNextSelect() {

    }

    private List<NhsWaypoinSearchModel> makeFakeData() {

        Realm realm = Realm.getDefaultInstance();
        RealmResults<NhsFavoriteModel> favoriteModels = realm.where(NhsFavoriteModel.class).findAll();

        List<NhsWaypoinSearchModel> nhsWaypoinSearchModels = new ArrayList<>();

        int i = 0;
        int size = favoriteModels.size();

        for (i = 0; i < size; i++) {
            NhsWaypoinSearchModel model = new NhsWaypoinSearchModel();
//            model.setName("즐겨찾기" + i);
            model.setId(favoriteModels.get(i).getId());
            favoriteModels.get(i).getName();
//            model.setLatitude(favoriteModels.get(i).getLatitude()+"");
//            model.setLongitude(favoriteModels.get(i).getLongitude()+"");
            model.setName(favoriteModels.get(i).getName());
            nhsWaypoinSearchModels.add(model);
        }

        return nhsWaypoinSearchModels;
    }

    @Override
    public void onDelete() {

        if (this.mode == NhsSelectPointActivity.MODE_ROUTE){


            Realm realm = Realm.getDefaultInstance();

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    LinkedList<NhsWaypoinSearchModel> list = mRecyclerFavorites.getSelectedList();
                    int i = 0;
                    int size = list.size();

                    NhsWaypoinSearchModel nhsWaypoinSearchModel = null;
                    RealmResults<NhsFavoriteModel> searchList = null;

                    for (i=0; i<size; i++) {

                        nhsWaypoinSearchModel = list.get(i);
                        searchList = realm.where(NhsFavoriteModel.class).equalTo("id", nhsWaypoinSearchModel.getId()).findAll();
                        searchList.deleteAllFromRealm();

                    }


                }


            }, new Realm.Transaction.OnSuccess(){

                @Override
                public void onSuccess() {
                    updateList();
                }
            });


        } else {

            Realm realm = Realm.getDefaultInstance();

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    NhsWaypoinSearchModel nhsWaypoinSearchModel = mRecyclerFavorites.getSelectedList().get(mRecyclerFavorites.getSelectedList().size() - 1);
                    RealmResults<NhsFavoriteModel> searchList = realm.where(NhsFavoriteModel.class).equalTo("id", nhsWaypoinSearchModel.getId()).findAll();
                    searchList.deleteAllFromRealm();

                    updateList();

                }
            }, new Realm.Transaction.OnSuccess(){

                @Override
                public void onSuccess() {
                    updateList();
                }

            });


        }



    }

    /**
    * 리스트 업데이트
    * @author FIESTA
    * @since  오전 12:23
    **/
    private void updateList(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 재설정
                mRecyclerFavorites = new RecyclerWaypointAdapter(Color.WHITE, makeFakeData());
                mRecyclerFavorites.setOnClickListener(NhsFavoritesActivity.this);
                mRecyclerViewFavorites.setAdapter(mRecyclerFavorites);
                mRecyclerFavorites.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(NhsWaypoinSearchModel model) {

        if (this.mode == MODE_ROUTE_SEARCH) {
            Log.d("JeLib","------------------------"+model.getLatitude()+" y:"+model.getLongitude());
            if (this.startData.isEmpty()) {

                int result = mNlvView.setRoutePosition(NhsFavoritesActivity.this, Constants.NAVI_SETPOSITION_START, Util.convertValue(model.getLongitude()),  Util.convertValue(model.getLatitude()),"start name", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                if (result == 0) {
                    this.startData = model.getLatitude() + " " + model.getLongitude();
                }
            } else if (this.endData.isEmpty()) {

                int result = mNlvView.setRoutePosition(NhsFavoritesActivity.this, Constants.NAVI_SETPOSITION_GOAL, Util.convertValue(model.getLongitude()),  Util.convertValue(model.getLatitude()),"goal name", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                if (result == 0) {
                    this.endData = model.getLatitude() + " " + model.getLongitude();
                }

            } else {

                int result = mNlvView.setRoutePosition(NhsFavoritesActivity.this, Constants.NAVI_SETPOSITION_WAYPOINT, Util.convertValue(model.getLongitude()),  Util.convertValue(model.getLatitude()),"waypoint name", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                if (result == 0) {
                    this.routeData = (model.getLatitude() + " " + model.getLongitude()) + "\n";
                }

            }

            try {
//                mNative.lanExecuteRP();
            }catch (Exception ex) {

            }

        } else {

            DialogFavorites dialog = new DialogFavorites(NhsFavoritesActivity.this, this.mode);
            dialog.setOnSearchWayPointListener(this);
            dialog.show();

        }
    }

    @Override
    public void onClick(AirPoint curPos) {

        this.dialogAddRoute = new DialogAddRoute(NhsFavoritesActivity.this,
            this.mode,
            new View.OnClickListener(){

                @Override
                public void onClick(View view) {

                    switch (view.getId()) {

                        case R.id.btn_add:
                            dialogAddRoute.dismiss();
                            break;
                        case R.id.btn_complate:

                            Intent searchMap = new Intent(NhsFavoritesActivity.this, NhsMapSearchActivity.class);
                            searchMap.putExtra(KEY_MODE, NhsSelectPointActivity.MODE_SEARCH_IN_ROUTE_SEARCH);
                            searchMap.putExtra(DATA_START, startData);
                            searchMap.putExtra(DATA_END, endData);
                            searchMap.putExtra(DATA_ROUTE, routeData);
                            NhsFavoritesActivity.this.startActivity(searchMap);

                            dialogAddRoute.dismiss();
                            break;
                        case R.id.btn_cancel:
                            dialogAddRoute.dismiss();
                            break;



                    }

                }
            });
        this.dialogAddRoute.show();

    }

    @Override
    public void setStart(AirPoint curPos) {

    }

    @Override
    public void setGoal(AirPoint curPos) {

    }

    @Override
    public void setWaypoint(AirPoint curPos) {

    }

    @Override
    public void OnSearch() {

    }

    @Override
    public void OnReset() {

    }

    @Override
    public void onLongClick(AirPoint curPos) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNlvView.clearRoutePosition();
    }

    /**
     * 맵 설정 레이어에 맞게 설정한다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-09-18 오후 3:40
     **/
    private void setMapLayout() {

        Integer[] bitData = new Integer[32];
        int i = 0;

        //초기화
        for (i = 0; i < 32; i++) {
            bitData[i] = 1;
        }

        // 가상 정보 레이어
        bitData[24] = (StorageUtil.getStorageMode(getContext(), IS_LAYOUT_NW) ? 1: 0);

        // NOTAM 정보 레이어
        bitData[26] = (StorageUtil.getStorageMode(getContext(), NOTAM_INFO_LAYER) ? 1 : 0);

        // 항공기 위치 표시 레이어
        bitData[25] = (StorageUtil.getStorageMode(getContext(), AIRCRAFT_POSITIONING_LAYER) ? 1 : 0);


        // 공역_정보_레이어
        bitData[24] = (StorageUtil.getStorageMode(getContext(), AIRSPACE_INFORMATION_LAYER) ? 1 : 0);

        // 장애물_정보_레이어
        bitData[7] = (StorageUtil.getStorageMode(getContext(), OBSTACLE_INFORMATION_LAYER) ? 1 : 0);


        // 이착륙장_정보_레이어
        bitData[6] = (StorageUtil.getStorageMode(getContext(), LANDING_INFORMATION_LAYER) ? 1 : 0);


        // 고속도로_표시_레이어
        bitData[1] = (StorageUtil.getStorageMode(getContext(), EXPRESSWAY_DISPLAY_LAYER) ? 1 : 0);


        // 강 정보 레이어
        bitData[2] = (StorageUtil.getStorageMode(getContext(), RIVER_INFORMATION_LAYER) ? 1 : 0);

        // 산악_정보_레이어
        bitData[3] = (StorageUtil.getStorageMode(getContext(), MOUNTAIN_INFORMATION_LAYER) ? 1 : 0);

        // 지도 레이어는 항상 1 이다
        bitData[0] = 1;

        // 배열을 리스트로 변환
        List<Integer> list = Arrays.asList(bitData);

        // 리스트 뒤집어 주기
        Collections.reverse(list);

        // 리스트를 배열로 다시 변환
        bitData = list.toArray(new Integer[list.size()]);

        byte[] convertByte = Util.encodeToByteArray(bitData);
        int convertInt = Util.byteToint(convertByte);
        lanSetDisplayLayer(convertInt);

    }
}
