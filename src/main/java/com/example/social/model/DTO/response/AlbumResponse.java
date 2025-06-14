package com.example.social.model.DTO.response;

import com.example.social.model.entity.Album;
import com.example.social.model.enums.AlbumType;
import com.example.social.model.enums.Visibility;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AlbumResponse {
    private Long id;
    private String albumName;
    private LocalDateTime createdAt;
    private AlbumType albumType;
    private Visibility visibility;
    private UserResponse owner;

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