package org.wizbots.labtab.retrofit;

import org.wizbots.labtab.model.CreateTokenResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface LabTabApiInterface {

    String BASE_URL = "http://test.wizbots.com/api/";

    //    Operations related to deal with tokens

    //    1. Creates New Token
    @FormUrlEncoded
    @POST("tokens/")
    Call<CreateTokenResponse> createTokenOrLoginUser(@Field("password") String password, @Field("email") String email);


    //    2. Deletes A Token
    @POST("tokens/{id}")
    Call<Object> deleteToken(@Path("id") String id, @Header("Auth-Token") String authToken);


    //    3. Returns A Token
    @GET("tokens/{id}")
    Call<Object> returnToken(@Path("id") String id, @Header("Auth-Token") String authToken);

}
