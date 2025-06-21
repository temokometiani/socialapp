package com.example.social.Repository;

import com.example.social.model.entity.File;
import com.example.social.model.entity.SelectedUserFileAccess;
import com.example.social.model.entity.User;
import com.example.social.model.enums.AccessLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SelectedUserFileAccessRepository extends JpaRepository<SelectedUserFileAccess, Long> {

    boolean existsByFileAndUser(File file, User user);
    List<SelectedUserFileAccess> findByFile(File file);
    List<SelectedUserFileAccess> findByUser(User user);
    Optional<SelectedUserFileAccess> findByUserAndFile(User user, File file);
    boolean existsByFileAndUserAndAccessLevel(File file, User user, AccessLevel accessLevel);
    Optional <SelectedUserFileAccess> findByFileAndUser(File file, User user); // Newly added method

}