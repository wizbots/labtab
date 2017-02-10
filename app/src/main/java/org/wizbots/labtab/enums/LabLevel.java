package org.wizbots.labtab.enums;

import org.wizbots.labtab.R;

public enum LabLevel {

    APPRENTICE(R.drawable.ic_level_apprentice),
    EXPLORER(R.drawable.ic_level_explorer),
    IMAGINEER(R.drawable.ic_level_imagineer),
    LAB_CERTIFIED(R.drawable.ic_level_lab_certified),
    MAKER(R.drawable.ic_level_maker),
    MASTER(R.drawable.ic_level_master),
    WIZARD(R.drawable.ic_level_wizard),
    NOVICE(R.drawable.ic_level_novice);

    private int value;

    LabLevel(int value) {
        this.value = value;
    }

    public int getLabLevel() {
        return value;
    }

}
