package org.wizbots.labtab.model.markabsent;

import java.util.ArrayList;

public class MarkStudentAbsentResponse {
    private ArrayList<Student> students;
    private boolean success;

    public MarkStudentAbsentResponse() {
    }

    public MarkStudentAbsentResponse(ArrayList<Student> students, boolean success) {
        this.students = students;
        this.success = success;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
