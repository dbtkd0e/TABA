package com.fall_detection_project.function_implement.dto;

import com.fall_detection_project.function_implement.domain.CaregiverForSenior;

public class CreateUserRequest {
    private String username;
    private String password;
    private CaregiverForSenior caregiver;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CaregiverForSenior getCaregiver() {
        return caregiver;
    }

    public void setCaregiver(CaregiverForSenior caregiver) {
        this.caregiver = caregiver;
    }
}
