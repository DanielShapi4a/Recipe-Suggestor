package com.example.recipes_suggester.service;

import com.example.recipes_suggester.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final UserRepository userRepository;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);

    public S3Service(S3Client s3Client, UserRepository userRepository) {
        this.s3Client = s3Client;
        this.userRepository = userRepository;
    }

    public String uploadFile(MultipartFile file, String username) throws IOException {
        String key = String.format("uploads/%s/%s", username, Objects.requireNonNull(file.getOriginalFilename()).replace("\\", "/"));
        String contentType = file.getContentType();

        logger.info("Uploading file to S3 with key: {}", key);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        String imageUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);

        return imageUrl;
    }
}
