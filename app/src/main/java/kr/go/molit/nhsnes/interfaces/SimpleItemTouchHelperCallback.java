package kr.go.molit.nhsnes.interfaces;


import static android.support.v7.widget.helper.ItemTouchHelper.Callback.makeMovementFlags;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;


/**
*  Recycler에서 사용될 아이템 터치 콜벡
* @author FIESTA
* @since  오전 8:46
**/
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

  public static final float ALPHA_FULL = 1.0f;

  private final OnItemTouchHelperAdapter mAdapter;

  public SimpleItemTouchHelperCallback(OnItemTouchHelperAdapter adapter) {
    mAdapter = adapter;
  }

  @Override
  public boolean isLongPressDragEnabled() {
    return true;
  }

  @Override
  public boolean isItemViewSwipeEnabled() {
    return true;
  }

  @Override
  public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
    if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
      final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
      final int swipeFlags = 0;
      return makeMovementFlags(dragFlags, swipeFlags);
    } else {
      final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
      final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
      return makeMovementFlags(dragFlags, swipeFlags);
    }
  }

  @Override
  public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
    if (source.getItemViewType() != target.getItemViewType()) {
      return false;
    }

    mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
    return true;
  }

  @Override
  public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
    mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
  }

  @Override
  public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
      // Fade out the view as it is swiped out of the parent's bounds
      final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
      viewHolder.itemView.setAlpha(alpha);
      viewHolder.itemView.setTranslationX(dX);
    } else {
      super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
  }

  @Override
  public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
    if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
      if (viewHolder instanceof OnItemTouchHelperViewHolder) {
        OnItemTouchHelperViewHolder itemViewHolder = (OnItemTouchHelperViewHolder) viewHolder;
        itemViewHolder.onItemSelected();
      }
    }

    super.onSelectedChanged(viewHolder, actionState);
  }

  @Override
  public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
    super.clearView(recyclerView, viewHolder);

    viewHolder.itemView.setAlpha(ALPHA_FULL);

    if (viewHolder instanceof OnItemTouchHelperViewHolder) {
      // Tell the view holder it's time to restore the idle state
      OnItemTouchHelperViewHolder itemViewHolder = (OnItemTouchHelperViewHolder) viewHolder;
      itemViewHolder.onItemClear();
    }
  }
}
