package kr.go.molit.nhsnes.model;
/**
 * 비행 계획 기본 모델
 * @author 문종락.
 * @version 1.0, 2017.04.01 최초 작성
 **/

public class NhsFlightDefaultPlanListModel extends NhsBaseModel {
    private int planId;
    private String date;
    private String title;

    public NhsFlightDefaultPlanListModel(int planId, String date, String title) {
        this.planId = planId;
        this.date = date;
        this.title = title;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }
}
