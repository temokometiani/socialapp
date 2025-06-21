package com.example.social.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
public class MinioInfo {

    @Column(name = "minio_file_path", nullable = false)
    private String filePath;

    @Column(name = "minio_object_key", nullable = false, unique = true)
    private String objectKey;

    @Column(name = "minio_bucket_name", nullable = false)
    private String bucketName;

    @Column(name = "minio_created_at", nullable = false)
    private LocalDateTime createdAt;


    public String getFilePath() {
        return filePath;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
