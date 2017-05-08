package org.wizbots.labtab.requesters;

import android.util.Log;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.interfaces.requesters.AddWizchipsListener;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.wizchips.WizchipsAddResponse;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.service.SyncManager;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;


public class AddWizchipsRequester implements Runnable {

    private static String TAG = AddWizchipsRequester.class.getSimpleName();

    private List<String> mStudentId;
    private String mProgramOrLab;
    private int mCount;


    public AddWizchipsRequester(String programOrLab, List<String> mStudentId, int mCount) {
        this.mProgramOrLab = programOrLab;
        this.mStudentId = mStudentId;
        this.mCount = mCount;
    }

    public AddWizchipsRequester(String programOrLab, String mStudentId, int mCount) {
        this.mProgramOrLab = programOrLab;
        this.mStudentId = new ArrayList<String>();
        this.mStudentId.add(mStudentId);
        this.mCount = mCount;
    }

    @Override
    public void run() {
        int statusCode = 0;

        LabTabResponse<ArrayList<WizchipsAddResponse>> addWizchipsResponse = LabTabHTTPOperationController.addWizchips(mStudentId, mCount);

        if (addWizchipsResponse != null) {
            statusCode = addWizchipsResponse.getResponseCode();
            //WizchipsAddResponse response = addWizchipsResponse.getResponse();
            for (WizchipsAddResponse wizchipsAddResponse : addWizchipsResponse.getResponse()) {
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "Wizchips added successfully = " + wizchipsAddResponse.getWizchips());
                    ProgramStudentsTable.getInstance().updateWizchips(wizchipsAddResponse.getStudent_id(), wizchipsAddResponse.getWizchips(), true);
                    ProgramStudentsTable.getInstance().updateWizchipsOffline(wizchipsAddResponse.getStudent_id(), 0, true);
                } else if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.d(TAG, "Student Not Found with id = " + mStudentId);
                } else {
                    Log.d(TAG, "Failed to add wizchips");
                    Student student = ProgramStudentsTable.getInstance().getWizchipsByStudentId(mProgramOrLab, wizchipsAddResponse.getStudent_id());
                    ProgramStudentsTable.getInstance().updateWizchipsOffline(wizchipsAddResponse.getStudent_id(), (student.getOfflinewizchips() + mCount), false);
                }
            }
        } else {
            Log.d(TAG, "Failed to add wizchips");
            for (String id : mStudentId) {
                Student student = ProgramStudentsTable.getInstance().getWizchipsByStudentId(mProgramOrLab, id);
                ProgramStudentsTable.getInstance().updateWizchipsOffline(id, (student.getOfflinewizchips() + mCount), false);
            }
        }
        SyncManager.getInstance().onRefreshData(1);
        for (AddWizchipsListener listener : LabTabApplication.getInstance().getUIListeners(AddWizchipsListener.class)) {
            if (statusCode == LabTabConstants.StatusCode.OK) {
                listener.onAddWizchipsSuccess();
            } else {
                listener.onAddWizchipsError();
            }
        }
    }
}
