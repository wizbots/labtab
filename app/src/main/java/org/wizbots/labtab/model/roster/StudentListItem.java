package org.wizbots.labtab.model.roster;

public class StudentListItem {
    private String morning;
    private String lunch;
    private String afternoon;
    private String latePickUp;

    public StudentListItem(String morning, String lunch, String afternoon, String latePickUp) {
        this.morning = morning;
        this.lunch = lunch;
        this.afternoon = afternoon;
        this.latePickUp = latePickUp;
    }

    public String getMorning() {
        return morning;
    }

    public void setMorning(String morning) {
        this.morning = morning;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getAfternoon() {
        return afternoon;
    }

    public void setAfternoon(String afternoon) {
        this.afternoon = afternoon;
    }

    public String getLatePickUp() {
        return latePickUp;
    }

    public void setLatePickUp(String latePickUp) {
        this.latePickUp = latePickUp;
    }
}
