package org.wizbots.labtab.model;

import org.wizbots.labtab.R;

public enum LabLevel {

    APPRENTICE(R.drawable.level_apprentice),
    EXPLORER(R.drawable.level_explorer),
    IMAGINEER(R.drawable.level_imagineer),
    LAB_CERTIFIED(R.drawable.level_lab_certified),
    MAKER(R.drawable.level_maker),
    MASTER(R.drawable.level_master),
    WIZARD(R.drawable.level_wizard);

    private int value;

    LabLevel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
