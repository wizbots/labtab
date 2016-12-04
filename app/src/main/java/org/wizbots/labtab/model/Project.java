package org.wizbots.labtab.model;

public class Project {
    private String projectName;
    private String projectStatus;

    public Project() {
    }

    public Project(String projectName, String projectStatus) {
        this.projectName = projectName;
        this.projectStatus = projectStatus;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }
}
