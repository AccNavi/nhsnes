package kr.go.molit.nhsnes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.modim.lan.lanandroid.Constants;
import com.modim.lan.lanandroid.NhsLanView;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.dialog.DialogConfirmSelectMap;
import kr.go.molit.nhsnes.fragment.FlightMarkFragment;
import kr.go.molit.nhsnes.widget.TextViewEx;

import static com.modim.lan.lanandroid.LanStorage.mNative;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_END;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_ROUTE;
import static kr.go.molit.nhsnes.activity.NhsSelectPointActivity.DATA_START;


/**
 * 출발지/도착지 확정 화면
 * @author FIESTA
 * @since  오전 1:39
 **/
public class NhsMapSearchWaypointActivity extends NhsBaseFragmentActivity {

  private int mode = NhsSelectPointActivity.MODE_DEPARTURE;              // 출발/도착 플래그
  private NhsLanView mNlvView = null;
  private DialogConfirmSelectMap mDialogConfirmSelectMap = null;      // 경로 확정 다이얼로그

  private String startData = "";
  private String endData = "";
  private String routeData = "";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_map_search_waypoint);

    getMode();

    setLayout();

    new Handler().postDelayed(new Runnable(){

      @Override
      public void run() {

        if(mNlvView !=null){

          String type = (String) StorageUtil.getStorageModeEx(NhsMapSearchWaypointActivity.this, FlightMarkFragment.MAP_TYPE);

          if (type.equals("1") || type.isEmpty()) {
            mNlvView.setMapKind(Constants.NAVI_MAP_VECTOR);
          }


          mNlvView.setShowPopup(false);
          double x = 0;
          double y = 0;

          try {

            mNlvView.clearRoutePosition();

            if (!startData.isEmpty()) {
              String[] spritStart = startData.split(" ");

              if (spritStart.length > 1) {

//                x = (int) (Float.parseFloat(spritStart[0]) * 10000000);
//                y = (int) (Float.parseFloat(spritStart[1]) * 10000000);

                x = Double.parseDouble(spritStart[1]);
                y = Double.parseDouble(spritStart[0]);

                Log.d("JeLib", "start x is : "+x+"");
                Log.d("JeLib", "start y is : "+y+"");

                int result = mNlvView.setRoutePosition(NhsMapSearchWaypointActivity.this, Constants.NAVI_SETPOSITION_START, y, x,"start name", new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {

                          }
                        });
                //mNative.lanSetRoutePosition(Constants.NAVI_SETPOSITION_START, x, y);

                if (result != 0) {
                  finish();
                }
              }

            }

          }catch (Exception ex) {

          }

          try {
            if (!endData.isEmpty()) {

              String[] spritStart = endData.split(" ");

              if (spritStart.length > 1) {

//                x = (int) (Float.parseFloat(spritStart[0]) * 10000000);
//                y = (int) (Float.parseFloat(spritStart[1]) * 10000000);

                x = Double.parseDouble(spritStart[1]);
                y = Double.parseDouble(spritStart[0]);

                Log.d("JeLib", "end x is : "+x+"");
                Log.d("JeLib", "end y is : "+y+"");

                int result = mNlvView.setRoutePosition(NhsMapSearchWaypointActivity.this,Constants.NAVI_SETPOSITION_GOAL, y, x,"goal name", new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {

                  }
                });

                if (result != 0) {
                  finish();
                }

                //mNative.lanSetRoutePosition(Constants.NAVI_SETPOSITION_GOAL, x, y);
              }

            }
          }catch (Exception ex) {

          }

          try {
            if (!routeData.isEmpty()) {
              String[] spritStart = routeData.split("\\n");
              String[] dataStart;

              int size = spritStart.length;
              int i = 0;

              for (i = 0; i < size; i++) {

                dataStart = spritStart[i].split(" ");

                if (dataStart.length > 1) {
//                  x = (int) (Float.parseFloat(dataStart[0]) * 10000000);
//                  y = (int) (Float.parseFloat(dataStart[1]) * 10000000);

                  x = Double.parseDouble(dataStart[1]);
                  y = Double.parseDouble(dataStart[0]);

                  Log.d("JeLib", "route x is : "+x+"");
                  Log.d("JeLib", "route y is : "+y+"");

                  int result = mNlvView.setRoutePosition(NhsMapSearchWaypointActivity.this,Constants.NAVI_SETPOSITION_WAYPOINT, y, x,"waypoint name", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                  });

                  if (result != 0) {
                    finish();
                  }

                }

              }

            }
          }catch (Exception ex) {

          }

//          if (!startData.isEmpty()) {
//            if (!endData.isEmpty()) {
//              mNative.lanExecuteRP();
//            }
//          }

        }
      }
    },1000);
  }
  @Override
  public void onDestroy(){
    super.onDestroy();
    try {
      this.mNlvView.clearRoutePosition();
    } catch(Exception e){

    }
  }
  /**
   * 레이아웃 설정
   * @author FIESTA
   * @since  오전 1:47
   **/
  private void setLayout(){
    this.mNlvView = (NhsLanView)findViewById(R.id.nlv_view);

    TextViewEx tvComplate = (TextViewEx)findViewById(R.id.tv_complate);
    TextViewEx tvCancel = (TextViewEx)findViewById(R.id.tv_cancel);

    switch (this.mode) {

      case NhsSelectPointActivity.MODE_DEPARTURE:
        tvComplate.setText("출발지 확정");
        break;
      case NhsSelectPointActivity.MODE_ARRIVAL:
        tvComplate.setText("도착지 확정");
        break;
      case NhsSelectPointActivity.MODE_ROUTE:
        tvComplate.setText("경유지 확정");
        tvCancel.setText("경유지 추가");
        break;

    }

    findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });

    findViewById(R.id.btn_complate).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        Intent intent = getIntent();
        intent.putExtra(DATA_START, startData);
        intent.putExtra(DATA_END, endData);
        intent.putExtra(DATA_ROUTE, routeData);
        setResult(RESULT_OK, intent);
        finish();

      }
    });

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

      this.mode = data.getInt(NhsSelectPointActivity.KEY_MODE, NhsSelectPointActivity.MODE_DEPARTURE);
      this.startData = data.getString(NhsSelectPointActivity.DATA_START, "");
      this.endData = data.getString(NhsSelectPointActivity.DATA_END, "");
      this.routeData = data.getString(NhsSelectPointActivity.DATA_ROUTE, "");

      TextView tvWaypoint = ((TextView) findViewById(R.id.tv_waypoints));
      String strCustom = "";

      if (!startData.isEmpty()) {
        strCustom+="[출발지]\n";
        strCustom+=(startData+"\n");
      }

      if (!routeData.isEmpty()) {
        strCustom+="[경유지]\n";
        strCustom+=(routeData+"\n");
      }

      if (!endData.isEmpty()) {
        strCustom+="[도착지]\n";
        strCustom+=(endData+"\n");
      }

      tvWaypoint.setText(strCustom);

//      if (this.mode == NhsSelectPointActivity.MODE_DEPARTURE) {  // 출발지
//
//        tvWaypoint.setText(startData);
//
//      } else if (this.mode == NhsSelectPointActivity.MODE_ARRIVAL) { // 도착지
//
//        tvWaypoint.setText(endData);
//
//      } else if (this.mode == NhsSelectPointActivity.MODE_ROUTE) { // 경유지
//
//        tvWaypoint.setText(routeData);
//
//      }

    }
  }

}
