package org.wizbots.labtab.requesters;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.database.ProgramAbsencesTable;
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.database.ProgramTable;
import org.wizbots.labtab.interfaces.requesters.GetProgramStudentsListener;
import org.wizbots.labtab.model.program.Absence;
import org.wizbots.labtab.model.program.Program;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.program.response.AbsenceResponse;
import org.wizbots.labtab.model.program.response.ProgramResponse;
import org.wizbots.labtab.model.program.response.StudentResponse;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;

public class ProgramStudentsRequester implements Runnable, LabTabConstants {
    private String program_id;

    public ProgramStudentsRequester() {
    }

    public ProgramStudentsRequester(String program_id) {
        this.program_id = program_id;
    }

    @Override
    public void run() {
        LabTabResponse<ProgramResponse> programsWithStudents = LabTabHTTPOperationController.getProgramWithListOfStudents(program_id);
        if (programsWithStudents != null) {
            ProgramResponse programResponse = programsWithStudents.getResponse();
            for (GetProgramStudentsListener getProgramStudentsListener : LabTabApplication.getInstance().getUIListeners(GetProgramStudentsListener.class)) {
                if (programsWithStudents.getResponseCode() == StatusCode.OK) {
                    getProgramStudentsListener.programStudentsFetchedSuccessfully(programResponse,
                            fetchAndInsertProgramDetails(programResponse),
                            fetchAndInsertStudentDetails(programResponse),
                            fetchAndInsertAbsenceDetails(programResponse)
                    );
                } else {
                    getProgramStudentsListener.unableToFetchProgramStudents(programsWithStudents.getResponseCode());
                }
            }
        } else {
            for (GetProgramStudentsListener getProgramStudentsListener : LabTabApplication.getInstance().getUIListeners(GetProgramStudentsListener.class)) {
                getProgramStudentsListener.unableToFetchProgramStudents(0);
            }
        }
    }

    private Program fetchAndInsertProgramDetails(ProgramResponse programResponse) {
        Program program = new Program();
        program.setSku(programResponse.getSku());
        program.setAvailability(programResponse.getAvailability().trim().toLowerCase().equals("true") ? "YES" : "NO");
        program.setName(programResponse.getName());
        program.setLocation(programResponse.getLocation());
        program.setCategory(programResponse.getCategory());
        program.setGrades(programResponse.getGrades());
        program.setRoom(programResponse.getRoom());
        program.setPrice(programResponse.getPrice());
        program.setStarts(programResponse.getStarts());
        program.setEnds(programResponse.getEnds());
        program.setTime_slot(programResponse.getTime_slot());
        program.setCapacity(programResponse.getCapacity());
        program.setSeason(programResponse.getSeason());
        program.setId(programResponse.getId());
        program.setMember_id(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id());
        program.setSessions(LabTabUtil.toJson(programResponse.getSessions()));
        ProgramTable.getInstance().insert(program);
        return program;
    }

    private ArrayList<Student> fetchAndInsertStudentDetails(ProgramResponse programResponse) {
        ArrayList<StudentResponse> studentResponseArrayList = new ArrayList<>();
        studentResponseArrayList.addAll(programResponse.getStudents());
        ArrayList<Student> studentArrayList = new ArrayList<>();

        if (!studentResponseArrayList.isEmpty()) {
            for (StudentResponse studentResponse : studentResponseArrayList) {
                Student student = new Student();
                student.setProgram_id(programResponse.getId());
                student.setMember_id(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id());
                student.setStudent_id(studentResponse.getId());
                student.setLab_time(studentResponse.getLab_time());
                student.setCompleted(studentResponse.getStats().getCompleted());
                student.setSkipped(studentResponse.getStats().getSkipped());
                student.setPending(studentResponse.getStats().getPending());
                student.setName(studentResponse.getName());
                student.setLevel(studentResponse.getLevel());
                student.setWizchips(studentResponse.getWizchips());
                student.setSync(true);
                student.setSpecial_needs(studentResponse.getSpecial_needs());
                student.setSelf_sign_out(studentResponse.isSelf_sign_out() ? 1 : 0);
                student.setPickup_instructions(studentResponse.getPickup_instructions());
                studentArrayList.add(student);
            }
            ProgramStudentsTable.getInstance().insert(studentArrayList);
        }
        return studentArrayList;
    }

    private ArrayList<Absence> fetchAndInsertAbsenceDetails(ProgramResponse programResponse) {
        ArrayList<AbsenceResponse> absenceResponseArrayList = new ArrayList<>();
        absenceResponseArrayList.addAll(programResponse.getAbsences());
        ArrayList<Absence> absences = new ArrayList<>();

        if (!absenceResponseArrayList.isEmpty()) {
            for (AbsenceResponse absenceResponse : absenceResponseArrayList) {
                Absence absence = new Absence();
                absence.setDate(absenceResponse.getDate());
                absence.setMark_absent_synced(SyncStatus.SYNCED);
                absence.setSend_absent_notification("0");
                absence.setMentor_id(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id());
                absence.setMentor_name(absenceResponse.getMentor_name());
                absence.setProgram_id(programResponse.getId());
                absence.setStudent_id(absenceResponse.getStudent_id());
                absence.setStudent_name(searchForName(programResponse.getStudents(), absenceResponse.getStudent_id()));
                absences.add(absence);
            }
            ProgramAbsencesTable.getInstance().insert(absences);
        }
        return absences;
    }

    private String searchForName(ArrayList<StudentResponse> studentResponseArrayList, String studentId) {
        String studentName = "";
        for (StudentResponse studentResponse : studentResponseArrayList) {
            if (studentId.equals(studentResponse.getId())) {
                studentName = studentResponse.getName();
                break;
            }
        }
        return studentName;
    }
}
