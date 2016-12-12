package org.wizbots.labtab.enums;

import org.wizbots.labtab.R;

public enum ProjectStatus {

    DONE(R.drawable.marks_done),
    SKIPPED(R.drawable.marks_skipped),
    PENDING(R.drawable.marks_pending),
    IMAGINEERING(R.drawable.marks_imagineering),
    PROGRAMMING(R.drawable.marks_programming),
    MECHANISMS(R.drawable.marks_mechanisms),
    STRUCTURES(R.drawable.marks_structures),
    CLOSE_NEXT_LEVEL(R.drawable.marks_close_to_next_level);

    private int value;

    ProjectStatus(int value) {
        this.value = value;
    }

    public int getProjectStatus() {
        return value;
    }

}
