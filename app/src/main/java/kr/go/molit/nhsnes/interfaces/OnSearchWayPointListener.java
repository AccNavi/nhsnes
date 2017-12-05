package kr.go.molit.nhsnes.interfaces;

/**
 * DialogSearchWaypoint 에 사용될 인터페이스
 *
 * @author FIESTA
 * @since 오후 9:00
 **/
public interface OnSearchWayPointListener {

  /**
   * 확정된 경로를 반환한다.
   *
   * @param type  경로 type (DialogSearchWaypoint 상수 참조 : ex) TYPE_NONE)
   * @param model NhsWaypoinSearchModel 데이터
   * @author FIESTA
   * @since 오후 9:03
   **/
  public void onComplate(int type, Object model);
  public void onComplate();
  public void onCancel();
  public void onNextSelect();
  public void onDelete();
}
