package com.fall_detection_project.function_implement.service;

import com.fall_detection_project.function_implement.domain.AssignmentDeviceCaregiver;
import com.fall_detection_project.function_implement.repository.AssignmentDeviceCaregiverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AssignmentDeviceCaregiverService {

    @Autowired
    private AssignmentDeviceCaregiverRepository assignmentDeviceCaregiverRepository;

    public List<Integer> getDeviceNosByCaregiverId(String caregiverId) {
        return assignmentDeviceCaregiverRepository.findDeviceNosByCaregiverId(caregiverId);
    }
}
