package org.wizbots.labtab.model.student.response;


public class ProjectResponse {
    String projectStatus;
    String projectName;

    public ProjectResponse() {
    }

    public ProjectResponse(String projectStatus, String projectName) {
        this.projectStatus = projectStatus;
        this.projectName = projectName;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
