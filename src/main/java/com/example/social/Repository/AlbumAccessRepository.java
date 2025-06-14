package com.example.social.Repository;

import com.example.social.model.entity.Album;
import com.example.social.model.entity.AlbumAccess;
import com.example.social.model.entity.User;
import com.example.social.model.enums.AccessLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumAccessRepository extends JpaRepository<AlbumAccess, Long> {
    List<AlbumAccess> findByAlbum(Album album);
    List<AlbumAccess> findByUser(User user);
    Optional<AlbumAccess> findByAlbumAndUser(Album album, User user);
    boolean existsByAlbumAndUserAndAccessLevel(Album album, User user, AccessLevel accessLevel);

    Optional<AlbumAccess> findByUserAndAlbum(User user, Album album);
}