package com.modim.lan.lanandroid;

/**
 * Created by cbj on 10/11/2017.
 */

public class WeatherInfo {
    public int uWeatherCount = 0;
    public Item[] item = null;

    public WeatherInfo(int num)
    {
        uWeatherCount = num;
        item = new Item[num];
        for( int i = 0; i < num; i++)
           item[i] = new Item();
    }

    public class Item {
        public float uLon;            // X
        public float uLat;            // Y
        public int uElev;            // 해발 고도
        public int uElevUnit;    // 해발고도 단위
        public int uHeading;     // 풍향
        public int uSpeed;       // 풍속 , (단위 : m/s)
        public int uVIS;         // 시정
        public int uCLDAMT;     // 운량
        public int uCLDALT;     // 운고
    }
}
