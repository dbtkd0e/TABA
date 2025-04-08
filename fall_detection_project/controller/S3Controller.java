package com.fall_detection_project.function_implement.controller;

import com.fall_detection_project.function_implement.domain.FallEventVideoMetadata;
import com.fall_detection_project.function_implement.service.FallEventVideoMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/videos")
public class S3Controller {

    @Autowired
    private FallEventVideoMetadataService fallEventVideoMetadataService;

    @GetMapping
    public List<FallEventVideoMetadata> getAllFallEvents() {
        return fallEventVideoMetadataService.getAllFallEvents();
    }

    @GetMapping("/{id}")
    public FallEventVideoMetadata getFallEventById(@PathVariable String id) {
        return fallEventVideoMetadataService.getFallEventById(id);
    }

    @PostMapping("/upload")
    public ResponseEntity<FallEventVideoMetadata> uploadVideo(@RequestParam("file") MultipartFile file,
                                                              @RequestParam("deviceId") Integer deviceId) throws IOException {
        FallEventVideoMetadata metadata = fallEventVideoMetadataService.uploadVideoToS3AndSaveMetadata(file, deviceId);
        return ResponseEntity.ok(metadata);
    }

    @PostMapping("/save-metadata")
    public ResponseEntity<FallEventVideoMetadata> saveMetadata(@RequestParam String videoUrl,
                                                               @RequestParam String thumbnailUrl,
                                                               @RequestParam String title,
                                                               @RequestParam String format) {
        FallEventVideoMetadata metadata = fallEventVideoMetadataService.saveMetadata(videoUrl, thumbnailUrl, title, format);
        return ResponseEntity.ok(metadata);
    }

    @GetMapping("/s3/save-metadata")
    public ResponseEntity<Void> saveMetadataFromS3() {
        fallEventVideoMetadataService.saveMetadataFromS3();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/download/{key}")
    public ResponseEntity<byte[]> downloadVideo(@PathVariable String key) throws IOException {
        byte[] videoData = fallEventVideoMetadataService.downloadVideoFromS3(key);
        return ResponseEntity.ok(videoData);
    }

    @GetMapping("/list")
    public List<String> listAllVideosInS3() {
        return fallEventVideoMetadataService.listAllVideosInS3();
    }
}
