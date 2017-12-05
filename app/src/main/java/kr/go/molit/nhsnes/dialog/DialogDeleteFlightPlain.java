package kr.go.molit.nhsnes.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.go.molit.nhsnes.R;
import kr.go.molit.nhsnes.activity.NhsBaseFragmentActivity;
import kr.go.molit.nhsnes.common.MagicSE_Util;
import kr.go.molit.nhsnes.common.ToastUtile;
import kr.go.molit.nhsnes.model.NhsFlightPlainModel;
import kr.go.molit.nhsnes.net.realm.FlightPlanInfo;
import kr.go.molit.nhsnes.net.service.FlightPlanService;
import kr.go.molit.nhsnes.net.service.NetConst;
import kr.go.molit.nhsnes.net.service.NetUtil;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jongrakmoon on 2017. 4. 2..
 */

public class DialogDeleteFlightPlain extends DialogBase implements View.OnClickListener {
    @NonNull
    private final Context context;
    private final FlightPlanInfo mFlightPlanInfo;

    public DialogDeleteFlightPlain(@NonNull Context context, FlightPlanInfo mFlightPlanInfo) {
        super(context);
        this.context = context;
        this.mFlightPlanInfo = mFlightPlanInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete_flight_plain);

        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);

        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isPushButton) {
                    DialogDeleteFlightPlain.this.onClick(findViewById(R.id.btn_ok));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                cancelFlightPlan(mFlightPlanInfo);
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    private void cancelFlightPlan(FlightPlanInfo info)
    {
        final LoadingDialog loading = LoadingDialog.create(context,null,null);
        loading.show();

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("planId",info.getPlanId());
        params.put("planSn",info.getPlanSn());
        params.put("messageType","CNL");
        params.put("acrftCd",info.getAcrftCd());
        params.put("planDeparture",info.getPlanDeparture());
        params.put("planEtd",info.getPlanEtd());
        params.put("planArrival",info.getPlanArrival());
        params.put("callsign",info.getCallsign());

        RequestBody body = NetUtil.mapToJsonBody(getContext(), params);

        FlightPlanService service = FlightPlanService.retrofit.create(FlightPlanService.class);
        Call<Map<String,Object>> callback = service.repoFlightPlanCancel(body);
        callback.enqueue(new Callback<Map<String,Object>>() {
            @Override
            public void onResponse(Call<Map<String,Object>> call, Response<Map<String,Object>> response) {
                Map<String, Object> model = response.body();
                Log.d("JeLib","model::"+model);
                if (response.code() == 200) {

                    JSONObject resultDataObject = null;

                    if (response != null) {

                        String resultCode = (String) model.get("result_code");

                        if (resultCode.equalsIgnoreCase("Y")) {

                            String resultData =  (String) model.get("result_data");

                            try {

                                // 복호화한다.
                                resultData = new MagicSE_Util(getContext()).getDecData(resultData);

                                // 복호화하 내용을 json으로 변환한다.
                                resultDataObject = new JSONObject(resultData);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    }

                    String result_code = (String)resultDataObject.optString("result_code");
                    String result_msg = (String)resultDataObject.optString("result_msg");
                    if(result_code==null){
                        result_code = (String)resultDataObject.optString("result_code");
                    }
                    if(result_msg==null){
                        result_msg = (String)resultDataObject.optString("result_msg");
                    }
                    if(result_code.trim().equals(NetConst.RESPONSE_SUCCESS))
                    {
                        Toast.makeText(getContext(), "삭제하였습니다.", Toast.LENGTH_SHORT).show();

                        if (context instanceof NhsBaseFragmentActivity) {
                            ((NhsBaseFragmentActivity) context).onDialogDissmiss();
                        }
                        dismiss();
                    } else {
                        new ToastUtile().showCenterText(context, result_msg );
                    }
                } else {
                    new ToastUtile().showCenterText(context,context.getString(R.string.error_network));
                }
                loading.dismiss();

            }

            @Override
            public void onFailure(Call<Map<String,Object>> call, Throwable t) {
                loading.dismiss();
                new ToastUtile().showCenterText(context, context.getString(R.string.error_network));
            }
        });
    }

}
