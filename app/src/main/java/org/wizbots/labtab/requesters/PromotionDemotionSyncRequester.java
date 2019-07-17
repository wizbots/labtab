package org.wizbots.labtab.requesters;

import android.util.Log;

import com.google.gson.Gson;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.R;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.database.StudentsProfileTable;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.promotedemote.PromotionDemotionResponse;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.service.LabTabSyncService;
import org.wizbots.labtab.service.SyncManager;
import org.wizbots.labtab.util.LabTabUtil;

public class PromotionDemotionSyncRequester implements Runnable, LabTabConstants {
    private final String TAG = PromotionDemotionSyncRequester.class.getSimpleName();
    private LabTabSyncService labTabSyncService;
    private Student student;
    private int position;
    boolean promoteDemote;

    public PromotionDemotionSyncRequester() {
    }

    public PromotionDemotionSyncRequester(LabTabSyncService labTabSyncService, Student student, int position) {
        this.labTabSyncService = labTabSyncService;
        this.student = student;
        this.position = position;
    }

    @Override
    public void run() {
        if (student.getPromotionDemotionSync().equals(SyncStatus.PROMOTION_NOT_SYNCED)) {
            promoteDemote = true;
        } else {
            promoteDemote = false;
        }
        Log.d(TAG, "promoteDemoteResponse Request");
        LabTabResponse<PromotionDemotionResponse> promoteDemoteResponse =
                LabTabHTTPOperationController.promoteDemoteStudents(
                        getStudents(),
                        promoteDemote,
                        LabTabApplication.getInstance().getUserAgent());

        if (promoteDemoteResponse != null && promoteDemoteResponse.getResponseCode() == StatusCode.OK) {
            promoteDemoteStudents();
            Log.d(TAG, "promoteDemoteResponse Success, Response Code : " + promoteDemoteResponse.getResponseCode() + " promoteDemoteResponse response: " + new Gson().toJson(promoteDemoteResponse.getResponse()));
        } else if (promoteDemoteResponse.getResponseCode() == StatusCode.FORBIDDEN) {
            Log.d(TAG, "promoteDemoteResponse Failed, Response Code : " +promoteDemoteResponse.getResponseCode());
        }else{
            Log.d(TAG, "promoteDemoteResponse Failed, Response Code : " +promoteDemoteResponse.getResponseCode());
        }
        labTabSyncService.promoteDemoteCompleted(position);
        SyncManager.getInstance().onRefreshData(1);
    }

    private String[] getStudents() {
        String[] students = new String[1];
        students[0] = student.getStudent_id();
        return students;
    }

    private void promoteDemoteStudents() {
        String level = student.getLevel();
        student.setLevel(LabTabUtil.getPromotionDemotionLevel(level, promoteDemote));
        ProgramStudentsTable.getInstance().updatePromoteDemote(student);
        StudentsProfileTable.getInstance().upDateStudentLevel(student.getStudent_id(), student.getLevel());
        ProgramStudentsTable.getInstance().upDateStudentLevel(student.getStudent_id(), student.getLevel());
        SyncManager.getInstance().onRefreshData(1);
    }

}
