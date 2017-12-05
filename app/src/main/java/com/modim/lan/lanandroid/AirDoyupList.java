package com.modim.lan.lanandroid;

/**
 * Created by raphael on 2017-08-29.
 */

public class AirDoyupList {
    public int mMaxCount = 0;
    public List[] lists = null;

    public AirDoyupList(int num)
    {
        mMaxCount = num;
        lists = new List[num];
        for(int i=0;i<num;i++)
            lists[i] = new List();
    }

    public class List
    {
        // jni의 ksc5601코드를 자바에서 encoding 변환하기 위한 임시버퍼
        public byte[] 	arrPath 		= null;
        public String path			= null; // 도엽의 경로
    }
}
