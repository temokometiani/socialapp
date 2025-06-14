//package com.example.social.Mapper;
//
//import com.example.social.model.DTO.response.FriendRequestResponse;
//import com.example.social.model.entity.FriendRequest;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;
//
//
//@Mapper(componentModel = "spring", uses = {UserMapper.class})
//public interface FriendRequestMapper {
//
//    FriendRequestMapper INSTANCE = Mappers.getMapper(FriendRequestMapper.class);
//
//    @Mapping(source = "sender_user_id", target = "sender")
//    @Mapping(source = "receiver_user_id", target = "receiver")
//    FriendRequestResponse toFriendRequestResponse(FriendRequest friendRequest);
//}
