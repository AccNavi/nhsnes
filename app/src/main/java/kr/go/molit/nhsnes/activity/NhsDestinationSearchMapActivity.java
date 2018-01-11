package kr.go.molit.nhsnes.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.adapter.RecyclerDestinationSearchMapList;

/**
 * Destination search map 화면
 *
 * @author FIESTA
 * @version 1.0.0
 * @since 오후 5:11
 **/
public class NhsDestinationSearchMapActivity extends NhsBaseFragmentActivity {
    private RecyclerView mRecyclerViewDestinationSearchMap;
    private RecyclerDestinationSearchMapList mRecyclerDestinationSearchMapList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_destination_search_map);
        setLayout();
    }

    /**
     * 레이아웃 설정
     *
     * @author FIESTA
     * @version 1.0.0
     * @since 오후 5:11
     **/
    private void setLayout() {
        mRecyclerViewDestinationSearchMap = (RecyclerView) findViewById(R.id.rv_search_destination_map);
        mRecyclerViewDestinationSearchMap.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerDestinationSearchMapList = new RecyclerDestinationSearchMapList(30, System.currentTimeMillis(), 30);
        mRecyclerDestinationSearchMapList.setData(mRecyclerDestinationSearchMapList.makeFakeData());
        mRecyclerViewDestinationSearchMap.setAdapter(mRecyclerDestinationSearchMapList);
    }
}
