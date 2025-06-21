package com.example.social.Mapper;

import com.example.social.model.DTO.response.FriendRequestResponse;
import com.example.social.model.entity.FriendRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface FriendRequestMapper {

    FriendRequestMapper INSTANCE = Mappers.getMapper(FriendRequestMapper.class);

    @Mapping(source = "sender", target = "sender")
    @Mapping(source = "receiver", target = "receiver")
    @Mapping(source = "createdAt", target = "createdAt")
    FriendRequestResponse toDto(FriendRequest friendRequest);
}
