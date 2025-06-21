package com.example.social.model.DTO.request;


import com.example.social.model.enums.FriendRequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


public class FriendRequestActionDto {
    @NotNull(message = "Friend Request ID cannot be null")
    private Long requestId;

    @NotNull(message = "Status cannot be null")
    private FriendRequestStatus status;

    public Long getRequestId() {
        return requestId;
    }

    public FriendRequestStatus getStatus() {
        return status;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public void setStatus(FriendRequestStatus status) {
        this.status = status;
    }
}