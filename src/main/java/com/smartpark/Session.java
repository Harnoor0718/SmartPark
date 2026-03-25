package com.smartpark;

public class Session {
    private static Session instance;
    private String token;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) instance = new Session();
        return instance;
    }

    public void setToken(String token) { this.token = token; }
    public String getToken() { return token; }
}