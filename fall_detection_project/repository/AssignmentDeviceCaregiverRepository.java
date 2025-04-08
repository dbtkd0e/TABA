package com.fall_detection_project.function_implement.repository;

import com.fall_detection_project.function_implement.domain.AssignmentDeviceCaregiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssignmentDeviceCaregiverRepository extends JpaRepository<AssignmentDeviceCaregiver, Long> {

    @Query("SELECT adc.deviceNo FROM AssignmentDeviceCaregiver adc WHERE adc.caregiverId = :caregiverId")
    List<Integer> findDeviceNosByCaregiverId(@Param("caregiverId") String caregiverId);

}
