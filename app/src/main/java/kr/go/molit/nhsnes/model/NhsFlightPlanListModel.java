package kr.go.molit.nhsnes.model;

/**
 * 비행 계획 비행 가능, 체크할 수 있는 모델
 *
 * @author 문종락.
 * @version 1.0, 2017.04.01 최초 작성
 **/

public class NhsFlightPlanListModel extends NhsFlightDefaultPlanListModel {
    private boolean isChecked = false;
    private boolean isPossible;

    //앱 메인화면 모델
    public NhsFlightPlanListModel(int planId, String date, String title, boolean isPossible) {
        super(planId, date, title);
        this.isPossible = isPossible;
    }


    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean getPossible() {
        return isPossible;
    }

    public void setPossible(boolean possible) {
        isPossible = possible;
    }
}
