package kr.go.molit.nhsnes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.dialog.DialogSearchWaypoint;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class NhsWayPointActivity extends NhsBaseFragmentActivity implements View.OnClickListener {
    private View mLayoutSearchPassage;
    private View mLayoutSearchPoint;
    private View mLayoutSearchLocationName;
    private View mLayoutFavorites;
    private View mLayoutCurrentSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_way_point);
        setLayout();
    }

    /**
    * 레이아웃을 설정한다.
    * @author FIESTA
    * @version 1.0.0
    * @since 오후 5:26
    **/
    private void setLayout() {
        mLayoutSearchPassage = findViewById(R.id.layout_search_passage);
        mLayoutSearchPoint = findViewById(R.id.layout_search_point);
        mLayoutSearchLocationName = findViewById(R.id.layout_search_location_name);
        mLayoutFavorites = findViewById(R.id.layout_favorites);
        mLayoutCurrentSearch = findViewById(R.id.layout_current_search);

        mLayoutSearchPassage.setOnClickListener(this);
        mLayoutSearchPoint.setOnClickListener(this);
        mLayoutSearchLocationName.setOnClickListener(this);
        mLayoutFavorites.setOnClickListener(this);
        mLayoutCurrentSearch.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.layout_search_passage:
                Toast.makeText(NhsWayPointActivity.this,"맵화면",Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_search_point:
                new DialogSearchWaypoint(this).show();
                break;
            case R.id.layout_search_location_name:
                intent = new Intent(this, NhsDestinationSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_favorites:
                intent = new Intent(this, NhsFavoritesActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_current_search:
                intent = new Intent(this, NhsCurrentSearchActivity.class);
                startActivity(intent);
                break;
        }
    }
}
