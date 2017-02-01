package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;
import org.wizbots.labtab.model.program.Student;

import java.util.ArrayList;

public interface MarkStudentAbsentListener extends BaseUIListener {
    void markAbsentSuccessful(ArrayList<Student> students, String date);

    void markAbsentUnSuccessful(int status);
}
