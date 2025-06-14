//package com.example.social.Service;
//
//import com.example.social.Repository.FriendRequestRepository;
//import com.example.social.Repository.UserRepository;
//import com.example.social.exception.ApiException;
//import com.example.social.exception.UserNotFoundException;
//import com.example.social.model.entity.FriendRequest;
//import com.example.social.model.entity.User;
//import com.example.social.model.enums.FriendRequestStatus;
//import com.example.social.util.SecurityUtils;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//
//@Service
//@RequiredArgsConstructor
//public class FriendService {
//
//    private final UserRepository userRepository;
//    private final FriendRequestRepository friendRequestRepository;
//    private final EmailService emailService;
//
//
//    @Transactional
//    public FriendRequest sendFriendRequest(Long receiverId) {
//        User sender = getCurrentUser();
//        User receiver = findUserById(receiverId);
//
//        if (sender.getId().equals(receiver.getId())) {
//            throw new ApiException("You cannot send a friend request to yourself.", HttpStatus.BAD_REQUEST);
//        }
//
//        if (friendRequestRepository.existsBySenderAndReceiver(sender, receiver) ||
//                friendRequestRepository.existsByReceiverAndSender(receiver, sender)) {
//            throw new ApiException("A friend request already exists with this user.", HttpStatus.CONFLICT);
//        }
//
//        FriendRequest friendRequest = new FriendRequest();
//        friendRequest.setSender(sender);
//        friendRequest.setReceiver(receiver);
//        friendRequest.setStatus(FriendRequestStatus.PENDING);
//        friendRequest.setCreatedAt(LocalDateTime.now());
//
//        emailService.sendEmail(
//                receiver.getEmail(),
//                "You have a new friend request!",
//                String.format("Hello %s,\n\nYou have received a new friend request from %s %s.",
//                        receiver.getName(), sender.getName(), sender.getLastName())
//        );
//
//        return friendRequestRepository.save(friendRequest);
//    }
//
//
//    @Transactional
//    public FriendRequest actionFriendRequest(Long requestId, FriendRequestStatus status) {
//        if (status == FriendRequestStatus.PENDING) {
//            throw new ApiException("Action must be ACCEPTED or REJECTED.", HttpStatus.BAD_REQUEST);
//        }
//
//        User currentUser = getCurrentUser();
//        FriendRequest request = friendRequestRepository.findById(requestId)
//                .orElseThrow(() -> new ApiException("Friend request not found.", HttpStatus.NOT_FOUND));
//
//        if (!request.getReceiver().getId().equals(currentUser.getId())) {
//            throw new ApiException("You are not authorized to action this friend request.", HttpStatus.FORBIDDEN);
//        }
//
//        if (request.getStatus() != FriendRequestStatus.PENDING) {
//            throw new ApiException("This friend request has already been actioned.", HttpStatus.BAD_REQUEST);
//        }
//
//        request.setStatus(status);
//
//        emailService.sendEmail(
//                request.getSender().getEmail(),
//                "Your friend request was " + status.toString().toLowerCase(),
//                String.format("Hello %s,\n\n%s %s has %s your friend request.",
//                        request.getSender().getName(), currentUser.getName(), currentUser.getLastName(), status.toString().toLowerCase())
//        );
//
//        return friendRequestRepository.save(request);
//    }
//
//
//    public Set<User> getMyFriends() {
//        User currentUser = getCurrentUser();
//
//        Stream<User> friendsFromReceived = friendRequestRepository
//                .findByReceiverAndStatus(currentUser, FriendRequestStatus.ACCEPTED)
//                .stream()
//                .map(FriendRequest::getSender);
//
//        Stream<User> friendsFromSent = friendRequestRepository
//                .findBySenderAndStatus(currentUser, FriendRequestStatus.ACCEPTED)
//                .stream()
//                .map(FriendRequest::getReceiver);
//
//        return Stream.concat(friendsFromReceived, friendsFromSent)
//                .collect(Collectors.toSet());
//    }
//
//
//    public List<FriendRequest> getMyPendingRequests() {
//        User currentUser = getCurrentUser();
//        return friendRequestRepository.findByReceiverAndStatus(currentUser, FriendRequestStatus.PENDING);
//    }
//
//    // Helper Methods
//
//    private User getCurrentUser() {
//        String keycloakId = SecurityUtils.getCurrentUserKeycloakId();
//        return userRepository.findByKeycloakUserId(keycloakId)
//                .orElseThrow(() -> new UserNotFoundException("Authenticated user not found in the database."));
//    }
//
//    private User findUserById(Long id) {
//        return userRepository.findById(id)
//                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found."));
//    }
//}
