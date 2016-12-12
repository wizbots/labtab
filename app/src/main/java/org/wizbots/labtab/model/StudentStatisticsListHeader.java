package org.wizbots.labtab.model;

public class StudentStatisticsListHeader {
    private String labLevel;
    private String projects;
    private String labTime;
    private String done;
    private String skipped;
    private String pending;
    private int imagineering;
    private int programming;
    private int mechanisms;
    private int structures;

    public StudentStatisticsListHeader() {
    }

    public StudentStatisticsListHeader(String labLevel, String projects, String labTime, String done, String skipped, String pending, int imagineering, int programming, int mechanisms, int structures) {
        this.labLevel = labLevel;
        this.projects = projects;
        this.labTime = labTime;
        this.done = done;
        this.skipped = skipped;
        this.pending = pending;
        this.imagineering = imagineering;
        this.programming = programming;
        this.mechanisms = mechanisms;
        this.structures = structures;
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

    public int getImagineering() {
        return imagineering;
    }

    public void setImagineering(int imagineering) {
        this.imagineering = imagineering;
    }

    public int getProgramming() {
        return programming;
    }

    public void setProgramming(int programming) {
        this.programming = programming;
    }

    public int getMechanisms() {
        return mechanisms;
    }

    public void setMechanisms(int mechanisms) {
        this.mechanisms = mechanisms;
    }

    public int getStructures() {
        return structures;
    }

    public void setStructures(int structures) {
        this.structures = structures;
    }
}

