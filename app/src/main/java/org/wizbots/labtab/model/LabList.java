package org.wizbots.labtab.model;


public class LabList {
    int level;
    String labName;
    int action;

    public LabList() {
    }

    public LabList(int level, String labName, int action) {
        this.level = level;
        this.labName = labName;
        this.action = action;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
