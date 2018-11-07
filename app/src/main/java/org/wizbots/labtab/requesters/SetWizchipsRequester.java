package org.wizbots.labtab.requesters;

import android.util.Log;

import com.google.gson.Gson;

import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.model.student.response.Students;
import org.wizbots.labtab.model.wizchips.WizchipsWithdrawResponse;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.service.SyncManager;


import java.net.HttpURLConnection;

public class SetWizchipsRequester implements Runnable {

    private static String TAG = SetWizchipsRequester.class.getSimpleName();

    private String mStudentId;
    private int mCount;

    public SetWizchipsRequester(String mStudentId, int mCount) {
        this.mStudentId = mStudentId;
        if (mCount < 0) {
            this.mCount = 0;
        } else {
            this.mCount = mCount;
        }
    }

    @Override
    public void run() {
        Log.d(TAG, "setWizchipsResponse Request");
        int statusCode = 0;
        LabTabResponse<WizchipsWithdrawResponse> withdrawWizchipsResponse = LabTabHTTPOperationController.setWizchips(mStudentId, mCount);

        if (withdrawWizchipsResponse != null) {
            statusCode = withdrawWizchipsResponse.getResponseCode();
            WizchipsWithdrawResponse response = withdrawWizchipsResponse.getResponse();
            for (Students students : response.getStudents()) {
                if (statusCode == HttpURLConnection.HTTP_OK && response.getSuccess().equalsIgnoreCase("true")) {
                    Log.d(TAG, "Wizchips set successfully = " + students.getWizchips());
                    ProgramStudentsTable.getInstance().updateWizchips(students.getId(), Integer.parseInt(students.getWizchips()), true);
                    ProgramStudentsTable.getInstance().updateWizchipsOffline(students.getId(), 0, true);
                    Log.d(TAG, "setWizchipsResponse Success, Response Code : " + withdrawWizchipsResponse.getResponseCode() + " withdrawWizchipsResponse response: " + new Gson().toJson(withdrawWizchipsResponse.getResponse()));
                } else if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.d(TAG, "Student Not Found with id = " + students.getId());
                    Log.d(TAG, "setWizchipsResponse Failed, Response Code : " + withdrawWizchipsResponse.getResponseCode());
                } else {
                    Log.d(TAG, "Failed to set wizchips");
                        Log.d(TAG, "setWizchipsResponse Failed, Response Code : " + withdrawWizchipsResponse.getResponseCode());
                }
            }
            Log.d(TAG, "setWizchipsResponse Failed, Response Code : " + withdrawWizchipsResponse.getResponseCode());
        } else {
            Log.d(TAG, "Failed to set wizchips");
        }
        SyncManager.getInstance().onRefreshData(1);
    }
}
