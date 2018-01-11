package kr.go.molit.nhsnes.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.modim.lan.lanandroid.AirDoyupList;
import com.modim.lan.lanandroid.INativeImple;
import com.modim.lan.lanandroid.NativeImplement;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.adapter.RecyclerNhsFlightInfoListAdapter;
import kr.go.molit.nhsnes.model.NhsAirListModel;

import static kr.go.molit.nhsnes.adapter.RecyclerNhsFlightInfoListAdapter.VIEWTYPE_TITLE_DATE_DOWNLOAD_DELETE;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class NhsNGIIDataActivity extends NhsBaseFragmentActivity {
    private RecyclerView rvList;
    private NativeImplement nativeImplement = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_nhs_ngii_data);
        nativeImplement = INativeImple.getInstance(getContext());

        setLayout();

//        setRecyclerView();
    }


    private void setRecyclerView() {

//        this.rvList.setLayoutManager(new LinearLayoutManager(NhsNGIIDataActivity.this));
//        RecyclerNhsFlightInfoListAdapter adapter = new RecyclerNhsFlightInfoListAdapter(NhsNGIIDataActivity.this, VIEWTYPE_TITLE_DATE_DOWNLOAD_DELETE);
//
//        // 리스트를 받는다.
//        AirDoyupList airDoyupList = nativeImplement.lanGetRouteDoyupList();
//
//        if (airDoyupList.lists != null) {
//
//            int size = airDoyupList.lists.length;
//            NhsAirListModel nhsAirListModel = null;
//
//            for (AirDoyupList.List list : airDoyupList.lists) {
//
//                nhsAirListModel = new NhsAirListModel();
//                nhsAirListModel.setTitle(list.);
//                nhsAirListModel.setResultUrl(model.getResult_url());
//                nhsAirListModel.setDataVer(model.getData_ver());
//                nhsAirListModel.setFileName(finalSaveFileName);
//
//                adapter.addData(nhsAirListModel);
//
//            }
//        }
//
//        adapter.notifyDataSetChanged();

    }

    /**
    * 레이아웃 설정
    * @author FIESTA
    * @version 1.0.0
    * @since 오후 5:23
    **/
    private void setLayout() {
        rvList = (RecyclerView) findViewById(R.id.rv_info_list);
    }
}
