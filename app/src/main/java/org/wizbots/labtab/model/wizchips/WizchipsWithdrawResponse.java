package org.wizbots.labtab.model.wizchips;


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
