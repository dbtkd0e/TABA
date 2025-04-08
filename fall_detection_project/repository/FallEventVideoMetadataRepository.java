package com.fall_detection_project.function_implement.repository;

import com.fall_detection_project.function_implement.domain.FallEventVideoMetadata;
import com.fall_detection_project.function_implement.domain.RecordingDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FallEventVideoMetadataRepository extends JpaRepository<FallEventVideoMetadata, Integer> {
    FallEventVideoMetadata findByVideoId(String videoId);

    @Query("SELECT f FROM FallEventVideoMetadata f WHERE f.recordingDevice.deviceNo IN :deviceNos")
    List<FallEventVideoMetadata> findByDeviceNoIn(List<Integer> deviceNos);
    List<FallEventVideoMetadata> findByRecordingDevice_DeviceNoIn(List<Integer> deviceNos);
    List<FallEventVideoMetadata> findByRecordingDeviceIn(List<RecordingDevice> recordingDevices);
}
