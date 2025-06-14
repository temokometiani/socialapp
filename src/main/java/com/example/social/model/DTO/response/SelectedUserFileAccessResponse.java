package com.example.social.model.DTO.response;

import com.example.social.model.enums.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SelectedUserFileAccessResponse {
    private Long id;
    private FileResponse file;
    private UserResponse user;
    private AccessLevel accessLevel;
}

