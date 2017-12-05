package kr.go.molit.nhsnes.interfaces;


/**
* Recycler에서 사용될 아이템 터치 인터페이스
* @author FIESTA
* @since  오전 8:21
**/
public interface OnItemTouchHelperAdapter {

  boolean onItemMove(int fromPosition, int toPosition);

  void onItemDismiss(int position);

}
