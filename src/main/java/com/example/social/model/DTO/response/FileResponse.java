package com.example.social.model.DTO.response;
import com.example.social.model.enums.FileType;
import com.example.social.model.enums.Visibility;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


public class FileResponse {
    private Long id;
    private String fileName;
    private String objectKey;
    private Long size;
    private String contentType;
    private FileType fileType;
    private Visibility visibility;
    private AlbumResponse album;
    private UserResponse owner;
    private String downloadUrl;
    private LocalDateTime uploadedAt;

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public Long getSize() {
        return size;
    }

    public String getContentType() {
        return contentType;
    }

    public FileType getFileType() {
        return fileType;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public AlbumResponse getAlbum() {
        return album;
    }

    public UserResponse getOwner() {
        return owner;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public void setAlbum(AlbumResponse album) {
        this.album = album;
    }

    public void setOwner(UserResponse owner) {
        this.owner = owner;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}