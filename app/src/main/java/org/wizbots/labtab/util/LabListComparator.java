package org.wizbots.labtab.util;

import org.wizbots.labtab.model.ProgramOrLab;

import java.util.Comparator;

/**
 * Created by ashish on 25/2/17.
 */

public class LabListComparator implements Comparator<ProgramOrLab> {

    public static final LabListComparator CHAT_COMPARATOR = new LabListComparator();

    @Override
    public int compare(ProgramOrLab object1, ProgramOrLab object2) {
        if (object1.getTitle() == null) {
            if (object2.getTitle() != null)
                return 1;
            return 0;
        } else {
            if (object2.getTitle() == null)
                return -1;
            return object1.getTitle().compareTo(object2.getTitle());
        }
    }
}
