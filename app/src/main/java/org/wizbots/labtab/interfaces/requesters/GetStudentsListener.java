package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;
import org.wizbots.labtab.model.program.Student;

import java.util.ArrayList;

public interface GetStudentsListener extends BaseUIListener {
    void studentsFetched(ArrayList<Student> studentArrayList);
}
