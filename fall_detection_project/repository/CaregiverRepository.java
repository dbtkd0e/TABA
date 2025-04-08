package com.fall_detection_project.function_implement.repository;

import com.fall_detection_project.function_implement.domain.CaregiverForSenior;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaregiverRepository extends JpaRepository<CaregiverForSenior, String> {
}
