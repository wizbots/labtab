package org.wizbots.labtab.requesters;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.model.metadata.MetaData;
import org.wizbots.labtab.model.metadata.ProgramMetaData;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.util.LabTabUtil;

import java.lang.reflect.Type;

public class ProjectsMetaDataRequester implements Runnable, LabTabConstants {
    private final String TAG = ProgramOrLabRequester.class.getSimpleName();
    @Override
    public void run() {
        Log.d(TAG, "labTabResponse Request");
       /* LabTabResponse<MetaData[]> labTabResponse = LabTabHTTPOperationController.getProjectMetaData();
        if (labTabResponse.getResponse() != null && labTabResponse.getResponseCode() == StatusCode.OK) {
            LabTabPreferences.getInstance(LabTabApplication.getInstance()).setProjectsMetaData(labTabResponse.getResponse());
            LabTabApplication.getInstance().setMetaDatas(labTabResponse.getResponse());
            Log.d(TAG, "labTabResponse Success, Response Code : " + labTabResponse.getResponseCode() + " labTabResponse response: " +new Gson().toJson(labTabResponse.getResponse()));
        }else{
            Log.d(TAG, "labTabResponse Failed, Response Code : " + labTabResponse.getResponseCode());
        }*/

        LabTabResponse<ProgramMetaData> labTabResponse = LabTabHTTPOperationController.getProjectMetaData();
        if (labTabResponse.getResponse() != null && labTabResponse.getResponseCode() == StatusCode.OK) {

            ProgramMetaData programMetaData = labTabResponse.getResponse();
            LabTabPreferences.getInstance(LabTabApplication.getInstance()).setProjectsMetaData(programMetaData.getLevel());
            LabTabApplication.getInstance().setMetaDatas(programMetaData.getLevel());
            LabTabPreferences.getInstance(LabTabApplication.getInstance()).setCategory(programMetaData.getCategories());

            Log.d(TAG, "labTabResponse Success, Response Code : " + labTabResponse.getResponseCode() + " labTabResponse response: " +new Gson().toJson(labTabResponse.getResponse()));
        }else{
            Log.d(TAG, "labTabResponse Failed, Response Code : " + labTabResponse.getResponseCode());
        }
        /* else {
        MetaData[] mD = (MetaData[]) LabTabUtil.fromJson(ProjectsMetaData.CONTENT, MetaData[].class);
        LabTabPreferences.getInstance(LabTabApplication.getInstance()).setProjectsMetaData(mD);
        LabTabApplication.getInstance().setMetaDatas(mD);
        }*/
    }
}
