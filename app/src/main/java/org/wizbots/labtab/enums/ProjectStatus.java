package org.wizbots.labtab.enums;

import org.wizbots.labtab.R;

public enum ProjectStatus {

    DONE(R.drawable.ic_marks_done),
    SKIPPED(R.drawable.ic_marks_skipped),
    PENDING(R.drawable.ic_marks_pending),
    IMAGINEERING(R.drawable.ic_marks_imagineering),
    PROGRAMMING(R.drawable.ic_marks_programming),
    MECHANISMS(R.drawable.ic_marks_mechanisms),
    STRUCTURES(R.drawable.ic_marks_structures),
    CLOSE_NEXT_LEVEL(R.drawable.ic_marks_close_to_next_level);

    private int value;

    ProjectStatus(int value) {
        this.value = value;
    }

    public int getProjectStatus() {
        return value;
    }

}
