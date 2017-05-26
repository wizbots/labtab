package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;
import org.wizbots.labtab.model.program.Absence;
import org.wizbots.labtab.model.program.Program;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.program.response.ProgramResponse;

import java.util.ArrayList;

public interface GetProgramStudentsListener extends BaseUIListener {
    void programStudentsFetchedSuccessfully(ProgramResponse programResponse, Program program, ArrayList<Student> studentArrayList, ArrayList<Absence> absenceArrayList);

    void unableToFetchProgramStudents(int responseCode);
}
