package com.fall_detection_project.function_implement.service;

import com.fall_detection_project.function_implement.domain.FallEventVideoMetadata;
import com.fall_detection_project.function_implement.domain.RecordingDevice;
import com.fall_detection_project.function_implement.repository.FallEventVideoMetadataRepository;
import com.fall_detection_project.function_implement.repository.AssignmentDeviceCaregiverRepository;
import com.fall_detection_project.function_implement.repository.RecordingDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FallEventVideoMetadataService {

    @Autowired
    private AssignmentDeviceCaregiverRepository assignmentRepository;

    @Autowired
    private FallEventVideoMetadataRepository repository;


    @Autowired
    private RecordingDeviceRepository recordingDeviceRepository;

    @Autowired
    private S3Service s3Service;

    public List<FallEventVideoMetadata> getAllFallEvents() {
        return repository.findAll();
    }

    public FallEventVideoMetadata getFallEventById(String videoId) {
        return repository.findByVideoId(videoId);
    }

    public FallEventVideoMetadata uploadVideoToS3AndSaveMetadata(MultipartFile file, Integer deviceId) throws IOException {
        try {
            // S3에 비디오 업로드
            String key = s3Service.uploadFile(file, deviceId); //key = deviceId_filename
            //위에서 업르드는 완료. url은 디바이스ID/영상이름
            //키에 / 이 포함되면 다른 api로 인식되어 안돼

            // 메타데이터 생성
            FallEventVideoMetadata metadata = new FallEventVideoMetadata();
//            key = key.replace("/","_");
            metadata.setVideoId(key); // S3의 파일 키를 videoId로 사용
//            key = key.replace("_","/");
            String[] keyArr = key.split("_");
            metadata.setVideoUrl("https://fall-detection-bucket.s3.ap-northeast-2.amazonaws.com/" + keyArr[0]+"/"+keyArr[1]+"_"+keyArr[2]);
            key = key.replace(".mp4",".jpg");
            keyArr = key.split("_");
            metadata.setThumbnailUrl("https://fall-detection-bucket.s3.ap-northeast-2.amazonaws.com/" + keyArr[0]+"/"+keyArr[1]+"_"+keyArr[2]); // 임의의 썸네일 URL 설정
            metadata.setUploadTime(LocalDateTime.now());
            metadata.setTitle(file.getOriginalFilename());
            metadata.setFormat(file.getContentType());

            // deviceId로 RecordingDevice 조회
            RecordingDevice recordingDevice = recordingDeviceRepository.findById(deviceId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid recording device ID: " + deviceId));
            metadata.setRecordingDevice(recordingDevice);

            // 데이터베이스에 메타데이터 저장
            return repository.save(metadata);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to save metadata", e);
        }
    }

    public FallEventVideoMetadata saveMetadata(String videoUrl, String thumbnailUrl, String title, String format) {
        FallEventVideoMetadata metadata = new FallEventVideoMetadata();
        metadata.setVideoId(videoUrl.substring(videoUrl.lastIndexOf('/') + 1)); // URL에서 파일명을 videoId로 사용
        metadata.setVideoUrl(videoUrl);
        metadata.setThumbnailUrl(thumbnailUrl);
        metadata.setUploadTime(LocalDateTime.now());
        metadata.setTitle(title);
        metadata.setFormat(format);

        return repository.save(metadata);
    }

    public void saveMetadataFromS3() {
        List<String> keys = s3Service.listFiles();
        for (String key : keys) {
            FallEventVideoMetadata metadata = new FallEventVideoMetadata();
            metadata.setVideoId(key);
            metadata.setVideoUrl("https://fall-detection-bucket.s3.ap-northeast-2.amazonaws.com/" + key);
            metadata.setThumbnailUrl("https://fall-detection-bucket.s3.ap-northeast-2.amazonaws.com/thumbnail/" + key); // 임의의 썸네일 URL 설정
            metadata.setUploadTime(LocalDateTime.now());
            metadata.setTitle(key);
            metadata.setFormat("video/mp4"); // 실제 형식을 얻을 수 있으면 수정 필요

            repository.save(metadata);
        }
    }

    public byte[] downloadVideoFromS3(String key) throws IOException {
        return s3Service.downloadFile(key);
    }

    public List<String> listAllVideosInS3() {
        return s3Service.listFiles();
    }
    public List<FallEventVideoMetadata> getAllFallEventsByCaregiver(String caregiverId) {
        // Fetch the device numbers associated with the caregiver
        List<Integer> deviceNos = assignmentRepository.findDeviceNosByCaregiverId(caregiverId);

        // Fetch the fall event videos for the devices
        return repository.findByDeviceNoIn(deviceNos);
    }
    @Autowired
    private FallEventVideoMetadataRepository fallEventVideoMetadataRepository;

    public List<FallEventVideoMetadata> getAllEventsByDeviceNos(List<Integer> deviceNos) {
        return fallEventVideoMetadataRepository.findByRecordingDevice_DeviceNoIn(deviceNos);
    }

}
