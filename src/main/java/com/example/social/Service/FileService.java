package com.example.social.Service;

import com.example.social.Repository.*;
import com.example.social.exception.ApiException;
import com.example.social.exception.FileStorageException;
import com.example.social.exception.UserNotFoundException;
import com.example.social.model.DTO.request.SelectedUserFileAccessRequest;
import com.example.social.model.entity.*;
import com.example.social.model.enums.AccessLevel;
import com.example.social.model.enums.FileType;
import com.example.social.model.enums.Visibility;
import com.example.social.util.SecurityUtils;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.example.social.Repository.SelectedUserFileAccessRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final AlbumRepository albumRepository;
    private final AlbumAccessRepository albumAccessRepository;
    private final UserRepository userRepository;
    private final MinioClient minioClient;
    private final FriendService friendService;
    private final SelectedUserFileAccessRepository selectedUserFileAccessRepository;
    private final EmailService emailService;

    @Value("${minio.bucket}")
    private String bucketName;

    @Transactional
    public File uploadFile(MultipartFile multipartFile, Long albumId, FileType fileType, Visibility visibility) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        User owner = getCurrentUser();
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new ApiException("Album not found.", HttpStatus.NOT_FOUND));

        // User must be owner or have MODIFY access
        checkAlbumModifyPermission(owner, album);

        //  File type must match album type
        if (album.getAlbumType().name() != fileType.name()) {
            throw new ApiException("File type (" + fileType + ") does not match album type (" + album.getAlbumType() + ").", HttpStatus.BAD_REQUEST);
        }

        //  Upload to Minio
        String objectKey = UUID.randomUUID().toString() + "-" + multipartFile.getOriginalFilename();
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectKey)
                        .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                        .contentType(multipartFile.getContentType())
                        .build()
        );

        // Create and Save File Entity
        MinioInfo minioInfo = new MinioInfo();
        minioInfo.setBucketName(bucketName);
        minioInfo.setObjectKey(objectKey);
        minioInfo.setFilePath(multipartFile.getOriginalFilename());
        minioInfo.setCreatedAt(LocalDateTime.now());

        File file = new File();
        file.setFileType(fileType);
        file.setVisibility(visibility);
        file.setAlbum(album);
        file.setOwner(owner);
        file.setMinioInfo(minioInfo);

        return fileRepository.save(file);
    }

    private void checkAlbumModifyPermission(User user, Album album) {
        boolean isOwner = album.getOwner().getId().equals(user.getId());
        boolean hasModifyAccess = albumAccessRepository
                .findByAlbumAndUser(album, user)
                .map(access -> access.getAccessLevel() == AccessLevel.MODIFY || access.getAccessLevel() == AccessLevel.OWNER)
                .orElse(false);

        if (!isOwner && !hasModifyAccess) {
            throw new ApiException("You do not have permission to modify this album.", HttpStatus.FORBIDDEN);
        }
    }

    private User getCurrentUser() {
        String keycloakId = SecurityUtils.getCurrentUserKeycloakId();
        return userRepository.findByKeycloakUserId(keycloakId)
                .orElseThrow(() -> new UserNotFoundException("Authenticated user not found in the database."));
    }


    public String getFileDownloadUrl(Long fileId) {
        boolean canSeeFile = this.getVisibleFiles().stream().anyMatch(file -> file.getId().equals(fileId));

        if (!canSeeFile) {
            throw new ApiException("File not found or you do not have permission to view it.", HttpStatus.NOT_FOUND);
        }

        File file = fileRepository.findById(fileId).get();

        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(file.getMinioInfo().getBucketName())
                            .object(file.getMinioInfo().getObjectKey())
                            .expiry(15, TimeUnit.MINUTES) // URL is valid for 15 minutes
                            .build());
        } catch (Exception e) {
            throw new FileStorageException("Could not generate download URL for the file.", e);
        }
    }

    @Transactional
    public File updateFileVisibility(Long fileId, Visibility newVisibility) {
        User currentUser = getCurrentUser();
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ApiException("File not found.", HttpStatus.NOT_FOUND));

        if (!file.getOwner().getId().equals(currentUser.getId())) {
            throw new ApiException("You do not have permission to update this file.", HttpStatus.FORBIDDEN);
        }

        file.setVisibility(newVisibility);
        return fileRepository.save(file);
    }

    public List<File> getVisibleFiles() {
        User currentUser = getCurrentUser();
        List<Long> friendIds = friendService.getMyFriends().stream().map(User::getId).collect(Collectors.toList());
        return fileRepository.findFilesVisibleToUser(currentUser, friendIds);
    }

    @Transactional
    public void deleteFile(Long fileId) {
        User currentUser = getCurrentUser();
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ApiException("File not found.", HttpStatus.NOT_FOUND));

        if (!file.getOwner().getId().equals(currentUser.getId())) {
            throw new ApiException("You do not have permission to delete this file.", HttpStatus.FORBIDDEN);
        }

        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(file.getMinioInfo().getBucketName())
                            .object(file.getMinioInfo().getObjectKey())
                            .build());
        } catch (Exception e) {
            throw new FileStorageException("Could not delete file from storage. Please try again.", e);
        }

        fileRepository.delete(file);
    }
    private boolean isAlbumVisibleToUser(Album album, User user) {
        if (album.getVisibility() == Visibility.PUBLIC || album.getOwner().getId().equals(user.getId())) {
            return true;
        }
        if (album.getVisibility() == Visibility.FRIENDS) {
            return friendService.getMyFriends().stream().anyMatch(friend -> friend.getId().equals(album.getOwner().getId()));
        }
        // Check for direct private access
        return albumAccessRepository.findByAlbumAndUser(album, user).isPresent();
    }

    private boolean isFileVisibleToUser(File file, User user) {
        if (file.getVisibility() == Visibility.PUBLIC || file.getOwner().getId().equals(user.getId())) {
            return true;
        }
        if (file.getVisibility() == Visibility.FRIENDS) {
            return friendService.getMyFriends().stream().anyMatch(friend -> friend.getId().equals(file.getOwner().getId()));
        }
        // Check for direct private access to the file
        return selectedUserFileAccessRepository.findByFileAndUser(file, user).isPresent();
    }
    @Transactional
    public void revokeFileAccess(Long fileId, Long userId) {
        User owner = getCurrentUser();
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ApiException("File not found.", HttpStatus.NOT_FOUND));

        if (!file.getOwner().getId().equals(owner.getId())) {
            throw new ApiException("You are not the owner of this file.", HttpStatus.FORBIDDEN);
        }

        User userToRevoke = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User to revoke access from not found."));

        SelectedUserFileAccess access = selectedUserFileAccessRepository.findByFileAndUser(file, userToRevoke)
                .orElseThrow(() -> new ApiException("No access record found for this user and file.", HttpStatus.NOT_FOUND));

        selectedUserFileAccessRepository.delete(access);
    }
    @Transactional
    public SelectedUserFileAccess grantFileAccess(Long fileId, SelectedUserFileAccessRequest request) {
        User owner = getCurrentUser();
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ApiException("File not found.", HttpStatus.NOT_FOUND));

        if (!file.getOwner().getId().equals(owner.getId())) {
            throw new ApiException("You are not the owner of this file.", HttpStatus.FORBIDDEN);
        }

        User userToGrant = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User to grant access to not found."));

        if (selectedUserFileAccessRepository.existsByFileAndUser(file, userToGrant)) {
            throw new ApiException("Access has already been granted to this user for this file.", HttpStatus.CONFLICT);
        }

        SelectedUserFileAccess newAccess = new SelectedUserFileAccess();
        newAccess.setFile(file);
        newAccess.setUser(userToGrant);
        newAccess.setAccessLevel(request.getAccessLevel());

        emailService.sendEmail(
                userToGrant.getEmail(),
                "Access granted to a file",
                String.format("Hello %s,\n\n%s %s has granted you %s access to the file '%s'.",
                        userToGrant.getName(), owner.getName(), owner.getLastName(), request.getAccessLevel().name(), file.getMinioInfo().getFilePath())
        );

        return selectedUserFileAccessRepository.save(newAccess);

    }

    public List<File> getVisibleFilesForAlbum(Long albumId) {
        User currentUser = getCurrentUser();
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new ApiException("Album not found", HttpStatus.NOT_FOUND));

        // Check if the user can see the album itself.
        boolean canSeeAlbum = isAlbumVisibleToUser(album, currentUser);
        if (!canSeeAlbum) {
            throw new ApiException("You do not have permission to view this album.", HttpStatus.FORBIDDEN);
        }
        return album.getFiles().stream()
                .filter(file -> isFileVisibleToUser(file, currentUser))
                .collect(Collectors.toList());
    }



}