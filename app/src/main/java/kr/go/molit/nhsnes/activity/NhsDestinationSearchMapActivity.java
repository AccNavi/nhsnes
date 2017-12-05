package kr.go.molit.nhsnes.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.adapter.RecyclerDestinationSearchMapList;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class NhsDestinationSearchMapActivity extends NhsBaseFragmentActivity {
    private RecyclerView mRecyclerViewDestinationSearchMap;
    private RecyclerDestinationSearchMapList mRecyclerDestinationSearchMapList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_destination_search_map);
        setLayout();
    }

    private void setLayout() {
        mRecyclerViewDestinationSearchMap = (RecyclerView) findViewById(R.id.rv_search_destination_map);
        mRecyclerViewDestinationSearchMap.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerDestinationSearchMapList = new RecyclerDestinationSearchMapList(30, System.currentTimeMillis(), 30);
        mRecyclerDestinationSearchMapList.setData(mRecyclerDestinationSearchMapList.makeFakeData());
        mRecyclerViewDestinationSearchMap.setAdapter(mRecyclerDestinationSearchMapList);
    }
}
