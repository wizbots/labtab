package org.wizbots.labtab.requesters;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.database.StudentsProfileTable;
import org.wizbots.labtab.interfaces.requesters.MarkStudentAbsentListener;
import org.wizbots.labtab.interfaces.requesters.PromotionDemotionListener;
import org.wizbots.labtab.model.program.Program;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.promotedemote.PromotionDemotionResponse;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;

public class PromotionDemotionRequester implements Runnable, LabTabConstants {

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
        LabTabResponse<PromotionDemotionResponse> promoteDemoteResponse = LabTabHTTPOperationController.promoteDemoteStudents(getStudents(), promoteDemote);
        if (promoteDemoteResponse != null) {
            for (PromotionDemotionListener promotionDemotionListener : LabTabApplication.getInstance().getUIListeners(PromotionDemotionListener.class)) {
                if (promoteDemoteResponse.getResponseCode() == StatusCode.OK) {
                    promoteDemoteStudents(promoteDemoteResponse.getResponse().getStudents());
                    promotionDemotionListener.promotionDemotionSuccessful(studentArrayList, program, promoteDemote);
                } else {
                    promotionDemotionListener.promotionDemotionUnSuccessful(promoteDemoteResponse.getResponseCode());
                }
            }
        } else {
            for (MarkStudentAbsentListener markStudentAbsentListener : LabTabApplication.getInstance().getUIListeners(MarkStudentAbsentListener.class)) {
                markStudentAbsentListener.markAbsentUnSuccessful(0);
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

}
