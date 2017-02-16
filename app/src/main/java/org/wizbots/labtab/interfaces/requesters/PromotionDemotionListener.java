package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;
import org.wizbots.labtab.model.program.Program;
import org.wizbots.labtab.model.program.Student;

import java.util.ArrayList;

public interface PromotionDemotionListener extends BaseUIListener {
    void promotionDemotionSuccessful(ArrayList<Student> student, Program program, boolean promote);

    void promotionDemotionUnSuccessful(int status, ArrayList<Student> student, Program program, boolean promote);
}
