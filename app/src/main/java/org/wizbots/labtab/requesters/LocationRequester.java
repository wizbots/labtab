package org.wizbots.labtab.requesters;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.database.LocationTable;
import org.wizbots.labtab.interfaces.requesters.LocationListener;
import org.wizbots.labtab.model.LocationResponse;
import org.wizbots.labtab.model.metadata.MetaData;
import org.wizbots.labtab.retrofit.LabTabResponse;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by anurag on 9/2/17.
 */

public class LocationRequester implements Runnable {

    @Override
    public void run() {
        int statusCode = 0;
        LabTabResponse<LocationResponse[]> locationResponse = LabTabHTTPOperationController.getLocation();
        if (locationResponse != null) {
            statusCode = locationResponse.getResponseCode();
            ArrayList<LocationResponse> list = new ArrayList<>(Arrays.asList(locationResponse.getResponse()));
            if(statusCode == HttpURLConnection.HTTP_OK){
                LocationTable.getInstance().insert(list);
            }
        }
        for (LocationListener listener : LabTabApplication.getInstance().getUIListeners(LocationListener.class)) {
            if (statusCode == HttpURLConnection.HTTP_OK) {
                listener.onLocationFetchSuccess();
            } else {
                listener.onLocationFetchError();
            }
        }
    }
}
