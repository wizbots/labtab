package org.wizbots.labtab.model;


public class LabDetails {
    private boolean check;
    private String studentName;
    private int level;
    private String no_of_projects;
    private String no_of_lab_time;
    private String no_of_done;
    private String no_of_skipped;
    private String no_of_pending;
    private boolean closeToNextLevel;

    public LabDetails() {
    }

    public LabDetails(boolean check, String studentName, int level, String no_of_projects, String no_of_lab_time, String no_of_done, String no_of_skipped, String no_of_pending, boolean closeToNextLevel) {
        this.check = check;
        this.studentName = studentName;
        this.level = level;
        this.no_of_projects = no_of_projects;
        this.no_of_lab_time = no_of_lab_time;
        this.no_of_done = no_of_done;
        this.no_of_skipped = no_of_skipped;
        this.no_of_pending = no_of_pending;
        this.closeToNextLevel = closeToNextLevel;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getNo_of_projects() {
        return no_of_projects;
    }

    public void setNo_of_projects(String no_of_projects) {
        this.no_of_projects = no_of_projects;
    }

    public String getNo_of_lab_time() {
        return no_of_lab_time;
    }

    public void setNo_of_lab_time(String no_of_lab_time) {
        this.no_of_lab_time = no_of_lab_time;
    }

    public String getNo_of_done() {
        return no_of_done;
    }

    public void setNo_of_done(String no_of_done) {
        this.no_of_done = no_of_done;
    }

    public String getNo_of_skipped() {
        return no_of_skipped;
    }

    public void setNo_of_skipped(String no_of_skipped) {
        this.no_of_skipped = no_of_skipped;
    }

    public String getNo_of_pending() {
        return no_of_pending;
    }

    public void setNo_of_pending(String no_of_pending) {
        this.no_of_pending = no_of_pending;
    }

    public boolean isCloseToNextLevel() {
        return closeToNextLevel;
    }

    public void setCloseToNextLevel(boolean closeToNextLevel) {
        this.closeToNextLevel = closeToNextLevel;
    }
}
