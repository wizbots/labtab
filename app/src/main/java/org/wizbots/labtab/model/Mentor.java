package org.wizbots.labtab.model;

public class Mentor {
    private String id;
    private String member_id;
    private String token;
    private String date;
    private String first_name;
    private String last_name;
    private String email;
    private String username;
    private String gender;
    private String state;
    private String street;
    private String city;
    private String zip_code;
    private String phone_1;
    private String phone_2;

    public Mentor() {
    }

    public Mentor(String id, String member_id, String token, String date) {
        this.id = id;
        this.member_id = member_id;
        this.token = token;
        this.date = date;
    }

    public Mentor(String id, String member_id, String token, String date, String first_name, String last_name, String email, String username, String gender, String state, String street, String city, String zip_code, String phone_1, String phone_2) {
        this.id = id;
        this.member_id = member_id;
        this.token = token;
        this.date = date;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.username = username;
        this.gender = gender;
        this.state = state;
        this.street = street;
        this.city = city;
        this.zip_code = zip_code;
        this.phone_1 = phone_1;
        this.phone_2 = phone_2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet_address() {
        return street;
    }

    public void setStreet_address(String street_address) {
        this.street = street_address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zip_code;
    }

    public void setZipCode(String zipCode) {
        this.zip_code = zipCode;
    }

    public String getPhone1() {
        return phone_1;
    }

    public void setPhone1(String phone1) {
        this.phone_1 = phone1;
    }

    public String getPhone2() {
        return phone_2;
    }

    public void setPhone2(String phone2) {
        this.phone_2 = phone2;
    }

    public String getFullName() {
        return this.first_name + " " + last_name;
    }
}
