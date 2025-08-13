package org.example.shoppingproject.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private String tokenType = "Bearer";
    private String username;
    private Set<String> roles;

    public LoginResponse() {}

    public LoginResponse(String token, String username, Set<String> roles) {
        this.token = token;
        this.username = username;
        this.roles = roles;
    }
}
