package org.wizbots.labtab.model;

public class StudentStatisticsDetailProjects {
    private String labStep;
    private String projectCategory;
    private String projectStatus;

    public StudentStatisticsDetailProjects() {
    }

    public StudentStatisticsDetailProjects(String labStep, String projectCategory, String projectStatus) {
        this.labStep = labStep;
        this.projectCategory = projectCategory;
        this.projectStatus = projectStatus;
    }

    public String getLabStep() {
        return labStep;
    }

    public void setLabStep(String labStep) {
        this.labStep = labStep;
    }

    public String getProjectCategory() {
        return projectCategory;
    }

    public void setProjectCategory(String projectCategory) {
        this.projectCategory = projectCategory;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }
}
