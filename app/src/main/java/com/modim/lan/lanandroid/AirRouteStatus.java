package com.modim.lan.lanandroid;

/**
 * Created by jolie on 7/20/2017.
 */

public class AirRouteStatus {
    public int uAverSpeed;			// 평균 속도
    public int  uAverAltitude;			// 평균 고도
    public int  uTotalDist;			// 총 비행시간
    public int  uTotalTime;			// 총 비행거리
    public int  uTotalPredictDist;		// 목적지까지의 남은 총 예상거리 (km)
    public int  uTotalPredictTime;		// 목적지까지의 남은 총 예상시간 (초)

    public class AirRouteWaypoint
    {
        public double lon;		// X
        public double lat;		// Y
        public int dist;		// 여기까지 거리
        public int time;		// 여기까지 시간
    }

    public int  uWaypointCount;
    public AirRouteWaypoint[] waypointList;

    public AirRouteStatus(int num)
    {
        uWaypointCount = num;
        waypointList = new AirRouteWaypoint[num];
        for( int i = 0; i < num; i++)
            waypointList[i] = new AirRouteWaypoint();
    }

}
