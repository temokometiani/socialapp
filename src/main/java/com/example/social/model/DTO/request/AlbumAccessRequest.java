package com.example.social.model.DTO.request;

import com.example.social.model.enums.AccessLevel;
import com.example.social.model.enums.SeeAllFiles;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlbumAccessRequest {
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Access level cannot be null")
    private AccessLevel accessLevel;

    @NotNull(message = "See all files setting cannot be null")
    private SeeAllFiles seeAllFiles;
}
