package com.modim.lan.lanandroid;

/**
 * Created by posk1 on 2017-11-23.
 */

public class AirSystemParametersInfo {

    public static int SYSP_BUFZONE1		= 0x00000001;	// 경로 이탈 버퍼존 , 단위 : m
    public static int SYSP_BUFZONE2		= 0x00000002;	// 경로 이탈 버퍼존 , 단위 : m
    public static int SYSP_BUFZONE3		= 0x00000004;	// 경로 이탈 버퍼존 , 단위 : m

    public static int SYSP_BUFOBSTACLE1	= 0x00000008;	// 장애물/공역/비행금지 경보존 , 단위 : nm
    public static int SYSP_BUFOBSTACLE2	= 0x00000010;	// 장애물/공역/비행금지 경보존 , 단위 : nm
    public static int SYSP_BUFOBSTACLE3	= 0x00000020;	// 장애물/공역/비행금지 경보존 , 단위 : nm

    public static int SYSP_BUFAIRTERRAIN1	= 0x00000040;	// 지형 경보존 , 단위 : nm
    public static int SYSP_BUFAIRTERRAIN2	= 0x00000080;	// 지형 경보존 , 단위 : nm
    public static int SYSP_BUFAIRTERRAIN3	= 0x00000100;	// 지형 경보존 , 단위 : nm

    public static int SYSP_BUFAIRCRAFT1	= 0x00000200;	// 인접항공기 버퍼존, 단위 : nm
    public static int SYSP_BUFAIRCRAFT2	= 0x00000400;	// 인접항공기 버퍼존, 단위 : nm
    public static int SYSP_BUFAIRCRAFT3	= 0x00000800;	// 인접항공기 버퍼존, 단위 : nm

    public static int SYSP_BUFMINALTITUDE	= 0x00001000;	// 비행 안전 고도 (최저) , 단위 : ft
    public static int SYSP_BUFMAXALTITUDE	= 0x00002000;	// 비행 안전 고도 (최고) , 단위 : ft

    public static int SYSP_BUFMAPDOWNRANGE	= 0x00004000;	// 맵 다운로드 범위 , 단위 : m
    public static int SYSP_DEVIATION		= 0x00008000;	// 경로이탈 재탐색 여부
    public static int SYSP_ALARM		    = 0x00010000;	// 경보 설정 여부
    public static int SYSP_SHOWALTITUDE	= 0x00020000;	// 측면고도를 표출 여부 설정


    int uParamMask;               // yeSystemParamMask 의 값 설정

    int uBufDeviation1;           // 경로 이탈 버퍼존 , 단위 : m
    int uBufDeviation2;           // 경로 이탈 버퍼존 , 단위 : m
    int uBufDeviation3;           // 경로 이탈 버퍼존 , 단위 : m

    int uBufObstacle1;            // 장애물/공역/비행금지구역 버퍼존 , 단위 : nm
    int uBufObstacle2;            // 장애물/공역/비행금지구역 버퍼존 , 단위 : nm
    int uBufObstacle3;            // 장애물/공역/비행금지구역 버퍼존 , 단위 : nm

    int uBufAirTerrain1;          // 지형 버퍼존 , 단위 : ft
    int uBufAirTerrain2;          // 지형 버퍼존 , 단위 : ft
    int uBufAirTerrain3;          // 지형 버퍼존 , 단위 : ft

    int uBufAirCraft1;            // 인접항공기 버퍼존 , 단위 : nm
    int uBufAirCraft2;            // 인접항공기 버퍼존 , 단위 : nm
    int uBufAirCraft3;               // 인접항공기 버퍼존 , 단위 : nm

    int uBufMinAltitude;          // 비행 안전 고도 (최저) , 단위 : ft
    int uBufMaxAltitude;          // 비행 안전 고도 (최고) , 단위 : ft

    int uBufMapDownloadRange;     // 맵 다운로드 범위 , 단위 : m

    boolean bDeviation;           // 경로이탈 재탐색 여부

    boolean bAlarm;                // 경보 설정 여부

    boolean bShowAltitude;        // 측면고도를 표출 여부 설정

    public int getuParamMask() {
        return uParamMask;
    }

    public void setuParamMask(int uParamMask) {
        this.uParamMask = uParamMask;
    }

    public int getuBufDeviation1() {
        return uBufDeviation1;
    }

    public void setuBufDeviation1(int uBufDeviation1) {
        this.uBufDeviation1 = uBufDeviation1;
    }

    public int getuBufDeviation2() {
        return uBufDeviation2;
    }

    public void setuBufDeviation2(int uBufDeviation2) {
        this.uBufDeviation2 = uBufDeviation2;
    }

    public int getuBufDeviation3() {
        return uBufDeviation3;
    }

    public void setuBufDeviation3(int uBufDeviation3) {
        this.uBufDeviation3 = uBufDeviation3;
    }

    public int getuBufObstacle1() {
        return uBufObstacle1;
    }

    public void setuBufObstacle1(int uBufObstacle1) {
        this.uBufObstacle1 = uBufObstacle1;
    }

    public int getuBufObstacle2() {
        return uBufObstacle2;
    }

    public void setuBufObstacle2(int uBufObstacle2) {
        this.uBufObstacle2 = uBufObstacle2;
    }

    public int getuBufObstacle3() {
        return uBufObstacle3;
    }

    public void setuBufObstacle3(int uBufObstacle3) {
        this.uBufObstacle3 = uBufObstacle3;
    }

    public int getuBufAirTerrain1() {
        return uBufAirTerrain1;
    }

    public void setuBufAirTerrain1(int uBufAirTerrain1) {
        this.uBufAirTerrain1 = uBufAirTerrain1;
    }

    public int getuBufAirTerrain2() {
        return uBufAirTerrain2;
    }

    public void setuBufAirTerrain2(int uBufAirTerrain2) {
        this.uBufAirTerrain2 = uBufAirTerrain2;
    }

    public int getuBufAirTerrain3() {
        return uBufAirTerrain3;
    }

    public void setuBufAirTerrain3(int uBufAirTerrain3) {
        this.uBufAirTerrain3 = uBufAirTerrain3;
    }

    public int getuBufAirCraft1() {
        return uBufAirCraft1;
    }

    public void setuBufAirCraft1(int uBufAirCraft1) {
        this.uBufAirCraft1 = uBufAirCraft1;
    }

    public int getuBufAirCraft2() {
        return uBufAirCraft2;
    }

    public void setuBufAirCraft2(int uBufAirCraft2) {
        this.uBufAirCraft2 = uBufAirCraft2;
    }

    public int getuBufAirCraft3() {
        return uBufAirCraft3;
    }

    public void setuBufAirCraft3(int uBufAirCraft3) {
        this.uBufAirCraft3 = uBufAirCraft3;
    }

    public int getuBufMinAltitude() {
        return uBufMinAltitude;
    }

    public void setuBufMinAltitude(int uBufMinAltitude) {
        this.uBufMinAltitude = uBufMinAltitude;
    }

    public int getuBufMaxAltitude() {
        return uBufMaxAltitude;
    }

    public void setuBufMaxAltitude(int uBufMaxAltitude) {
        this.uBufMaxAltitude = uBufMaxAltitude;
    }

    public int getuBufMapDownloadRange() {
        return uBufMapDownloadRange;
    }

    public void setuBufMapDownloadRange(int uBufMapDownloadRange) {
        this.uBufMapDownloadRange = uBufMapDownloadRange;
    }

    public boolean isbDeviation() {
        return bDeviation;
    }

    public void setbDeviation(boolean bDeviation) {
        this.bDeviation = bDeviation;
    }

    public boolean isbAlarm() {
        return bAlarm;
    }

    public void setbAlarm(boolean bAlarm) {
        this.bAlarm = bAlarm;
    }

    public boolean isbShowAltitude() {
        return bShowAltitude;
    }

    public void setbShowAltitude(boolean bShowAltitude) {
        this.bShowAltitude = bShowAltitude;
    }
}
