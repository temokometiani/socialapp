package com.example.social.model.DTO.request;

import com.example.social.model.enums.AccessLevel;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectedUserFileAccessRequest {
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Access level cannot be null")
    private AccessLevel accessLevel;

    public Long getUserId() {
        return userId;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }
}