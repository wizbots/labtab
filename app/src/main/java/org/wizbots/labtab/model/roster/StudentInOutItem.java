package org.wizbots.labtab.model.roster;

public class StudentInOutItem {
    private String date;
    private String inTime;
    private String outTime;
    private Boolean isInSigned;
    private Boolean isOutSigned;

    public StudentInOutItem(String date, String inTime, String outTime, Boolean isInSigned, Boolean isOutSigned) {
        this.date = date;
        this.inTime = inTime;
        this.outTime = outTime;
        this.isInSigned = isInSigned;
        this.isOutSigned = isOutSigned;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public Boolean getInSigned() {
        return isInSigned;
    }

    public void setInSigned(Boolean inSigned) {
        isInSigned = inSigned;
    }

    public Boolean getOutSigned() {
        return isOutSigned;
    }

    public void setOutSigned(Boolean outSigned) {
        isOutSigned = outSigned;
    }
}
