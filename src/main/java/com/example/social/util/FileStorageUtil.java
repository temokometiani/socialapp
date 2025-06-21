// src/main/java/com/example/social/util/FileStorageUtil.java
package com.example.social.util;

import com.example.social.exception.FileStorageException;
import com.example.social.model.entity.MinioInfo;
import io.minio.*;
import io.minio.http.Method;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value; // Make sure this import is present
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class FileStorageUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageUtil.class);

    private final MinioClient minioClient;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("forsocialapp")
    private String bucketName;

    @Value("${minio.public-url-expiration-minutes:1440}")
    private int publicUrlExpirationMinutes;

    public MinioInfo uploadFile(MultipartFile file, String folder) {
        if (file.isEmpty()) {
            throw new FileStorageException("Cannot upload empty file.");
        }

        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                LOGGER.info("MinIO bucket '{}' created.", bucketName);
            }

            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String objectKey = folder + "/" + UUID.randomUUID().toString() + fileExtension;

            InputStream inputStream = file.getInputStream();
            long fileSize = file.getSize();
            String contentType = file.getContentType();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectKey)
                            .stream(inputStream, fileSize, -1)
                            .contentType(contentType)
                            .build());

            LOGGER.info("File '{}' uploaded successfully to bucket '{}' as object '{}'.", originalFilename, bucketName, objectKey);

            MinioInfo minioInfo = new MinioInfo();
            minioInfo.setFilePath(endpoint + "/" + bucketName + "/" + objectKey); // 'endpoint' is now accessible
            minioInfo.setObjectKey(objectKey);
            minioInfo.setBucketName(bucketName);
            minioInfo.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));

            return minioInfo;

        } catch (MinioException e) {
            LOGGER.error("MinIO error during file upload: {}", e.getMessage());
            throw new FileStorageException("Error uploading file to MinIO: " + e.getMessage(), e);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            LOGGER.error("General error during file upload: {}", e.getMessage());
            throw new FileStorageException("Could not upload the file: " + e.getMessage(), e);
        }
    }

    public InputStream downloadFile(String objectKey) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectKey)
                            .build());
        } catch (MinioException e) {
            LOGGER.error("MinIO error during file download for object '{}': {}", objectKey, e.getMessage());
            throw new FileStorageException("Error downloading file from MinIO: " + e.getMessage(), e);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            LOGGER.error("General error during file download for object '{}': {}", objectKey, e.getMessage());
            throw new FileStorageException("Could not download the file: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String objectKey) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectKey)
                            .build());
            LOGGER.info("File object '{}' deleted successfully from bucket '{}'.", objectKey, bucketName);
        } catch (MinioException e) {
            LOGGER.error("MinIO error during file deletion for object '{}': {}", objectKey, e.getMessage());
            throw new FileStorageException("Error deleting file from MinIO: " + e.getMessage(), e);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            LOGGER.error("General error during file deletion for object '{}': {}", objectKey, e.getMessage());
            throw new FileStorageException("Could not delete the file: " + e.getMessage(), e);
        }
    }

    public String generatePresignedUrl(String objectKey) {
        try {
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectKey)
                            .expiry(publicUrlExpirationMinutes, TimeUnit.MINUTES)
                            .build());
            LOGGER.info("Generated pre-signed URL for object '{}'.", objectKey);
            return url;
        } catch (MinioException e) {
            LOGGER.error("MinIO error generating presigned URL for object '{}': {}", objectKey, e.getMessage());
            throw new FileStorageException("Error generating presigned URL: " + e.getMessage(), e);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            LOGGER.error("General error generating presigned URL for object '{}': {}", objectKey, e.getMessage());
            throw new FileStorageException("Could not generate presigned URL: " + e.getMessage(), e);
        }
    }
}