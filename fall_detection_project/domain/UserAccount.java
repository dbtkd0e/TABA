package com.fall_detection_project.function_implement.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "User_Accounts")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userAccountNo;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @ManyToOne
    @JoinColumn(name = "caregiver_id", referencedColumnName = "caregiver_id")
    private CaregiverForSenior caregiver;

    // Getters and setters

    public Long getUserAccountNo() {
        return userAccountNo;
    }

    public void setUserAccountNo(Long userAccountNo) {
        this.userAccountNo = userAccountNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public CaregiverForSenior getCaregiver() {
        return caregiver;
    }

    public void setCaregiver(CaregiverForSenior caregiver) {
        this.caregiver = caregiver;
    }

}
