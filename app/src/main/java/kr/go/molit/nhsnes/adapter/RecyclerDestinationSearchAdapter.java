package kr.go.molit.nhsnes.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.model.NhsDestinationSearchModel;

/**
 * Created by jongrakmoon on 2017. 4. 4..
 */

public class RecyclerDestinationSearchAdapter extends RecyclerView.Adapter<RecyclerDestinationSearchAdapter.DestinationHolder> implements View.OnClickListener{


    private DateFormat dateFormat;

    private List<NhsDestinationSearchModel> destinationSearchModels;

    private ISearchClickListener onClickListener;
    private LinkedList<NhsDestinationSearchModel> selectedList = new LinkedList<>();

    public RecyclerDestinationSearchAdapter() {
        this.dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        this.destinationSearchModels = new ArrayList<>();
    }

    public RecyclerDestinationSearchAdapter(List<NhsDestinationSearchModel> destinationSearchModels) {
        this();
        this.destinationSearchModels = destinationSearchModels;
        sort();
    }


    @Override
    public RecyclerDestinationSearchAdapter.DestinationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_destination_search_list, parent, false);
        return new RecyclerDestinationSearchAdapter.DestinationHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerDestinationSearchAdapter.DestinationHolder holder, int position) {
        NhsDestinationSearchModel model = destinationSearchModels.get(position);

        if (!TextUtils.isEmpty(model.getDestinationDetail())) {
            holder.mImageViewIcon.setImageResource(R.drawable.icon_waypoint1);
            holder.mTextViewDestinationDetail.setVisibility(View.VISIBLE);
            holder.mTextViewDestinationDetail.setText(model.getDestinationDetail());
        } else {
            holder.mImageViewIcon.setImageResource(R.drawable.icon_search3);
            holder.mTextViewDestinationDetail.setVisibility(View.GONE);
        }

        holder.mTextViewDestination.setText(model.getDestinationName());
        holder.mTextViewDate.setText(dateFormat.format(model.getDate()));

        holder.view.setTag(this.destinationSearchModels.get(position));
        holder.view.setOnClickListener(this);
    }

    public void setData(List<NhsDestinationSearchModel> destinationSearchModels) {
        this.destinationSearchModels = destinationSearchModels;
//        sort();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (destinationSearchModels != null) {
            return destinationSearchModels.size();
        } else {
            return 0;
        }
    }

    public class DestinationHolder extends RecyclerView.ViewHolder {

        private View view = null;
        private TextView mTextViewDestination;
        private TextView mTextViewDestinationDetail;
        private TextView mTextViewDate;
        private ImageView mImageViewIcon;

        public DestinationHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            mTextViewDestination = (TextView) itemView.findViewById(R.id.tv_destination);
            mTextViewDestinationDetail = (TextView) itemView.findViewById(R.id.tv_destination_detail);
            mTextViewDate = (TextView) itemView.findViewById(R.id.tv_date);
            mImageViewIcon = (ImageView) itemView.findViewById(R.id.iv_search_icon);
        }
    }

    public static List<NhsDestinationSearchModel> makeFakeData() {

        List<NhsDestinationSearchModel> models = new ArrayList<>();

        NhsDestinationSearchModel model = new NhsDestinationSearchModel();

        for (int i = 0; i < 10; i++) {
            model = new NhsDestinationSearchModel();
            model.setDestinationName("남양주" + i);
            model.setLongitude("123.12334");
            model.setLatitude("80.132334");
            model.setDate(System.currentTimeMillis());
            models.add(model);
        }

        for (int i = 0; i < 2; i++) {
            model = new NhsDestinationSearchModel();
            model.setDestinationName("남양주" + i);
            model.setDestinationDetail("경기도 남양주시 진건읍 배양리" + i);
            model.setLongitude("123.12334");
            model.setLatitude("80.132334");
            model.setDate(System.currentTimeMillis());
            models.add(model);
        }
        return models;
    }

    private void sort() {
        List<NhsDestinationSearchModel> sortedModel = new ArrayList<>();

        // 널값 아닌걸 앞으로
        for (int i = 0; i < destinationSearchModels.size(); i++) {
            if (destinationSearchModels.get(i).getDestinationDetail() != null) {
                sortedModel.add(destinationSearchModels.get(i));
            }
        }

        // 널값인것을 뒤로
        for (int i = 0; i < destinationSearchModels.size(); i++) {
            if (destinationSearchModels.get(i).getDestinationDetail() == null) {
                sortedModel.add(destinationSearchModels.get(i));
            }
        }

        this.destinationSearchModels = sortedModel;
    }

    @Override
    public void onClick(View view) {

        NhsDestinationSearchModel model = (NhsDestinationSearchModel)view.getTag();

//        double _x = Double.parseDouble(model.getLongitude());
//        double _y = Double.parseDouble(model.getLatitude());
//
//        Log.i("TEST","Click:::"+_x + " " + _y + " name : " + model.getDestinationName());
//
//        int i = 0;
//        int size = this.selectedList.size();
//        boolean isCheck = true;
//
//        for (i = 0; i < size; i++) {
//            if (this.selectedList.get(i) == model) {
//                isCheck = false;
//                this.selectedList.remove(i);
//                break;
//            } else {
//                isCheck = true;
//            }
//        }
//
//        if (isCheck) {
//            view.setBackgroundResource(R.drawable.img_toastpopup4_foc);
//            this.selectedList.add(model);
//        } else {
//            view.setBackgroundResource(0);
//        }

        if (this.onClickListener != null) {
            this.onClickListener.onClick(model);
        }

    }

    public ISearchClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(ISearchClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Object getSelectedListObj() {
        return selectedList;
    }


    /**
     * 검색 데이터 전달
     */
    public interface ISearchClickListener {
        void onClick(NhsDestinationSearchModel model);
    }

}
