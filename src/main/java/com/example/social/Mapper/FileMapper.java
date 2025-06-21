package com.example.social.Mapper;


import com.example.social.model.DTO.response.FileResponse;
import com.example.social.model.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {UserMapper.class, AlbumMapper.class})
public interface FileMapper {

    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    @Mapping(source = "minioInfo.objectKey", target = "objectKey")
    @Mapping(source = "minioInfo.filePath", target = "fileName")
    @Mapping(source = "owner", target = "owner")
    @Mapping(source = "album", target = "album")
    @Mapping(source = "minioInfo.createdAt", target = "uploadedAt")
    @Mapping(target = "downloadUrl", ignore = true)
    @Mapping(target = "size", ignore = true)
    @Mapping(target = "contentType", ignore = true)
    FileResponse toDto(File file);
}
