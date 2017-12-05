package kr.go.molit.nhsnes.common;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
* 토스트 유틸
* @author FIESTA
* @since  오전 12:48
**/
public class ToastUtile {

  /**
   * 가운데 정렬 토스트 메세지를 띄운다.
   * @param context
   * @param msg
   */
  public void showCenterText(Context context, String msg) {
    Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
    if( v != null) {
      toast.setGravity(Gravity.CENTER, 0, 0);
    }
    toast.show();
  }
}
