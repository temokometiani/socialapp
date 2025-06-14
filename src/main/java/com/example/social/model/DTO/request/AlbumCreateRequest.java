package com.example.social.model.DTO.request;


import com.example.social.model.enums.AlbumType;
import com.example.social.model.enums.Visibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlbumCreateRequest {
    @NotBlank(message = "Album name cannot be empty")
    @Size(min = 1, max = 100, message = "Album name must be between 1 and 100 characters")
    private String name;

    @NotNull(message = "Album type cannot be null")
    private AlbumType type;

    @NotNull(message = "Visibility cannot be null")
    private Visibility visibility;
}