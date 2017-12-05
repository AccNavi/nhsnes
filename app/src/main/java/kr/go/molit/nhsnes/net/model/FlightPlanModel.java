package kr.go.molit.nhsnes.net.model;

import java.util.ArrayList;
import java.util.List;

import kr.go.molit.nhsnes.model.FlightRouteModel;
import kr.go.molit.nhsnes.net.realm.FlightPlanInfo;

/**
 * Created by user on 2017-08-17.
 */

public class FlightPlanModel {
    private String result_code;
    private String result_msg;
    private FlightPlanInfo FplDetail;
    private ArrayList<FlightRouteModel> route;
    private List<FlightPlanInfo> list;

    public ArrayList<FlightRouteModel> getRoute() {
        return route;
    }

    public void setRoute(ArrayList<FlightRouteModel> route) {
        this.route = route;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }

    public List<FlightPlanInfo> getList() {
        return list;
    }

    public void setList(List<FlightPlanInfo> list) {
        this.list = list;
    }

    public FlightPlanInfo getFplDetail() {
        return FplDetail;
    }

    public void setFplDetail(FlightPlanInfo fplDetail) {
        FplDetail = fplDetail;
    }
}
