package org.wizbots.labtab.model.student.response;

public class CreatorResponse {
    private String phone_2;
    private String email;
    private String phone_1;

    public CreatorResponse() {
    }

    public CreatorResponse(String phone_2, String email, String phone_1) {
        this.phone_2 = phone_2;
        this.email = email;
        this.phone_1 = phone_1;
    }

    public String getPhone_2() {
        return phone_2;
    }

    public void setPhone_2(String phone_2) {
        this.phone_2 = phone_2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_1() {
        return phone_1;
    }

    public void setPhone_1(String phone_1) {
        this.phone_1 = phone_1;
    }

    public String parentsPhone() {
        String parentsPhone = "";
        if (getPhone_1().equals("") && getPhone_2().equals("")) {
            parentsPhone = "";
        } else if (!getPhone_1().equals("") && getPhone_2().equals("")) {
            parentsPhone = getPhone_1();
        } else if (getPhone_1().equals("") && !getPhone_2().equals("")) {
            parentsPhone = getPhone_2();
        } else {
            parentsPhone = getPhone_1() + "\n" + getPhone_2();
        }
        return parentsPhone;
    }

}
