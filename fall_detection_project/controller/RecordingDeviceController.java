package com.fall_detection_project.function_implement.controller;

import com.fall_detection_project.function_implement.domain.RecordingDevice;
import com.fall_detection_project.function_implement.service.RecordingDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
public class RecordingDeviceController {

    @Autowired
    private RecordingDeviceService recordingDeviceService;

    @PostMapping("/add")
    public ResponseEntity<RecordingDevice> addDevice(@RequestBody RecordingDevice device) {
        if (device.getDeviceId() == null || device.getDeviceId().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        RecordingDevice savedDevice = recordingDeviceService.addDevice(device);
        return ResponseEntity.ok(savedDevice);
    }
}
