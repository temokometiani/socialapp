package com.example.social.model.DTO.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String lastName;
    private LocalDate dob;
    private String email;
    private String keycloakUserId;

    public UserResponse(Long id, String name, String lastName, LocalDate dob, String email, String keycloakUserId) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.dob = dob;
        this.email = email;
        this.keycloakUserId = keycloakUserId;
    }
}