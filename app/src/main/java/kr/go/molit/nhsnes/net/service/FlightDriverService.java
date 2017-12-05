package kr.go.molit.nhsnes.net.service;

import kr.go.molit.nhsnes.net.model.FlightDriveModel;
import kr.go.molit.nhsnes.net.model.NetSecurityModel;
import kr.go.molit.nhsnes.net.model.TpmArrivalModel;
import kr.go.molit.nhsnes.net.model.TpmDepartureModel;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


/**
* 주행 화면
* @author FIESTA
* @since  오전 12:16
**/
public interface FlightDriverService {

    /**
     * 암호화된 서버 경로
     **/
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("NIF/psm/position/psmPosition.do")
//    Call<FlightDriveModel> repoFlightDriver(@Body RequestBody json);
    Call<NetSecurityModel> repoFlightDriver(@Body RequestBody json);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("NIF/fpm/arrival/fpmArrival.do")
//    Call<TpmArrivalModel> repoFpmArrival(@Body RequestBody json);
    Call<NetSecurityModel> repoFpmArrival(@Body RequestBody json);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("NIF/fpm/departure/fpmDeparture.do")
//    Call<TpmDepartureModel> repFpmDeparture(@Body RequestBody json);
    Call<NetSecurityModel> repFpmDeparture(@Body RequestBody json);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NetConst.MAIN_SERVER)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    /**
     * 예전 경로
     *
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("ws/psm/position/psmPosition.do")
    Call<FlightDriveModel> repoFlightDriver(@Body RequestBody json);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("ws/fpm/arrival/fpmArrival.do")
    Call<TpmArrivalModel> repoFpmArrival(@Body RequestBody json);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("ws/fpm/departure/fpmDeparture.do")
    Call<TpmDepartureModel> repFpmDeparture(@Body RequestBody json);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NetConst.MAIN_SERVER)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    **/
}
