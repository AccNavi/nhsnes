package com.modim.lan.lanandroid;

public class POIInfo 
{
	public int mMaxCount = 0;
	public Item[] item = null;

	public POIInfo(int num)
	{
		mMaxCount = num;
		item = new Item[num];
		for( int i = 0; i < num; i++)
			item[i] = new Item();
	}
		
	public class Item
	{
		// jni의 ksc5601코드를 자바에서 encoding 변환하기 위한 임시버퍼
		public byte[] 	arrTitle 		= null;

		public String title			= null; // 명칭
		public int      nX;
		public int      nY;
	}
}
