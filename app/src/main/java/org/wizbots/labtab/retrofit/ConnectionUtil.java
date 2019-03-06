package org.wizbots.labtab.retrofit;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

public class ConnectionUtil {

    private static String TAG = ConnectionUtil.class.getName();

    public static <T> LabTabResponse execute(Call call) {
        try {
            Log.d(TAG, "Request Url : " + call.request().url() + " Request Body : " + new Gson().toJson(call.request().body()));
            Response<T> response = call.execute();
            Log.d(TAG, "Response Code : " + response.code());
            return new LabTabResponse<T>(response.code(), response.body(), response.headers());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Error in execute api request" + e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d(TAG, "Error in execute api" + ex.getMessage());
        }
        return null;
    }
}
