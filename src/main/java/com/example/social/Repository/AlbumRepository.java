package com.example.social.Repository;

import com.example.social.model.entity.Album;
import com.example.social.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    Optional<Album> findByIdAndOwner(Long albumId, User owner);

    @Query("SELECT a FROM Album a WHERE " +
            "a.owner = :currentUser OR " + // Albums owned by the current user
            "(a.visibility = 'PUBLIC') OR " + // Public albums
            "(a.visibility = 'FRIENDS_ONLY' AND a.owner.id IN :friendIds)")
    List<Album> findAlbumsVisibleToUser(@Param("currentUser") User currentUser, @Param("friendIds") List<Long> friendIds);
}