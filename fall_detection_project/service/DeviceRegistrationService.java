package com.fall_detection_project.function_implement.service;

import com.fall_detection_project.function_implement.domain.AssignmentDeviceCaregiver;
import com.fall_detection_project.function_implement.domain.RecordingDevice;
import com.fall_detection_project.function_implement.repository.AssignmentDeviceCaregiverRepository;
import com.fall_detection_project.function_implement.repository.CaregiverRepository;
import com.fall_detection_project.function_implement.repository.RecordingDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DeviceRegistrationService {

    @Autowired
    private AssignmentDeviceCaregiverRepository assignmentRepository;

    private static final Logger logger = Logger.getLogger(DeviceRegistrationService.class.getName());

    @Transactional
    public AssignmentDeviceCaregiver registerDevice(String caregiverId, Integer deviceNo) {
        try {
            logger.info("Registering device: caregiverId=" + caregiverId + ", deviceNo=" + deviceNo);
            AssignmentDeviceCaregiver assignment = new AssignmentDeviceCaregiver(caregiverId, deviceNo);
            return assignmentRepository.save(assignment);
        } catch (Exception e) {
            logger.severe("Transaction failed and rolled back: " + e.getMessage());
            throw e;
        }
    }
}

