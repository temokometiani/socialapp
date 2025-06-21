package com.example.social.Mapper;
import com.example.social.model.DTO.response.AlbumAccessResponse;
import com.example.social.model.entity.AlbumAccess;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {UserMapper.class, AlbumMapper.class})
public interface AlbumAccessMapper {

    AlbumAccessMapper INSTANCE = Mappers.getMapper(AlbumAccessMapper.class);

    @Mapping(source = "album", target = "album")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "grantedAt", target = "grantedAt")
    AlbumAccessResponse toDto(AlbumAccess albumAccess);
}