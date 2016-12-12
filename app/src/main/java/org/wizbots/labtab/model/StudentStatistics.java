package org.wizbots.labtab.model;

public class StudentStatistics {
    private String labLevel;
    private String noOfProjects;
    private String noOfLabTime;
    private String noOfDone;
    private String noOfSkipped;
    private String noOfPending;
    private String noOfImagineering;
    private String noOfProgramming;
    private String noOfMechanisms;
    private String noOfStructures;

    public StudentStatistics() {
    }

    public StudentStatistics(String labLevel, String noOfProjects, String noOfLabTime, String noOfDone, String noOfSkipped, String noOfPending, String noOfImagineering, String noOfProgramming, String noOfMechanisms, String noOfStructures) {
        this.labLevel = labLevel;
        this.noOfProjects = noOfProjects;
        this.noOfLabTime = noOfLabTime;
        this.noOfDone = noOfDone;
        this.noOfSkipped = noOfSkipped;
        this.noOfPending = noOfPending;
        this.noOfImagineering = noOfImagineering;
        this.noOfProgramming = noOfProgramming;
        this.noOfMechanisms = noOfMechanisms;
        this.noOfStructures = noOfStructures;
    }

    public String getLabLevel() {
        return labLevel;
    }

    public void setLabLevel(String labLevel) {
        this.labLevel = labLevel;
    }

    public String getNoOfProjects() {
        return noOfProjects;
    }

    public void setNoOfProjects(String noOfProjects) {
        this.noOfProjects = noOfProjects;
    }

    public String getNoOfLabTime() {
        return noOfLabTime;
    }

    public void setNoOfLabTime(String noOfLabTime) {
        this.noOfLabTime = noOfLabTime;
    }

    public String getNoOfDone() {
        return noOfDone;
    }

    public void setNoOfDone(String noOfDone) {
        this.noOfDone = noOfDone;
    }

    public String getNoOfSkipped() {
        return noOfSkipped;
    }

    public void setNoOfSkipped(String noOfSkipped) {
        this.noOfSkipped = noOfSkipped;
    }

    public String getNoOfPending() {
        return noOfPending;
    }

    public void setNoOfPending(String noOfPending) {
        this.noOfPending = noOfPending;
    }

    public String getNoOfImagineering() {
        return noOfImagineering;
    }

    public void setNoOfImagineering(String noOfImagineering) {
        this.noOfImagineering = noOfImagineering;
    }

    public String getNoOfProgramming() {
        return noOfProgramming;
    }

    public void setNoOfProgramming(String noOfProgramming) {
        this.noOfProgramming = noOfProgramming;
    }

    public String getNoOfMechanisms() {
        return noOfMechanisms;
    }

    public void setNoOfMechanisms(String noOfMechanisms) {
        this.noOfMechanisms = noOfMechanisms;
    }

    public String getNoOfStructures() {
        return noOfStructures;
    }

    public void setNoOfStructures(String noOfStructures) {
        this.noOfStructures = noOfStructures;
    }
}
