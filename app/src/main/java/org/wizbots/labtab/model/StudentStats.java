package org.wizbots.labtab.model;


import java.util.ArrayList;

public class StudentStats {
    String id;
    String first_name;
    String last_name;
    ArrayList<ProjectHistory> project_history;

    public StudentStats() {
    }

    public StudentStats(String id, String first_name, String last_name, ArrayList<ProjectHistory> project_history) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.project_history = project_history;
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

    public ArrayList<ProjectHistory> getProject_history() {
        return project_history;
    }

    public void setProject_history(ArrayList<ProjectHistory> project_history) {
        this.project_history = project_history;
    }
}
