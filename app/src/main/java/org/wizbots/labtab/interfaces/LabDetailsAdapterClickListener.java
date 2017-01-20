package org.wizbots.labtab.interfaces;

import org.wizbots.labtab.model.program.Student;

public interface LabDetailsAdapterClickListener {
    void onActionViewClick(Student student);

    void onActionEditClick(Student student);

    void onActionCloseToNextLevelClick(Student student);

    void onCheckChanged(int position, boolean value);
}
