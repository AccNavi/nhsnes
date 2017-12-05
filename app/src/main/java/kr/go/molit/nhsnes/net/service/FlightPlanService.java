package kr.go.molit.nhsnes.net.service;

import java.util.Map;

import kr.go.molit.nhsnes.net.model.FlightPlanModel;
import kr.go.molit.nhsnes.net.model.NetSecurityModel;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by user on 2017-08-17.
 */

public interface FlightPlanService {

    /**
     * 암호화된 서버
     **/

     @Headers({ "Content-Type: application/json;charset=UTF-8"})
     @POST("NIF/fpm/list/fpmList.do")
     //Call<FlightPlanModel> repoFlightPlanList(@Body RequestBody json);
     Call<NetSecurityModel> repoFlightPlanList(@Body RequestBody json);

     @Headers({ "Content-Type: application/json;charset=UTF-8"})
     @POST("NIF/fpm/detail/fpmDetail.do")
     //Call<FlightPlanModel> repoFlightPlanDetail(@Body RequestBody json);
     Call<NetSecurityModel> repoFlightPlanDetail(@Body RequestBody json);

     @Headers({ "Content-Type: application/json;charset=UTF-8"})
     @POST("NIF/fpm/change/fpmChange.do")
     //Call<Map<String,Object>> repoModifyFlightPlan(@Body RequestBody json);
     Call<Map<String,Object>> repoModifyFlightPlan(@Body RequestBody json);

     @Headers({ "Content-Type: application/json;charset=UTF-8"})
     @POST("NIF/fpm/fpl/fpmFpl.do")
     //Call<Map<String,Object>> repoRegFlightPlan(@Body RequestBody json);
     Call<Map<String,Object>> repoRegFlightPlan(@Body RequestBody json);

     @Headers({ "Content-Type: application/json;charset=UTF-8"})
     @POST("NIF/fpm/cancel/fpmCancel.do")
     //Call<Map<String,Object>> repoFlightPlanCancel(@Body RequestBody json);
     Call<Map<String,Object>> repoFlightPlanCancel(@Body RequestBody json);

     public static final Retrofit retrofit = new Retrofit.Builder()
     .baseUrl(NetConst.MAIN_SERVER)
     .addConverterFactory(GsonConverterFactory.create())
     .build();


    /**
     * 예전 경로
     *
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("ws/fpm/list/fpmList.do")
    Call<FlightPlanModel> repoFlightPlanList(@Body RequestBody json);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("ws/fpm/detail/fpmDetail.do")
    Call<FlightPlanModel> repoFlightPlanDetail(@Body RequestBody json);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("ws/fpm/change/fpmChange.do")
    Call<Map<String,Object>> repoModifyFlightPlan(@Body RequestBody json);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("ws/fpm/fpl/fpmFpl.do")
    Call<Map<String,Object>> repoRegFlightPlan(@Body RequestBody json);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("ws/fpm/cancel/fpmCancel.do")
    Call<Map<String,Object>> repoFlightPlanCancel(@Body RequestBody json);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NetConst.MAIN_SERVER)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    **/
}
