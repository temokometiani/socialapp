package com.example.social.Mapper;

import com.example.social.model.DTO.response.AlbumResponse;
import com.example.social.model.entity.Album;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface AlbumMapper {

    AlbumMapper INSTANCE = Mappers.getMapper(AlbumMapper.class);

    AlbumResponse toDto(Album album);
}
