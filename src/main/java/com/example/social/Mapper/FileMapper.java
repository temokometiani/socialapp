package com.example.social.Mapper;


import com.example.social.model.DTO.response.FileResponse;
import com.example.social.model.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {UserMapper.class, AlbumMapper.class})
public interface FileMapper {

    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    @Mapping(source = "minioInfo.objectKey", target = "objectKey")
    @Mapping(source = "minioInfo.filePath", target = "fileName")
    @Mapping(source = "owner", target = "owner")
    @Mapping(source = "album", target = "album")
    // will need to add logic in your service to generate and set the downloadUrl
    @Mapping(target = "downloadUrl", ignore = true)
    // will need to add logic in your service to get the file size and content type
    @Mapping(target = "size", ignore = true)
    @Mapping(target = "contentType", ignore = true)
    FileResponse toFileResponse(File file);
}
