package com.example.social.Mapper;// src/main/java/com/example/social/mapper/UserMapper.java
import com.example.social.model.DTO.request.RegisterRequest;
import com.example.social.model.DTO.response.UserResponse;
import com.example.social.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring") // componentModel = "spring" makes MapStruct generate a Spring component
public interface UserMapper {
//    @Mapping(source = "id", target = "id")
    UserResponse toDto(User user);
}