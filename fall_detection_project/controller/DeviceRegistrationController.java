package com.fall_detection_project.function_implement.controller;

import com.fall_detection_project.function_implement.domain.AssignmentDeviceCaregiver;
import com.fall_detection_project.function_implement.service.DeviceRegistrationService;
import com.fall_detection_project.function_implement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/devices")
public class DeviceRegistrationController {

    @Autowired
    private DeviceRegistrationService deviceRegistrationService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(DeviceRegistrationController.class);

    @PostMapping("/register")
    public ResponseEntity<AssignmentDeviceCaregiver> registerDevice(@RequestBody Map<String, Object> request) {
        String caregiverId = (String) request.get("caregiverId");
        Integer deviceNo = (Integer) request.get("deviceNo");

        logger.info("Register Device Request: caregiverId={}, deviceNo={}", caregiverId, deviceNo);
        try {
            AssignmentDeviceCaregiver adc = deviceRegistrationService.registerDevice(caregiverId, deviceNo);
            return ResponseEntity.ok(adc);
        } catch (Exception e) {
            logger.error("Error registering device: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
