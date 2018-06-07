package org.wizbots.labtab.requesters;

import android.util.Log;

import com.google.gson.Gson;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.database.ProgramAbsencesTable;
import org.wizbots.labtab.model.markabsent.MarkStudentAbsentResponse;
import org.wizbots.labtab.model.program.Absence;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.service.LabTabSyncService;

public class MarkStudentAbsentSyncRequester implements Runnable, LabTabConstants {
    private final static String TAG = MarkStudentAbsentSyncRequester.class.getSimpleName();
    private LabTabSyncService labTabSyncService;
    private Absence absence;
    private String date;
    private boolean sendNotification;
    private int position;


    public MarkStudentAbsentSyncRequester() {
    }

    public MarkStudentAbsentSyncRequester(LabTabSyncService labTabSyncService, Absence absence, String date, boolean sendNotification, int position) {
        this.labTabSyncService = labTabSyncService;
        this.absence = absence;
        this.date = date;
        this.sendNotification = sendNotification;
        this.position = position;
    }

    @Override
    public void run() {
        Log.d(TAG, "markStudentAbsentResponse Request");
        LabTabResponse<MarkStudentAbsentResponse> markStudentAbsentResponse = LabTabHTTPOperationController.markStudentAbsents(getStudents(absence.getStudent_id()), date, absence.getProgram_id(), sendNotification, LabTabApplication.getInstance().getUserAgent());
        if (markStudentAbsentResponse != null) {
            if (markStudentAbsentResponse.getResponseCode() == StatusCode.OK) {
                markStudentsAbsent(SyncStatus.SYNCED, absence);
                Log.d(TAG, "markStudentAbsentResponse Success, Response Code : " + markStudentAbsentResponse.getResponseCode() + " markStudentAbsentResponse  response: " + new Gson().toJson(markStudentAbsentResponse.getResponse()));
            } else {
                markStudentsAbsent(SyncStatus.NOT_SYNCED, absence);
                Log.d(TAG, "markStudentAbsentResponse Failed, Response Code : " + markStudentAbsentResponse.getResponseCode());
            }
        } else {
            markStudentsAbsent(SyncStatus.NOT_SYNCED, absence);
            Log.d(TAG, "markStudentAbsentResponse Failed, Response Code : " + markStudentAbsentResponse.getResponseCode());
        }
        labTabSyncService.onMarkAbsentCompleted(position);
    }


    private String[] getStudents(String student) {
        String[] students = new String[1];
        students[0] = student;
        return students;
    }

    private void markStudentsAbsent(String syncStatus, Absence absence) {
        absence.setMark_absent_synced(syncStatus);
        ProgramAbsencesTable.getInstance().updateAbsence(absence);
    }
}