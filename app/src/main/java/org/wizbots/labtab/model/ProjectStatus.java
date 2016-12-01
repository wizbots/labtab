package org.wizbots.labtab.model;

import org.wizbots.labtab.R;

public enum ProjectStatus {

    DONE(R.drawable.marks_done),
    PENDING(R.drawable.marks_pending),
    SKIPPED(R.drawable.marks_skipped);

    private int value;

    ProjectStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
