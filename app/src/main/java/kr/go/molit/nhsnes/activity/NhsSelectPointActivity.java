package kr.go.molit.nhsnes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.Serializable;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.dialog.DialogSearchWaypoint;
import kr.go.molit.nhsnes.interfaces.OnSearchWayPointListener;
import kr.go.molit.nhsnes.widget.ActionBarEx;
import kr.go.molit.nhsnes.widget.TextViewEx;


/**
 * 출발지/목적지 선택 화면
 *
 * @author FIESTA
 * @since 오전 12:29
 **/
public class NhsSelectPointActivity extends NhsBaseFragmentActivity implements View.OnClickListener, OnSearchWayPointListener {

  public final static String DATA = "DATA";
  public final static String DATA_START = "DATA_START";
  public final static String DATA_END = "DATA_END";
  public final static String DATA_ROUTE = "DATA_ROUTE";
  public final static String SHOW_POPUP = "SHOW_POPUP";
  public final static String KEY_MODE = "KEY_MODE";
  public final static int MODE_DEPARTURE = 0;        // 출발지
  public final static int MODE_ARRIVAL = 1;          // 도착지
  public final static int MODE_ROUTE = 2;          // 경로
  public final static int MODE_ROUTE_SEARCH = 3; // 경로 탐색
  public final static int MODE_SEARCH_IN_ROUTE_SEARCH = 3; // 경로 탐색 중 좌표 검색
  public final static int MODE_NONE = 4; //
  public final static int MODE_SIMULATION = 5; // 모의비행
  public final static int MODE_MAP = 0;        // 맵검색

  //public final static int MODE_HISTORY_MAP = 5;

  private View mLayoutSearchPoint;
  private View mLayoutSearchLocationName;
  private View mLayoutFavorites;
  private View mLayoutCurrentSearch;
  private View mLayoutRouting;
  private ActionBarEx mAbeTitle;
  private int mode = MODE_DEPARTURE;              // 출발/도착 플래그
  private String startData = "";                    // 이미 선택된 지정된 출발 좌표
  private String endData = "";                      // 이미 선택된 지정된 도착 좌표
  private String routeData = "";                   // 이미 선택된 지정된 경유지 좌표

  private DialogSearchWaypoint mDialogSearchWaypoint;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_select_point);
    getMode();
    setLayout();
  }

  /**
   * 출발지인지, 도착지인지 구분하는 플래그를 받는다
   *
   * @author FIESTA
   * @since 오전 12:44
   **/
  private void getMode() {

    Bundle data = getIntent().getExtras();

    if (data != null) {

      mode = data.getInt(KEY_MODE, MODE_DEPARTURE);
      startData = data.getString(DATA_START, "");
      endData = data.getString(DATA_END, "");
      routeData = data.getString(DATA_ROUTE, "");
    }

  }

  private void setLayout() {

    mLayoutSearchPoint = findViewById(R.id.layout_search_point);
    mLayoutSearchLocationName = findViewById(R.id.layout_search_location_name);
    mLayoutFavorites = findViewById(R.id.layout_favorites);
    mLayoutCurrentSearch = findViewById(R.id.layout_current_search);
    mLayoutRouting = findViewById(R.id.layout_routing_point);
    mAbeTitle = (ActionBarEx) findViewById(R.id.abe_title);
    TextViewEx tvRoute = (TextViewEx) findViewById(R.id.tv_routing);

    mLayoutSearchPoint.setOnClickListener(this);
    mLayoutSearchLocationName.setOnClickListener(this);
    mLayoutFavorites.setOnClickListener(this);
    mLayoutCurrentSearch.setOnClickListener(this);
    mLayoutRouting.setOnClickListener(this);

    switch (mode) {

      case MODE_DEPARTURE:
//        mAbeTitle.setTitleText("출발지 선택");
        mAbeTitle.setTitleText("경로탐색");
        mLayoutRouting.setVisibility(View.VISIBLE);
        break;

      case MODE_ARRIVAL:
//        mAbeTitle.setTitleText("목적지 선택");
        mAbeTitle.setTitleText("경로탐색");
        mLayoutRouting.setVisibility(View.VISIBLE);
        break;

      case MODE_ROUTE:
        mAbeTitle.setTitleText("경로 탐색");
        mLayoutRouting.setVisibility(View.VISIBLE);
        break;

      case MODE_ROUTE_SEARCH:
        mAbeTitle.setTitleText("경로 탐색");
        mLayoutRouting.setVisibility(View.VISIBLE);
        break;

      default:
        mLayoutRouting.setVisibility(View.VISIBLE);
        break;

    }

  }

  @Override
  public void onClick(View v) {
    Intent intent;
    switch (v.getId()) {

      case R.id.layout_routing_point:

        intent = new Intent(this, NhsMapSearchActivity.class);
        intent.putExtra(KEY_MODE, mode);
        intent.putExtra(DATA_START, startData);
        intent.putExtra(DATA_END, endData);
        intent.putExtra(DATA_ROUTE, routeData);
        startActivityForResult(intent, 0);

        break;

      case R.id.layout_search_point:

        this.mDialogSearchWaypoint = new DialogSearchWaypoint(this, mode,  startData, endData, routeData, this);
        this.mDialogSearchWaypoint.show();

        break;

      case R.id.layout_search_location_name:
        intent = new Intent(this, NhsDestinationSearchActivity.class);
        intent.putExtra(KEY_MODE, mode);
        intent.putExtra(DATA_START, startData);
        intent.putExtra(DATA_END, endData);
        intent.putExtra(DATA_ROUTE, routeData);
        startActivityForResult(intent, 0);
        break;
      case R.id.layout_favorites:

        intent = new Intent(this, NhsMapSearchActivity.class);
        intent.putExtra(KEY_MODE, mode);
        intent.putExtra(NhsMapSearchActivity.MAP_TYPE, NhsMapSearchActivity.TYPE_FAVORITE);
        startActivityForResult(intent, 0);

        break;
      case R.id.layout_current_search:
//        intent = new Intent(this, NhsCurrentSearchActivity.class);
//        intent.putExtra(KEY_MODE, mode);
//        intent.putExtra(DATA_START, startData);
//        intent.putExtra(DATA_END, endData);
//        intent.putExtra(DATA_ROUTE, routeData);
//        startActivityForResult(intent, 0);

        intent = new Intent(this, NhsMapSearchActivity.class);
        intent.putExtra(KEY_MODE, mode);
        intent.putExtra(NhsMapSearchActivity.MAP_TYPE, NhsMapSearchActivity.TYPE_SEARCH_RECENT);
        startActivityForResult(intent, 0);
        break;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if ((requestCode == MODE_ARRIVAL || requestCode == MODE_DEPARTURE) && resultCode == RESULT_OK) {

      if (this.mDialogSearchWaypoint != null) {
        this.mDialogSearchWaypoint.dismiss();
      }

      data.putExtra(KEY_MODE, mode);
      setResult(RESULT_OK, data);
      finish();

    } else if (resultCode == RESULT_OK) {

      if (this.mDialogSearchWaypoint != null) {
        if (this.mDialogSearchWaypoint.isShowing()){
          this.mDialogSearchWaypoint.dismiss();
        }
      }

      setResult(RESULT_OK, data);
      finish();

    } else if (resultCode == RESULT_CANCELED) {

//      setResult(RESULT_CANCELED, data);
//      finish();

    }

  }

  /**
   * 데이터를 전달한다
   *
   * @author FIESTA
   * @since 오후 9:33
   **/
  @Override
  public void onComplate(int type, Object model) {
    Intent intent = new Intent();
    intent.putExtra(DATA, (Serializable) model);
    intent.putExtra(KEY_MODE, mode);
    setResult(RESULT_OK, intent);
    finish();
  }

  @Override
  public void onComplate() {

  }

  @Override
  public void onCancel() {

  }

  @Override
  public void onNextSelect() {

  }

  @Override
  public void onDelete() {

  }

}
