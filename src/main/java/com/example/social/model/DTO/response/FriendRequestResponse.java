package com.example.social.model.DTO.response;


import com.example.social.model.enums.FriendRequestStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class FriendRequestResponse {
    private Long id;
    private UserResponse sender;
    private UserResponse receiver;
    private FriendRequestStatus status;
    private LocalDateTime createdAt;
}