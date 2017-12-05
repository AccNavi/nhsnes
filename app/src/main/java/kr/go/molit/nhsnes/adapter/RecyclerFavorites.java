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
import kr.go.molit.nhsnes.model.NhsFavoriteModel;

/**
 * Created by jongrakmoon on 2017. 4. 1..
 */

public class RecyclerFavorites extends RecyclerView.Adapter<RecyclerFavorites.FavoritesHolder> implements View.OnClickListener{

    public interface OnClickListener{
        public void onClick(Object mode);
    }

    private OnClickListener onClickListener;

    private List<NhsFavoriteModel> favoriteModels;
    private LinkedList<NhsFavoriteModel> selectedList = new LinkedList<>();

    public RecyclerFavorites() {
        this.favoriteModels = new ArrayList<>();
    }

    public RecyclerFavorites(List<NhsFavoriteModel> favoriteModels) {
        this.favoriteModels = favoriteModels;
    }


    @Override
    public FavoritesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_favorites_list, parent, false);
        return new FavoritesHolder(view);
    }

    @Override
    public void onBindViewHolder(final FavoritesHolder holder, int position) {
        NhsFavoriteModel model = favoriteModels.get(position);
        holder.mTextViewFavoriteName.setText(model.getName());
        holder.vIew.setTag(this.favoriteModels.get(position));
        holder.vIew.setOnClickListener(this);
    }

    public void setData(List<NhsFavoriteModel> favoriteModels) {
        this.favoriteModels = favoriteModels;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (favoriteModels != null) {
            return favoriteModels.size();
        } else {
            return 0;
        }
    }

    @Override
    public void onClick(View view) {

        NhsFavoriteModel model = (NhsFavoriteModel)view.getTag();

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

        if (isCheck) {
//            view.setBackgroundResource(R.drawable.img_toastpopup4_foc);
            this.selectedList.add(model);
        } else {
//            view.setBackgroundColor(view.getResources().getColor(android.R.color.transparent));
        }


        if (this.onClickListener != null) {

            this.onClickListener.onClick(selectedList);

        }

    }

    public void remoteAllSelectedList(){
        this.selectedList.clear();
    }

    public LinkedList<NhsFavoriteModel> getSelectedList() {
        return selectedList;
    }

    public Object getSelectedListObj() {
        return selectedList;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class FavoritesHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewFavoriteName;
        private ViewGroup mLayoutFavorite;
        private View vIew;

        public FavoritesHolder(View itemView) {
            super(itemView);
            this.vIew = itemView;
            mTextViewFavoriteName = (TextView) itemView.findViewById(R.id.tv_favorite_name);
            mLayoutFavorite = (ViewGroup) itemView.findViewById(R.id.layout_favorites);
        }
    }
}
