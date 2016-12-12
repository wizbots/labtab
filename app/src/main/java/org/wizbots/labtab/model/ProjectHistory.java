package org.wizbots.labtab.model;

import java.util.ArrayList;

public class ProjectHistory {
    String lablevel;
    ArrayList<Project> projects;
    int labTime;
    int imagineeringNuggetCount;
    int mechaninismsNuggetCount;
    int programmingNuggetCount;
    int structuresNuggetCount;

    public ProjectHistory() {
    }

    public ProjectHistory(String lablevel, ArrayList<Project> projects, int labTime, int imagineeringNuggetCount, int mechaninismsNuggetCount, int programmingNuggetCount, int structuresNuggetCount) {
        this.lablevel = lablevel;
        this.projects = projects;
        this.labTime = labTime;
        this.imagineeringNuggetCount = imagineeringNuggetCount;
        this.mechaninismsNuggetCount = mechaninismsNuggetCount;
        this.programmingNuggetCount = programmingNuggetCount;
        this.structuresNuggetCount = structuresNuggetCount;
    }

    public String getLablevel() {
        return lablevel;
    }

    public void setLablevel(String lablevel) {
        this.lablevel = lablevel;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }

    public int getLabTime() {
        return labTime;
    }

    public void setLabTime(int labTime) {
        this.labTime = labTime;
    }

    public int getImagineeringNuggetCount() {
        return imagineeringNuggetCount;
    }

    public void setImagineeringNuggetCount(int imagineeringNuggetCount) {
        this.imagineeringNuggetCount = imagineeringNuggetCount;
    }

    public int getMechaninismsNuggetCount() {
        return mechaninismsNuggetCount;
    }

    public void setMechaninismsNuggetCount(int mechaninismsNuggetCount) {
        this.mechaninismsNuggetCount = mechaninismsNuggetCount;
    }

    public int getProgrammingNuggetCount() {
        return programmingNuggetCount;
    }

    public void setProgrammingNuggetCount(int programmingNuggetCount) {
        this.programmingNuggetCount = programmingNuggetCount;
    }

    public int getStructuresNuggetCount() {
        return structuresNuggetCount;
    }

    public void setStructuresNuggetCount(int structuresNuggetCount) {
        this.structuresNuggetCount = structuresNuggetCount;
    }
}

