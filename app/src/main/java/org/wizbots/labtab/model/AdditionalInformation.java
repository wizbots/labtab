package org.wizbots.labtab.model;


public class AdditionalInformation {
    private boolean check;
    private String studentName;
    private String selfCheckout;
    private String pickupInstructions;
    private String in_out_times;
    private String notes;

    public AdditionalInformation() {
    }


    public AdditionalInformation(boolean check, String studentName, String selfCheckout, String pickupInstructions, String in_out_times, String notes) {
        this.check = check;
        this.studentName = studentName;
        this.selfCheckout = selfCheckout;
        this.pickupInstructions = pickupInstructions;
        this.in_out_times = in_out_times;
        this.notes = notes;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSelfCheckout() {
        return selfCheckout;
    }

    public void setSelfCheckout(String selfCheckout) {
        this.selfCheckout = selfCheckout;
    }

    public String getPickupInstructions() {
        return pickupInstructions;
    }

    public void setPickupInstructions(String pickupInstructions) {
        this.pickupInstructions = pickupInstructions;
    }

    public String getIn_out_times() {
        return in_out_times;
    }

    public void setIn_out_times(String in_out_times) {
        this.in_out_times = in_out_times;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
