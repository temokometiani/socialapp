package com.example.social.model.DTO.request;


import com.example.social.model.enums.FriendRequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendRequestActionDto {
    @NotNull(message = "Friend Request ID cannot be null")
    private Long requestId;

    @NotNull(message = "Status cannot be null")
    private FriendRequestStatus status; }