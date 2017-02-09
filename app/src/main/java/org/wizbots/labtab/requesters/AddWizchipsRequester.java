package org.wizbots.labtab.requesters;

import android.util.Log;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.interfaces.requesters.AddWizchipsListener;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.wizchips.WizchipsAddResponse;
import org.wizbots.labtab.retrofit.LabTabResponse;

import java.net.HttpURLConnection;


public class AddWizchipsRequester implements Runnable {

    private static String TAG = AddWizchipsRequester.class.getSimpleName();

    private String mStudentId;
    private ProgramOrLab mProgramOrLab;
    private int mCount;

    public AddWizchipsRequester(ProgramOrLab programOrLab, String mStudentId, int mCount) {
        this.mProgramOrLab = programOrLab;
        this.mStudentId = mStudentId;
        this.mCount = mCount;
    }

    @Override
    public void run() {
        int statusCode = 0;
        LabTabResponse<WizchipsAddResponse> addWizchipsResponse = LabTabHTTPOperationController.addWizchips(mStudentId, mCount);

        if (addWizchipsResponse != null) {
            statusCode = addWizchipsResponse.getResponseCode();
            WizchipsAddResponse response = addWizchipsResponse.getResponse();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                Log.d(TAG , "Wizchips added successfully = " + response.getWizchips());
                ProgramStudentsTable.getInstance().updateWizchips(mStudentId, response.getWizchips(), true);
            }else if(statusCode == HttpURLConnection.HTTP_NOT_FOUND){
                Log.d(TAG , "Student Not Found with id = " + mStudentId);
            } else {
                Log.d(TAG , "Failed to add wizchips");
                Student student = ProgramStudentsTable.getInstance().getWizchipsByStudentId(mProgramOrLab.getId(), mStudentId);
                ProgramStudentsTable.getInstance().updateWizchipsOffline(mStudentId, (student.getOfflinewizchips() + mCount), false);
            }
        }else {
            Log.d(TAG , "Failed to add wizchips");
            Student student = ProgramStudentsTable.getInstance().getWizchipsByStudentId(mProgramOrLab.getId(), mStudentId);
            ProgramStudentsTable.getInstance().updateWizchipsOffline(mStudentId, (student.getOfflinewizchips() + mCount), false);
        }
        for (AddWizchipsListener listener : LabTabApplication.getInstance().getUIListeners(AddWizchipsListener.class)) {
            if (statusCode == LabTabConstants.StatusCode.OK) {
                listener.onAddWizchipsSuccess();
            } else {
                listener.onAddWizchipsError();
            }
        }
    }
}
