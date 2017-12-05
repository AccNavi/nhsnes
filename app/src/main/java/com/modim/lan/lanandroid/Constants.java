package com.modim.lan.lanandroid;

public class Constants {
	public final static String TAG = "LAN";

	// Alarm EventType
	public final static int NAVI_EVT_NONE = 0;  	// 더미 이벤트
	public final static int NAVI_EVT_ALARM = 1;  	// 알람 이벤트
	public final static int NAVI_EVT_GUIDE = 2;  	// 알람 이벤트

    public final static int NAVI_MAP_VECTOR = 0;
    public final static int NAVI_MAP_SATELLITE = 1;
    public final static int NAVI_MAP_DEM = 2;

	public final static int NAVI_TOUCH_EVENT_DOWN = 0;
	public final static int NAVI_TOUCH_EVENT_UP= 1;
	public final static int NAVI_TOUCH_EVENT_MOVE = 2;

	public final static int NAVI_SETPOSITION_START = 0;
	public final static int NAVI_SETPOSITION_WAYPOINT = 1;
	public final static int NAVI_SETPOSITION_GOAL = 2;

    public final static int RG_DIR_BACK = 1;
    public final static int RG_DIR_BACK_LEFT = 2;
    public final static int RG_DIR_LEFT = 3;
    public final static int RG_DIR_STRAIGHT_LEFT = 4;
    public final static int RG_DIR_STRAIGHT = 5;
    public final static int RG_DIR_STRAIGHT_RIGHT = 6;
    public final static int RG_DIR_RIGHT = 7;
    public final static int RG_DIR_BACK_RIGHT = 8;

    public final static String[] strDir = {
      "","Back", "Back Left","Left", "Left", "Straight", "Right", "Right", "Back Right"
    };
    public final static int NAVI_TOUCH_TIME = 10;

	// 프로젝트 루트 디렉토리
	public final static String LAN_ROOTDIRECTORY = "/mnt/sdcard/LANMap/";
}

