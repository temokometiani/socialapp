package com.example.social.model.DTO.response;


import com.example.social.model.enums.AccessLevel;
import com.example.social.model.enums.SeeAllFiles;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AlbumAccessResponse {
    private Long id;
    private AlbumResponse album;
    private UserResponse user;
    private AccessLevel accessLevel;
    private SeeAllFiles seeAllFiles;
    private LocalDateTime grantedAt;


}