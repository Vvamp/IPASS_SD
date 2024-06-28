package org.vvamp.ingenscheveer.models.api;

public class LoginResult {
    public String token;
    public String username;
    public String role;

    public LoginResult(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }
}
