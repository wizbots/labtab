package org.wizbots.labtab.model.program.response;


public class AbsenceResponse {
    private String mentor_name;
    private String student_id;
    private String mentor_id;
    private String date;

    public AbsenceResponse() {
    }

    public AbsenceResponse(String mentor_name, String student_id, String mentor_id, String date) {
        this.mentor_name = mentor_name;
        this.student_id = student_id;
        this.mentor_id = mentor_id;
        this.date = date;
    }

    public String getMentor_name() {
        return mentor_name;
    }

    public void setMentor_name(String mentor_name) {
        this.mentor_name = mentor_name;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getMentor_id() {
        return mentor_id;
    }

    public void setMentor_id(String mentor_id) {
        this.mentor_id = mentor_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
