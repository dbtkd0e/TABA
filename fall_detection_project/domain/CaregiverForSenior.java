package com.fall_detection_project.function_implement.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Caregivers_For_Seniors")
public class CaregiverForSenior {

    @Id
    @Column(name = "caregiver_id")
    private String caregiverNo;

    @Column(name = "caregiver_name", nullable = false)
    private String caregiversName;

    @Column(name = "user_id")
    private Long userAccountNo;

    @ManyToMany
    @JoinTable(
            name = "assignment_device_caregiver",
            joinColumns = @JoinColumn(name = "caregiver_id"),
            inverseJoinColumns = @JoinColumn(name = "device_no")
    )
    private List<RecordingDevice> recordingDevices;


    public List<RecordingDevice> getRecordingDevices() {
        return recordingDevices;
    }

    public void setRecordingDevices(List<RecordingDevice> recordingDevices) {
        this.recordingDevices = recordingDevices;
    }

    public Long getUserAccountNo() {
        return userAccountNo;
    }

    public void setUserAccountNo(Long userAccountNo) {
        this.userAccountNo = userAccountNo;
    }

    // Getters and setters
    public String getCaregiverNo() {
        return caregiverNo;
    }



    public void setCaregiverNo(String caregiverNo) {
        this.caregiverNo = caregiverNo;
    }

    public String getCaregiversName() {
        return caregiversName;
    }

    public void setCaregiversName(String caregiversName) {
        this.caregiversName = caregiversName;
    }

    public String getCaregiverId() {
        return caregiverNo;
    }


}
