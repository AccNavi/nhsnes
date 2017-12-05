package kr.go.molit.nhsnes.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;

import kr.go.molit.nhsnes.common.StorageUtil;
import kr.go.molit.nhsnes.common.Util;

import static kr.go.molit.nhsnes.fragment.SystemSettingFragment.AUTO_SCREEN_BRIGHTNESS;
import static kr.go.molit.nhsnes.fragment.SystemSettingFragment.SCREEN_VALUE;

/**
* 기본이 되는 Fragment
* @author FIESTA
* @version 1.0.0
* @since 2017-09-07 오전 9:40
**/
public class BaseFragment extends Fragment{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();

        boolean iskeepScreen = StorageUtil.getStorageMode(getActivity(), "keepScreen");

        if (iskeepScreen) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getActivity().getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }


        boolean checkBrightness = checkSettingPermissionCode(getActivity());

        if (checkBrightness) {

            boolean isAutoScreenBrightness = StorageUtil.getStorageModeWithDefaultValue(getContext(), AUTO_SCREEN_BRIGHTNESS, true);

            if (isAutoScreenBrightness) {

                // 자동 밝기 설정
                Util.setAutoBrightness(getActivity(), true);

                // 발기를 조절한다.
                Util.refreshBrightness(getActivity(), -1);

            } else {

                // 수동 밝기 설정
                Util.setAutoBrightness(getActivity(), false);

                // 화면 밝기에 맞게 설정한다.
                int brightness = Integer.parseInt(StorageUtil.getStorageModeEx(getActivity(), SCREEN_VALUE, "0"));

                float calValue = (brightness * 1) / (float) 100;

                // 발기를 조절한다.
                Util.refreshBrightness(getActivity(), calValue);

            }
        }
    }

    public boolean checkSettingPermissionCode(Activity context) {
        boolean permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = Settings.System.canWrite(context);
        } else {
            permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }
        if (permission) {
            return true;
        } else {
            return false;
        }
    }
}
