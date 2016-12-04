package org.wizbots.labtab.model;

import java.util.ArrayList;

public class Student {
    private String id;
    private String first_name;
    private String last_name;
    private String level;
    private ArrayList<Project> projects_history;

    public Student() {
    }

    public Student(String id, String first_name, String last_name, String level, ArrayList<Project> projects_history) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.level = level;
        this.projects_history = projects_history;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public ArrayList<Project> getProjects_history() {
        return projects_history;
    }

    public void setProjects_history(ArrayList<Project> projects_history) {
        this.projects_history = projects_history;
    }
}
