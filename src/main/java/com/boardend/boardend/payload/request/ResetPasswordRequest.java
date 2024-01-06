package com.boardend.boardend.payload.request;

public class ResetPasswordRequest {
    private String resetToken;
    private String newPassword;

    // Add getters and setters

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
