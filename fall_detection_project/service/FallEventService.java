package com.fall_detection_project.function_implement.service;

import com.fall_detection_project.function_implement.domain.FallEventVideoMetadata;
import com.fall_detection_project.function_implement.domain.UserAccount;
import com.fall_detection_project.function_implement.repository.AssignmentDeviceCaregiverRepository;
import com.fall_detection_project.function_implement.repository.FallEventVideoMetadataRepository;
import com.fall_detection_project.function_implement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FallEventService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssignmentDeviceCaregiverRepository assignmentRepository;

    @Autowired
    private FallEventVideoMetadataRepository fallEventRepository;

    public String getCurrentCaregiverId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount user = (UserAccount) authentication.getPrincipal();
        if (user == null || user.getCaregiver() == null) {
            throw new IllegalStateException("Authenticated user or caregiver information is missing");
        }
        return user.getCaregiver().getCaregiverNo();
    }

    public List<FallEventVideoMetadata> getAllFallEventsByCaregiver(String caregiverId) {
        if (caregiverId == null) {
            throw new IllegalArgumentException("Caregiver ID cannot be null");
        }
        List<Integer> deviceNos = assignmentRepository.findDeviceNosByCaregiverId(caregiverId);
        if (deviceNos.isEmpty()) {
            throw new IllegalArgumentException("No devices found for caregiver ID: " + caregiverId);
        }
        return fallEventRepository.findByDeviceNoIn(deviceNos);
    }
}
