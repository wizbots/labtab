package org.wizbots.labtab.retrofit;

public interface WebService {

    String BASE_URL = "https://ndprototype.nicodart.com";

/*    @FormUrlEncoded
    @POST("/authenticate")
    Call<ResponseBody> signIn(@Field("username") String username, @Field("password") String password, @Field("client_id") String client_id, @Field("grant_type") String grant_type);

    @Headers({
            "Content-type:application/json",
            "Accept: application/json",
            "User-Agent: nicodart-ios/1.0 (iPhone; iOS 8.1.2; Scale/2.00)"
    })
    @POST("/permission/changeDoseLevel/{level}")
    Call<ResponseBody> changeDoseLevel(@Header("Authorization") String authorization, @Path("level") int level);

    @POST("/permission/changeTargetNicotine/{level}")
    Call<ResponseBody> changeTargetNicotine(@Header("Authorization") String authorization, @Path("level") int level);

    @Headers({
            "Content-type:application/json",
            "Accept: application/json",
            "User-Agent: nicodart-ios/1.0 (iPhone; iOS 8.1.2; Scale/2.00)"
    })
    @GET("/api/values/devices")
    Call<ResponseBody> getDevices(@Header("Authorization") String authorization);*/

}
