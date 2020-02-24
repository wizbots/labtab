package org.wizbots.labtab.requesters;

import android.util.Log;

import com.google.gson.Gson;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.R;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.database.StudentsProfileTable;
import org.wizbots.labtab.interfaces.requesters.PromotionDemotionListener;
import org.wizbots.labtab.model.program.Program;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.promotedemote.PromotionDemotionResponse;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.service.SyncManager;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;

public class PromotionDemotionRequester implements Runnable, LabTabConstants {
    private final String TAG = PromotionDemotionRequester.class.getSimpleName();
    private ArrayList<Student> studentArrayList;
    private Program program;
    private boolean promoteDemote;

    public PromotionDemotionRequester() {
    }

    public PromotionDemotionRequester(ArrayList<Student> studentArrayList, Program program, boolean promoteDemote) {
        this.studentArrayList = studentArrayList;
        this.program = program;
        this.promoteDemote = promoteDemote;
    }

    @Override
    public void run() {
        boolean isHighestOrLowestCheck = true;
        if (promoteDemote) {
            for (Student student : studentArrayList) {
                isHighestOrLowestCheck &= student.getLevel().toUpperCase().equals(LabLevels.MASTER);
            }
        } else {
            for (Student student : studentArrayList) {
                isHighestOrLowestCheck &= student.getLevel().toUpperCase().equals(LabLevels.NOVICE);
            }
        }
        if (isHighestOrLowestCheck) {
            for (PromotionDemotionListener promotionDemotionListener : LabTabApplication.getInstance().getUIListeners(PromotionDemotionListener.class)) {
                if (studentArrayList.size() == 1) {
                    if (promoteDemote) {
                        promotionDemotionListener.promotionDemotionUnSuccessful(5000, studentArrayList, program, promoteDemote);
                    } else {
                        promotionDemotionListener.promotionDemotionUnSuccessful(6000, studentArrayList, program, promoteDemote);
                    }
                } else {
                    if (promoteDemote) {
                        promotionDemotionListener.promotionDemotionUnSuccessful(7000, studentArrayList, program, promoteDemote);
                    } else {
                        promotionDemotionListener.promotionDemotionUnSuccessful(8000, studentArrayList, program, promoteDemote);
                    }
                }
                break;
            }
        } else {
            Log.d(TAG, "promoteDemoteResponse Request");
            LabTabResponse<PromotionDemotionResponse> promoteDemoteResponse = LabTabHTTPOperationController.promoteDemoteStudents(getStudents(), promoteDemote, LabTabApplication.getInstance().getUserAgent());
            if (promoteDemoteResponse != null) {
                for (PromotionDemotionListener promotionDemotionListener : LabTabApplication.getInstance().getUIListeners(PromotionDemotionListener.class)) {
                    if (promoteDemoteResponse.getResponseCode() == StatusCode.OK) {
                        promoteDemoteStudents(promoteDemoteResponse.getResponse().getStudents());
                        promotionDemotionListener.promotionDemotionSuccessful(studentArrayList, program, promoteDemote);
                        Log.d(TAG, "promoteDemoteResponse Success, Response Code : " + promoteDemoteResponse.getResponseCode() + " promoteDemoteResponse response: " + new Gson().toJson(promoteDemoteResponse.getResponse()));
                        break;
                    } else if (promoteDemoteResponse.getResponseCode() == StatusCode.NO_INTERNET) {
                        promotionDemotionListener.noInternetConnection();
                    } else if (promoteDemoteResponse.getResponseCode() == StatusCode.FORBIDDEN) {
                        promotionDemotionListener.notHavePermissionForPromotionDemotion(LabTabApplication.getInstance().getResources().getString(R.string.you_dont_have_permission));
                    } else {
                        promoteDemoteStudentsList();
                        promotionDemotionListener.promotionDemotionUnSuccessful(promoteDemoteResponse.getResponseCode(), studentArrayList, program, promoteDemote);
                        Log.d(TAG, "promoteDemoteResponse Failed, Response Code : " + promoteDemoteResponse.getResponseCode());
                        break;
                    }
                }

            } else {
                for (PromotionDemotionListener promotionDemotionListener : LabTabApplication.getInstance().getUIListeners(PromotionDemotionListener.class)) {
                    promoteDemoteStudentsList();
                    promotionDemotionListener.promotionDemotionUnSuccessful(0, studentArrayList, program, promoteDemote);
                    break;
                }
                Log.d(TAG, "promoteDemoteResponse Failed, Response Code : " + promoteDemoteResponse.getResponseCode());
            }
        }
    }

    private String[] getStudents() {
        String[] students = new String[studentArrayList.size()];
        for (int i = 0; i < studentArrayList.size(); i++) {
            students[i] = studentArrayList.get(i).getStudent_id();
        }
        return students;
    }

    private void promoteDemoteStudents(ArrayList<org.wizbots.labtab.model.promotedemote.Student> students) {
        if (students != null && !students.isEmpty()) {
            for (int i = 0; i < students.size(); i++) {
                Student student = getStudent(students.get(i).getId());
                StudentsProfileTable.getInstance().upDateStudentLevel(student.getStudent_id(), student.getLevel());
                ProgramStudentsTable.getInstance().upDateStudentLevel(student.getStudent_id(), student.getLevel());
            }
            SyncManager.getInstance().onRefreshData(1);
        }
    }

    private Student getStudent(String studentId) {
        Student studentFound = null;
        for (Student student : studentArrayList) {
            if (student.getStudent_id().equals(studentId)) {
                studentFound = student;
                break;
            }
        }
        studentFound.setLevel(LabTabUtil.getPromotionDemotionLevel(studentFound.getLevel(), promoteDemote));
        return studentFound;
    }

    private void promoteDemoteStudentsList() {
        for (int i = 0; i < studentArrayList.size(); i++) {
            Student student = getStudent(studentArrayList.get(i).getStudent_id());
            ProgramStudentsTable.getInstance().offlinePromoteDemote(student, promoteDemote);
        }
        SyncManager.getInstance().onRefreshData(1);
    }

}
