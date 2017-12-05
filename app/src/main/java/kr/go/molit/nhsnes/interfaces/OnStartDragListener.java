package kr.go.molit.nhsnes.interfaces;

import android.support.v7.widget.RecyclerView;

/**
* Recycler에서 사용될 아이템 터치 인터페이스
* @author FIESTA
* @since  오전 8:35
**/
public interface OnStartDragListener {
  void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
