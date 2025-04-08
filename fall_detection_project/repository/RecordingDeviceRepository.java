package com.fall_detection_project.function_implement.repository;

import com.fall_detection_project.function_implement.domain.RecordingDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecordingDeviceRepository extends JpaRepository<RecordingDevice, Integer> {
    @Override
    Optional<RecordingDevice> findById(Integer integer);
}
