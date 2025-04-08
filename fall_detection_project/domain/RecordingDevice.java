package com.fall_detection_project.function_implement.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "recording_devices")
public class RecordingDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer deviceNo;

    @Column(nullable = false)
    private String deviceId;

    private String deviceEmplacement;

    @ManyToMany(mappedBy = "recordingDevices")
    private List<CaregiverForSenior> caregivers;

    public Integer getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(Integer deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceEmplacement() {
        return deviceEmplacement;
    }

    public void setDeviceEmplacement(String deviceEmplacement) {
        this.deviceEmplacement = deviceEmplacement;
    }

    public List<CaregiverForSenior> getCaregivers() {
        return caregivers;
    }

    public void setCaregivers(List<CaregiverForSenior> caregivers) {
        this.caregivers = caregivers;
    }
// Getters and Setters...
}
