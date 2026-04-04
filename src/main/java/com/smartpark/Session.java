package com.smartpark;

public class Session {
    private static Session instance;
    private String token;
    private String username;
    private int userId;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) instance = new Session();
        return instance;
    }

    public void setToken(String token) { this.token = token; }
    public String getToken() { return token; }

    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }

    public void setUserId(int userId) { this.userId = userId; }
    public int getUserId() { return userId; }
}