package org.wizbots.labtab.model.student.response;

public class StudentResponse {
    private String enrollments_count;
    private String last_name;
    private String date_of_birth;
    private CreatorResponse creator;
    private String grade;
    private String after_care_after;
    private String id;
    private String first_name;
    private String level;
    private String absence_count;
    private String allergies;
    private String special_needs;
    private String after_care_before;
    private String after_care_phone;
    private String after_care_name;
    private ProjectHistoryResponse projects_history;

    public StudentResponse() {
    }

    public StudentResponse(String enrollments_count, String last_name, String date_of_birth, CreatorResponse creator, String grade, String after_care_after, String id, String first_name, String level, String absence_count, String allergies, String special_needs, String after_care_before, String after_care_phone, String after_care_name, ProjectHistoryResponse projects_history) {
        this.enrollments_count = enrollments_count;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
        this.creator = creator;
        this.grade = grade;
        this.after_care_after = after_care_after;
        this.id = id;
        this.first_name = first_name;
        this.level = level;
        this.absence_count = absence_count;
        this.allergies = allergies;
        this.special_needs = special_needs;
        this.after_care_before = after_care_before;
        this.after_care_phone = after_care_phone;
        this.after_care_name = after_care_name;
        this.projects_history = projects_history;
    }

    public String getEnrollments_count() {
        return enrollments_count;
    }

    public void setEnrollments_count(String enrollments_count) {
        this.enrollments_count = enrollments_count;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public CreatorResponse getCreator() {
        return creator;
    }

    public void setCreator(CreatorResponse creator) {
        this.creator = creator;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAfter_care_after() {
        return after_care_after;
    }

    public void setAfter_care_after(String after_care_after) {
        this.after_care_after = after_care_after;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLevel() {
        return level == null ? null : level.toUpperCase();
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAbsence_count() {
        return absence_count;
    }

    public void setAbsence_count(String absence_count) {
        this.absence_count = absence_count;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getSpecial_needs() {
        return special_needs;
    }

    public void setSpecial_needs(String special_needs) {
        this.special_needs = special_needs;
    }

    public String getAfter_care_before() {
        return after_care_before;
    }

    public void setAfter_care_before(String after_care_before) {
        this.after_care_before = after_care_before;
    }

    public String getAfter_care_phone() {
        return after_care_phone;
    }

    public void setAfter_care_phone(String after_care_phone) {
        this.after_care_phone = after_care_phone;
    }

    public String getAfter_care_name() {
        return after_care_name;
    }

    public void setAfter_care_name(String after_care_name) {
        this.after_care_name = after_care_name;
    }

    public ProjectHistoryResponse getProject_history() {
        return projects_history;
    }

    public void setProject_history(ProjectHistoryResponse project_history) {
        this.projects_history = project_history;
    }
}
