package com.example.social.model.DTO.request;
import com.example.social.model.enums.Visibility;
import lombok.Getter;
import lombok.Setter;

public class FileUpdateRequest {
    private Visibility visibility;

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
}