package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.widget.TextViewEx;


/**
 * 즐겨찾기 추가
 *
 * @author FIESTA
 * @since 오전 2:44
 **/
public class DialogAddFavorites extends DialogBase implements View.OnClickListener, View.OnTouchListener, View.OnDragListener {

    private Context mContext;
    private IFavorite mIFavorite;
    public String start = "";
    public String end = "";
    public String route = "";

    private EditText mEtFavoriteName;

    private boolean isSingleMode = false;
    private String title = "";
    String[] spritRoute = null;

    public void setRoute(String route) {
        this.route = route;
    }

    public DialogAddFavorites(@NonNull Context context, IFavorite iFavorite) {
        super(context);
        mContext = context;
        this.mIFavorite = iFavorite;
    }

    /**
     * 하나만 즐겨찾기를 원할 경우
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 2017-10-20 오전 10:31
     **/
    public void setSingleMode(String title) {
        this.isSingleMode = true;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (this.isSingleMode) {

            setContentView(R.layout.dialog_add_single_favorites);
            ((TextViewEx) findViewById(R.id.tve_name)).setText(this.title);

        } else {
            setContentView(R.layout.dialog_add_favorites);
        }

        mEtFavoriteName = (EditText) findViewById(R.id.et_favorite_name);
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mEtFavoriteName.getText().toString();
                if (null != str && str.length() > 0) {
                    mIFavorite.onSave(str);
                    dismiss();
                } else {
                    Toast.makeText(mContext, "즐겨찾기 명을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIFavorite.onCancel();
                dismiss();
            }
        });


        ViewGroup vgParentRoute = (ViewGroup) findViewById(R.id.ll_routes);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        String[] starts = this.start.split(" ");
        String[] ends = this.end.split(" ");
        String[] routs = this.route.split(" ");
        String n, e;

        DecimalFormat form = new DecimalFormat(".#######");
        double dNumber = 10.12345;
        System.out.println(form.format(dNumber));

        // 출발지
        if (!this.start.isEmpty()) {

            n = "N " + form.format(Double.parseDouble(starts[0]));
            e = "E " + form.format(Double.parseDouble(starts[1]));

            ((TextViewEx) findViewById(R.id.tv_start)).setText(n + " " + e);


        }
        // 목적지
        if (!this.end.isEmpty()) {

            n = "N " + form.format(Double.parseDouble(ends[0]));
            e = "E " + form.format(Double.parseDouble(ends[1]));

            ((TextViewEx) findViewById(R.id.tv_end)).setText(n + " " + e);

        }

        // 경유지 지정
        if (!this.route.isEmpty()) {

            this.spritRoute = this.route.split("\\n");
            String[] dataRoute;

            int size = this.spritRoute.length;
            int i = 0;

            for (i = 0; i < size; i++) {

                TextViewEx view = (TextViewEx) inflater.inflate(R.layout.item_route, null);
                vgParentRoute.addView(view);

                dataRoute = this.spritRoute[i].split(" ");

                n = "N " + form.format(Double.parseDouble(dataRoute[0]));
                e = "E " + form.format(Double.parseDouble(dataRoute[1]));

                view.setText(n + " " + e);
                view.setTag(i);
                view.setOnTouchListener(this);
                view.setOnDragListener(this);

            }

        }


    }

    @Override
    public void onClick(View v) {

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

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {

        if (dragEvent.getAction() == DragEvent.ACTION_DROP) {
            //handle the dragged view being dropped over a target view
            TextViewEx dropped = (TextViewEx) dragEvent.getLocalState();
            TextViewEx dropTarget = (TextViewEx) view;

            String temp = dropped.getText().toString();
            Object tagTemp1 = dropped.getTag();
            Object tagTemp2 = dropTarget.getTag();

            // 스위치..
            String splitTemp2 = this.spritRoute[(int) tagTemp2];
            this.spritRoute[(int) tagTemp2] = this.spritRoute[(int) tagTemp1];
            this.spritRoute[(int) tagTemp1] = splitTemp2;

            dropped.setText(dropTarget.getText().toString());
            dropped.setTag(dropTarget.getTag());
            dropTarget.setText(temp);
            dropTarget.setTag(tagTemp1);


            if (this.spritRoute != null) {

                int size = this.spritRoute.length;
                int i = 0;

                StringBuilder sb = new StringBuilder();

                for (i = 0; i < size; i++) {

                    sb.append(this.spritRoute[i]);
                    sb.append("\n");
                }

                this.route = sb.toString();
            }

            //stop displaying the view where it was before it was dragged
//            dropped.setVisibility(View.INVISIBLE);
//
//            //if an item has already been dropped here, there will be different string
//            String text=dropTarget.getText().toString();
//            //if there is already an item here, set it back visible in its original place
//            if(text.equals(text1.getText().toString())) text1.setVisibility(View.VISIBLE);
//            else if(text.equals(text2.getText().toString())) text2.setVisibility(View.VISIBLE);
//            else if(text.equals(text3.getText().toString())) text3.setVisibility(View.VISIBLE);
//
//            //update the text and color in the target view to reflect the data being dropped
//            dropTarget.setText(dropped.getText());
//            dropTarget.setBackgroundColor(Color.BLUE);
        }
        return true;

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(null, shadowBuilder, view, 0);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {



        super.setOnDismissListener(listener);
    }

    public interface IFavorite {
        public void onSave(String name);

        public void onCancel();
    }

    }