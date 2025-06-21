package com.example.social.Repository;


import com.example.social.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByKeycloakUserId(String keycloakUserId);
    Optional<User> findByEmail(String email);
    Optional<User> findByKeycloakUserId(String keycloakUserId);
    boolean existsByEmail(String email);
    List<User> findByNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String name, String lastName);
//    public List<User> getAll();
}