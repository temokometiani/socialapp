package com.example.social.model.DTO.request;
import com.example.social.model.enums.Visibility;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class FileUpdateRequest {
    private Visibility visibility;
}