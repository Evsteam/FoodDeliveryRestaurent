package com.evs.foodexp.commonPkg.DTO;

public class ForgotPasswordRequest {

    private String action;
    private String email;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
