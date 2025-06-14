package com.example.social.Mapper;


import com.example.social.model.DTO.response.UserResponse;
import com.example.social.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toDto(User user);
    User toEntity(UserResponse user);
}
