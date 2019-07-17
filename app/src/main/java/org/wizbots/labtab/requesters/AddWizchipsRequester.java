package org.wizbots.labtab.requesters;

import android.util.Log;

import com.google.gson.Gson;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.R;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.interfaces.requesters.AddWizchipsListener;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.student.response.Students;
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
    private boolean isOfflineWizchipsSync = false;


    public AddWizchipsRequester(String programOrLab, List<String> mStudentId, int mCount) {
        this.mProgramOrLab = programOrLab;
        this.mStudentId = mStudentId;
        this.mCount = mCount;
    }

    public AddWizchipsRequester(String programOrLab, String mStudentId, int mCount, boolean isOfflineWizchipsSync) {
        this.mProgramOrLab = programOrLab;
        this.mStudentId = new ArrayList<String>();
        this.mStudentId.add(mStudentId);
        this.mCount = mCount;
        this.isOfflineWizchipsSync = isOfflineWizchipsSync;
    }

    @Override
    public void run() {
        Log.d(TAG, "addWizchipsResponse Request");
        int statusCode = 0;

        LabTabResponse<WizchipsAddResponse> addWizchipsResponse = LabTabHTTPOperationController.addWizchips(mStudentId, mCount);

        if (addWizchipsResponse != null) {
            statusCode = addWizchipsResponse.getResponseCode();
            //WizchipsAddResponse response = addWizchipsResponse.getResponse();
            WizchipsAddResponse wizchipsAddResponse = addWizchipsResponse.getResponse();
            for (Students students : wizchipsAddResponse.getStudents()) {
                if (statusCode == HttpURLConnection.HTTP_OK && wizchipsAddResponse.getSuccess().equalsIgnoreCase("true")) {
                    Log.d(TAG, "Wizchips aazadded successfully = " + students.getWizchips());
                    ProgramStudentsTable.getInstance().updateWizchips(students.getId(), Integer.parseInt(students.getWizchips()), true);
                    ProgramStudentsTable.getInstance().updateWizchipsOffline(students.getId(), 0, true);
                    Log.d(TAG, "addWizchipsResponse Success, Response Code : " + statusCode + " addWizchipsResponse response: " + new Gson().toJson(addWizchipsResponse.getResponse()));
                } else if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.d(TAG, "Student Not Found with id = " + mStudentId);
                    Log.d(TAG, "addWizchipsResponse Failed to add wizchips,addWizchipsResponse Response Code : " + statusCode);
                } else {
                    Log.d(TAG, "Failed to add wizchips");
                    Student student = ProgramStudentsTable.getInstance().getWizchipsByStudentId(mProgramOrLab, students.getId());
                    if (isOfflineWizchipsSync)
                        ProgramStudentsTable.getInstance().updateWizchipsOffline(students.getId(), (student.getOfflinewizchips() + mCount), false);
                    else {
                        ProgramStudentsTable.getInstance().updateWizchipsOffline(students.getId(), mCount, false);
                    }
                    Log.d(TAG, "addWizchipsResponse Failed to add wizchips,addWizchipsResponse Response Code : " + statusCode);
                }
            }

        } else {
            Log.d(TAG, "Failed to add wizchips,addWizchipsResponse Response Code : " + statusCode);
            for (String id : mStudentId) {
                Student student = ProgramStudentsTable.getInstance().getWizchipsByStudentId(mProgramOrLab, id);
                if (isOfflineWizchipsSync)
                    ProgramStudentsTable.getInstance().updateWizchipsOffline(id, mCount, false);
                else
                    ProgramStudentsTable.getInstance().updateWizchipsOffline(id, (student.getOfflinewizchips() + mCount), false);
            }
        }
        SyncManager.getInstance().onRefreshData(1);
        for (AddWizchipsListener listener : LabTabApplication.getInstance().getUIListeners(AddWizchipsListener.class)) {
            if (statusCode == LabTabConstants.StatusCode.OK) {
                listener.onAddWizchipsSuccess();
            }  else if (statusCode == LabTabConstants.StatusCode.FORBIDDEN) {
                listener.notHavePermissionForWizchips(LabTabApplication.getInstance().getResources().getString(R.string.you_dont_have_permission));
            } else {
                listener.onAddWizchipsError();
            }
        }
    }
}
