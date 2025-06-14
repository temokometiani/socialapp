//package com.example.social.Service;
//
//import com.example.social.Repository.AlbumAccessRepository;
//import com.example.social.Repository.AlbumRepository;
//import com.example.social.Repository.UserRepository;
//import com.example.social.exception.ApiException;
//import com.example.social.exception.UserNotFoundException;
//import com.example.social.model.DTO.request.AlbumAccessRequest;
//import com.example.social.model.DTO.request.AlbumCreateRequest;
//import com.example.social.model.DTO.request.AlbumUpdateRequest;
//import com.example.social.model.entity.Album;
//import com.example.social.model.entity.AlbumAccess;
//import com.example.social.model.entity.User;
//import com.example.social.model.enums.AccessLevel;
//import com.example.social.util.SecurityUtils;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class AlbumService {
//
//    private final AlbumRepository albumRepository;
//    private final AlbumAccessRepository albumAccessRepository;
//    private final UserRepository userRepository;
//    private final EmailService emailService;
//    private final FriendService friendService;
//
//    @Transactional
//    public Album createAlbum(AlbumCreateRequest request) {
//        User owner = getCurrentUser();
//
//        Album album = new Album();
//        album.setAlbumName(request.getName());
//        album.setAlbumType(request.getType());
//        album.setVisibility(request.getVisibility());
//        album.setOwner(owner);
//        album.setCreatedAt(LocalDateTime.now());
//
//        Album savedAlbum = albumRepository.save(album);
//
//        AlbumAccess ownerAccess = new AlbumAccess();
//        ownerAccess.setAlbum(savedAlbum);
//        ownerAccess.setUser(owner);
//        ownerAccess.setAccessLevel(AccessLevel.OWNER);
//        albumAccessRepository.save(ownerAccess);
//
//        return savedAlbum;
//    }
//
//    @Transactional
//    public Album updateAlbum(Long albumId, AlbumUpdateRequest request) {
//        User owner = getCurrentUser();
//        Album album = findAlbumByIdAndOwner(albumId, owner);
//
//        if (request.getName() != null) {
//            album.setAlbumName(request.getName());
//        }
//        if (request.getVisibility() != null) {
//            album.setVisibility(request.getVisibility());
//        }
//        return albumRepository.save(album);
//    }
//
//    @Transactional
//    public AlbumAccess grantAlbumAccess(Long albumId, AlbumAccessRequest request) {
//        User owner = getCurrentUser();
//        Album album = findAlbumByIdAndOwner(albumId, owner);
//        User userToGrant = findUserById(request.getUserId());
//
//        if (owner.getId().equals(userToGrant.getId())) {
//            throw new ApiException("You cannot grant access to yourself.", HttpStatus.BAD_REQUEST);
//        }
//
//        if (albumAccessRepository.findByAlbumAndUser(album, userToGrant).isPresent()) {
//            throw new ApiException("Access has already been granted to this user for this album.", HttpStatus.CONFLICT);
//        }
//
//        AlbumAccess newAccess = new AlbumAccess();
//        newAccess.setAlbum(album);
//        newAccess.setUser(userToGrant);
//        newAccess.setAccessLevel(request.getAccessLevel());
//        newAccess.setSeeAllFiles(request.getSeeAllFiles());
//
//        emailService.sendEmail(
//                userToGrant.getEmail(),
//                "You have been granted access to an album",
//                String.format("Hello %s,\n\n%s %s has granted you %s access to the album '%s'.",
//                        userToGrant.getName(), owner.getName(), owner.getLastName(), request.getAccessLevel().name(), album.getAlbumName())
//        );
//
//        return albumAccessRepository.save(newAccess);
//    }
//
//    @Transactional
//    public void revokeAlbumAccess(Long albumId, Long userId) {
//        User owner = getCurrentUser();
//        Album album = findAlbumByIdAndOwner(albumId, owner);
//        User userToRevoke = findUserById(userId);
//
//        AlbumAccess access = albumAccessRepository.findByAlbumAndUser(album, userToRevoke)
//                .orElseThrow(() -> new ApiException("No access record found for this user and album.", HttpStatus.NOT_FOUND));
//
//        albumAccessRepository.delete(access);
//    }
//
//
//    public List<Album> getVisibleAlbums() {
//        User currentUser = getCurrentUser();
//        List<Long> friendIds = friendService.getMyFriends().stream().map(User::getId).collect(Collectors.toList());
//
//        return albumRepository.findAlbumsVisibleToUser(currentUser, friendIds);
//    }
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
//
//    private Album findAlbumByIdAndOwner(Long albumId, User owner) {
//        return albumRepository.findByIdAndOwner(albumId, owner)
//                .orElseThrow(() -> new ApiException("Album not found or you are not the owner.", HttpStatus.NOT_FOUND));
//    }
//}
