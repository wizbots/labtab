package org.wizbots.labtab.model.wizchips;


import org.wizbots.labtab.model.student.response.Students;

import java.util.ArrayList;

public class WizchipsAddResponse {

    private ArrayList<Students> students;

    private String success;

    public  ArrayList<Students> getStudents ()
    {
        return students;
    }

    public void setStudents ( ArrayList<Students> students)
    {
        this.students = students;
    }

    public String getSuccess ()
    {
        return success;
    }

    public void setSuccess (String success)
    {
        this.success = success;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [students = "+students+", success = "+success+"]";
    }
/*
    int wizchips;
    boolean success;
    String student_id;

    public int getWizchips() {
        return wizchips;
    }

    public void setWizchips(int wizchips) {
        this.wizchips = wizchips;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }*/


}
