package org.wizbots.labtab.requesters;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.database.ProgramAbsencesTable;
import org.wizbots.labtab.interfaces.requesters.MarkStudentAbsentListener;
import org.wizbots.labtab.model.Mentor;
import org.wizbots.labtab.model.markabsent.MarkStudentAbsentResponse;
import org.wizbots.labtab.model.program.Absence;
import org.wizbots.labtab.model.program.Program;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.retrofit.LabTabResponse;

import java.util.ArrayList;

public class MarkStudentAbsentRequester implements Runnable, LabTabConstants {
    private ArrayList<Student> studentArrayList;
    private String date;
    private Program program;
    private boolean sendNotification;


    public MarkStudentAbsentRequester() {
    }

    public MarkStudentAbsentRequester(ArrayList<Student> studentArrayList, String date, Program program, boolean sendNotification) {
        this.studentArrayList = studentArrayList;
        this.date = date;
        this.program = program;
        this.sendNotification = sendNotification;
    }

    @Override
    public void run() {
        LabTabResponse<MarkStudentAbsentResponse> markStudentAbsentResponse = LabTabHTTPOperationController.markStudentAbsents(getStudents(), date, program.getId(), sendNotification);
        if (markStudentAbsentResponse != null) {
            for (MarkStudentAbsentListener markStudentAbsentListener : LabTabApplication.getInstance().getUIListeners(MarkStudentAbsentListener.class)) {
                if (markStudentAbsentResponse.getResponseCode() == StatusCode.OK) {
                    markStudentsAbsent();
                    markStudentAbsentListener.markAbsentSuccessful(studentArrayList, date);
                } else {
                    markStudentAbsentListener.markAbsentUnSuccessful(markStudentAbsentResponse.getResponseCode());
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

    private void markStudentsAbsent() {
        ArrayList<Absence> absenceArrayList = new ArrayList<>();
        Mentor mentor = LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor();
        for (Student student : studentArrayList) {
            absenceArrayList.add(new Absence(student.getName(), mentor.getFullName(), program.getId(), student.getStudent_id(), mentor.getMember_id(), date));
        }
        ProgramAbsencesTable.getInstance().insert(absenceArrayList);
    }
}
