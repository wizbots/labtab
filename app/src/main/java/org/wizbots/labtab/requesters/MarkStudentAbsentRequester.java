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
import org.wizbots.labtab.service.SyncManager;

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
        ArrayList<Absence> checkAbsence = new ArrayList<>();
        for (Student student : studentArrayList) {
            checkAbsence.addAll(ProgramAbsencesTable.getInstance().findAbsencesForSpecificDate(date, student.getStudent_id()));
        }
        if (checkAbsence.size() == studentArrayList.size()) {
            for (MarkStudentAbsentListener markStudentAbsentListener : LabTabApplication.getInstance().getUIListeners(MarkStudentAbsentListener.class)) {
                if(studentArrayList.size() == 1)
                    markStudentAbsentListener.markAbsentUnSuccessful(1001);
                else
                    markStudentAbsentListener.markAbsentUnSuccessful(1002);
                break;
            }
        } else {
        LabTabResponse<MarkStudentAbsentResponse> markStudentAbsentResponse = LabTabHTTPOperationController.markStudentAbsents(getStudents(), date, program.getId(), sendNotification);
        if (markStudentAbsentResponse != null) {
            for (MarkStudentAbsentListener markStudentAbsentListener : LabTabApplication.getInstance().getUIListeners(MarkStudentAbsentListener.class)) {
                if (markStudentAbsentResponse.getResponseCode() == StatusCode.OK) {
                    markStudentsAbsent(SyncStatus.SYNCED);
                    markStudentAbsentListener.markAbsentSuccessful(studentArrayList, date);
                } else {
                    markStudentsAbsent(SyncStatus.NOT_SYNCED);
                    markStudentAbsentListener.markAbsentUnSuccessful(markStudentAbsentResponse.getResponseCode());
                }
            }
        } else {
            for (MarkStudentAbsentListener markStudentAbsentListener : LabTabApplication.getInstance().getUIListeners(MarkStudentAbsentListener.class)) {
                markStudentsAbsent(SyncStatus.NOT_SYNCED);
//                markStudentAbsentListener.markAbsentUnSuccessful(0);
                markStudentAbsentListener.markAbsentSuccessful(studentArrayList, date);
            }
        }}
        SyncManager.getInstance().onRefreshData(1);
    }


    private String[] getStudents() {
        String[] students = new String[studentArrayList.size()];
        for (int i = 0; i < studentArrayList.size(); i++) {
            students[i] = studentArrayList.get(i).getStudent_id();
        }
        return students;
    }

    private void markStudentsAbsent(String syncStatus) {
        ArrayList<Absence> absenceArrayList = new ArrayList<>();
        Mentor mentor = LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor();
        for (Student student : studentArrayList) {
            absenceArrayList.add(new Absence(student.getName(), syncStatus, sendNotification ? "1" : "0", mentor.getFullName(), program.getId(), student.getStudent_id(), mentor.getMember_id(), date));
        }
        ProgramAbsencesTable.getInstance().insert(absenceArrayList);
    }
}
