package kr.go.molit.nhsnes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.activity.NhsSelectPointActivity;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.model.NhsWaypoinSearchModel;

/**
 * Created by jongrakmoon on 2017. 4. 1..
 */

public class RecyclerWaypointAdapter extends RecyclerView.Adapter<RecyclerWaypointAdapter.CurrentSearchHolder> implements View.OnClickListener {

  public interface OnClickListener{
    public void onClick(NhsWaypoinSearchModel model);
  }

  private List<NhsWaypoinSearchModel> currentSearchModels;
  private int textColor;
  private int type = NhsSelectPointActivity.MODE_NONE;
  private LinkedList<NhsWaypoinSearchModel> selectedList = new LinkedList<>();
  private OnClickListener onClickListener = null;
  private View.OnLongClickListener onLongClickListener = null;

  public RecyclerWaypointAdapter(int textColor) {
    this.textColor = textColor;
    this.currentSearchModels = new ArrayList<>();
  }

  public RecyclerWaypointAdapter(int textColor, List<NhsWaypoinSearchModel> currentSearchModels) {
    this(textColor);
    this.currentSearchModels = currentSearchModels;
  }

  public RecyclerWaypointAdapter(int textColor, List<NhsWaypoinSearchModel> currentSearchModels, int type) {
    this(textColor);
    this.currentSearchModels = currentSearchModels;
    this.type = type;
  }

  @Override
  public CurrentSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_current_search_list, parent, false);
    return new CurrentSearchHolder(view);
  }

  @Override
  public void onBindViewHolder(CurrentSearchHolder holder, int position) {

    NhsWaypoinSearchModel model = currentSearchModels.get(position);

    holder.mTv_name.setText(model.getName());
    holder.mTextViewLongitude.setText("N " +model.getLongitude());
    holder.mTextViewLatitude.setText("E " + model.getLatitude());
    holder.view.setTag(R.string.tag_data, this.currentSearchModels.get(position));
    holder.view.setTag(R.string.tag_holder, holder);
    holder.view.setOnClickListener(this);
    holder.view.setOnLongClickListener(this.onLongClickListener);

    holder.mTextViewLongitude.setTextColor(textColor);
    holder.mTextViewLatitude.setTextColor(textColor);

    if (this.selectedList.indexOf(model) >= 0){
//      holder.view.setBackgroundResource(R.drawable.img_toastpopup4_foc);
    } else {
//      holder.view.setBackgroundColor(holder.view.getResources().getColor(android.R.color.transparent));
    }
  }

  public void setData(List<NhsWaypoinSearchModel> currentSearchModels) {
    this.currentSearchModels = currentSearchModels;
    notifyDataSetChanged();
  }

  public void clearData(){

    if (this.currentSearchModels == null) {
      this.currentSearchModels = new ArrayList<>();
    }

    this.currentSearchModels.clear();

  }

  public void removeData(NhsWaypoinSearchModel model){
    this.currentSearchModels.remove(model);
  }
  @Override
  public void onClick(View view) {

    NhsWaypoinSearchModel model = (NhsWaypoinSearchModel) view.getTag(R.string.tag_data);
    this.selectedList.add(model);

    /**
    NhsWaypoinSearchModel model = (NhsWaypoinSearchModel) view.getTag(R.string.tag_data);

      int i = 0;
      int size = this.selectedList.size();
      boolean isCheck = true;

      for (i = 0; i < size; i++) {
        if (this.selectedList.get(i) == model) {
          isCheck = false;
          this.selectedList.remove(i);
          break;
        } else {
          isCheck = true;
        }
      }

//      if (isCheck) {
//        view.setBackgroundResource(R.drawable.img_toastpopup4_foc);
        this.selectedList.add(model);
//      } else {
//        view.setBackgroundColor(view.getResources().getColor(android.R.color.transparent));
//      }

     **/
    if (this.onClickListener != null) {

      this.onClickListener.onClick(model);

    }

  }

  /**
   * 선택한 좌표들을 반환한다.
   *
   * @return Object
   * @author FIESTA
   * @since 오전 7:22
   **/
  public Object getSelectedListObj() {
    return this.selectedList;
  }

  /**
   * 선택한 좌표들을 반환한다.
   *
   * @return LinkedList<NhsWaypoinSearchModel>
   * @author FIESTA
   * @since 오전 7:22
   **/
  public LinkedList<NhsWaypoinSearchModel> getSelectedList() {
    return this.selectedList;
  }

  /**
   * 선택한 좌표들을 반환한다.
   *
   * @return LinkedList<NhsWaypoinSearchModel>
   * @author FIESTA
   * @since 오전 7:22
   **/
  public int getSelectedListSize() {
    return this.selectedList.size();
  }

  /**
  * 데이터를 반환한다.
  * @author FIESTA
  * @since  오후 11:24
  **/
  public NhsWaypoinSearchModel getData(int position) {

    return this.currentSearchModels.get(position);
    
  }

   /**
   * 아이템을 추가한다
   *
   * @author FIESTA
   * @since 오후 9:20
   **/
  public void addItem(NhsWaypoinSearchModel model) {
    if (this.currentSearchModels == null) {
      this.currentSearchModels = new ArrayList<>();
    }

    this.currentSearchModels.add(model);
  }

  /**
   * 아이템을 가져온다
   *
   * @author FIESTA
   * @since 오후 9:29
   **/
  public NhsWaypoinSearchModel getItem(int position) {
    if (this.currentSearchModels == null) {
      this.currentSearchModels = new ArrayList<>();
    }

    if (this.currentSearchModels.size() == 0) {
      return null;
    } else {
      return this.currentSearchModels.get(position);
    }
  }


  @Override
  public int getItemCount() {
    if (currentSearchModels != null) {
      return currentSearchModels.size();
    } else {
      return 0;
    }
  }

  public class CurrentSearchHolder extends RecyclerView.ViewHolder {
    private TextView mTv_name;
    private TextView mTextViewLongitude;
    private TextView mTextViewLatitude;
    private ViewGroup mLayoutCurrentSearch;
    private View view;

    public CurrentSearchHolder(View itemView) {
      super(itemView);
      this.view = itemView;
      mTv_name= (TextView) itemView.findViewById(R.id.tv_name);
      mTextViewLongitude = (TextView) itemView.findViewById(R.id.tv_longitude);
      mTextViewLatitude = (TextView) itemView.findViewById(R.id.tv_latitude);
      mLayoutCurrentSearch = (ViewGroup) itemView.findViewById(R.id.layout_current_search);
    }
  }

  public static List<NhsWaypoinSearchModel> makeFakeData() {

    List<NhsWaypoinSearchModel> nhsCurrentModels = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      NhsWaypoinSearchModel model = new NhsWaypoinSearchModel();
      model.setLatitude("68.1223" + i);
      model.setLongitude("100.1233" + i);
      nhsCurrentModels.add(model);
    }

    return nhsCurrentModels;
  }

  public OnClickListener getOnClickListener() {
    return onClickListener;
  }

  public void setOnClickListener(OnClickListener onClickListener) {
    this.onClickListener = onClickListener;
  }

  public View.OnLongClickListener getOnLongClickListener() {
    return onLongClickListener;
  }

  public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
    this.onLongClickListener = onLongClickListener;
  }

  public List<NhsWaypoinSearchModel> getCurrentSearchModels() {
    return currentSearchModels;
  }
}
