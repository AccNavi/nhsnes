package com.modim.lan.lanandroid;

/**
 * Created by cbj on 10/11/2017.
 */

public class Notam
{
    public int uNotamCount = 0;
    public Item[] item = null;

    public Notam(int num)
    {
        uNotamCount = num;
        item = new Item[num];
        //for( int i = 0; i < num; i++)
        //    item[i] = new Item();
    }

    public class Item {
        public int uCoordCount;         // 전체 좌표 개수
        public CoordPoint[] pPoint = null;

        public Item(int num)
        {
            uCoordCount = num;
            pPoint = new CoordPoint[num];
            for( int i = 0; i < num; i++)
                pPoint[i] = new CoordPoint();
        }

        public class CoordPoint {
            public float x;
            public float y;
        }

        public int uMinFL;               // Q코드 최저 비행고도 (단위 : FL)
        public int uMaxFL;              // Q코드 최고 비행고도 (단위 : FL)
        public char cGisType;           // GIS 종류, POLYGON : 0 , CIRCLE : 1
        public int rad;                  // GIS 반경/폭
        public String strEcode;         // E 항목
        public String strQcode;         // Q 항목

        public int iClass;              // 내부 사용 0
        public int iIndex;              // 내부 사용 0
        public int iSubIndex;           // 내부 사용 0
    }
}
