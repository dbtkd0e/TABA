package com.fall_detection_project.function_implement.repository;

import com.fall_detection_project.function_implement.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, Long> {
    UserAccount findByUsername(String username);
}
