package com.example.social.model.DTO.response;

import com.example.social.model.enums.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


public class SelectedUserFileAccessResponse {

    private Long id;
    private FileResponse file;
    private UserResponse user;
    private AccessLevel accessLevel;


    public Long getId() {
        return id;
    }

    public FileResponse getFile() {
        return file;
    }

    public UserResponse getUser() {
        return user;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFile(FileResponse file) {
        this.file = file;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }


}

