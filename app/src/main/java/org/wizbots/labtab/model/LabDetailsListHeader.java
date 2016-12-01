package org.wizbots.labtab.model;


public class LabDetailsListHeader {
    private String check;
    private String name;
    private String level;
    private String projects;
    private String lab_time;
    private int done;
    private int skipped;
    private int pending;
    private String action;

    public LabDetailsListHeader() {
    }

    public LabDetailsListHeader(String check, String name, String level, String projects, String lab_time, int done, int skipped, int pending, String action) {
        this.check = check;
        this.name = name;
        this.level = level;
        this.projects = projects;
        this.lab_time = lab_time;
        this.done = done;
        this.skipped = skipped;
        this.pending = pending;
        this.action = action;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public String getLab_time() {
        return lab_time;
    }

    public void setLab_time(String lab_time) {
        this.lab_time = lab_time;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
