package com.example.social.Repository;


import com.example.social.model.entity.FriendRequest;
import com.example.social.model.entity.User;
import com.example.social.model.enums.FriendRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);
    boolean existsBySenderAndReceiverAndStatus(User sender, User receiver, FriendRequestStatus status);
    boolean existsByReceiverAndSenderAndStatus(User receiver, User sender, FriendRequestStatus status);
    boolean existsBySenderAndReceiver(User sender, User receiver);
    boolean existsByReceiverAndSender(User receiver, User sender);
    List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequestStatus status);
    List<FriendRequest> findBySenderAndStatus(User sender, FriendRequestStatus status);

}
