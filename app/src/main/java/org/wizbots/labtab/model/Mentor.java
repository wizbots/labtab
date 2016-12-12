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
    private String street_address;
    private String city;
    private String zipCode;
    private String phone1;
    private String phone2;

    public Mentor() {
    }

    public Mentor(String id, String member_id, String token, String date) {
        this.id = id;
        this.member_id = member_id;
        this.token = token;
        this.date = date;
    }

    public Mentor(String id, String member_id, String token, String date, String first_name, String last_name, String email, String username, String gender, String state, String street_address, String city, String zipCode, String phone1, String phone2) {
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
        this.street_address = street_address;
        this.city = city;
        this.zipCode = zipCode;
        this.phone1 = phone1;
        this.phone2 = phone2;
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
        return street_address;
    }

    public void setStreet_address(String street_address) {
        this.street_address = street_address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }
}
