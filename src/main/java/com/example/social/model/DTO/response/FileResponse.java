package com.example.social.model.DTO.response;
import com.example.social.model.enums.FileType;
import com.example.social.model.enums.Visibility;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
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
}