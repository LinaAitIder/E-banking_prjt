package org.ebanking.dto.response;

import org.ebanking.model.User;

public class SmsVerifLoginResponse {
    private User user;
    private String role;
    private String jwtToken;

    public SmsVerifLoginResponse(User user, String role, String jwtToken) {
        this.user = user;
        this.role = role;
        this.jwtToken = jwtToken;
    }

    public User getUser() {
        return user;
    }

    public String getRole() {
        return role;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
