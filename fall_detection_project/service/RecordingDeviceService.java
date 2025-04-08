package com.fall_detection_project.function_implement.service;

import com.fall_detection_project.function_implement.domain.RecordingDevice;
import com.fall_detection_project.function_implement.repository.CaregiverRepository;
import com.fall_detection_project.function_implement.repository.RecordingDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordingDeviceService {

    @Autowired
    private RecordingDeviceRepository recordingDeviceRepository;

    @Autowired
    private CaregiverRepository caregiverRepository;
    public RecordingDevice addDevice(RecordingDevice device) {
        return recordingDeviceRepository.save(device);
    }
}
