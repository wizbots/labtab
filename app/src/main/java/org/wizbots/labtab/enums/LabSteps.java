package org.wizbots.labtab.enums;

import org.wizbots.labtab.R;

public enum LabSteps {

    LAB_STEP1(R.drawable.ic_lab_step_1),
    LAB_STEP2(R.drawable.ic_lab_step_2),
    LAB_STEP3(R.drawable.ic_lab_step_3),
    LAB_STEP4(R.drawable.ic_lab_step_4);

    private int value;

    LabSteps(int value) {
        this.value = value;
    }

    public int getLabStep() {
        return value;
    }

}
