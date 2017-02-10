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

import java.net.HttpURLConnection;


public class WithdrawWizchipsRequester implements Runnable {

    private static String TAG = WithdrawWizchipsRequester.class.getSimpleName();

    private String mStudentId;
    private String mProgramOrLab;
    private int mCount;

    public WithdrawWizchipsRequester(String programOrLab, String mStudentId, int mCount) {
        this.mProgramOrLab = programOrLab;
        this.mStudentId = mStudentId;
        this.mCount = mCount;
    }

    @Override
    public void run() {
        int statusCode = 0;
        LabTabResponse<WizchipsWithdrawResponse> withdrawWizchipsResponse = LabTabHTTPOperationController.withdrawWizchips(mStudentId, mCount);

        if (withdrawWizchipsResponse != null) {
            statusCode = withdrawWizchipsResponse.getResponseCode();
            WizchipsWithdrawResponse response = withdrawWizchipsResponse.getResponse();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                Log.d(TAG , "Wizchips withdraw successfully = " + response.getWizchips());
                ProgramStudentsTable.getInstance().updateWizchips(mStudentId, response.getWizchips(), true);
            }else if(statusCode == HttpURLConnection.HTTP_NOT_FOUND){
                Log.d(TAG , "Student Not Found with id = " + mStudentId);
            } else {
                Log.d(TAG , "Failed to withdraw wizchips");
                Student student = ProgramStudentsTable.getInstance().getWizchipsByStudentId(mProgramOrLab, mStudentId);
                ProgramStudentsTable.getInstance().updateWizchipsOffline(mStudentId, getChips(student.getWizchips(), student.getOfflinewizchips(), mCount), false);
            }
        }else {
            Log.d(TAG , "Failed to withdraw wizchips");
            Student student = ProgramStudentsTable.getInstance().getWizchipsByStudentId(mProgramOrLab, mStudentId);
            ProgramStudentsTable.getInstance().updateWizchipsOffline(mStudentId, getChips(student.getWizchips(), student.getOfflinewizchips(), mCount), false);
        }
        for (WithdrawWizchipsListener listener : LabTabApplication.getInstance().getUIListeners(WithdrawWizchipsListener.class)) {
            if (statusCode == LabTabConstants.StatusCode.OK) {
                listener.onWithdrawWizchipsSuccess();
            } else {
                listener.onWithdrawWizchipsError();
            }
        }
    }

    private int getChips(int onlineCount, int oldCount, int newCount){
        return (onlineCount + oldCount) > 0 ? (oldCount - newCount) : oldCount;
    }
}
