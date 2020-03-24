package org.wizbots.labtab.model.roster;

public class RosterThirdListItem {
    private String studentName;
    private String pickup;
    private String dropOff;
    private String afterCareProvider;
    private String afterCarePhone;

    public RosterThirdListItem(String studentName, String pickup, String dropOff, String afterCareProvider, String afterCarePhone) {
        this.studentName = studentName;
        this.pickup = pickup;
        this.dropOff = dropOff;
        this.afterCareProvider = afterCareProvider;
        this.afterCarePhone = afterCarePhone;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDropOff() {
        return dropOff;
    }

    public void setDropOff(String dropOff) {
        this.dropOff = dropOff;
    }

    public String getAfterCareProvider() {
        return afterCareProvider;
    }

    public void setAfterCareProvider(String afterCareProvider) {
        this.afterCareProvider = afterCareProvider;
    }

    public String getAfterCarePhone() {
        return afterCarePhone;
    }

    public void setAfterCarePhone(String afterCarePhone) {
        this.afterCarePhone = afterCarePhone;
    }
}
