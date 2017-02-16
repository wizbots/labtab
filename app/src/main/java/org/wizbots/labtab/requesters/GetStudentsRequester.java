package org.wizbots.labtab.requesters;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.interfaces.requesters.GetStudentsListener;
import org.wizbots.labtab.model.program.Program;
import org.wizbots.labtab.model.program.Student;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GetStudentsRequester implements Runnable, LabTabConstants {

    ArrayList<String> selectedStudents;
    Program program;

    public GetStudentsRequester() {
    }

    public GetStudentsRequester(ArrayList<String> selectedStudents, Program program) {
        this.selectedStudents = selectedStudents;
        this.program = program;
    }

    @Override
    public void run() {
        ArrayList<Student> studentsAvailable = ProgramStudentsTable.getInstance().getStudentsListByProgramId(program.getId());
        ArrayList<Student> studentArrayList = new ArrayList<>();
        if (!selectedStudents.isEmpty()) {
            Set<String> uniqueIds = new HashSet<>();
            uniqueIds.addAll(selectedStudents);
            selectedStudents.clear();
            selectedStudents.addAll(uniqueIds);
            for (String string : selectedStudents) {
                for (Student student : studentsAvailable) {
                    if (student.getStudent_id().equals(string)) {
                        studentArrayList.add(student);
                        break;
                    }
                }
            }
        }
        for (GetStudentsListener getStudentsListener : LabTabApplication.getInstance().getUIListeners(GetStudentsListener.class)) {
            getStudentsListener.studentsFetched(studentArrayList);
        }

    }

}
