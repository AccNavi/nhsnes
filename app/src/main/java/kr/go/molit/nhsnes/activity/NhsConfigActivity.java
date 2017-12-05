package kr.go.molit.nhsnes.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.fragment.FlightInfoFragment;
import kr.go.molit.nhsnes.fragment.FlightMarkFragment;
import kr.go.molit.nhsnes.fragment.SystemSettingFragment;
import kr.go.molit.nhsnes.widget.ButtonEx;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class NhsConfigActivity extends NhsBaseFragmentActivity implements View.OnClickListener {
    private static final String TAG_FLIGHT_INFO = "flight_info";
    private static final String TAG_FLIGHT_MARK = "flight_mark";
    private static final String TAG_SYSTEM_SETTING = "system_setting";
    public static final String TAG_MOVE_PAGE = "move_page";

    private Fragment mFragmentFlightInfo, mFragmentFlightMark, mFragmentSystemSetting;

    private ButtonEx mButtonExFlightInfo, mButtonExFlightMark, mButtonExSystemSetting;
    private ViewGroup mLayoutConfigTab;

    private int selectedColor, unselectedColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_config);
        setLayout();
        moveInitPage();
    }

    /**
    * 로드시 이동할 페이지를 결정한다.
    * @author FIESTA
    * @since  오전 12:16
    **/
    private void moveInitPage(){

        Bundle data = getIntent().getExtras();
        int movePage = 0;

        if (data != null) {

            movePage = data.getInt(TAG_MOVE_PAGE, 0);

        }

        switch (movePage) {

            case 0:
                setViewFlightInfo();
                break;

            case 1:
                setViewFlightMark();
                break;

            case 2:
                setViewSystemSetting();
                break;

            default:
                setViewFlightInfo();
                break;

        }

    }

    private void setLayout() {
        selectedColor = Color.WHITE;
        unselectedColor = getResources().getColor(R.color.textColorTransparentWhite);

        mButtonExFlightInfo = (ButtonEx) findViewById(R.id.bt_flight_info);
        mButtonExFlightInfo.setOnClickListener(this);
        mButtonExFlightMark = (ButtonEx) findViewById(R.id.bt_flight_mark);
        mButtonExFlightMark.setOnClickListener(this);
        mButtonExSystemSetting = (ButtonEx) findViewById(R.id.bt_system_setting);
        mButtonExSystemSetting.setOnClickListener(this);
        mLayoutConfigTab = (ViewGroup) findViewById(R.id.layout_config_tab);

//        setViewFlightInfo();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_flight_info:
                setViewFlightInfo();
                break;
            case R.id.bt_flight_mark:
                setViewFlightMark();
                break;
            case R.id.bt_system_setting:
                setViewSystemSetting();
                break;
        }
    }

    private void setViewFlightInfo() {

//        if (mFragmentFlightInfo == null) {
            mFragmentFlightInfo = new FlightInfoFragment();
//        }
        replaceContent(mFragmentFlightInfo, TAG_FLIGHT_INFO, R.id.layoutContents);
        mLayoutConfigTab.setBackgroundResource(R.drawable.img_tab3_1);
        mButtonExFlightInfo.setTextColor(selectedColor);
        mButtonExFlightMark.setTextColor(unselectedColor);
        mButtonExSystemSetting.setTextColor(unselectedColor);

    }

    private void setViewFlightMark() {
//        if (mFragmentFlightMark == null) {
            mFragmentFlightMark = new FlightMarkFragment();
//        }
        replaceContent(mFragmentFlightMark, TAG_FLIGHT_MARK, R.id.layoutContents);
        mLayoutConfigTab.setBackgroundResource(R.drawable.img_tab3_2);
        mButtonExFlightInfo.setTextColor(unselectedColor);
        mButtonExFlightMark.setTextColor(selectedColor);
        mButtonExSystemSetting.setTextColor(unselectedColor);

    }

    private void setViewSystemSetting() {
//        if (mFragmentSystemSetting == null) {
            mFragmentSystemSetting = new SystemSettingFragment();
//        }
        replaceContent(mFragmentSystemSetting, TAG_SYSTEM_SETTING, R.id.layoutContents);
        mLayoutConfigTab.setBackgroundResource(R.drawable.img_tab3_3);
        mButtonExFlightInfo.setTextColor(unselectedColor);
        mButtonExFlightMark.setTextColor(unselectedColor);
        mButtonExSystemSetting.setTextColor(selectedColor);

    }
}
