package org.wizbots.labtab.model.promotedemote;

public class PromotionDemotionResponse {
    private String students[];
    private boolean success;

    public PromotionDemotionResponse() {
    }

    public PromotionDemotionResponse(String[] students, boolean success) {
        this.students = students;
        this.success = success;
    }

    public String[] getStudents() {
        return students;
    }

    public void setStudents(String[] students) {
        this.students = students;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
