package kr.go.molit.nhsnes.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.dialog.DialogModifyRoute;
import kr.go.molit.nhsnes.interfaces.OnItemTouchHelperAdapter;
import kr.go.molit.nhsnes.interfaces.OnItemTouchHelperViewHolder;
import kr.go.molit.nhsnes.interfaces.OnStartDragListener;

/**
 * 화면 검색에서 경로 수정을 위한 어뎁터
 *
 * @author FIESTA
 * @since 오전 8:20
 **/
public class RecyclerModifyRoute extends RecyclerView.Adapter<RecyclerModifyRoute.ItemViewHolder>
        implements OnItemTouchHelperAdapter {

  private List<String> mItems = new ArrayList<>();

  private final OnStartDragListener mDragStartListener;
  private DialogModifyRoute.IRouteResultListener mIRouteResultListener;

  public RecyclerModifyRoute(Context context, OnStartDragListener dragStartListener) {
    mDragStartListener = dragStartListener;
  }

  public void setRoutesListener(DialogModifyRoute.IRouteResultListener listener){
    mIRouteResultListener = listener;
  }

  @Override
  public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modify_route, parent, false);
    ItemViewHolder itemViewHolder = new ItemViewHolder(view);
    return itemViewHolder;
  }

  @Override
  public void onBindViewHolder(final ItemViewHolder holder, int position) {
    holder.textView.setText(mItems.get(position));

    // Start a drag whenever the handle view it touched
    holder.handleView.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
          mDragStartListener.onStartDrag(holder);
        }
        return false;
      }
    });
  }

  @Override
  public void onItemDismiss(int position) {
    if(mItems.size() > 0 && (mItems.size()-1) >= position ){
      mItems.remove(position);
      putRoutes();
      notifyDataSetChanged();
//            notifyItemRemoved(position);
    }
  }

  public List<String> getItems() {
    return mItems;
  }

  public void setItems(List<String> list) {
    this.mItems = list;
  }

  @Override
  public boolean onItemMove(int fromPosition, int toPosition) {
    Collections.swap(mItems, fromPosition, toPosition);
    notifyItemMoved(fromPosition, toPosition);
    putRoutes();
    return true;
  }

  @Override
  public int getItemCount() {
    return mItems.size();
  }

  public static class ItemViewHolder extends RecyclerView.ViewHolder implements
          OnItemTouchHelperViewHolder {

    public final TextView textView;
    public final ImageView handleView;

    public ItemViewHolder(View itemView) {
      super(itemView);
      textView = (TextView) itemView.findViewById(R.id.text);
      handleView = (ImageView) itemView.findViewById(R.id.handle);
    }

    @Override
    public void onItemSelected() {
//      itemView.setBackgroundColor(Color.LTGRAY);
    }

    @Override
    public void onItemClear() {
    }
  }

  private void putRoutes(){
    if(null != mItems && mItems.size() > 0){
      String result = "";
      for(String str : mItems){
        if(result.length() > 0){
          result += "\n";
        }
        result += str;
      }
      if(null != mIRouteResultListener){
        mIRouteResultListener.onResult(result);
      }
    }

  }

}
