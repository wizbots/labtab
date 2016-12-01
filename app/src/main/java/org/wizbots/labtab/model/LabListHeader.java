package org.wizbots.labtab.model;


public class LabListHeader {
    private String level;
    private String name;
    private String action;

    public LabListHeader() {
    }

    public LabListHeader(String level, String name, String action) {
        this.level = level;
        this.name = name;
        this.action = action;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
