package kr.go.molit.nhsnes.activity;

import static com.modim.lan.lanandroid.LanStorage.mNative;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_END;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_ROUTE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_START;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.KEY_MODE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.MODE_ROUTE_SEARCH;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.modim.lan.lanandroid.AirPoint;
import com.modim.lan.lanandroid.Constants;
import com.modim.lan.lanandroid.NhsLanView;

import java.io.Serializable;
import java.util.ArrayList;
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

import static com.modim.lan.lanandroid.LanStorage.mNative;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_END;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_ROUTE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_START;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.KEY_MODE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.MODE_ROUTE_SEARCH;

/**
 * 명칭 검색 화면
 *
 * @author FIESTA
 * @version 1.0.0
 * @since 오후 4:59
 **/
public class NhsCurrentSearchActivity extends NhsBaseFragmentActivity implements RecyclerWaypointAdapter.OnClickListener, OnSearchWayPointListener, OnClickOptionMapMenu, OnLongClickOptionMapMenu {

    private RecyclerView mRecyclerViewCurrentSearch;
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

                    String type = (String) StorageUtil.getStorageModeEx(NhsCurrentSearchActivity.this, FlightMarkFragment.MAP_TYPE);

                    if (type.equals("1") || type.isEmpty()) {
                        mNlvView.setMapKind(Constants.NAVI_MAP_VECTOR);
                    }

                    mNlvView.setOnClickOptionMapMenu(NhsCurrentSearchActivity.this);
                    mNlvView.setOnLongClickOptionMapMenu(NhsCurrentSearchActivity.this);
                    mNlvView.showPopup = true;

                }
            }
        }, 1000);
    }

    /**
     * 레이아웃을 설정한다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 오후 4:59
     **/
    private void setLayout() {

        Bundle data = getIntent().getExtras();

        if (data != null) {

            this.mode = data.getInt(NhsSelectPointActivity.KEY_MODE, NhsSelectPointActivity.MODE_NONE);

        }

        this.mNlvView = (NhsLanView) findViewById(R.id.nlv_view);
        this.mNlvView.showPopup = false;

        mRecyclerViewCurrentSearch = (RecyclerView) findViewById(R.id.rv_favorites);
        mRecyclerViewCurrentSearch.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerFavorites = new RecyclerWaypointAdapter(Color.WHITE, makeFakeData());
        mRecyclerFavorites.setOnClickListener(this);
        mRecyclerViewCurrentSearch.setAdapter(mRecyclerFavorites);

    }

    /**
     * 선택된 정보를 반환
     *
     * @return NhsFavoriteModel NhsFavoriteModel
     * @author FIESTA
     * @version 1.0.0
     * @since 오후 4:59
     **/
    public NhsFavoriteModel getSelectedModel() {
        return selectedModel;
    }


    /**
     * 확정된 경로를 반환한다.
     *
     * @param type  경로 type (DialogSearchWaypoint 상수 참조 : ex) TYPE_NONE)
     * @param model NhsWaypoinSearchModel 데이터
     * @author FIESTA
     * @since 오후 9:03
     **/
    @Override
    public void onComplate(int type, Object model) {

    }

    /**
     * 완려되며 호출된다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 오후 5:01
     **/
    @Override
    public void onComplate() {

        Intent intent = new Intent();
        intent.putExtra(NhsSelectPointActivity.DATA, (Serializable) mRecyclerFavorites.getSelectedListObj());
        intent.putExtra(NhsSelectPointActivity.KEY_MODE, mode);
        setResult(RESULT_OK, intent);
        finish();


    }

    /**
     * 취소되면 호출된다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 오후 5:01
     **/
    @Override
    public void onCancel() {


//        mRecyclerFavorites.remoteAllSelectedList();

    }

    /**
     * 다음을 선택하면 호출된다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 오후 5:01
     **/
    @Override
    public void onNextSelect() {

    }

    /**
     * 가상 데이터를 만든다.
     *
     * @return makeFakeData 가상 리스트
     * @author FIESTA
     * @version 1.0.0
     * @since 오후 5:01
     **/
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
//      model.setLatitude(favoriteModels.get(i).getLatitude()+"");
//      model.setLongitude(favoriteModels.get(i).getLongitude()+"");
            model.setName(favoriteModels.get(i).getName());
            nhsWaypoinSearchModels.add(model);
        }

        return nhsWaypoinSearchModels;
    }

    /**
     * 삭제 선택시 호출된다.
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 오후 5:02
     **/
    @Override
    public void onDelete() {

        if (this.mode == NhsSelectPointActivity.MODE_ROUTE) {


            Realm realm = Realm.getDefaultInstance();

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    LinkedList<NhsWaypoinSearchModel> list = mRecyclerFavorites.getSelectedList();
                    int i = 0;
                    int size = list.size();

                    NhsWaypoinSearchModel nhsWaypoinSearchModel = null;
                    RealmResults<NhsFavoriteModel> searchList = null;

                    for (i = 0; i < size; i++) {

                        nhsWaypoinSearchModel = list.get(i);
                        searchList = realm.where(NhsFavoriteModel.class).equalTo("id", nhsWaypoinSearchModel.getId()).findAll();
                        searchList.deleteAllFromRealm();

                    }


                }


            }, new Realm.Transaction.OnSuccess() {

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
            }, new Realm.Transaction.OnSuccess() {

                @Override
                public void onSuccess() {
                    updateList();
                }

            });


        }


    }

    /**
     * 리스트 업데이트
     *
     * @author FIESTA
     * @since 오전 12:23
     **/
    private void updateList() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 재설정
                mRecyclerFavorites = new RecyclerWaypointAdapter(Color.WHITE, makeFakeData());
                mRecyclerFavorites.setOnClickListener(NhsCurrentSearchActivity.this);
                mRecyclerViewCurrentSearch.setAdapter(mRecyclerFavorites);
                mRecyclerFavorites.notifyDataSetChanged();
            }
        });
    }

    /**
     * 정보를 클릭하면 호출된다
     *
     * @param model 데이터
     * @author FIESTA
     * @version 1.0.0
     * @since 오후 5:02
     **/
    @Override
    public void onClick(NhsWaypoinSearchModel model) {

        if (this.mode == MODE_ROUTE_SEARCH) {

            if (this.startData.isEmpty()) {
                mNative.lanSetRoutePosition(Constants.NAVI_SETPOSITION_START, Util.convertValue(model.getLongitude()), Util.convertValue(model.getLatitude()), "start name", 0);
                this.startData = model.getLatitude() + " " + model.getLongitude();
            } else if (this.endData.isEmpty()) {
                mNative.lanSetRoutePosition(Constants.NAVI_SETPOSITION_GOAL, Util.convertValue(model.getLongitude()), Util.convertValue(model.getLatitude()), "goal name", 0);
                this.endData = model.getLatitude() + " " + model.getLongitude();
            } else {
                mNative.lanSetRoutePosition(Constants.NAVI_SETPOSITION_WAYPOINT, Util.convertValue(model.getLongitude()), Util.convertValue(model.getLatitude()), "waypoint name", 0);
                this.routeData = (model.getLatitude() + " " + model.getLongitude()) + "\n";
            }

            try {
//                mNative.lanExecuteRP();
            } catch (Exception ex) {

            }

        } else {

            DialogFavorites dialog = new DialogFavorites(NhsCurrentSearchActivity.this, this.mode);
            dialog.setOnSearchWayPointListener(this);
            dialog.show();

        }
    }

    /**
     * 맵을 선택하면 호출된다.
     *
     * @param curPos 맵에 선택된 정보
     * @author FIESTA
     * @version 1.0.0
     * @since 오후 5:03
     **/
    @Override
    public void onClick(AirPoint curPos) {

        this.dialogAddRoute = new DialogAddRoute(NhsCurrentSearchActivity.this,
                this.mode,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        switch (view.getId()) {

                            case R.id.btn_add:
                                dialogAddRoute.dismiss();
                                break;
                            case R.id.btn_complate:

                                Intent searchMap = new Intent(NhsCurrentSearchActivity.this, NhsMapSearchActivity.class);
                                searchMap.putExtra(KEY_MODE, NhsSelectPointActivity.MODE_SEARCH_IN_ROUTE_SEARCH);
                                searchMap.putExtra(DATA_START, startData);
                                searchMap.putExtra(DATA_END, endData);
                                searchMap.putExtra(DATA_ROUTE, routeData);
                                NhsCurrentSearchActivity.this.startActivity(searchMap);

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

    /**
     * 시작지점 클릭시 호출된다
     *
     * @param curPos 맵에 선택된 정보
     * @author FIESTA
     * @version 1.0.0
     * @since 오후 5:03
     **/
    @Override
    public void setStart(AirPoint curPos) {

    }

    /**
     * 도착지점 클릭시 호출된다
     *
     * @param curPos 맵에 선택된 정보
     * @author FIESTA
     * @version 1.0.0
     * @since 오후 5:03
     **/
    @Override
    public void setGoal(AirPoint curPos) {

    }

    /**
     * 경유지지점 클릭시 호출된다
     *
     * @param curPos 맵에 선택된 정보
     * @author FIESTA
     * @version 1.0.0
     * @since 오후 5:03
     **/
    @Override
    public void setWaypoint(AirPoint curPos) {

    }

    /**
     * 찾기 클릭시 호출된다
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 오후 5:03
     **/
    @Override
    public void OnSearch() {

    }

    /**
     * 초기화 클릭시 호출된다
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 오후 5:03
     **/
    @Override
    public void OnReset() {

    }

    /**
     * 길게 클릭시 호출된다
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 오후 5:03
     **/
    @Override
    public void onLongClick(AirPoint curPos) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNlvView.clearRoutePosition();
    }
}
