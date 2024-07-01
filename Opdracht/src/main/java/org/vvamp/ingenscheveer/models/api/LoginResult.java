package org.vvamp.ingenscheveer.models.api;

public class LoginResult {
    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private String token;
    private String username;
    private String role;

    public LoginResult(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }
}
