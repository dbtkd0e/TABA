package com.fall_detection_project.function_implement.service;

import com.fall_detection_project.function_implement.domain.RecordingDevice;
import com.fall_detection_project.function_implement.repository.RecordingDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.imgscalr.Scalr;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

@Service
public class S3Service {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private RecordingDeviceRepository recordingDeviceRepository;

    public String uploadFile(MultipartFile file, Integer deviceId) throws IOException {
        try {
            // RecordingDevice 정보 가져오기
            RecordingDevice device = recordingDeviceRepository.findById(deviceId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid device ID"));
            String deviceEmplacement = device.getDeviceEmplacement();

            // 현재 시간 포맷팅
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            // 파일 이름 설정
            String fileName = deviceEmplacement + "_" + timestamp + ".mp4";

            // S3에 업로드할 키
            String s3Key = deviceId + "_" + fileName;
            String s3Path = deviceId + "/" + fileName;

            // 파일을 임시 파일로 변환
            File tempFile = convertMultiPartToFile(file);

            // S3에 파일 업로드
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Path)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(tempFile));

            // 비디오에서 썸네일 생성
            BufferedImage thumbnail = createThumbnail(tempFile);
            if (thumbnail == null) {
                throw new IOException("Failed to extract thumbnail from video");
            }

            BufferedImage resizedThumbnail = Scalr.resize(thumbnail, 150);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(resizedThumbnail, "jpg", os);
            byte[] thumbnailBytes = os.toByteArray();

            // 섬네일 파일 이름 설정
            String thumbnailFileName = deviceEmplacement + "_" + timestamp + ".jpg";
            String thumbnailS3Path = deviceId + "/" + thumbnailFileName;

            // 섬네일을 S3에 업로드
            InputStream thumbnailInputStream = new ByteArrayInputStream(thumbnailBytes);
            PutObjectRequest thumbnailPutObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(thumbnailS3Path)
                    .build();
            s3Client.putObject(thumbnailPutObjectRequest, RequestBody.fromInputStream(thumbnailInputStream, thumbnailBytes.length));

            // 임시 파일 삭제
            tempFile.delete();

            return s3Key;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to upload file to S3", e);
        }
    }

    private BufferedImage createThumbnail(File videoFile) throws IOException {
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(videoFile);
        try {
            frameGrabber.start();
            Frame frame = frameGrabber.grabImage();
            if (frame == null) {
                throw new IOException("No frame could be grabbed.");
            }
            Java2DFrameConverter converter = new Java2DFrameConverter();
            return converter.convert(frame);
        } catch (Exception e) {
            throw new IOException("Failed to extract thumbnail from video", e);
        } finally {
            frameGrabber.stop();
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public byte[] downloadFile(String key) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        return s3Client.getObject(getObjectRequest).readAllBytes();
    }

    public List<String> listFiles() {
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();
        ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);
        return listObjectsV2Response.contents().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }
}
