package com.example.social.Mapper;
import com.example.social.model.DTO.response.SelectedUserFileAccessResponse;
import com.example.social.model.entity.SelectedUserFileAccess;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {UserMapper.class, FileMapper.class})
public interface SelectedUserFileAccessMapper {

    SelectedUserFileAccessMapper INSTANCE = Mappers.getMapper(SelectedUserFileAccessMapper.class);


    SelectedUserFileAccessResponse toDto(SelectedUserFileAccess selectedUserFileAccess);
}