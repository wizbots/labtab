package org.wizbots.labtab.model.promotedemote;

import java.util.ArrayList;

public class PromotionDemotionResponse {
    private ArrayList<Student> students;
    private boolean success;

    public PromotionDemotionResponse() {
    }

    public PromotionDemotionResponse(ArrayList<Student> students, boolean success) {
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
