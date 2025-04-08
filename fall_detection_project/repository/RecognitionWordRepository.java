package com.fall_detection_project.function_implement.repository;


import com.fall_detection_project.function_implement.domain.RecognitionWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecognitionWordRepository extends JpaRepository<RecognitionWord, Long> {
}