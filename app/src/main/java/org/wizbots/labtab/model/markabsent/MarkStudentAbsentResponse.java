package org.wizbots.labtab.model.markabsent;

public class MarkStudentAbsentResponse {
    private String students[];
    private boolean success;

    public MarkStudentAbsentResponse() {
    }

    public MarkStudentAbsentResponse(String[] students, boolean success) {
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
