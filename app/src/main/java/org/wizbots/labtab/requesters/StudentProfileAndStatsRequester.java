package org.wizbots.labtab.requesters;

import android.util.Log;

import com.google.gson.Gson;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.database.StudentStatsTable;
import org.wizbots.labtab.database.StudentsProfileTable;
import org.wizbots.labtab.interfaces.requesters.GetStudentProfileAndStatsListener;
import org.wizbots.labtab.model.student.StudentProfile;
import org.wizbots.labtab.model.student.StudentStats;
import org.wizbots.labtab.model.student.response.ApprenticeResponse;
import org.wizbots.labtab.model.student.response.ExplorerResponse;
import org.wizbots.labtab.model.student.response.ImagineerResponse;
import org.wizbots.labtab.model.student.response.LabCertifiedResponse;
import org.wizbots.labtab.model.student.response.MakerResponse;
import org.wizbots.labtab.model.student.response.MasterResponse;
import org.wizbots.labtab.model.student.response.ProjectResponse;
import org.wizbots.labtab.model.student.response.StudentResponse;
import org.wizbots.labtab.model.student.response.WizardResponse;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.util.LabTabUtil;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class StudentProfileAndStatsRequester implements Runnable, LabTabConstants {
    private final String TAG = StudentProfileAndStatsRequester.class.getSimpleName();
    private String student_id;
    StudentResponse studentProfileAndStatsResponse;
    StudentProfile studentProfile;
    ArrayList<StudentStats> statsArrayList;

    public StudentProfileAndStatsRequester() {
    }

    public StudentProfileAndStatsRequester(String student_id) {
        this.student_id = student_id;
    }

    @Override
    public void run() {
        Log.d(TAG, "studentResponse Request");
        int statusCode = 0;
        LabTabResponse<StudentResponse> studentResponse = LabTabHTTPOperationController.getStudentStatsAndProfile(student_id);
/*        if (studentResponse != null) {
            StudentResponse studentProfileAndStatsResponse = studentResponse.getResponse();
            StudentProfile studentProfile = getStudentProfile(studentProfileAndStatsResponse);
            ArrayList<StudentStats> statsArrayList = getStudentStats(studentProfileAndStatsResponse);

            for (GetStudentProfileAndStatsListener getProgramStudentsListener : LabTabApplication.getInstance().getUIListeners(GetStudentProfileAndStatsListener.class)) {
                if (studentResponse.getResponseCode() == StatusCode.OK) {
                    getProgramStudentsListener.studentProfileFetchedSuccessfully(studentProfileAndStatsResponse,
                            studentProfile,
                            statsArrayList);
                } else {
                    getProgramStudentsListener.unableToFetchStudent(studentResponse.getResponseCode());
                }
            }
        } else {
            for (GetStudentProfileAndStatsListener getStudentProfileAndStatsListener : LabTabApplication.getInstance().getUIListeners(GetStudentProfileAndStatsListener.class)) {
                getStudentProfileAndStatsListener.unableToFetchStudent(0);
            }
        }*/

        if (studentResponse != null) {
            statusCode = studentResponse.getResponseCode();
            if(statusCode == HttpURLConnection.HTTP_OK){
                studentProfileAndStatsResponse = studentResponse.getResponse();
                studentProfile = getStudentProfile(studentProfileAndStatsResponse);
                statsArrayList = getStudentStats(studentProfileAndStatsResponse);
                Log.d(TAG, "studentResponse Success, Response Code : " + studentResponse.getResponseCode() + " studentResponse response: " + new Gson().toJson(studentResponse.getResponse()));
            }
        }else{
            Log.d(TAG, "studentResponse Failed, Response Code : " + statusCode);
        }

        for (GetStudentProfileAndStatsListener getProgramStudentsListener : LabTabApplication.getInstance().getUIListeners(GetStudentProfileAndStatsListener.class)) {
            if (statusCode == StatusCode.OK && studentProfileAndStatsResponse != null && studentProfile != null && statsArrayList != null) {
                getProgramStudentsListener.studentProfileFetchedSuccessfully(studentProfileAndStatsResponse, studentProfile, statsArrayList);
            }else if(!LabTabApplication.getInstance().isNetworkAvailable() && studentProfileAndStatsResponse == null && studentProfile == null && statsArrayList == null){
                getProgramStudentsListener.offlineNoData();
            } else {
                getProgramStudentsListener.unableToFetchStudent(statusCode);
            }
        }
    }

    private StudentProfile getStudentProfile(StudentResponse studentResponse) {
        StudentProfile studentProfile = new StudentProfile();
        studentProfile.setEnrollments_count(studentResponse.getEnrollments_count());

        studentProfile.setLast_name(studentResponse.getLast_name());
        studentProfile.setDate_of_birth(studentResponse.getDate_of_birth());

        studentProfile.setCreator(LabTabUtil.toJson(studentResponse.getCreator()));
        studentProfile.setGrade(studentResponse.getGrade());

        studentProfile.setAfter_care_after(studentResponse.getAfter_care_after());
        studentProfile.setId(studentResponse.getId());

        studentProfile.setFirst_name(studentResponse.getFirst_name());
        studentProfile.setLevel(studentResponse.getLevel());

        studentProfile.setAbsence_count(studentResponse.getAbsence_count());
        studentProfile.setAllergies(studentResponse.getAllergies());

        studentProfile.setSpecial_needs(studentResponse.getSpecial_needs());
        studentProfile.setAfter_care_before(studentResponse.getAfter_care_before());

        studentProfile.setAfter_care_phone(studentResponse.getAfter_care_phone());
        studentProfile.setAfter_care_name(studentResponse.getAfter_care_name());
        StudentsProfileTable.getInstance().insert(studentProfile);
        return studentProfile;
    }

    private ArrayList<StudentStats> getStudentStats(StudentResponse studentResponse) {
        ArrayList<StudentStats> statsArrayList = new ArrayList<>();
        statsArrayList.add(getImagineerStats(studentResponse));
        statsArrayList.add(getExplorerStats(studentResponse));
        statsArrayList.add(getWizardStats(studentResponse));
        statsArrayList.add(getMasterStats(studentResponse));
        statsArrayList.add(getLabCertifiedStats(studentResponse));
        statsArrayList.add(getMakerStats(studentResponse));
        statsArrayList.add(getApprenticeStats(studentResponse));
        StudentStatsTable.getInstance().insert(statsArrayList);
        return statsArrayList;
    }

    private int getCount(ArrayList<ProjectResponse> projectResponseArrayList, String type) {
        int count = 0;
        if (projectResponseArrayList.isEmpty()) {
            return count;
        }
        switch (type) {
            case Marks.DONE:
                for (ProjectResponse projectResponse : projectResponseArrayList) {
                    if (projectResponse.getProjectStatus().toUpperCase().equals(Marks.DONE)) {
                        count++;
                    }
                }
                break;
            case Marks.SKIPPED:
                for (ProjectResponse projectResponse : projectResponseArrayList) {
                    if (projectResponse.getProjectStatus().toUpperCase().equals(Marks.SKIPPED)) {
                        count++;
                    }
                }
                break;
            case Marks.PENDING:
                for (ProjectResponse projectResponse : projectResponseArrayList) {
                    if (projectResponse.getProjectStatus().toUpperCase().equals(Marks.PENDING)) {
                        count++;
                    }
                }
                break;
            default:
                count = 0;
                break;
        }
        return count;
    }

    private ArrayList<ProjectResponse> getProjects(ArrayList<ProjectResponse> projectResponseArrayList, String type) {
        ArrayList<ProjectResponse> responseArrayList = new ArrayList<>();
        if (projectResponseArrayList.isEmpty()) {
            return responseArrayList;
        }
        switch (type) {
            case Marks.DONE:
                for (ProjectResponse projectResponse : projectResponseArrayList) {
                    if (projectResponse.getProjectStatus().toUpperCase().equals(Marks.DONE)) {
                        responseArrayList.add(projectResponse);
                    }
                }
                break;
            case Marks.SKIPPED:
                for (ProjectResponse projectResponse : projectResponseArrayList) {
                    if (projectResponse.getProjectStatus().toUpperCase().equals(Marks.SKIPPED)) {
                        responseArrayList.add(projectResponse);
                    }
                }
                break;
            case Marks.PENDING:
                for (ProjectResponse projectResponse : projectResponseArrayList) {
                    if (projectResponse.getProjectStatus().toUpperCase().equals(Marks.PENDING)) {
                        responseArrayList.add(projectResponse);
                    }
                }
                break;
            default:
                break;
        }
        return responseArrayList;
    }

    private StudentStats getImagineerStats(StudentResponse studentResponse) {
        ImagineerResponse imagineerResponse = studentResponse.getProject_history().getImagineer();

        StudentStats studentStatLevelImagineer = new StudentStats();
        studentStatLevelImagineer.setId(studentResponse.getId());
        studentStatLevelImagineer.setLevel(LabLevels.IMAGINEER);

        studentStatLevelImagineer.setProject_count(imagineerResponse.getProjects().size());
        studentStatLevelImagineer.setLab_time_count(imagineerResponse.getLab_time());
        studentStatLevelImagineer.setDone_count(getCount(imagineerResponse.getProjects(), Marks.DONE));
        studentStatLevelImagineer.setSkipped_count(getCount(imagineerResponse.getProjects(), Marks.SKIPPED));
        studentStatLevelImagineer.setPending_count(getCount(imagineerResponse.getProjects(), Marks.PENDING));

        studentStatLevelImagineer.setProjects(LabTabUtil.toJson(imagineerResponse.getProjects()));

        studentStatLevelImagineer.setImagineering_count(imagineerResponse.getImagineering().size());
        studentStatLevelImagineer.setProgramming_count(imagineerResponse.getProgramming().size());
        studentStatLevelImagineer.setMechanisms_count(imagineerResponse.getMechanisms().size());
        studentStatLevelImagineer.setStructures_count(imagineerResponse.getStructures().size());

        studentStatLevelImagineer.setProjects_imagineering(LabTabUtil.toJson(imagineerResponse.getImagineering()));
        studentStatLevelImagineer.setProjects_programming(LabTabUtil.toJson(imagineerResponse.getProgramming()));
        studentStatLevelImagineer.setProjects_mechanisms(LabTabUtil.toJson(imagineerResponse.getMechanisms()));
        studentStatLevelImagineer.setProjects_structures(LabTabUtil.toJson(imagineerResponse.getStructures()));

        return studentStatLevelImagineer;
    }

    private StudentStats getExplorerStats(StudentResponse studentResponse) {
        ExplorerResponse explorerResponse = studentResponse.getProject_history().getExplorer();

        StudentStats studentStatLevelExplorer = new StudentStats();
        studentStatLevelExplorer.setId(studentResponse.getId());
        studentStatLevelExplorer.setLevel(LabLevels.EXPLORER);

        studentStatLevelExplorer.setProject_count(explorerResponse.getProjects().size());
        studentStatLevelExplorer.setLab_time_count(explorerResponse.getLab_time());
        studentStatLevelExplorer.setDone_count(getCount(explorerResponse.getProjects(), Marks.DONE));
        studentStatLevelExplorer.setSkipped_count(getCount(explorerResponse.getProjects(), Marks.SKIPPED));
        studentStatLevelExplorer.setPending_count(getCount(explorerResponse.getProjects(), Marks.PENDING));

        studentStatLevelExplorer.setProjects(LabTabUtil.toJson(explorerResponse.getProjects()));

        studentStatLevelExplorer.setImagineering_count(explorerResponse.getImagineering().size());
        studentStatLevelExplorer.setProgramming_count(explorerResponse.getProgramming().size());
        studentStatLevelExplorer.setMechanisms_count(explorerResponse.getMechanisms().size());
        studentStatLevelExplorer.setStructures_count(explorerResponse.getStructures().size());

        studentStatLevelExplorer.setProjects_imagineering(LabTabUtil.toJson(explorerResponse.getImagineering()));
        studentStatLevelExplorer.setProjects_programming(LabTabUtil.toJson(explorerResponse.getProgramming()));
        studentStatLevelExplorer.setProjects_mechanisms(LabTabUtil.toJson(explorerResponse.getMechanisms()));
        studentStatLevelExplorer.setProjects_structures(LabTabUtil.toJson(explorerResponse.getStructures()));

        return studentStatLevelExplorer;
    }

    private StudentStats getWizardStats(StudentResponse studentResponse) {
        WizardResponse wizardResponse = studentResponse.getProject_history().getWizard();

        StudentStats studentStatLevelWizard = new StudentStats();
        studentStatLevelWizard.setId(studentResponse.getId());
        studentStatLevelWizard.setLevel(LabLevels.WIZARD);

        studentStatLevelWizard.setProject_count(wizardResponse.getProjects().size());
        studentStatLevelWizard.setLab_time_count(wizardResponse.getLab_time());
        studentStatLevelWizard.setDone_count(getCount(wizardResponse.getProjects(), Marks.DONE));
        studentStatLevelWizard.setSkipped_count(getCount(wizardResponse.getProjects(), Marks.SKIPPED));
        studentStatLevelWizard.setPending_count(getCount(wizardResponse.getProjects(), Marks.PENDING));

        studentStatLevelWizard.setProjects(LabTabUtil.toJson(wizardResponse.getProjects()));

        studentStatLevelWizard.setImagineering_count(wizardResponse.getImagineering().size());
        studentStatLevelWizard.setProgramming_count(wizardResponse.getProgramming().size());
        studentStatLevelWizard.setMechanisms_count(wizardResponse.getMechanisms().size());
        studentStatLevelWizard.setStructures_count(wizardResponse.getStructures().size());

        studentStatLevelWizard.setProjects_imagineering(LabTabUtil.toJson(wizardResponse.getImagineering()));
        studentStatLevelWizard.setProjects_programming(LabTabUtil.toJson(wizardResponse.getProgramming()));
        studentStatLevelWizard.setProjects_mechanisms(LabTabUtil.toJson(wizardResponse.getMechanisms()));
        studentStatLevelWizard.setProjects_structures(LabTabUtil.toJson(wizardResponse.getStructures()));

        return studentStatLevelWizard;
    }

    private StudentStats getMasterStats(StudentResponse studentResponse) {
        MasterResponse masterResponse = studentResponse.getProject_history().getMaster();

        StudentStats studentStatLevelMaster = new StudentStats();
        studentStatLevelMaster.setId(studentResponse.getId());
        studentStatLevelMaster.setLevel(LabLevels.MASTER);

        studentStatLevelMaster.setProject_count(masterResponse.getProjects().size());
        studentStatLevelMaster.setLab_time_count(masterResponse.getLab_time());
        studentStatLevelMaster.setDone_count(getCount(masterResponse.getProjects(), Marks.DONE));
        studentStatLevelMaster.setSkipped_count(getCount(masterResponse.getProjects(), Marks.SKIPPED));
        studentStatLevelMaster.setPending_count(getCount(masterResponse.getProjects(), Marks.PENDING));

        studentStatLevelMaster.setProjects(LabTabUtil.toJson(masterResponse.getProjects()));

        studentStatLevelMaster.setImagineering_count(masterResponse.getImagineering().size());
        studentStatLevelMaster.setProgramming_count(masterResponse.getProgramming().size());
        studentStatLevelMaster.setMechanisms_count(masterResponse.getMechanisms().size());
        studentStatLevelMaster.setStructures_count(masterResponse.getStructures().size());

        studentStatLevelMaster.setProjects_imagineering(LabTabUtil.toJson(masterResponse.getImagineering()));
        studentStatLevelMaster.setProjects_programming(LabTabUtil.toJson(masterResponse.getProgramming()));
        studentStatLevelMaster.setProjects_mechanisms(LabTabUtil.toJson(masterResponse.getMechanisms()));
        studentStatLevelMaster.setProjects_structures(LabTabUtil.toJson(masterResponse.getStructures()));

        return studentStatLevelMaster;
    }

    private StudentStats getLabCertifiedStats(StudentResponse studentResponse) {
        LabCertifiedResponse labCertifiedResponse = studentResponse.getProject_history().getLab_certified();

        StudentStats studentStatLabCertified = new StudentStats();
        studentStatLabCertified.setId(studentResponse.getId());
        studentStatLabCertified.setLevel(LabLevels.LAB_CERTIFIED);

        studentStatLabCertified.setProject_count(labCertifiedResponse.getProjects().size());
        studentStatLabCertified.setLab_time_count(labCertifiedResponse.getLab_time());
        studentStatLabCertified.setDone_count(getCount(labCertifiedResponse.getProjects(), Marks.DONE));
        studentStatLabCertified.setSkipped_count(getCount(labCertifiedResponse.getProjects(), Marks.SKIPPED));
        studentStatLabCertified.setPending_count(getCount(labCertifiedResponse.getProjects(), Marks.PENDING));

        studentStatLabCertified.setProjects(LabTabUtil.toJson(labCertifiedResponse.getProjects()));

        studentStatLabCertified.setImagineering_count(labCertifiedResponse.getImagineering().size());
        studentStatLabCertified.setProgramming_count(labCertifiedResponse.getProgramming().size());
        studentStatLabCertified.setMechanisms_count(labCertifiedResponse.getMechanisms().size());
        studentStatLabCertified.setStructures_count(labCertifiedResponse.getStructures().size());

        studentStatLabCertified.setProjects_imagineering(LabTabUtil.toJson(labCertifiedResponse.getImagineering()));
        studentStatLabCertified.setProjects_programming(LabTabUtil.toJson(labCertifiedResponse.getProgramming()));
        studentStatLabCertified.setProjects_mechanisms(LabTabUtil.toJson(labCertifiedResponse.getMechanisms()));
        studentStatLabCertified.setProjects_structures(LabTabUtil.toJson(labCertifiedResponse.getStructures()));

        return studentStatLabCertified;
    }

    private StudentStats getMakerStats(StudentResponse studentResponse) {
        MakerResponse makerResponse = studentResponse.getProject_history().getMaker();

        StudentStats studentStatLevelMaker = new StudentStats();
        studentStatLevelMaker.setId(studentResponse.getId());
        studentStatLevelMaker.setLevel(LabLevels.MAKER);

        studentStatLevelMaker.setProject_count(makerResponse.getProjects().size());
        studentStatLevelMaker.setLab_time_count(makerResponse.getLab_time());
        studentStatLevelMaker.setDone_count(getCount(makerResponse.getProjects(), Marks.DONE));
        studentStatLevelMaker.setSkipped_count(getCount(makerResponse.getProjects(), Marks.SKIPPED));
        studentStatLevelMaker.setPending_count(getCount(makerResponse.getProjects(), Marks.PENDING));

        studentStatLevelMaker.setProjects(LabTabUtil.toJson(makerResponse.getProjects()));

        studentStatLevelMaker.setImagineering_count(makerResponse.getImagineering().size());
        studentStatLevelMaker.setProgramming_count(makerResponse.getProgramming().size());
        studentStatLevelMaker.setMechanisms_count(makerResponse.getMechanisms().size());
        studentStatLevelMaker.setStructures_count(makerResponse.getStructures().size());

        studentStatLevelMaker.setProjects_imagineering(LabTabUtil.toJson(makerResponse.getImagineering()));
        studentStatLevelMaker.setProjects_programming(LabTabUtil.toJson(makerResponse.getProgramming()));
        studentStatLevelMaker.setProjects_mechanisms(LabTabUtil.toJson(makerResponse.getMechanisms()));
        studentStatLevelMaker.setProjects_structures(LabTabUtil.toJson(makerResponse.getStructures()));

        return studentStatLevelMaker;
    }

    private StudentStats getApprenticeStats(StudentResponse studentResponse) {
        ApprenticeResponse apprenticeResponse = studentResponse.getProject_history().getApprentice();

        StudentStats studentStatLevelApprentice = new StudentStats();
        studentStatLevelApprentice.setId(studentResponse.getId());
        studentStatLevelApprentice.setLevel(LabLevels.APPRENTICE);

        studentStatLevelApprentice.setProject_count(apprenticeResponse.getProjects().size());
        studentStatLevelApprentice.setLab_time_count(apprenticeResponse.getLab_time());
        studentStatLevelApprentice.setDone_count(getCount(apprenticeResponse.getProjects(), Marks.DONE));
        studentStatLevelApprentice.setSkipped_count(getCount(apprenticeResponse.getProjects(), Marks.SKIPPED));
        studentStatLevelApprentice.setPending_count(getCount(apprenticeResponse.getProjects(), Marks.PENDING));

        studentStatLevelApprentice.setProjects(LabTabUtil.toJson(apprenticeResponse.getProjects()));

        studentStatLevelApprentice.setImagineering_count(apprenticeResponse.getImagineering().size());
        studentStatLevelApprentice.setProgramming_count(apprenticeResponse.getProgramming().size());
        studentStatLevelApprentice.setMechanisms_count(apprenticeResponse.getMechanisms().size());
        studentStatLevelApprentice.setStructures_count(apprenticeResponse.getStructures().size());

        studentStatLevelApprentice.setProjects_imagineering(LabTabUtil.toJson(apprenticeResponse.getImagineering()));
        studentStatLevelApprentice.setProjects_programming(LabTabUtil.toJson(apprenticeResponse.getProgramming()));
        studentStatLevelApprentice.setProjects_mechanisms(LabTabUtil.toJson(apprenticeResponse.getMechanisms()));
        studentStatLevelApprentice.setProjects_structures(LabTabUtil.toJson(apprenticeResponse.getStructures()));

        return studentStatLevelApprentice;
    }

}
