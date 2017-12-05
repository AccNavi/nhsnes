
package com.modim.lan.lanandroid;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class NativeImplement {
    public boolean mJNIInit = false;
    public static final String KML_DATA_PATH;     // KML 을 불러올 경로
    public static String GPS_LOG_DATA_PATH;     // GPS LOG 을 불러올 경로
    public static String MAP_PATH="";     // 맵 경로

    static {
        System.loadLibrary("lanAndroidJni");
        KML_DATA_PATH = Environment.getExternalStorageDirectory() + "/ACC_NAVI/KML_Data/";
//        GPS_LOG_DATA_PATH = Environment.getExternalStorageDirectory() + "/ACC_NAVI/GPSLog";
    }

    public NativeImplement() {

        JniConfig config = new JniConfig();

        // 외부 usb 저장 경로를 가져온다
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LANMap";
        MAP_PATH = path;
        GPS_LOG_DATA_PATH = "/GPSLog/";
        config.sRootDirectory = path;
//        config.sRootDirectory = "/storage/0000-0000/LANMap"; // for Galuxy Wide ,  요놈은 외부 저장장치 이름이 틀리다.

        config.sRootPackage = "com/modim/lan/lanandroid";

        config.configValueB = 2;
        config.configValueC = 3;

        mJNIInit = lanInitialize(config);
        if (!mJNIInit)
            lanDestroy();
    }

    /**
     * KML 폴더에 있는 파일 이름들을 가져온다.
     *
     * @author FIESTA
     * @since 오후 4:33
     **/
    public static ArrayList<String> getKmlFileName() {

        File file = new File(KML_DATA_PATH);
        File list[] = file.listFiles();
        ArrayList<String> fileName = new ArrayList<>();

        try {
            for (int i = 0; i < list.length; i++) {
                fileName.add(list[i].getName());
            }
        }catch (Exception ex) {

        }

        return fileName;
   }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        lanDestroy();
    }

    public boolean isNativeImplement()
    {
        return mJNIInit;
    }

    public  void lanUpdateWrapper(int iUpdateType)
    {
         lanUpdate(iUpdateType);
    }

    public  void lanRenderWrapper(int iRenderType)
    {
         lanRender(iRenderType);
    }

    public  void lanZoomByPositionWrapper(int iMode, double fX, double fY)
    {
        lanZoomByPosition(iMode, fX, fY);
    }

    public static native boolean lanInitialize(Object obj);
    public static native void lanRenderInitialize();
    public static native void lanDestroy();
    public static native void lanPause();
    public static native void lanResume();
    public static native void lanTogglePauseResume();
    public static native int lanVersion(int iVerType);
    //Map
    public static native void lanRender(int iRenderType);
    public static native void lanUpdate(int iUpdateType);
    public static native void lanSetMapKind(int iKind);
    public static native void lanSingleTouch(int nEvent, double fX, double fY);
    public static native void lanResize(int w, int h);
    public static native void lanZoomByPosition(int iMode, double fX, double fY);
    public static native AirPoint lanScreenToMap(double x, double y);
    public static native void lanSetMapViewmode(int iMode);
    public static native void lanSetMapColor(int iColor);
    public static native AirPoint lanGetMapPos();
    public static native void lanSetDisplayLayer(int iLayer);

    //RP
    public static native int lanSetRoutePosition(int rpPos, double lon, double lat, String strName, int iRepresent);
    public static native int lanExecuteRP(RpOption option);
    public static native int lanPretestExecuteRP(RpOption option);
    public static native void lanClearRoutePosition();
    public static native int lanIsRoute();

    //Route Info
    public static native AirRouteStatus lanGetRouteInfo();
    public static native AirDoyupList lanGetRouteDoyupList();
    public static native int lanGetRouteDoyupListCount();

    //RG
    public static native int lanSimulStartTrajectory();
    public static native int lanSimulStopTrajectory();
    public static native void lanSimulPauseTrajectory();
    public static native void lanSimulResumeTrajectory();
    public static native int lanSimulIsTrajectory();
    public static native int lanSimulIsPause();
    public static native int lanSimulRouteOut();

    public static native int lanExceuteGuide();

    //Trajectory
    // 항적 데이타 저장 시작
    public static native  void lanStartTrajectory();
    // 항적 데이타 저장 종료
    public static native  void lanStopTrajectory();
    // 항적 데이타 저장중인가?
    public static native boolean lanIsTrajectory();
    // 현재까지의 항적 데이타 총 거리
    public static native int	lanGetTrajectoryDist();
    // 현재까지의 항적 데이타 총 시간
    public static native int	lanGetTrajectoryTime();
    // 현재까지의 항적 데이타 총 개수
    public static native int	lanGetTrajectoryCount();

    // 저장된 항적 데이타 디렉토리를 반환함.
    public static native String lanGetTrajectoryDirectory();
    // 항적 데이타 로그 주행 시작
    public static native boolean lanLogStartTrajectory(String lpszLogName);
    // 항적 데이타 로그 주행 종료
    public static native void lanLogStopTrajectory();
    // 항적 데이타 로그 주행 속도 설정
    public static native void lanLogSpeedTrajectory(int iSpeedLevel);
    // 항적 데이타 로그 주행 Pause 설정
    public static native void lanLogPauseTrajectory();
    // 항적 데이타 로그 주행 Resume 설정
    public static native void lanLogResumeTrajectory();
    // 항적 데이타 로그 주행중인가? (Start가 되었다면 TRUE임)
    public static native boolean lanLogIsTrajectory();
    // 항적 데이타 로그 Pause중인가?
    public static native boolean lanLogIsPause();

    //InternalIF
    public static native void lanReceiveGPSData(AirGPSData gpsData);

    //Event
    public static native long lanGetLastError();
    public static native int lanNaviEventType();
    public static native boolean lanNaviEvent(Object objEvent);

    public static native void lanTest1();
    public static native void lanTest2();
    public static native void lanTest3();

    // POI
    public static native void       lanSearchMapPreviewStart(float lon, float lat);
    public static native void       lanSearchMapPreviewEnd();
    public static native int			lanPoiSortByIndex(int nIndex);
    public static native POIInfo		lanPoiGetList();
    public static native int			lanPoiGetCount();
    public static native int  		lanPoiBySearch(byte[] strPOI, boolean bInclude);
    public static native void		lanPoiSetMaxSearch(int jMaxSearchCount);
    public static native String		lanPoiGetVersion();

    //User
    public static native void       lanSetKMLDataPath(String strFileName);

    public static native boolean	lanReceiveAroundAviation(AroundAviation av);
    public static native boolean	lanSetWeatherInfo(WeatherInfo weather);
    public static native boolean	lanReceiveNotam(Notam notam);
    public static native boolean	lanReceiveSnowtam(Snowtam snowtam);

    // 주어진 중심 좌표로 Zoom을 수행한다.
    public static native void lanSetZoomLevelByPosition (int iZoomLevel, float fX, float fY);

    public static native boolean lanGetPortCodeName(double fLon, double fLat, StringBuffer  strBuffer);

    public static native void lanSystemParametersInfo(AirSystemParametersInfo sysParam);

    public static native void lanSimulSpeedTrajectory(int iSpeedLevel);
}

