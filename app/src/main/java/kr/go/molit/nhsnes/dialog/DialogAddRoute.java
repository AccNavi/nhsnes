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
 * 경유지 추가 팝업
 *
 * @author FIESTA
 * @since 오후 8:02
 **/
public class DialogAddRoute extends DialogBase {

  private View.OnClickListener onClickListener;

  public DialogAddRoute(@NonNull Context context, int mode, View.OnClickListener onClickListener) {
    super(context);
    this.onClickListener = onClickListener;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_add_route);

    findViewById(R.id.btn_add).setOnClickListener(onClickListener);
    findViewById(R.id.btn_complate).setOnClickListener(onClickListener);
    findViewById(R.id.btn_cancel).setOnClickListener(this.onClickListener);

    setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss(DialogInterface dialogInterface) {
        if (isPushButton) {
          DialogAddRoute.this.onClickListener.onClick(findViewById(R.id.btn_add));
        }
      }
    });


  }



}
