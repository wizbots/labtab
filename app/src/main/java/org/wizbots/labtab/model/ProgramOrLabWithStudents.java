package org.wizbots.labtab.model;

import org.wizbots.labtab.model.student.Student;

import java.util.ArrayList;

public class ProgramOrLabWithStudents {
    private int sku;
    private String ends;
    private String title;
    private String starts;
    private String state;
    private String street;
    private int enrollment_count;
    private String address;
    private String id;
    private ArrayList<Student> students;

    public ProgramOrLabWithStudents() {
    }

    public ProgramOrLabWithStudents(int sku, String ends, String title, String starts, String state, String street, int enrollment_count, String address, String id, ArrayList<Student> students) {
        this.sku = sku;
        this.ends = ends;
        this.title = title;
        this.starts = starts;
        this.state = state;
        this.street = street;
        this.enrollment_count = enrollment_count;
        this.address = address;
        this.id = id;
        this.students = students;
    }

    public int getSku() {
        return sku;
    }

    public void setSku(int sku) {
        this.sku = sku;
    }

    public String getEnds() {
        return ends;
    }

    public void setEnds(String ends) {
        this.ends = ends;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStarts() {
        return starts;
    }

    public void setStarts(String starts) {
        this.starts = starts;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getEnrollment_count() {
        return enrollment_count;
    }

    public void setEnrollment_count(int enrollment_count) {
        this.enrollment_count = enrollment_count;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }
}
