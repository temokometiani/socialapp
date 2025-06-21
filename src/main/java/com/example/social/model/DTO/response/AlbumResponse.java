package com.example.social.model.DTO.response;

import com.example.social.model.entity.Album;
import com.example.social.model.enums.AlbumType;
import com.example.social.model.enums.Visibility;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class AlbumResponse {
    private Long id;
    private String albumName;
    private LocalDateTime createdAt;
    private AlbumType albumType;
    private Visibility visibility;
    private UserResponse owner;

    public AlbumResponse(Long id, String albumName, LocalDateTime createdAt, AlbumType albumType, Visibility visibility, UserResponse owner) {
        this.id = id;
        this.albumName = albumName;
        this.createdAt = createdAt;
        this.albumType = albumType;
        this.visibility = visibility;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public AlbumType getAlbumType() {
        return albumType;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public UserResponse getOwner() {
        return owner;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setAlbumType(AlbumType albumType) {
        this.albumType = albumType;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public void setOwner(UserResponse owner) {
        this.owner = owner;
    }
//    public static AlbumResponse map(Album album) {
//        return new AlbumResponse(
//                album.getId(),
//                album.getAlbumName(),
//                album.getCreatedAt(),
//                album.getAlbumType(),
//                album.getVisibility(),
//                null
//        );
//    }
}