package org.wizbots.labtab.requesters;

import android.util.Log;

import com.google.gson.Gson;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.database.LocationTable;
import org.wizbots.labtab.interfaces.requesters.LocationListener;
import org.wizbots.labtab.model.LocationResponse;
import org.wizbots.labtab.retrofit.LabTabResponse;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by anurag on 9/2/17.
 */

public class LocationRequester implements Runnable {

    private static final String TAG = LocationRequester.class.getSimpleName();

    @Override
    public void run() {
        Log.d(TAG, "locationResponse Request");
        int statusCode = 0;
        LabTabResponse<LocationResponse[]> locationResponse = LabTabHTTPOperationController.getLocation();
        if (locationResponse != null) {
            statusCode = locationResponse.getResponseCode();
            ArrayList<LocationResponse> list = new ArrayList<>(Arrays.asList(locationResponse.getResponse()));
            if (statusCode == HttpURLConnection.HTTP_OK) {
                LocationTable.getInstance().insert(list);
                Log.d(TAG, "locationResponse Success, Response Code : " + statusCode + "locationResponse  response: " + new Gson().toJson(locationResponse.getResponse()));
            }
        } else {
            Log.d(TAG, "locationResponse Failed, Response Code : " + statusCode);
        }
        for (LocationListener listener : LabTabApplication.getInstance().getUIListeners(LocationListener.class)) {
            if (statusCode == HttpURLConnection.HTTP_OK) {
                listener.onLocationFetchSuccess();
            } else if (statusCode == LabTabConstants.StatusCode.NO_INTERNET) {
                listener.noInternetConnection();
            } else {
                listener.onLocationFetchError();
            }
        }
    }
}
