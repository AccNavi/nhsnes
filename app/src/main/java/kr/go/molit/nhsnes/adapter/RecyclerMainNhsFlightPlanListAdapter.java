package kr.go.molit.nhsnes.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.activity.NhsCurrentSearchActivity;
import kr.go.molit.nhsnes.activity.NhsFavoritesActivity;
import kr.go.molit.nhsnes.activity.NhsFlightHistoryActivity;
import kr.go.molit.nhsnes.activity.NhsFlightWriteActivity;
import kr.go.molit.nhsnes.dialog.DialogConfirmPath;
import kr.go.molit.nhsnes.dialog.DialogFlightDeny;
import kr.go.molit.nhsnes.dialog.DialogSearchPath;
import kr.go.molit.nhsnes.dialog.DialogSelectFlightPlain;
import kr.go.molit.nhsnes.model.NhsFlightPlainModel;
import kr.go.molit.nhsnes.model.NhsFlightPlanListModel;
import kr.go.molit.nhsnes.net.realm.FlightPlanInfo;
import kr.go.molit.nhsnes.widget.TextViewEx;


public class RecyclerMainNhsFlightPlanListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<FlightPlanInfo> nhsFlightPlanListModelList;
    private SparseArray<FlightPlanInfo> selectedIds;
    private int viewType;
    private Context context;
    public static final int VIEWTYPE_NHS_MAIN_ACT_RECENT = 0;
    public static final int VIEWTYPE_NHS_MAIN_ACT_FAV = 1;
    public static final int VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_ALL = 2;
    public static final int VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_APPROVED = 3;
    public static final int VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_DENIED = 4;
    public static final int VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_TMP = 6;
    public static final int VIEWTYPE_NHS_FLIGHT_PLAN_GET_ROUTE = 7;

    public static final int VIEWTYPE_NHS_FLIGHT_HISTORY_ACT = 5;
    private View.OnClickListener onClickListener = null;

    public RecyclerMainNhsFlightPlanListAdapter(List<FlightPlanInfo> nhsFlightPlanListModelListRecent, int viewType, Context context) {
        if (nhsFlightPlanListModelListRecent != null) {
            setData(nhsFlightPlanListModelListRecent, viewType);
        }
        this.viewType = viewType;
        this.context = context;

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setData(List<FlightPlanInfo> nhsFlightPlanListModel, int viewType) {
        this.nhsFlightPlanListModelList = nhsFlightPlanListModel;
        this.viewType = viewType;
        notifyDataSetChanged();
    }

    public ArrayList<FlightPlanInfo> getData(){
        return (ArrayList<FlightPlanInfo>)nhsFlightPlanListModelList;
    }

    public FlightPlanInfo getData(int position){
        if(null != nhsFlightPlanListModelList && position >= nhsFlightPlanListModelList.size()){
            return nhsFlightPlanListModelList.get(position);
        } else {
            return null;
        }
    }

    public void Clear(){
        if(this.selectedIds!=null){
            this.selectedIds.clear();
        }
        if(this.nhsFlightPlanListModelList!=null) {
            this.nhsFlightPlanListModelList.clear();
        }
        notifyDataSetChanged();
    }

    @Nullable
    public SparseArray<FlightPlanInfo> getSelectedItemIds() {
        if (selectedIds == null || selectedIds.size() == 0) {
            return null;
        } else {
            return selectedIds;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (this.viewType == VIEWTYPE_NHS_MAIN_ACT_RECENT ||
                this.viewType == VIEWTYPE_NHS_FLIGHT_HISTORY_ACT ||
                this.viewType == VIEWTYPE_NHS_FLIGHT_PLAN_GET_ROUTE) {

            if (this.viewType == VIEWTYPE_NHS_FLIGHT_HISTORY_ACT) {
                return new NhsFlightListHolderType3(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_main_nhsflightplan_list_type3, parent, false));
            } else {
                return new NhsFlightListHolderType4(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_main_nhsflightplan_list_type4, parent, false));
            }
//
        } else if (this.viewType == VIEWTYPE_NHS_MAIN_ACT_FAV) {
            return new NhsFlightListHolderType2(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_main_nhsflightplan_list_type2, parent, false));
        } else if (this.viewType == VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_ALL
                || this.viewType == VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_APPROVED
                || this.viewType == VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_DENIED
                || this.viewType == VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_TMP) {
            return new NhsFlightListHolderType3(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_main_nhsflightplan_list_type3, parent, false));
        } else {
            return null;
//            return new NhsFlightListHolderType1(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_main_nhsflightplan_list_type1, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final FlightPlanInfo data = nhsFlightPlanListModelList.get(position);

        if (holder instanceof NhsFlightListHolderType1) {
            final NhsFlightListHolderType1 type1 = (NhsFlightListHolderType1) holder;

            String callsign = data.getCallsign();
            if(callsign==null){
                callsign = data.getAcrftCd();
            }
            //type1.tvTitle.setText(callsign + " " + data.getAcrftType() + " " + data.getPlanDeparture() + " " + data.getPlanArrival() + " (" + data.getPlanDate() + ")");

            type1.tvTitle.setText(callsign + " " + data.getAcrftType() + "(" + data.getPlanDate() + ")");

            //type1.tvDate.setText(format.format(data.getRegDate()));
            type1.tvDate.setText(data.getPlanDate());
            type1.bgLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = type1.tvTitle.getContext();
                    Intent intent = new Intent(context, NhsCurrentSearchActivity.class);
                    context.startActivity(intent);
                }
            });
        } else if (holder instanceof NhsFlightListHolderType2) {
            final NhsFlightListHolderType2 type2 = (NhsFlightListHolderType2) holder;

            String callsign = data.getCallsign();
            if(callsign==null){
                callsign = data.getAcrftCd();
            }
            type2.tvTitle.setText(callsign + " " + data.getAcrftType() + "(" + data.getPlanDate() + ")");

//            type2.tvDate.setText(data.getDate());
            String planDoccd = data.getPlanDoccd();
            if(planDoccd!=null && planDoccd.trim().equals("01")){
                type2.cbPossible.setChecked(true);
            } else {
                type2.cbPossible.setChecked(false);
            }
            //type2.cbPossible.setChecked(data.getSuperLightPlane());
            type2.bgLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = type2.tvTitle.getContext();
                    Intent intent;
                    if (viewType == VIEWTYPE_NHS_MAIN_ACT_FAV) {
                        intent = new Intent(context, NhsFavoritesActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
        } else if (holder instanceof NhsFlightListHolderType3) {
            final NhsFlightListHolderType3 type3 = (NhsFlightListHolderType3) holder;

            String callsign = data.getCallsign();
            if(callsign==null){
                callsign = data.getAcrftCd();
            }

            if (data.getPlanDate() != null) {
                type3.tvTitle.setText(callsign + " " + data.getAcrftType() + "(" + data.getPlanDate() + ")");
            } else {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm");
                String createAT = sdf.format(data.getCreateAt());
                type3.tvTitle.setText(callsign + " " + data.getAcrftType() + "(" + createAT + ")");
            }
            String planDoccd = data.getPlanDoccd();
            if(planDoccd!=null && planDoccd.trim().equals("01")){
                type3.cbPossible.setChecked(true);
            } else {
                type3.cbPossible.setChecked(false);
            }

            if(selectedIds != null && selectedIds.get(position)!=null){
                type3.cbSquare.setChecked(true);
            } else {
                type3.cbSquare.setChecked(false);
            }


            type3.cbSquare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (selectedIds == null) {
                        selectedIds = new SparseArray<>();
                    }
                    if (isChecked) {
                        //selectedIds.put(position, data.getFlightIdentity());
                        selectedIds.put(position, data);
                    } else {
                        selectedIds.remove(position);
                    }
                    Log.d("##", selectedIds.toString());
                }
            });
            type3.bgLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != data.getPlanStatus() && data.getPlanStatus().trim().equals("99")){
                        //DialogFlightDeny dialog = new DialogFlightDeny(context, data);
                        //dialog.show();
                    }else if (viewType == VIEWTYPE_NHS_FLIGHT_HISTORY_ACT) {
                        DialogSearchPath dialog = new DialogSearchPath(context, data);
                        //DialogSearchPath dialog = new DialogSearchPath(context, data.getDepartureAerodrome());
                        dialog.show();
                    } else if(viewType == VIEWTYPE_NHS_FLIGHT_PLAN_LIST_ACT_TMP){
                        Context context = type3.tvTitle.getContext();
                        // 임시저장 클릭
                        Intent intent = new Intent (context, NhsFlightWriteActivity.class);
                        intent.putExtra(DialogSelectFlightPlain.INTENT_PLAN_DATA, data.getCallsign());
                        intent.putExtra(DialogSelectFlightPlain.INTENT_PLAN_TYPE, viewType);
                        context.startActivity(intent);
                    } else {
                        DialogSelectFlightPlain dialog = new DialogSelectFlightPlain(context, data);
                        dialog.show();
                    }
                }
            });
        } else if (holder instanceof NhsFlightListHolderType4) {
            NhsFlightListHolderType4 type4 = (NhsFlightListHolderType4) holder;

            String callsign = data.getCallsign();
            if(callsign==null){
                callsign = data.getAcrftCd();
            }

            if (this.viewType == VIEWTYPE_NHS_FLIGHT_PLAN_GET_ROUTE) {
                DecimalFormat format = new DecimalFormat(".####");
                String msg = data.getPlanDate() + " " + data.getPlanDeparture() + " " + data.getPlanArrival() + "\n";

                if (!data.getPlanRoute().isEmpty()) {

                    String[] split = data.getPlanRoute().split("\n");

                    int i, j = 0;
                    int size = split.length;
                    int size2 = 0;
                    String[] splite2;

                    for (i=0; i<size; i++) {

                        splite2 = split[i].split(" ");
                        size2 = splite2.length;

                        for (j=0; j<size2; j++) {
                            msg += format.format(Double.parseDouble(splite2[j]))+"...";
                            if (j == 0) {
                                msg += " / ";
                            }
                        }

                        if (i < size-1) {
                            msg += "\n";
                        }
                    }


                }
                type4.tvTitle.setText(msg);
            } else {
                type4.tvTitle.setText(callsign + " " + data.getAcrftType() + " " + data.getPlanDeparture() + " " + data.getPlanArrival() + " (" + data.getPlanDate() + ")");
            }
            String planDoccd = data.getPlanDoccd();
            if(planDoccd!=null && planDoccd.trim().equals("01")){
                type4.cbAllow.setChecked(true);
            } else {
                type4.cbAllow.setChecked(false);
            }
            //type4.cbAllow.setChecked(data.getSuperLightPlane());
            if (this.onClickListener  != null) {
                type4.bgLayout.setTag(position);
                type4.bgLayout.setOnClickListener(this.onClickListener);
            } else if (viewType == VIEWTYPE_NHS_MAIN_ACT_RECENT) {

                type4.bgLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogSearchPath dialog = new DialogSearchPath(context, data);
                        //DialogSearchPath dialog = new DialogSearchPath(context, data.getDepartureAerodrome());
                        dialog.show();
                    }
                });

            }else {
                type4.bgLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (viewType == VIEWTYPE_NHS_FLIGHT_HISTORY_ACT) {
                            DialogSearchPath dialog = new DialogSearchPath(context, data);
                            //DialogSearchPath dialog = new DialogSearchPath(context, data.getDepartureAerodrome());
                            dialog.show();
                        } else {
                            DialogSelectFlightPlain dialog = new DialogSelectFlightPlain(context, data.getCallsign());
                            //DialogSelectFlightPlain dialog = new DialogSelectFlightPlain(context, data.getFlightIdentity());
                            dialog.show();
                        }

                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        if (this.nhsFlightPlanListModelList == null) {
            return 0;
        }
        return this.nhsFlightPlanListModelList.size();
    }

    public class NhsFlightListHolderType1 extends RecyclerView.ViewHolder {
        LinearLayout bgLayout;
        TextViewEx tvDate;
        TextViewEx tvTitle;

        public NhsFlightListHolderType1(View itemView) {
            super(itemView);
            bgLayout = (LinearLayout) itemView.findViewById(R.id.bg_layout);
            tvDate = (TextViewEx) itemView.findViewById(R.id.tv_date);
            tvTitle = (TextViewEx) itemView.findViewById(R.id.tv_title);
        }
    }

    public class NhsFlightListHolderType2 extends RecyclerView.ViewHolder {
        LinearLayout bgLayout;
        TextViewEx tvDate;
        TextViewEx tvTitle;
        CheckBox cbPossible;

        public NhsFlightListHolderType2(View itemView) {
            super(itemView);
            bgLayout = (LinearLayout) itemView.findViewById(R.id.bg_layout);
            tvDate = (TextViewEx) itemView.findViewById(R.id.tv_date);
            tvDate.setVisibility(View.GONE);
            tvTitle = (TextViewEx) itemView.findViewById(R.id.tv_title);
            cbPossible = (CheckBox) itemView.findViewById(R.id.cb_star);

        }
    }

    public class NhsFlightListHolderType3 extends RecyclerView.ViewHolder {
        LinearLayout bgLayout;
        TextViewEx tvTitle;
        CheckBox cbPossible;
        CheckBox cbSquare;


        public NhsFlightListHolderType3(View itemView) {
            super(itemView);
            bgLayout = (LinearLayout) itemView.findViewById(R.id.bg_layout);
            tvTitle = (TextViewEx) itemView.findViewById(R.id.tv_title);
            cbPossible = (CheckBox) itemView.findViewById(R.id.cb_star);
            cbSquare = (CheckBox) itemView.findViewById(R.id.cb_square);

        }
    }

    public class NhsFlightListHolderType4 extends RecyclerView.ViewHolder {
        LinearLayout bgLayout;
        TextViewEx tvTitle;
        CheckBox cbAllow;


        public NhsFlightListHolderType4(View itemView) {
            super(itemView);
            bgLayout = (LinearLayout) itemView.findViewById(R.id.bg_layout);
            tvTitle = (TextViewEx) itemView.findViewById(R.id.tv_title);
            cbAllow = (CheckBox) itemView.findViewById(R.id.cb_allow);

        }
    }


    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
