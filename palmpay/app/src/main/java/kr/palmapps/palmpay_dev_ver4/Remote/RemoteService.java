package kr.palmapps.palmpay_dev_ver4.Remote;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import kr.palmapps.palmpay_dev_ver4.Item.MemberInfoItem;
import kr.palmapps.palmpay_dev_ver4.Item.OrderListItem;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 서버에 호출할 메서드를 선언하는 인터페이스
 */
public interface RemoteService {
    String Emulator = "http://10.0.2.2:3000";
    String GalaxyS7 = "http://192.168.0.13:3000";

    String BASE_URL = GalaxyS7;

    // node.js auth 관련
    @GET("/auth/isPossibleId/{email}")
    Call<JsonObject> isPossibleId(@Path("email") String email);

    @POST("/auth/signup")
    Call<JsonObject> sendSignUpInfo(@Body MemberInfoItem memberInfoItem);

    @POST("/auth/signin")
    Call<JsonObject> goSignIn(@Body MemberInfoItem memberInfoItem);


    //MainActivity에서의 메서드들
    @GET("/partners")
    Call<JsonArray> getAllPartnersList();

    // id는 비콘을 통해 얻어오는 값
    @GET("/menu/{id}/menupans")
    Call<JsonArray> getAllMenusList(@Path("id") String id);

    @GET("/menu/{id}")
    Call<JsonObject> getStoreName(@Path("id") String id);

    @POST("/order/send")
    Call<JsonObject> sendUserOrderList(@Body JsonArray jsonArray);


    //MyOrderedActivity에서의 메서드들
    @POST("/order/noworderlist")
    Call<JsonArray> getNowOrderList(@Body JsonObject jsonObject);

    @POST("/order/pastorderlist")
    Call<JsonArray> getPastOrderList(@Body JsonObject jsonObject);

}
