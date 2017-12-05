package com.modim.lan.lanandroid;

/**
 * Created by cbj on 10/11/2017.
 */

public class AroundAviation {
    public int uAroundCount = 0;
    public Item[] item = null;

    public AroundAviation(int num)
    {
        uAroundCount = num;
        item = new Item[num];
        for( int i = 0; i < num; i++)
           item[i] = new Item();
    }

    public class Item {
        public String strCallSign;        // 항공기ID(CallSign)
        public float lon;        // X
        public float lat;        // Y
        public int elev;        // 해발 고도
        public int heading;    // 비행기 헤딩 방향
        public int uState;   // 비행기 상황별 표출
    }
}
