package com.example.social.Mapper;
import com.example.social.model.DTO.response.SelectedUserFileAccessResponse;
import com.example.social.model.entity.SelectedUserFileAccess;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring", uses = {UserMapper.class, FileMapper.class})
interface SelectedUserFileAccessMapper {

    SelectedUserFileAccessMapper INSTANCE = Mappers.getMapper(SelectedUserFileAccessMapper.class);

    @Mapping(source = "file", target = "file")
    @Mapping(source = "user", target = "user")
    SelectedUserFileAccessResponse toSelectedUserFileAccessResponse(SelectedUserFileAccess selectedUserFileAccess);
}