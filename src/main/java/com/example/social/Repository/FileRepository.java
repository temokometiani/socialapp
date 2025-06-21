package com.example.social.Repository;

import com.example.social.model.entity.Album;
import com.example.social.model.entity.File;
import com.example.social.model.entity.User;
import com.example.social.model.enums.FileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {


    List<File> findByAlbum(Album album);
    List<File> findByOwner(User owner);
    @Query("SELECT DISTINCT f FROM File f LEFT JOIN f.album a " +
            "WHERE f.visibility = 'PUBLIC' " +
            "OR a.visibility = 'PUBLIC' " +
            "OR f.owner = :viewer " +
            "OR ((f.visibility = 'FRIENDS' OR a.visibility = 'FRIENDS') AND f.owner.id IN :friendIds) " +
            "OR EXISTS (SELECT sufa FROM SelectedUserFileAccess sufa WHERE sufa.file = f AND sufa.user = :viewer) " +
            "OR EXISTS (SELECT aa FROM AlbumAccess aa WHERE aa.album = a AND aa.user = :viewer)")
    List<File> findFilesVisibleToUser(@Param("viewer") User viewer, @Param("friendIds") List<Long> friendIds);
    Optional<File> findByIdAndOwner(Long fileId, User owner);
    Optional<File> findByMinioInfo_ObjectKey(String objectKey);
    List<File> findByAlbumAndFileType(Album album, FileType fileType);

}
