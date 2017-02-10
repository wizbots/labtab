package org.wizbots.labtab.requesters;

import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.database.StudentsProfileTable;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.promotedemote.PromotionDemotionResponse;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.service.LabTabSyncService;
import org.wizbots.labtab.util.LabTabUtil;

public class PromotionDemotionSyncRequester implements Runnable, LabTabConstants {
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

        LabTabResponse<PromotionDemotionResponse> promoteDemoteResponse =
                LabTabHTTPOperationController.promoteDemoteStudents(
                        getStudents(),
                        promoteDemote);

        if (promoteDemoteResponse != null && promoteDemoteResponse.getResponseCode() == StatusCode.OK) {
            promoteDemoteStudents();
        }
        labTabSyncService.promoteDemoteCompleted(position);
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
    }

}
