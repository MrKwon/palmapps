package kr.palmapps.palmpay_dev_ver4.Remote;


import com.google.gson.JsonObject;

import kr.palmapps.palmpay_dev_ver4.Item.MemberInfoItem;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 서버에 호출할 메소드를 선언하는 인터페이스
 */
public interface RemoteService {
    String BASE_URL = "http://10.0.2.2:3000";

    // node.js auth 관련
    @GET("/auth/isPossibleId/{email}")
    Call<JsonObject> isPossibleId(@Path("email") String email);

    @POST("/auth/signup")
    Call<JsonObject> sendSignUpInfo(@Body MemberInfoItem memberInfoItem);

    @POST("/auth/signin")
    Call<JsonObject> goSignIn(@Body MemberInfoItem memberInfoItem);

}
