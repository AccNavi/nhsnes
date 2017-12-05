package kr.go.molit.nhsnes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.model.NhsDestinationSearchMapModel;
import kr.go.molit.nhsnes.widget.TextViewEx;

/**
 * Created by jongrakmoon on 2017. 4. 1..
 */

public class RecyclerDestinationSearchMapList extends RecyclerView.Adapter<RecyclerDestinationSearchMapList.DestinationMapHolder> {

    private List<NhsDestinationSearchMapModel> destinationSearchMapModels;
    private int remainTime;
    private long arriveTime;
    private float distance;

    private DateFormat dateFormat;

    public RecyclerDestinationSearchMapList(int remainTime, long arriveTime, float distance) {
        this.destinationSearchMapModels = new ArrayList<>();
        this.remainTime = remainTime;
        this.arriveTime = arriveTime;
        this.distance = distance;
        this.dateFormat = new SimpleDateFormat("a HH:mm분 도착예정");
    }

    public RecyclerDestinationSearchMapList(int remainTime, long arriveTime, float distance, List<NhsDestinationSearchMapModel> destinationSearchMapModels) {
        this(remainTime, arriveTime, distance);
        this.destinationSearchMapModels = destinationSearchMapModels;
    }


    @Override
    public DestinationMapHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_destination_search_map_list, parent, false);
        return new DestinationMapHolder(view);
    }

    @Override
    public void onBindViewHolder(final DestinationMapHolder holder, int position) {
        if (position == 0) {
            holder.mIvIcon.setImageResource(R.drawable.icon_pin);
        } else if (position == 1) {
            holder.mIvIcon.setImageResource(R.drawable.icon_pin_s);
        } else if (position == destinationSearchMapModels.size()) {
            holder.mIvIcon.setImageResource(R.drawable.icon_pin_g);
        } else {
            holder.mIvIcon.setImageResource(R.drawable.icon_pin_w);
        }

        //헤더뷰
        if (position == 0) {
            holder.mLayoutHeader.setVisibility(View.VISIBLE);
            holder.mTextViewRemainTime.setText(remainTime + "분");
            holder.mTextViewArriveTime.setText(dateFormat.format(arriveTime));
            holder.mTextViewDistance.setText((Math.round(distance * 10) / 10) + "km");
        } else {
            holder.mLayoutHeader.setVisibility(View.GONE);
            NhsDestinationSearchMapModel model = destinationSearchMapModels.get(position - 1);
            if (position == 1) {
                holder.mTextViewAddressName.setText("출발지: ");
            } else if (position == destinationSearchMapModels.size()) {
                holder.mTextViewAddressName.setText("도착지: ");
            } else {
                holder.mTextViewAddressName.setText("경유지: ");
            }
            holder.mTextViewAddressName.append(model.getName());

            holder.mTextViewAddress.setText(model.getAddress());
        }


    }

    public void setData(List<NhsDestinationSearchMapModel> destinationSearchMapModels) {
        this.destinationSearchMapModels = destinationSearchMapModels;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (destinationSearchMapModels != null) {
            return destinationSearchMapModels.size() + 1;
        } else {
            return 0;
        }
    }

    public List<NhsDestinationSearchMapModel> makeFakeData() {
        List<NhsDestinationSearchMapModel> models = new ArrayList<>();
        NhsDestinationSearchMapModel model = new NhsDestinationSearchMapModel();
        model.setName("탄천 비행장");
        model.setAddress("경기도 성남수 분당구 수내동");
        models.add(model);
        model = new NhsDestinationSearchMapModel();
        model.setName("남양주 비행장");
        model.setAddress("경기도 남양주시 진건읍 배양");
        models.add(model);
        model = new NhsDestinationSearchMapModel();
        model.setName("수원 비행장");
        model.setAddress("경기도 수원시 권선구 세류동");
        models.add(model);
        return models;
    }

    public class DestinationMapHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLayoutDestinationMap;
        private ImageView mIvIcon;
        private RelativeLayout mLayoutHeader;
        private TextViewEx mTextViewDistance;
        private TextViewEx mTextViewRemainTime;
        private TextViewEx mTextViewArriveTime;
        private TextViewEx mTextViewAddressName;
        private TextViewEx mTextViewAddress;

        public DestinationMapHolder(View itemView) {
            super(itemView);
            mLayoutDestinationMap = (LinearLayout) itemView.findViewById(R.id.layout_destination_map);
            mIvIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            mLayoutHeader = (RelativeLayout) itemView.findViewById(R.id.layout_header);
            mTextViewDistance = (TextViewEx) itemView.findViewById(R.id.tv_distance);
            mTextViewRemainTime = (TextViewEx) itemView.findViewById(R.id.tv_remain_time);
            mTextViewArriveTime = (TextViewEx) itemView.findViewById(R.id.tv_arrive_time);
            mTextViewAddressName = (TextViewEx) itemView.findViewById(R.id.tv_address_name);
            mTextViewAddress = (TextViewEx) itemView.findViewById(R.id.tv_address);
        }
    }
}
