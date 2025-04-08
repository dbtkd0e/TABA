package com.fall_detection_project.function_implement.service;

import com.fall_detection_project.function_implement.domain.AssignmentDeviceCaregiver;
import com.fall_detection_project.function_implement.domain.CaregiverForSenior;
import com.fall_detection_project.function_implement.domain.RecordingDevice;
import com.fall_detection_project.function_implement.domain.UserAccount;
import com.fall_detection_project.function_implement.repository.AssignmentDeviceCaregiverRepository;
import com.fall_detection_project.function_implement.repository.CaregiverRepository;
import com.fall_detection_project.function_implement.repository.UserRepository;
import com.fall_detection_project.function_implement.repository.RecordingDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CaregiverRepository caregiverRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RecordingDeviceRepository recordingDeviceRepository;
    private final AssignmentDeviceCaregiverRepository assignmentDeviceCaregiverRepository;

    @Autowired
    public UserService(UserRepository userRepository, CaregiverRepository caregiverRepository, BCryptPasswordEncoder passwordEncoder, RecordingDeviceRepository recordingDeviceRepository, AssignmentDeviceCaregiverRepository assignmentDeviceCaregiverRepository) {
        this.userRepository = userRepository;
        this.caregiverRepository = caregiverRepository;
        this.passwordEncoder = passwordEncoder;
        this.recordingDeviceRepository = recordingDeviceRepository;
        this.assignmentDeviceCaregiverRepository = assignmentDeviceCaregiverRepository;
    }

    @Transactional
    public UserAccount createUser(String username, String password, String caregiverName) throws Exception {
        if (userRepository.findByUsername(username) != null) {
            throw new Exception("이미 존재하는 사용자 이름입니다.");
        }

        // 보호자 정보 생성
        CaregiverForSenior caregiver = new CaregiverForSenior();
        caregiver.setCaregiverNo(UUID.randomUUID().toString());
        caregiver.setCaregiversName(caregiverName);

        caregiverRepository.save(caregiver);

        // 사용자 계정 생성
        UserAccount newUser = new UserAccount();
        newUser.setUsername(username);
        newUser.setPasswordHash(passwordEncoder.encode(password));  // 암호 인코딩
        newUser.setCaregiver(caregiver);

        // 사용자 계정 저장
        UserAccount savedUser = userRepository.save(newUser);

        //Mapping
        caregiver.setUserAccountNo(newUser.getUserAccountNo());
        caregiverRepository.save(caregiver);

        // 로그 추가
        System.out.println("사용자 생성 완료: " + savedUser.getUsername());

        return savedUser;
    }
    public UserAccount findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void deleteUser(String username) throws Exception {
        UserAccount user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Exception("사용자를 찾을 수 없습니다.");
        }
        CaregiverForSenior caregiver = user.getCaregiver();
        // user_accounts 테이블의 레코드 삭제
        userRepository.delete(user);

        /* 여기서 오류뜰 가능성 있음 */
        caregiverRepository.delete(caregiver);
        System.out.println("사용자 삭제 완료: " + username);
    }

    @Transactional
    public void addDeviceToUser(String username, Integer deviceId) throws Exception {
        UserAccount user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Exception("사용자를 찾을 수 없습니다.");
        }

        RecordingDevice device = recordingDeviceRepository.findById(deviceId).orElseThrow(() -> new Exception("디바이스를 찾을 수 없습니다."));
        // Create the mapping between caregiver and device
        assignmentDeviceCaregiverRepository.save(new AssignmentDeviceCaregiver(user.getCaregiver().getCaregiverNo(), device.getDeviceNo()));
        System.out.println("디바이스 추가 완료: " + deviceId + " to user: " + username);
    }

public String getCurrentCaregiverId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserAccount) {
            UserAccount user = (UserAccount) authentication.getPrincipal();
            System.out.println("User: " + user.getUsername()); // 사용자 이름 로그 출력
            return user.getCaregiver().getCaregiverId();
        }
        return null;
    }


}
