package com.modim.lan.lanandroid;

import static com.modim.lan.lanandroid.LanStorage.mNative;
import static com.modim.lan.lanandroid.NativeImplement.lanGetTrajectoryDirectory;
import static com.modim.lan.lanandroid.NativeImplement.lanLogStartTrajectory;
import static com.modim.lan.lanandroid.NativeImplement.lanPoiBySearch;
import static com.modim.lan.lanandroid.NativeImplement.lanPoiGetList;
import static com.modim.lan.lanandroid.NativeImplement.lanPretestExecuteRP;
import static com.modim.lan.lanandroid.NativeImplement.lanSearchMapPreviewEnd;
import static com.modim.lan.lanandroid.NativeImplement.lanSearchMapPreviewStart;
import static com.modim.lan.lanandroid.NativeImplement.lanSetMapColor;
import static com.modim.lan.lanandroid.NativeImplement.lanSetMapKind;
import static com.modim.lan.lanandroid.NativeImplement.lanSetMapViewmode;
import static kr.go.molit.nhsnes.fragment.SystemSettingFragment.CHANGE_COLOR_AUTOMATICALLY;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.activity.NhsFlightActivity;
import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.interfaces.OnClickOptionMapMenu;
import kr.go.molit.nhsnes.interfaces.OnLongClickOptionMapMenu;
import kr.go.molit.nhsnes.widget.TextViewEx;

/**
 * Created by user on 2017-07-21.
 */

public class NhsLanView extends FrameLayout implements SensorEventListener {

  private static LANGLSurfaceView mGLView;
  private View mUIView;

  private int mainTime = 0;
  private final static String TAG = Constants.TAG;
  private int MY_PERMISSIONS_REQUEST_READ = 2289;
  private TimerTask task = null;
  public Dialog optionMenu;

  private SensorManager mSensorManager;
  private Sensor mSensor;
  private SensorEventListener mGyroLis;
  private Sensor mGgyroSensor = null;
  private Context mContext = null;
  public boolean showPopup = false; // 팝업창을 보여줄지 말지 결정
  private OnClickOptionMapMenu onClickOptionMapMenu;   // 옵션 클릭 리스너

  private OnLongClickOptionMapMenu onLongClickOptionMapMenu;   // 옵션 롱클릭 리스너

  public NhsLanView(@NonNull Context context) {
    super(context);
    this.mContext = context;
    Init();
  }

  public NhsLanView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    this.mContext = context;
    Init();
  }

  public NhsLanView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.mContext = context;
    Init();
  }

  protected View setHandler(int id, Object listener) {
    return setHandlerByView(getRootView(), id, listener);
  }

  protected View setHandlerByView(View viewRoot, int id, Object listener) {
    Log.d("JeLib", "setHandlerByView---------" + listener);
    View v = viewRoot.findViewById(id);
    if (v == null) {
      Log.d(TAG, "setHandlerByView id is null : " + id);
      return null;
    }

    if (v instanceof Button) {
      Button button = (Button) v;
      button.setOnClickListener((OnClickListener) listener);
      return button;
    } else if (v instanceof LinearLayout) {
      LinearLayout layout = (LinearLayout) v;
      layout.setOnClickListener((OnClickListener) listener);
      return layout;
    }
    // 항상 EditText 핸들러가 TextView 핸들러보다 먼저 와야 한다. Control의 포함관계 때문이다.
    else if (v instanceof EditText) {
      EditText edit = (EditText) v;
      edit.addTextChangedListener((TextWatcher) listener);
      return edit;
    } else if (v instanceof TextView) {
      TextView txt = (TextView) v;
      txt.setOnClickListener((OnClickListener) listener);
      return txt;
    } else {
      v.setOnClickListener((OnClickListener) listener);
      return v;
    }
  }

  /**
   * 로그 주행
   *
   * @author FIESTA
   * @since 오전 3:49
   **/
  public void startLogTrajectory(String logName) {
    lanGetTrajectoryDirectory();
    lanLogStartTrajectory(logName);
  }

  public void Init() {
    mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
    mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

    final LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    if (mUIView == null) {
      LayoutInflater inflater = LayoutInflater.from(getContext());
      mUIView = inflater.inflate(R.layout.activity_main, null);
      addView(mUIView, param);
    }

    mNative = INativeImple.getInstance(getContext());
//    mGLView = IGLSurface.getInstance(getContext());


    if (mGLView != null)  {

      ViewParent view = mGLView.getParent();
      if (view != null) {
        ((NhsLanView) view).removeAllViews();
      }
//      mGLView.onPause();
    }

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {

        mGLView =  new LANGLSurfaceView(getContext());
        addView(mGLView, param);
//        mUIView.bringToFront();

        mGLView.setEventHadler(new Handler() {
          @Override
          public void handleMessage(Message msg) {
            if (msg.what == 100) {
              if (showPopup) {
//                        optionMenu.show();

                // 화면 터치시..
                if (onClickOptionMapMenu != null) {

                  if (mGLView != null) {
                    AirPoint curPos = mNative.lanScreenToMap(mGLView.fX, mGLView.fY);
                    Log.d("JeLib", "x:" + curPos.x + " y:" + curPos.y);
                    onClickOptionMapMenu.onClick(curPos);
                  }
                }
              }
            } else if (msg.what == 111) {
              if (showPopup) {
//                        optionMenu.show();

                // 화면 터치시..
                if (onLongClickOptionMapMenu != null) {

                  if (mGLView != null) {
                    AirPoint curPos = mNative.lanScreenToMap(mGLView.fX, mGLView.fY);
                    Log.d("JeLib", "x:" + curPos.x + " y:" + curPos.y);
                    onLongClickOptionMapMenu.onLongClick(curPos);
                  }
                }
              }
            }
          }
        });

      }
    }, 500);

  }

  public void setMapKind(int state) {
    lanSetMapKind(state);
  }

  OnClickListener btnListener = new OnClickListener() {
    public void onClick(View v) {
      Log.d("JeLib", "btnListener---------" + btnListener);
      switch (v.getId()) {
        case R.id.buttonPlus: {
          lanSetMapKind(Constants.NAVI_MAP_DEM);
        }
        break;
        case R.id.buttonVector: {
          lanSetMapKind(Constants.NAVI_MAP_VECTOR);
          break;
        }
        // ikhwang temp
        //case R.id.buttonSatellite: {
        //    lanSetMapKind(Constants.NAVI_MAP_SATELLITE);
        //    break;
        //}
        case R.id.buttonMinus: {
          Button btn = (Button) v;
          Button btnPlay = (Button) findViewById(R.id.buttonRGPlayPause);
          if (mNative.lanIsRoute() == 0)
            break;

          if (mNative.lanSimulIsTrajectory() == 1) {
            //stop
            mNative.lanSimulStopTrajectory();
            btn.setText("Simul Start");
            btnPlay.setEnabled(false);
            btnPlay.setText("Simul Play");
          } else {
            mNative.lanSimulStartTrajectory();
            btn.setText("Simul Stop");
            btnPlay.setEnabled(true);
            btnPlay.setText("Simul Play");
          }

        }
        //Log.d(TAG, "buttonMinus");
        break;
        case R.id.buttonRGPlayPause: {
          Button btn = (Button) v;
          if (mNative.lanIsRoute() == 0 || mNative.lanSimulIsTrajectory() == 0)
            break;

          if (mNative.lanSimulIsPause() == 1) {
            mNative.lanSimulResumeTrajectory();
            btn.setText("Simul Pause");
          } else {
            mNative.lanSimulPauseTrajectory();
            btn.setText("Simul Continue");
          }
        }
        break;
        case R.id.buttonRGOut: {
          mNative.lanSimulRouteOut();
        }
        break;
        case R.id.buttonPOI:
          String mPOIText = "강남";
          byte[] strKSC5601 = null;
          try {
            strKSC5601 = mPOIText.getBytes("ksc5601");
          } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
          }
          int nCount = lanPoiBySearch(strKSC5601, false);
          strKSC5601 = null;

          if (nCount != 0) {
            POIInfo poiList = lanPoiGetList();
            for (int i = 0; i < poiList.mMaxCount; i++) {
              // poi 데이타는 Y축이 반대로 되어 있다.
              try {
                poiList.item[i].title = new String(poiList.item[i].arrTitle, "ksc5601");
              } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
              }
              Log.d(TAG, "POI ( " + i + " ) : Name(" + poiList.item[i].title + ") , X(" + poiList.item[i].nX + ") , Y(" + poiList.item[i].nY + ")");
            }
          }
          break;
        case R.id.btnNorthUp: {
          lanSetMapViewmode(1);
        }
        break;
        case R.id.btnHeadingUp: {
          lanSetMapViewmode(0);
        }
        break;
        case R.id.btnBirdView: {
          lanSetMapViewmode(2);
        }
        break;
        case R.id.btnGeoMove: {
          lanSearchMapPreviewStart(1265840630, 373358870);
        }
        break;
        case R.id.btnGeoEnd: {
          lanSearchMapPreviewEnd();
        }
        break;
        default:
          break;
      }
    }
  };

  public void onStop() {
    try {
      if (task != null) {
        task.cancel();
      }
    } catch (Exception e) {

    }
  }

  public void onPause() {
    try {
      mSensorManager.unregisterListener(this);
//      mSensorManager.unregisterListener(mGyroLis);
    } catch (Exception e) {

    }
  }

  public void onResume() {
    try {
//
      mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
//
//      // 자이로 센서를 등록한다.
//      mSensorManager.registerListener(mGyroLis, mGgyroSensor, SensorManager.SENSOR_DELAY_UI);
//
    } catch (Exception e) {

    }
  }
    /*
    public void CreateOptionMenu()
    {
        optionMenu = new Dialog(getContext());
        optionMenu.setContentView(R.layout.optionmenu);
        optionMenu.setCanceledOnTouchOutside(true);

        Button btnStart = (Button) optionMenu.findViewById(R.id.btnSetStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                AirPoint curPos =  mNative.lanScreenToMap(mGLView.fX, mGLView.fY);
//                mNative.lanSetRoutePosition(Constants.NAVI_SETPOSITION_START,curPos.x,curPos.y);

                if (onClickOptionMapMenu != null) {
                    onClickOptionMapMenu.setStart(curPos);
                }

                optionMenu.dismiss();
            }
        });

        Button btnGoal = (Button) optionMenu.findViewById(R.id.btnSetGoal);
        btnGoal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                AirPoint curPos =  mNative.lanScreenToMap(mGLView.fX, mGLView.fY);
//                mNative.lanSetRoutePosition(Constants.NAVI_SETPOSITION_GOAL,curPos.x,curPos.y);

                if (onClickOptionMapMenu != null) {
                    onClickOptionMapMenu.setGoal(curPos);
                }

                optionMenu.dismiss();
            }
        });

        Button btnWaypoint = (Button) optionMenu.findViewById(R.id.btnSetWayPoint);
        btnWaypoint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                AirPoint curPos =  mNative.lanScreenToMap(mGLView.fX, mGLView.fY);
//                mNative.lanSetRoutePosition(Constants.NAVI_SETPOSITION_WAYPOINT,curPos.x,curPos.y);

                if (onClickOptionMapMenu != null) {
                    onClickOptionMapMenu.setWaypoint(curPos);
                }

                optionMenu.dismiss();
            }
        });

        Button btnSearch = (Button) optionMenu.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                RpOption option = new RpOption(0,0);
                mNative.lanExecuteRP(option);

                if (onClickOptionMapMenu != null) {
                    onClickOptionMapMenu.OnSearch();
                }

                optionMenu.dismiss();
            }
        });
        Button btnReset = (Button) optionMenu.findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                mNative.lanClearRoutePosition();

                if (onClickOptionMapMenu != null) {
                    onClickOptionMapMenu.OnReset();
                }

                optionMenu.dismiss();
            }
        });
    }
    */

  private Dialog dialog;

  public int executeRP(Context context, int bReRP, int iSpeed, OnClickListener onClickListener) {
    RpOption option = new RpOption(bReRP, iSpeed);

    int testValue = lanPretestExecuteRP(option);
    int result = -1;

    if (testValue == 24) {  // 공역으로 지나가면

      showDialogRoute(context, "금지구역", "금지구역이 있습니다\n우회하시겠습니까?", onClickListener);

    } else if (testValue == 25) { // 공역으로 피해서 간다면

      result = mNative.lanExecuteRP(option);

    }




    Log.i("TEST", "testValue:::" + testValue);
    return result;
  }

  public int executeRPDirect(int bReRP, int iSpeed) {
    RpOption option = new RpOption(bReRP, iSpeed);

    int result = mNative.lanExecuteRP(option);




    Log.i("TEST", "result:::" + result);
    return result;
  }

  public void showDialogRoute(Context context, String title, String msg, final OnClickListener onClickListener){

    dialog = new Dialog(context);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    //팝업 주위를 눌러도 창 닫히지 않음
    dialog.setCanceledOnTouchOutside(false);

    dialog.setContentView(R.layout.dialog_popup_type1);

    TextView alert_title = (TextView)dialog.findViewById(R.id.alert_title);
    TextView alert_msg = (TextView)dialog.findViewById(R.id.alert_msg);

    alert_title.setText(title);
    alert_msg.setText(msg);

    TextViewEx alert_btn_ok = (TextViewEx) dialog.findViewById(R.id.alert_btn_ok);
    TextViewEx alert_btn_cancel = (TextViewEx) dialog.findViewById(R.id.alert_btn_cancel);

    alert_btn_ok.setText("확인");
    alert_btn_cancel.setText("취소");

    alert_btn_cancel.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        dialog.dismiss();
        onClickListener.onClick(view);
      }
    });

    alert_btn_ok.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        dialog.dismiss();
        onClickListener.onClick(view);
      }
    });

    //팝업 바탕 알파값 주기
    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
    lp.dimAmount= 0.95f;
    dialog.getWindow().setAttributes(lp);
    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    dialog.show();
  }


  public AirPoint screenToMap(float fx, float fy) {
    return mNative.lanScreenToMap(fx, fy);
  }

  public void clearRoutePosition() {
    try {
      mNative.lanClearRoutePosition();
    } catch (Exception e) {

    }
  }

  public int setRoutePosition(Context context, int pos_type, double fx, double fy, String strName, OnClickListener onClickListener) {
    Log.d("JeLib", "fx:" + fx + " fy:" + fy);
    //AirPoint curPos =  mNative.lanScreenToMap(fx, fy);
    int result = 0;

    try {

      result = mNative.lanSetRoutePosition(pos_type, fx, fy, strName, 0);

      if (result == 1) {

        showDialogRoute(context, "금지구역", "비행금지구역입니다.", onClickListener);

        // 다이얼로그 취소 버튼 숨기기..
        this.dialog.findViewById(R.id.alert_btn_cancel).setVisibility(View.GONE);

      } else {
        result = 0;
      }

    }catch (Exception ex){

    } finally {

      return  result;

    }

  }

  public void setMapViewmode(int mode) {
    mNative.lanSetMapViewmode(mode);
  }

  public void excuteRPEx(AirPoint startPos, List<AirPoint> wayPosArr, AirPoint endPos, int bReRP, int iSpeed) {
    Log.d("JeLib", "simulPlay1");

    Log.d("JeLib", "NAVI_SETPOSITION_START");
    if (startPos != null) {
      mNative.lanSetRoutePosition(Constants.NAVI_SETPOSITION_START, startPos.x, startPos.y, "start name", 0);
    }
    Log.d("JeLib", "NAVI_SETPOSITION_WAYPOINT");
    if (wayPosArr != null) {
      for (AirPoint p : wayPosArr) {
        mNative.lanSetRoutePosition(Constants.NAVI_SETPOSITION_WAYPOINT, p.x, p.y, "waypoint name", 0);
      }
    }
    Log.d("JeLib", "NAVI_SETPOSITION_GOAL");
    if (endPos != null) {
      mNative.lanSetRoutePosition(Constants.NAVI_SETPOSITION_GOAL, endPos.x, endPos.y, "goal name", 0);
    }

    Log.d("JeLib", "lanExecuteRP");
    RpOption option = new RpOption(0, 0);
    mNative.lanExecuteRP(option);
  }

  public void simulPlay(AirPoint startPos, List<AirPoint> wayPosArr, AirPoint endPos) {
    Log.d("JeLib", "simulPlay1");

    Log.d("JeLib", "NAVI_SETPOSITION_START");
    if (startPos != null) {
      mNative.lanSetRoutePosition(Constants.NAVI_SETPOSITION_START, startPos.x, startPos.y, "start name", 0);
    }
    Log.d("JeLib", "NAVI_SETPOSITION_WAYPOINT");
    if (wayPosArr != null) {
      for (AirPoint p : wayPosArr) {
        mNative.lanSetRoutePosition(Constants.NAVI_SETPOSITION_WAYPOINT, p.x, p.y, "waypoint name", 0);
      }
    }
    Log.d("JeLib", "NAVI_SETPOSITION_GOAL");
    if (endPos != null) {
      mNative.lanSetRoutePosition(Constants.NAVI_SETPOSITION_GOAL, endPos.x, endPos.y, "goal name", 0);
    }

    Log.d("JeLib", "lanExecuteRP");
    RpOption option = new RpOption(0, 0);
    mNative.lanExecuteRP(option);

    Log.d("JeLib", "lanSimulStartTrajectory");
    mNative.lanSimulStartTrajectory();
    if (mNative.lanIsRoute() == 0 || mNative.lanSimulIsTrajectory() == 0) {
      Log.d("JeLib", "return");
      return;
    }
    if (mNative.lanSimulIsPause() == 1) {
      Log.d("JeLib", "lanSimulResumeTrajectory");
      mNative.lanSimulResumeTrajectory();
    } else {
      Log.d("JeLib", "lanSimulPauseTrajectory");
      mNative.lanSimulPauseTrajectory();
    }
  }

  public void simulRouteOut(){
    mNative.lanSimulRouteOut();
  }

  public void logStartTrajectory(String lpszLogName){
    mNative.lanLogStartTrajectory(lpszLogName);
  }

  public void setKMLDataPath(String lpszLogName){
//    mNative.lanSetKMLDataPath(lpszLogName);
  }

  public void onAccuracyChanged(Sensor sensor, int accuracy) {
  }

  public void onSensorChanged(SensorEvent event) {
    boolean isAuto = StorageUtil.getStorageMode(getContext(), CHANGE_COLOR_AUTOMATICALLY);

    if (isAuto) {
      if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
        if (event.values[0] < 30) {
          lanSetMapColor(1);
          Log.d("sensortest", "1");
        } else {
          lanSetMapColor(0);
          Log.d("sensortest", "0");
        }


      }
    }
  }

  public OnClickOptionMapMenu getOnClickOptionMapMenu() {
    return onClickOptionMapMenu;
  }

  public void setOnClickOptionMapMenu(OnClickOptionMapMenu onClickOptionMapMenu) {
    this.onClickOptionMapMenu = onClickOptionMapMenu;
  }

  public OnLongClickOptionMapMenu getOnLongClickOptionMapMenu() {
    return onLongClickOptionMapMenu;
  }

  public void setOnLongClickOptionMapMenu(OnLongClickOptionMapMenu onLongClickOptionMapMenu) {
    this.onLongClickOptionMapMenu = onLongClickOptionMapMenu;
  }

  public boolean isShowPopup() {
    return showPopup;
  }

  public void setShowPopup(boolean showPopup) {
    this.showPopup = showPopup;
  }


}
