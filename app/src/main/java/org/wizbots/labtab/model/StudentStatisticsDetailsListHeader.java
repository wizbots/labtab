package org.wizbots.labtab.model;

public class StudentStatisticsDetailsListHeader {
    private String studentName;
    private String labLevel;
    private String projects;
    private String labTime;
    private String done;
    private String skipped;
    private String pending;

    public StudentStatisticsDetailsListHeader() {
    }

    public StudentStatisticsDetailsListHeader(String studentName, String labLevel, String projects, String labTime, String done, String skipped, String pending) {
        this.studentName = studentName;
        this.labLevel = labLevel;
        this.projects = projects;
        this.labTime = labTime;
        this.done = done;
        this.skipped = skipped;
        this.pending = pending;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getLabLevel() {
        return labLevel;
    }

    public void setLabLevel(String labLevel) {
        this.labLevel = labLevel;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public String getLabTime() {
        return labTime;
    }

    public void setLabTime(String labTime) {
        this.labTime = labTime;
    }

    public String getDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
    }

    public String getSkipped() {
        return skipped;
    }

    public void setSkipped(String skipped) {
        this.skipped = skipped;
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }
}

