package kr.go.molit.nhsnes.activity;

import static kr.go.molit.nhsnes.activity.NhsConfigActivity.TAG_MOVE_PAGE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import kr.go.molit.nhsnes.R;


/**
 * 환경설정
 *
 * @author FIESTA
 * @since 오전 12:29
 **/
public class NhsSettingtActivity extends NhsBaseFragmentActivity implements View.OnClickListener{

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_setting);

    setLayout();
  }

  private void setLayout() {

    findViewById(R.id.ll_flight_setting).setOnClickListener(this);
    findViewById(R.id.ll_config).setOnClickListener(this);
    findViewById(R.id.ll_system_setting).setOnClickListener(this);

  }

  @Override
  public void onClick(View v) {

    Intent config = null;

    switch (v.getId()) {

      // 비행 정보 설정
      case R.id.ll_flight_setting:

        config = new Intent(NhsSettingtActivity.this, NhsConfigActivity.class);
        config.putExtra(TAG_MOVE_PAGE, 0);
        startActivity(config);

        break;

      // 비행 표시 설정
      case R.id.ll_config:

        config = new Intent(NhsSettingtActivity.this, NhsConfigActivity.class);
        config.putExtra(TAG_MOVE_PAGE, 1);
        startActivity(config);

        break;

      // 시스템 설정
      case R.id.ll_system_setting:

        config = new Intent(NhsSettingtActivity.this, NhsConfigActivity.class);
        config.putExtra(TAG_MOVE_PAGE, 2);
        startActivity(config);

        break;



    }
  }



}

