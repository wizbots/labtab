package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;
import org.wizbots.labtab.model.student.StudentProfile;
import org.wizbots.labtab.model.student.StudentStats;
import org.wizbots.labtab.model.student.response.StudentResponse;

import java.util.ArrayList;

public interface GetStudentProfileAndStatsListener extends BaseUIListener {
    void studentProfileFetchedSuccessfully(StudentResponse studentResponse, StudentProfile studentProfile, ArrayList<StudentStats> statsArrayList);

    void unableToFetchStudent(int responseCode);
}
