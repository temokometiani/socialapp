package com.example.social.model.DTO.response;


import com.example.social.model.enums.FriendRequestStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


public class FriendRequestResponse {
    private Long id;
    private UserResponse sender;
    private UserResponse receiver;
    private FriendRequestStatus status;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public UserResponse getSender() {
        return sender;
    }

    public UserResponse getReceiver() {
        return receiver;
    }

    public FriendRequestStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSender(UserResponse sender) {
        this.sender = sender;
    }

    public void setReceiver(UserResponse receiver) {
        this.receiver = receiver;
    }

    public void setStatus(FriendRequestStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}