package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import kr.go.molit.nhsnes.R;

/**
 * 인디케이터를 사용하기 위한 클래스
 *
 * @author 정제영
 * @version 1.0, 2017.03.06 최초 작성
 **/

public class LoadingDialog extends DialogBase {
    private boolean isBackPress = true;

    public void setBackPress(boolean isBack) {
        this.isBackPress = isBack;
    }

    public static LoadingDialog create(Context context, CharSequence title,
                                       CharSequence message) {
        return create(context, title, message, false);
    }

    public static LoadingDialog create(Context context, CharSequence title,
                                       CharSequence message, boolean indeterminate) {
        return create(context, title, message, indeterminate, false, null);
    }

    public static LoadingDialog create(Context context, CharSequence title,
                                       CharSequence message, boolean indeterminate, boolean cancelable) {
        return create(context, title, message, indeterminate, cancelable, null);
    }

    public static LoadingDialog create(Context context, CharSequence title,
                                       CharSequence message, boolean indeterminate,
                                       boolean cancelable, OnCancelListener cancelListener) {
        LoadingDialog dialog = new LoadingDialog(context);
        dialog.setTitle(title);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        /* The next line will add the ProgressBar to the dialog. */
        dialog.addContentView(new ProgressBar(context), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return dialog;
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        super.show();
    }

    public LoadingDialog(Context context) {
        super(context, R.style.indicatorDialog);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        if (isShowing() && isBackPress) {
            dismiss();
        }
    }
}
