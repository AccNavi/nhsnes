package com.modim.lan.lanandroid;

/**
 * Created by cbj on 10/11/2017.
 */

public class Snowtam
{
    public int uSnowtamCount = 0;
    public Item[] item = null;

    public Snowtam(int num)
    {
        uSnowtamCount = num;
        item = new Item[num];
        //for( int i = 0; i < num; i++)
        //    item[i] = new Item();
    }

    public class Item {
        public String strAPCD;          // A) 공항지명부호

        public int uSubCount;         // Sub Snowtam 정보 개수
        public Sub[] pSub = null;

        public Item(int num)
        {
            uSubCount = num;
            pSub = new Sub[num];
            for( int i = 0; i < num; i++)
                pSub[i] = new Sub();
        }

        public class Sub {
            public String strRWYNM;          // C) RUNWAY 명칭
            public String strRWY_DEPST;     // F) 활주로 전체에 덮여있는 퇴적물을 표시
            public String strFCT;            // H) 마찰계수
        }
    }
}
