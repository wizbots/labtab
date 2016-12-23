package org.wizbots.labtab.model;


public class ListOfSkips {
    private boolean check;
    private String studentName;
    private String no_of_hours;
    private String no_of_generals;
    private String notes;

    public ListOfSkips() {
    }

    public ListOfSkips(boolean check, String studentName, String no_of_hours, String no_of_generals, String notes) {
        this.check = check;
        this.studentName = studentName;
        this.no_of_hours = no_of_hours;
        this.no_of_generals = no_of_generals;
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

    public String getNo_of_hours() {
        return no_of_hours;
    }

    public void setNo_of_hours(String no_of_hours) {
        this.no_of_hours = no_of_hours;
    }

    public String getNo_of_generals() {
        return no_of_generals;
    }

    public void setNo_of_generals(String no_of_generals) {
        this.no_of_generals = no_of_generals;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
