package org.wizbots.labtab.model.wizchips;


import org.wizbots.labtab.model.student.response.Students;

import java.util.ArrayList;

public class WizchipsWithdrawResponse {

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
}
