package org.wizbots.labtab.model.program;


public class Absence {
    private String student_name;
    private String mark_absent_synced;
    private String send_absent_notification;
    private String mentor_name;
    private String program_id;
    private String student_id;
    private String mentor_id;
    private String date;
    private boolean check;

    public Absence() {
    }

    public Absence(String student_name, String mark_absent_synced, String send_absent_notification, String mentor_name, String program_id, String student_id, String mentor_id, String date) {
        this.student_name = student_name;
        this.mark_absent_synced = mark_absent_synced;
        this.send_absent_notification = send_absent_notification;
        this.mentor_name = mentor_name;
        this.program_id = program_id;
        this.student_id = student_id;
        this.mentor_id = mentor_id;
        this.date = date;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getMark_absent_synced() {
        return mark_absent_synced;
    }

    public void setMark_absent_synced(String mark_absent_synced) {
        this.mark_absent_synced = mark_absent_synced;
    }

    public String getSend_absent_notification() {
        return send_absent_notification;
    }

    public void setSend_absent_notification(String send_absent_notification) {
        this.send_absent_notification = send_absent_notification;
    }

    public String getMentor_name() {
        return mentor_name;
    }

    public void setMentor_name(String mentor_name) {
        this.mentor_name = mentor_name;
    }

    public String getProgram_id() {
        return program_id;
    }

    public void setProgram_id(String program_id) {
        this.program_id = program_id;
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

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
