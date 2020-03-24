package org.wizbots.labtab.model.roster;

public class RosterFirstListItem {
    private String studentName;
    private String gender;
    private String age;
    private String grade;
    private String contactName;
    private String phone;
    private String email;
    private String address;
    private String allergies;
    private Boolean isExpanded;

    public Boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(Boolean expanded) {
        isExpanded = expanded;
    }

    public RosterFirstListItem(String studentName, String gender, String age, String grade, String contactName, String phone, String email, String address, String allergies, Boolean isExpanded) {
        this.studentName = studentName;
        this.gender = gender;
        this.age = age;
        this.grade = grade;
        this.contactName = contactName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.allergies = allergies;
        this.isExpanded = isExpanded;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }
}
