package com.example.social.model.DTO.request;


import com.example.social.model.enums.Visibility;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlbumUpdateRequest {
    @Size(min = 1, max = 100, message = "Album name must be between 1 and 100 characters")
    private String name;

    private Visibility visibility;

    public String getName() {
        return name;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
}
