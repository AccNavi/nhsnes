package kr.go.molit.nhsnes.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.adapter.RecyclerNhsFlightInfoListAdapter;
import kr.go.molit.nhsnes.common.Util;
import kr.go.molit.nhsnes.model.NhsFlightDeleteModel;

/**
 * Created by jongrakmoon on 2017. 3. 31..
 */

public class NhsKMLImportActivity extends NhsBaseFragmentActivity implements View.OnClickListener{
    private RecyclerView rvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_nhs_kml_import);
        setLayout();
        setRecyclerView();
    }


    /**
    * recyclerview 설정
    * @author FIESTA
    * @version 1.0.0
    * @since 오후 5:20
    **/
    private void setRecyclerView() {

        this.rvList = (RecyclerView)findViewById(R.id.rv_info_list);

        String path = Environment.getExternalStorageDirectory().toString() + "/ACC_NAVI/KML_Data";

        File directory = new File(path);
        File[] files = directory.listFiles();

        NhsFlightDeleteModel airChartDeleteModel = null;
        String fileDate = "";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        RecyclerNhsFlightInfoListAdapter adapter = new RecyclerNhsFlightInfoListAdapter(this, RecyclerNhsFlightInfoListAdapter.VIEWTYPE_TITLE_DATE_DELETE);

        if (files != null) {

            for (int i = 0; i < files.length; i++) {

                date.setTime(files[i].lastModified());
                fileDate = sdf.format(date);

                airChartDeleteModel = new NhsFlightDeleteModel();
                airChartDeleteModel.setFileName(files[i].getName());
                airChartDeleteModel.setDate(fileDate);
                airChartDeleteModel.setFile(files[i]);

                adapter.addData(airChartDeleteModel);

            }

        }

        rvList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    /**
    * 레이아웃 설정
    * @author FIESTA
    * @version 1.0.0
    * @since 오후 5:20
    **/
    private void setLayout() {
        rvList = (RecyclerView) findViewById(R.id.rv_info_list);
        findViewById(R.id.btn_kml_import).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            // kml 파일 입력
            case R.id.btn_kml_import:
                selectKmlFile();
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {

            List<Uri> files = Utils.getSelectedFilesFromResult(data);

            for (Uri uri: files) {
                File file = Utils.getFileForUri(uri);
                Toast.makeText(getContext(), file.getPath(), Toast.LENGTH_SHORT).show();
                Util.moveFile(file.getParent()+"/", file.getName(), Environment.getExternalStorageDirectory() + "/ACC_NAVI/KML_Data/");
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setRecyclerView();
            }
        });

    }


    /**
    * KML 데이터를 선택한다.
    * @author FIESTA
    * @version 1.0.0
    * @since 2017-09-15 오후 12:14
    **/
    private void selectKmlFile(){

        Intent i = new Intent(NhsKMLImportActivity.this, FilePickerActivity.class);

        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

        startActivityForResult(i, 0);

    }
}
