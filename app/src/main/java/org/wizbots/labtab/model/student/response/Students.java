package org.wizbots.labtab.model.student.response;

/**
 * Created by ashish on 24/5/17.
 */

public class Students
{
    private String id;

    private String wizchips;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getWizchips ()
    {
        return wizchips;
    }

    public void setWizchips (String wizchips)
    {
        this.wizchips = wizchips;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", wizchips = "+wizchips+"]";
    }
}
