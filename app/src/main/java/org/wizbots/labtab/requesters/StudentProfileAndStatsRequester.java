package org.wizbots.labtab.requesters;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.interfaces.requesters.GetStudentProfileAndStatsListener;
import org.wizbots.labtab.model.student.response.StudentResponse;
import org.wizbots.labtab.retrofit.LabTabResponse;

public class StudentProfileAndStatsRequester implements Runnable, LabTabConstants {
    private String student_id;

    public StudentProfileAndStatsRequester() {
    }

    public StudentProfileAndStatsRequester(String student_id) {
        this.student_id = student_id;
    }

    @Override
    public void run() {
        LabTabResponse<StudentResponse> studentResponse = LabTabHTTPOperationController.getStudentStatsAndProfile(student_id);
        if (studentResponse != null) {
            StudentResponse studentProfileAndStatsResponse = studentResponse.getResponse();
            for (GetStudentProfileAndStatsListener getProgramStudentsListener : LabTabApplication.getInstance().getUIListeners(GetStudentProfileAndStatsListener.class)) {
                if (studentResponse.getResponseCode() == StatusCode.OK) {
                } else {
                    getProgramStudentsListener.unableToFetchStudent(studentResponse.getResponseCode());
                }
            }
        } else {
            for (GetStudentProfileAndStatsListener getStudentProfileAndStatsListener : LabTabApplication.getInstance().getUIListeners(GetStudentProfileAndStatsListener.class)) {
                getStudentProfileAndStatsListener.unableToFetchStudent(0);
            }
        }
    }
}
