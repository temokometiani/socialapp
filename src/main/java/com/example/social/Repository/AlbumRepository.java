package com.example.social.Repository;

import com.example.social.model.entity.Album;
import com.example.social.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    Optional<Album> findByIdAndOwner(Long albumId, User owner);

    List<Album> findByOwner(User owner);

    List<Album> findAlbumsVisibleToUser(User currentUser, List<Long> friendIds);

    //List<Album> findAlbumsVisibleToUser(User currentUser, List<Long> friendIds);
}