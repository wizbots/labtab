package org.wizbots.labtab.model.wizchips;

/**
 * Created by ashish on 7/2/17.
 */

public class WizchipsWithdrawResponse {

    int wizchips;
    boolean success;

    public int getWizchips() {
        return wizchips;
    }

    public void setWizchips(int wizchips) {
        this.wizchips = wizchips;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
