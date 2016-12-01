package org.wizbots.labtab.model;


public class CreateTokenResponse {
    private String date;
    private String token;
    private String id;
    private String member_id;

    public CreateTokenResponse() {
    }

    public CreateTokenResponse(String date, String token, String id, String member_id) {
        this.date = date;
        this.token = token;
        this.id = id;
        this.member_id = member_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
}
