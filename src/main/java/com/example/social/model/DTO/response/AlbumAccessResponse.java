package com.example.social.model.DTO.response;


import com.example.social.model.enums.AccessLevel;
import com.example.social.model.enums.SeeAllFiles;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


public class AlbumAccessResponse {
    private Long id;
    private AlbumResponse album;
    private UserResponse user;
    private AccessLevel accessLevel;
    private SeeAllFiles seeAllFiles;
    private LocalDateTime grantedAt;

    public Long getId() {
        return id;
    }

    public AlbumResponse getAlbum() {
        return album;
    }

    public UserResponse getUser() {
        return user;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public SeeAllFiles getSeeAllFiles() {
        return seeAllFiles;
    }

    public LocalDateTime getGrantedAt() {
        return grantedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAlbum(AlbumResponse album) {
        this.album = album;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public void setSeeAllFiles(SeeAllFiles seeAllFiles) {
        this.seeAllFiles = seeAllFiles;
    }

    public void setGrantedAt(LocalDateTime grantedAt) {
        this.grantedAt = grantedAt;
    }
}