package org.wizbots.labtab.requesters;

import android.util.Log;

import com.google.gson.Gson;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.R;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.interfaces.requesters.WithdrawWizchipsListener;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.student.response.Students;
import org.wizbots.labtab.model.wizchips.WizchipsWithdrawResponse;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.service.SyncManager;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;


public class WithdrawWizchipsRequester implements Runnable {

    private static String TAG = WithdrawWizchipsRequester.class.getSimpleName();

    private List<String> mStudentId;
    private String mProgramOrLab;
    private int mCount;
    private boolean isOfflineWizchipsSync = false;

    public WithdrawWizchipsRequester(String programOrLab, List<String> mStudentId, int mCount) {
        this.mProgramOrLab = programOrLab;
        this.mStudentId = mStudentId;
        this.mCount = mCount;
    }

    public WithdrawWizchipsRequester(String programOrLab, String mStudentId, int mCount, boolean isOfflineWizchipsSync) {
        this.mProgramOrLab = programOrLab;
        this.mStudentId = new ArrayList<String>();
        this.mStudentId.add(mStudentId);
        this.mCount = mCount;
        this.isOfflineWizchipsSync = isOfflineWizchipsSync;
    }

    @Override
    public void run() {
        Log.d(TAG, "withdrawWizchipsResponse Request");
        int statusCode = 0;
        LabTabResponse<WizchipsWithdrawResponse> withdrawWizchipsResponse = LabTabHTTPOperationController.withdrawWizchips(mStudentId, mCount);

        if (withdrawWizchipsResponse != null) {
            statusCode = withdrawWizchipsResponse.getResponseCode();
            WizchipsWithdrawResponse response = withdrawWizchipsResponse.getResponse();
            for (Students students : response.getStudents()) {
                if (statusCode == HttpURLConnection.HTTP_OK && response.getSuccess().equalsIgnoreCase("true")) {
                    Log.d(TAG, "Wizchips withdraw successfully = " + students.getWizchips());
                    ProgramStudentsTable.getInstance().updateWizchips(students.getId(), Integer.parseInt(students.getWizchips()), true);
                    ProgramStudentsTable.getInstance().updateWizchipsOffline(students.getId(), 0, true);
                    Log.d(TAG, "withdrawWizchipsResponse Success, Response Code : " + withdrawWizchipsResponse.getResponseCode() + " withdrawWizchipsResponse response: " + new Gson().toJson(withdrawWizchipsResponse.getResponse()));
                } else if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.d(TAG, "Student Not Found with id = " + students.getId());
                    Log.d(TAG, "withdrawWizchipsResponse Failed, Response Code : " + withdrawWizchipsResponse.getResponseCode());
                } else if (statusCode == HttpURLConnection.HTTP_FORBIDDEN) {
                    Log.d(TAG, "Student Not Found with id = " + students.getId());
                    Log.d(TAG, "withdrawWizchipsResponse Failed, Response Code : " + withdrawWizchipsResponse.getResponseCode());
                } else {
                    Log.d(TAG, "Failed to withdraw wizchips");
                    Student student = ProgramStudentsTable.getInstance().getWizchipsByStudentId(mProgramOrLab, students.getId());
                    if (isOfflineWizchipsSync)
                        ProgramStudentsTable.getInstance().updateWizchipsOffline(students.getId(), -mCount, false);
                    else {
                        ProgramStudentsTable.getInstance().updateWizchipsOffline(students.getId(), getChips(student.getWizchips(), student.getOfflinewizchips(), mCount), false);
                        Log.d(TAG, "withdrawWizchipsResponse Failed, Response Code : " + withdrawWizchipsResponse.getResponseCode());
                    }
                }
            }
            Log.d(TAG, "withdrawWizchipsResponse Failed, Response Code : " + withdrawWizchipsResponse.getResponseCode());
        } else {
            for (String id : mStudentId) {
                Log.d(TAG, "Failed to withdraw wizchips");
                Student student = ProgramStudentsTable.getInstance().getWizchipsByStudentId(mProgramOrLab, id);
                if (isOfflineWizchipsSync)
                    ProgramStudentsTable.getInstance().updateWizchipsOffline(id, -mCount, false);
                else
                    ProgramStudentsTable.getInstance().updateWizchipsOffline(id, getChips(student.getWizchips(), student.getOfflinewizchips(), mCount), false);
            }
        }
        SyncManager.getInstance().onRefreshData(1);
        for (WithdrawWizchipsListener listener : LabTabApplication.getInstance().getUIListeners(WithdrawWizchipsListener.class)) {
            if (statusCode == LabTabConstants.StatusCode.OK) {
                listener.onWithdrawWizchipsSuccess();
            } else if (statusCode == LabTabConstants.StatusCode.FORBIDDEN) {
                listener.notHavePermissionToWithdraw(LabTabApplication.getInstance().getResources().getString(R.string.you_dont_have_permission));
            } else if (statusCode == LabTabConstants.StatusCode.NO_INTERNET) {
                listener.noInternetConnection();
            } else {
                listener.onWithdrawWizchipsError();
            }
        }
    }

    private int getChips(int onlineCount, int oldCount, int newCount) {
        return (onlineCount + oldCount) > 0 ? (oldCount - newCount) : oldCount;
    }
}
