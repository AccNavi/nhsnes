package kr.go.molit.nhsnes.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.model.NhsFavoriteModel;
import kr.go.molit.nhsnes.model.NhsMapModel;
import kr.go.molit.nhsnes.model.NhsSearchWayPointModel;


/**
 * 즐겨찾기 전용 어뎁터
 */
public class RecyclerFavoriteAdapter extends RecyclerView.Adapter<RecyclerFavoriteAdapter.CurrentSearchHolder>{


    private ArrayList<NhsMapModel> currentMapModels;

    private IFavorite mIFavorite;

    public RecyclerFavoriteAdapter(){

    }

    public RecyclerFavoriteAdapter(IFavorite iFavorite) {
        this.mIFavorite = iFavorite;
    }



    public void setFavoriteData(ArrayList<NhsFavoriteModel> models){
        setFavoriteData(models, false);
    }

    public void setFavoriteData(ArrayList<NhsFavoriteModel> models, boolean addState){
        if(null == currentMapModels){
            currentMapModels = new ArrayList<NhsMapModel>();
        } else {
            if (!addState) {
                currentMapModels = null;
            }
        }

        if(null != models && 0 < models.size()){
            for(NhsFavoriteModel model : models){
                NhsMapModel mapModel = new NhsMapModel();
                mapModel.setFavoriteData(model);
                currentMapModels.add(mapModel);
            }
            notifyDataSetChanged();
        }
    }


    public void setWayPointData(ArrayList<NhsSearchWayPointModel> models){
        setWayPointData(models, false);
    }

    /**
    * 데이터 추가
    * @author FIESTA
    * @version 1.0.0
    * @since 오후 5:27
    **/
    public void setWayPointData(ArrayList<NhsSearchWayPointModel> models, boolean addState){
        if(null == currentMapModels){
            currentMapModels = new ArrayList<NhsMapModel>();
        } else {
            if (!addState) {
                currentMapModels = null;
            }
        }
        if(null != models && 0 < models.size()){
            for(NhsSearchWayPointModel model : models){
                NhsMapModel mapModel = new NhsMapModel();
                mapModel.setSearchWayPointData(model);
                currentMapModels.add(mapModel);
            }
            notifyDataSetChanged();
        }
    }


    @Override
    public CurrentSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_current_search_list, parent, false);
        return new CurrentSearchHolder(view);
    }

    @Override
    public void onBindViewHolder(CurrentSearchHolder holder, int position) {

        final NhsMapModel model = currentMapModels.get(position);

        holder.mTv_name.setText(model.getName());
//        holder.mTextViewLongitude.setText(model.getLongitude());
//        holder.mTextViewLatitude.setText(model.getLatitude());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIFavorite.onClick(model);
            }
        });
        holder.view.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View view) {
                mIFavorite.onLongClick(model);
                return false;
            }
        });

        holder.mTextViewLongitude.setTextColor(Color.WHITE);
        holder.mTextViewLatitude.setTextColor(Color.WHITE);
    }



    @Override
    public int getItemCount() {
        if (currentMapModels != null) {
            return currentMapModels.size();
        } else {
            return 0;
        }
    }

    /**
     * View Holder
     */
    public class CurrentSearchHolder extends RecyclerView.ViewHolder {
        private TextView mTv_name;
        private TextView mTextViewLongitude;
        private TextView mTextViewLatitude;
        private ViewGroup mLayoutCurrentSearch;
        private View view;

        public CurrentSearchHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            mTv_name = (TextView) itemView.findViewById(R.id.tv_name);
            mTextViewLongitude = (TextView) itemView.findViewById(R.id.tv_longitude);
            mTextViewLatitude = (TextView) itemView.findViewById(R.id.tv_latitude);
            mLayoutCurrentSearch = (ViewGroup) itemView.findViewById(R.id.layout_current_search);
        }
    }

    public interface IFavorite {
        public void onClick(NhsMapModel data);
        public void onLongClick(NhsMapModel data);
    }

}
