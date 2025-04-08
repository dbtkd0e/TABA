package com.fall_detection_project.function_implement.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fall_event_videos_metadata")
public class FallEventVideoMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dataId;
    private String videoId;
    private String videoUrl;
    private String thumbnailUrl;
    private LocalDateTime uploadTime;
    private String title;
    private String format;

    @ManyToOne
    @JoinColumn(name = "device_no", nullable = true)
    private RecordingDevice recordingDevice;

    // Getters and Setters...

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public RecordingDevice getRecordingDevice() {
        return recordingDevice;
    }

    public void setRecordingDevice(RecordingDevice recordingDevice) {
        this.recordingDevice = recordingDevice;
    }
}
