//package com.example.social.Controller;
//
//
//import com.example.social.Service.FriendService;
//import com.example.social.Mapper.FriendRequestMapper;
//import com.example.social.Mapper.UserMapper;
//import com.example.social.model.DTO.request.FriendRequestActionDto;
//import com.example.social.model.DTO.request.FriendRequestDto;
//import com.example.social.model.DTO.response.FriendRequestResponse;
//import com.example.social.model.DTO.response.UserResponse;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/friends")
//@RequiredArgsConstructor
//@Tag(name = "3. Friends", description = "APIs for managing friendships")
//@SecurityRequirement(name = "keycloak_oauth")
//@PreAuthorize("isAuthenticated()")
//public class FriendController {
//
//    private final FriendService friendService;
//    private final FriendRequestMapper friendRequestMapper;
//    private final UserMapper userMapper;
//
//    @PostMapping("/request")
//    @Operation(summary = "Send a friend request")
//    public ResponseEntity<FriendRequestResponse> sendFriendRequest(@Valid @RequestBody FriendRequestDto requestDto) {
//        var friendRequest = friendService.sendFriendRequest(requestDto.getReceiverId());
//        return new ResponseEntity<>(friendRequestMapper.toFriendRequestResponse(friendRequest), HttpStatus.CREATED);
//    }
//
//    @PutMapping("/request/action")
//    @Operation(summary = "Accept or reject a friend request")
//    public ResponseEntity<FriendRequestResponse> actionFriendRequest(@Valid @RequestBody FriendRequestActionDto actionDto) {
//        var friendRequest = friendService.actionFriendRequest(actionDto.getRequestId(), actionDto.getStatus());
//        return ResponseEntity.ok(friendRequestMapper.toFriendRequestResponse(friendRequest));
//    }
//
//    @GetMapping("/requests/pending")
//    @Operation(summary = "Get my pending incoming friend requests")
//    public ResponseEntity<List<FriendRequestResponse>> getPendingRequests() {
//        List<FriendRequestResponse> pendingRequests = friendService.getMyPendingRequests().stream()
//                .map(friendRequestMapper::toFriendRequestResponse)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(pendingRequests);
//    }
//
//    @GetMapping
//    @Operation(summary = "Get my friends list")
//    public ResponseEntity<List<UserResponse>> getMyFriends() {
//        List<UserResponse> friends = friendService.getMyFriends().stream()
//                .map(userMapper::toUserResponse)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(friends);
//    }
//}
//
