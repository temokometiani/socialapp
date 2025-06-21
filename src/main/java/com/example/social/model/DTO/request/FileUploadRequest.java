package com.example.social.model.DTO.request;

import com.example.social.model.enums.FileType;
import com.example.social.model.enums.Visibility;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileUploadRequest {
    @NotNull(message = "Album ID cannot be null")
    private Long albumId;

    @NotNull(message = "File type cannot be null")
    private FileType fileType;

    @NotNull(message = "Visibility cannot be null")
    private Visibility visibility;

    @NotNull(message = "File cannot be null")
    private MultipartFile file;

    public Long getAlbumId() {
        return albumId;
    }

    public FileType getFileType() {
        return fileType;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}