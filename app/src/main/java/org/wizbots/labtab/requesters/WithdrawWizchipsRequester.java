package org.wizbots.labtab.requesters;

import android.util.Log;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.interfaces.requesters.WithdrawWizchipsListener;
import org.wizbots.labtab.model.program.Student;
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

    public WithdrawWizchipsRequester(String programOrLab, List<String> mStudentId, int mCount) {
        this.mProgramOrLab = programOrLab;
        this.mStudentId = mStudentId;
        this.mCount = mCount;
    }

    public WithdrawWizchipsRequester(String programOrLab, String mStudentId, int mCount) {
        this.mProgramOrLab = programOrLab;
        this.mStudentId = new ArrayList<String>();
        this.mStudentId.add(mStudentId);
        this.mCount = mCount;
    }

    @Override
    public void run() {
        int statusCode = 0;
        LabTabResponse<ArrayList<WizchipsWithdrawResponse>> withdrawWizchipsResponse = LabTabHTTPOperationController.withdrawWizchips(mStudentId, mCount);

        if (withdrawWizchipsResponse != null) {
            statusCode = withdrawWizchipsResponse.getResponseCode();
            // WizchipsWithdrawResponse response = withdrawWizchipsResponse.getResponse();
            for (WizchipsWithdrawResponse wizchipsWithdrawResponse : withdrawWizchipsResponse.getResponse()) {
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "Wizchips withdraw successfully = " + wizchipsWithdrawResponse.getWizchips());
                    ProgramStudentsTable.getInstance().updateWizchips(wizchipsWithdrawResponse.getStudent_id(), wizchipsWithdrawResponse.getWizchips(), true);
                    ProgramStudentsTable.getInstance().updateWizchipsOffline(wizchipsWithdrawResponse.getStudent_id(), 0, true);
                } else if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.d(TAG, "Student Not Found with id = " + wizchipsWithdrawResponse.getStudent_id());
                } else {
                    Log.d(TAG, "Failed to withdraw wizchips");
                    Student student = ProgramStudentsTable.getInstance().getWizchipsByStudentId(mProgramOrLab, wizchipsWithdrawResponse.getStudent_id());
                    ProgramStudentsTable.getInstance().updateWizchipsOffline(wizchipsWithdrawResponse.getStudent_id(), getChips(student.getWizchips(), student.getOfflinewizchips(), mCount), false);
                }
            }
        } else {
            for (String id : mStudentId) {
                Log.d(TAG, "Failed to withdraw wizchips");
                Student student = ProgramStudentsTable.getInstance().getWizchipsByStudentId(mProgramOrLab, id);
                ProgramStudentsTable.getInstance().updateWizchipsOffline(id, getChips(student.getWizchips(), student.getOfflinewizchips(), mCount), false);
            }
        }
        SyncManager.getInstance().onRefreshData(1);
        for (WithdrawWizchipsListener listener : LabTabApplication.getInstance().getUIListeners(WithdrawWizchipsListener.class)) {
            if (statusCode == LabTabConstants.StatusCode.OK) {
                listener.onWithdrawWizchipsSuccess();
            } else {
                listener.onWithdrawWizchipsError();
            }
        }
    }

    private int getChips(int onlineCount, int oldCount, int newCount) {
        return (onlineCount + oldCount) > 0 ? (oldCount - newCount) : oldCount;
    }
}
