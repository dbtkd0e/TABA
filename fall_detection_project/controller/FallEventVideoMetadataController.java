package com.fall_detection_project.function_implement.controller;

import com.fall_detection_project.function_implement.domain.AssignmentDeviceCaregiver;
import com.fall_detection_project.function_implement.domain.FallEventVideoMetadata;
import com.fall_detection_project.function_implement.domain.UserAccount;
import com.fall_detection_project.function_implement.dto.FallEventVideoDetailsDto;
import com.fall_detection_project.function_implement.dto.FallEventVideoMetadataDto;
import com.fall_detection_project.function_implement.service.AssignmentDeviceCaregiverService;
import com.fall_detection_project.function_implement.service.FallEventVideoMetadataService;
import com.fall_detection_project.function_implement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fall_event")
public class FallEventVideoMetadataController {
    private static final Logger logger = LoggerFactory.getLogger(FallEventVideoMetadataController.class);

    @Autowired
    private FallEventVideoMetadataService service;

    @Autowired
    private AssignmentDeviceCaregiverService assignmentDeviceCaregiverService;

    @Autowired
    private UserService userService;

    @PostMapping("/save_metadata")
    public ResponseEntity<FallEventVideoMetadata> saveMetadata(@RequestBody FallEventVideoMetadata metadata) {
        try {
            FallEventVideoMetadata savedMetadata = service.saveMetadata(
                    metadata.getVideoUrl(),
                    metadata.getThumbnailUrl(),
                    metadata.getTitle(),
                    metadata.getFormat()
            );
            return ResponseEntity.ok(savedMetadata);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/save_metadata_from_s3")
    public ResponseEntity<Void> saveMetadataFromS3() {
        try {
            service.saveMetadataFromS3();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



@GetMapping("/all")
public ResponseEntity<List<FallEventVideoMetadataDto>> getAllFallEvents() {
    try {
        String caregiverId = userService.getCurrentCaregiverId();
        System.out.println("care"+caregiverId);
        if (caregiverId != null) {List<Integer> deviceNos = assignmentDeviceCaregiverService.getDeviceNosByCaregiverId(caregiverId);
            List<FallEventVideoMetadata> events = service.getAllEventsByDeviceNos(deviceNos);
            System.out.println("list"+events);
            List<FallEventVideoMetadataDto> eventDtos = events.stream().map(event -> {
                FallEventVideoMetadataDto dto = new FallEventVideoMetadataDto();
                dto.setVideoId(event.getVideoId());
                dto.setUploadTime(event.getUploadTime());
                dto.setThumbnailUrl(event.getThumbnailUrl());
                dto.setTitle(event.getTitle());
                return dto;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(eventDtos);
        }
        else{
            List<FallEventVideoMetadataDto> events = service.getAllFallEvents().stream().map(event -> {
                FallEventVideoMetadataDto dto = new FallEventVideoMetadataDto();
                dto.setVideoId(event.getVideoId());
                dto.setUploadTime(event.getUploadTime());
                dto.setThumbnailUrl(event.getThumbnailUrl());
                dto.setTitle(event.getTitle());
                return dto;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(events);
        }
    } catch (Exception e) {
        logger.error("Error occurred while fetching fall events: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

    @GetMapping("/{id}")
    public ResponseEntity<FallEventVideoDetailsDto> getFallEventById(@PathVariable String id) {
        try {
            FallEventVideoMetadata event = service.getFallEventById(id);
            FallEventVideoDetailsDto dto = new FallEventVideoDetailsDto();
            dto.setUploadTime(event.getUploadTime());
            dto.setTitle(event.getTitle());
            dto.setVideoUrl(event.getVideoUrl());
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
