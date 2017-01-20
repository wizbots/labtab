package org.wizbots.labtab.model.student.response;


import java.util.ArrayList;

public class ApprenticeResponse {
    private String lab_time;
    private ArrayList<String> imagineering;
    private ArrayList<String> mechanisms;
    private ArrayList<String> programming;
    private ArrayList<String> structures;
    private ArrayList<ProjectResponse> projects;

    public ApprenticeResponse() {
    }

    public ApprenticeResponse(String lab_time, ArrayList<String> imagineering, ArrayList<String> mechanisms, ArrayList<String> programming, ArrayList<String> structures, ArrayList<ProjectResponse> projects) {
        this.lab_time = lab_time;
        this.imagineering = imagineering;
        this.mechanisms = mechanisms;
        this.programming = programming;
        this.structures = structures;
        this.projects = projects;
    }

    public String getLab_time() {
        return lab_time;
    }

    public void setLab_time(String lab_time) {
        this.lab_time = lab_time;
    }

    public ArrayList<String> getImagineering() {
        return imagineering;
    }

    public void setImagineering(ArrayList<String> imagineering) {
        this.imagineering = imagineering;
    }

    public ArrayList<String> getMechanisms() {
        return mechanisms;
    }

    public void setMechanisms(ArrayList<String> mechanisms) {
        this.mechanisms = mechanisms;
    }

    public ArrayList<String> getProgramming() {
        return programming;
    }

    public void setProgramming(ArrayList<String> programming) {
        this.programming = programming;
    }

    public ArrayList<String> getStructures() {
        return structures;
    }

    public void setStructures(ArrayList<String> structures) {
        this.structures = structures;
    }

    public ArrayList<ProjectResponse> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<ProjectResponse> projects) {
        this.projects = projects;
    }
}
