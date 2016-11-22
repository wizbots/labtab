package org.wizbots.labtab.retrofit;


public class CZResponse<T> {

    private int responseCode;
    private T response;


    public CZResponse(int responseCode, T response) {
        super();
        this.responseCode = responseCode;
        this.response = response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T responseString) {
        this.response = responseString;
    }

}
