package com.fall_detection_project.function_implement.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "assignment_device_caregiver")
public class AssignmentDeviceCaregiver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "caregiver_id")
    private String caregiverId;

    @Column(name = "device_no")
    private Integer deviceNo;

    public AssignmentDeviceCaregiver() {}

    public AssignmentDeviceCaregiver(String caregiverId, Integer deviceNo) {
        this.caregiverId = caregiverId;
        this.deviceNo = deviceNo;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(String caregiverId) {
        this.caregiverId = caregiverId;
    }

    public Integer getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(Integer deviceNo) {
        this.deviceNo = deviceNo;
    }
}
