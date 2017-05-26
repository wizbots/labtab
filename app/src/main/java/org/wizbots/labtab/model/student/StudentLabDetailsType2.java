package org.wizbots.labtab.model.student;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.interfaces.StudentTypeInterface;

public class StudentLabDetailsType2 implements StudentTypeInterface {
    private String studentName;
    private String labLevel;
    private String noOfProjects;
    private String noOfLabTime;
    private String noOfDone;
    private String noOfSkipped;
    private String noOfPending;

    public StudentLabDetailsType2() {
    }

    public StudentLabDetailsType2(String studentName, String labLevel, String noOfProjects, String noOfLabTime, String noOfDone, String noOfSkipped, String noOfPending) {
        this.studentName = studentName;
        this.labLevel = labLevel;
        this.noOfProjects = noOfProjects;
        this.noOfLabTime = noOfLabTime;
        this.noOfDone = noOfDone;
        this.noOfSkipped = noOfSkipped;
        this.noOfPending = noOfPending;
        setCount();
    }

    private void setCount() {
      /* LabTabApplication.getInstance().getCompletedProjects() += Integer.parseInt(noOfDone);
        countSkippedProjects += Integer.parseInt(noOfSkipped);
        countPendingProjects += Integer.parseInt(noOfPending);
        totalProjects += Integer.parseInt(noOfProjects);
        labTime += Integer.parseInt(noOfLabTime);*/
        LabTabApplication.getInstance().setCount(Integer.parseInt(noOfDone), Integer.parseInt(noOfPending), Integer.parseInt(noOfSkipped), Integer.parseInt(noOfProjects), Integer.parseInt(noOfLabTime));

    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
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
}

