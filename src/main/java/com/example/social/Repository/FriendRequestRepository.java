package com.example.social.Repository;


import com.example.social.model.entity.FriendRequest;
import com.example.social.model.entity.User;
import com.example.social.model.enums.FriendRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    @Query("SELECT CASE WHEN fr.sender = :user THEN fr.receiver ELSE fr.sender END " +
            "FROM FriendRequest fr " +
            "WHERE (fr.sender = :user OR fr.receiver = :user) " +
            "AND fr.status = 'ACCEPTED'")
    List<User> findFriendsOfUser(@Param("user") User user);

    @Query("SELECT fr FROM FriendRequest fr WHERE fr.status = 'ACCEPTED' AND " +
            "((fr.sender = :user1 AND fr.receiver = :user2) OR (fr.sender = :user2 AND fr.receiver = :user1))")
    Optional<FriendRequest> findAcceptedFriendRequestBetween(@Param("user1") User user1, @Param("user2") User user2);

}
