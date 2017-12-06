package kr.go.molit.nhsnes.net.service;

import kr.go.molit.nhsnes.net.model.AirChartListModel;
import kr.go.molit.nhsnes.net.model.AirListModel;
import kr.go.molit.nhsnes.net.model.FlightPlanModel;
import kr.go.molit.nhsnes.net.model.NetSecurityModel;
import kr.go.molit.nhsnes.net.model.PsmHkMessage;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * 각종 조회 서비스
 *
 * @author FIESTA
 * @version 1.0.0
 * @since 2017-09-01 오후 9:49
 **/
public interface FlightInfoService {

    /**
     * 암호화된 서버 경로
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("NIF/nis/list/airChartList.do")
    //Call<AirChartListModel> searchAirChartList();
    Call<NetSecurityModel> searchAirChartList(@Body RequestBody json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("NIF/nis/list/airspaceList.do")
        //Call<AirListModel> airspaceList(@Body RequestBody json);
    Call<NetSecurityModel> airspaceList(@Body RequestBody json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("NIF/nis/list/obstacleList.do")
        //Call<AirListModel> obstacleList(@Body RequestBody json);
    Call<NetSecurityModel> obstacleList(@Body RequestBody json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("NIF/nis/list/airfieldList.do")
        //Call<AirListModel> airfieldList(@Body RequestBody json);
    Call<NetSecurityModel> airfieldList(@Body RequestBody json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("NIF/nis/list/airportList.do")
        //Call<AirListModel> airportList(@Body RequestBody json);
    Call<NetSecurityModel> airportList(@Body RequestBody json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("NIF/nis/list/flightPathList.do")
        //Call<AirListModel> flightPathList(@Body RequestBody json);
    Call<NetSecurityModel> flightPathList(@Body RequestBody json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("NIF/nis/list/routeList.do")
        //Call<AirListModel> routeList(@Body RequestBody json);
    Call<NetSecurityModel> routeList(@Body RequestBody json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("NIF/nis/list/vfrPathList.do")
        //Call<AirListModel> vfrPath(@Body RequestBody json);
    Call<NetSecurityModel> vfrPath(@Body RequestBody json);


    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("NIF/nis/list/pylonList.do")
        //Call<AirListModel> pylonList(@Body RequestBody json);
    Call<NetSecurityModel> pylonList(@Body RequestBody json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("NIF/nis/list/runwayList.do")
        // Call<AirListModel> runwayList(@Body RequestBody json);
    Call<NetSecurityModel> runwayList(@Body RequestBody json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("NIF/nis/list/heliportList.do")
        // Call<AirListModel> heliportList(@Body RequestBody json);
    Call<NetSecurityModel> heliportList(@Body RequestBody json);


    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("NIF/nis/list/notamList.do")
        //Call<AirListModel> notamList(@Body RequestBody json);
    Call<NetSecurityModel> notamList(@Body RequestBody json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("NIF/psm/hkMessage/psmHkMessage.do")
    Call<NetSecurityModel> psmHkMessage(@Body RequestBody json);


    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NetConst.MAIN_SERVER)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    /**
     * 예전 경로
     *
     @Headers({ "Content-Type: application/json;charset=UTF-8"})
     @POST("nis/list/airChartList.do") Call<AirChartListModel> searchAirChartList();

     @Headers({ "Content-Type: application/json;charset=UTF-8"})
     @POST("nis/list/airspaceList.do") Call<AirListModel> airspaceList(@Body RequestBody json);

     @Headers({ "Content-Type: application/json;charset=UTF-8"})
     @POST("nis/list/obstacleList.do") Call<AirListModel> obstacleList(@Body RequestBody json);

     @Headers({ "Content-Type: application/json;charset=UTF-8"})
     @POST("nis/list/airfieldList.do") Call<AirListModel> airfieldList(@Body RequestBody json);

     @Headers({ "Content-Type: application/json;charset=UTF-8"})
     @POST("nis/list/airportList.do") Call<AirListModel> airportList(@Body RequestBody json);

     @Headers({ "Content-Type: application/json;charset=UTF-8"})
     @POST("nis/list/flightPathList.do") Call<AirListModel> flightPathList(@Body RequestBody json);

     @Headers({ "Content-Type: application/json;charset=UTF-8"})
     @POST("nis/list/routeList.do") Call<AirListModel> routeList(@Body RequestBody json);

     @Headers({ "Content-Type: application/json;charset=UTF-8"})
     @POST("nis/list/notamList.do") Call<AirListModel> notamList(@Body RequestBody json);

     public static final Retrofit retrofit = new Retrofit.Builder()
     .baseUrl(NetConst.INFO_SERVER)
     .addConverterFactory(GsonConverterFactory.create())
     .build();

     **/
}
