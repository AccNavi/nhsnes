package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.interfaces.OnStartDragListener;


/**
 * 화면 검색에서 경로 수정을 위한 다이얼로그
 *
 * @author FIESTA
 * @since 오후 8:02
 **/
public class DialogComplateSelectMap extends DialogBase implements OnStartDragListener {

  private View.OnClickListener onClickListener;

  private String start = "";
  private String end = "";
  private String route = "";

  public DialogComplateSelectMap(@NonNull Context context, int mode, View.OnClickListener onClickListener) {
    super(context);
    this.onClickListener = onClickListener;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_complate_select_map);

    TextView tvStart = (TextView) findViewById(R.id.tv_start);
    TextView tvEnd = (TextView) findViewById(R.id.tv_end);
    TextView tvRoute = (TextView) findViewById(R.id.tv_route);

    findViewById(R.id.btn_add).setOnClickListener(onClickListener);
    findViewById(R.id.btn_flight).setOnClickListener(onClickListener);
    findViewById(R.id.btn_cancel).setOnClickListener(this.onClickListener);

    tvStart.setText(this.start);
    tvEnd.setText(this.end);
    tvRoute.setText(this.route);

    setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss(DialogInterface dialogInterface) {
        if (isPushButton) {
          DialogComplateSelectMap.this.onClickListener.onClick(findViewById(R.id.btn_add));
        }
      }
    });


  }


  @Override
  public void onStartDrag(RecyclerView.ViewHolder viewHolder) {

  }


  public String getStart() {
    return start;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public String getEnd() {
    return end;
  }

  public void setEnd(String end) {
    this.end = end;
  }

  public String getRoute() {
    return route;
  }

  public void setRoute(String route) {
    this.route = route;
  }

}
