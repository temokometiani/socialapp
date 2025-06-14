//package com.example.social.Service;
//
//import com.example.social.Repository.AlbumAccessRepository;
//import com.example.social.Repository.AlbumRepository;
//import com.example.social.Repository.FileRepository;
//import com.example.social.Repository.UserRepository;
//import com.example.social.exception.ApiException;
//import com.example.social.exception.FileStorageException;
//import com.example.social.exception.UserNotFoundException;
//import com.example.social.model.entity.Album;
//import com.example.social.model.entity.File;
//import com.example.social.model.entity.MinioInfo;
//import com.example.social.model.entity.User;
//import com.example.social.model.enums.AccessLevel;
//import com.example.social.model.enums.FileType;
//import com.example.social.model.enums.Visibility;
//import com.example.social.util.SecurityUtils;
//import io.minio.GetPresignedObjectUrlArgs;
//import io.minio.MinioClient;
//import io.minio.PutObjectArgs;
//import io.minio.RemoveObjectArgs;
//import io.minio.http.Method;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//import io.minio.errors.ErrorResponseException;
//import io.minio.errors.InsufficientDataException;
//import io.minio.errors.InternalException;
//import io.minio.errors.InvalidResponseException;
//import io.minio.errors.ServerException;
//import io.minio.errors.XmlParserException;
//
//import java.io.IOException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//
//@Service
//@RequiredArgsConstructor
//public class FileService {
//
//    private final FileRepository fileRepository;
//    private final AlbumRepository albumRepository;
//    private final AlbumAccessRepository albumAccessRepository;
//    private final UserRepository userRepository;
//    private final MinioClient minioClient;
//    private final FriendService friendService;
//
//    @Value("${minio.bucket}")
//    private String bucketName;
//
//    @Transactional
//    public File uploadFile(MultipartFile multipartFile, Long albumId, FileType fileType, Visibility visibility) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
//        User owner = getCurrentUser();
//        Album album = albumRepository.findById(albumId)
//                .orElseThrow(() -> new ApiException("Album not found.", HttpStatus.NOT_FOUND));
//
//        // User must be owner or have MODIFY access
//        checkAlbumModifyPermission(owner, album);
//
//        //  File type must match album type
//        if (album.getAlbumType().name() != fileType.name()) {
//            throw new ApiException("File type (" + fileType + ") does not match album type (" + album.getAlbumType() + ").", HttpStatus.BAD_REQUEST);
//        }
//
//        //  Upload to Minio
//        String objectKey = UUID.randomUUID().toString() + "-" + multipartFile.getOriginalFilename();
//        minioClient.putObject(
//                PutObjectArgs.builder()
//                        .bucket(bucketName)
//                        .object(objectKey)
//                        .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
//                        .contentType(multipartFile.getContentType())
//                        .build()
//        );
//
//        // Create and Save File Entity
//        MinioInfo minioInfo = new MinioInfo();
//        minioInfo.setBucketName(bucketName);
//        minioInfo.setObjectKey(objectKey);
//        minioInfo.setFilePath(multipartFile.getOriginalFilename());
//        minioInfo.setCreatedAt(LocalDateTime.now());
//
//        File file = new File();
//        file.setFileType(fileType);
//        file.setVisibility(visibility);
//        file.setAlbum(album);
//        file.setOwner(owner);
//        file.setMinioInfo(minioInfo);
//
//        return fileRepository.save(file);
//    }
//
//    private void checkAlbumModifyPermission(User user, Album album) {
//        boolean isOwner = album.getOwner().getId().equals(user.getId());
//        boolean hasModifyAccess = albumAccessRepository
//                .findByAlbumAndUser(album, user)
//                .map(access -> access.getAccessLevel() == AccessLevel.MODIFY || access.getAccessLevel() == AccessLevel.OWNER)
//                .orElse(false);
//
//        if (!isOwner && !hasModifyAccess) {
//            throw new ApiException("You do not have permission to modify this album.", HttpStatus.FORBIDDEN);
//        }
//    }
//
//    private User getCurrentUser() {
//        String keycloakId = SecurityUtils.getCurrentUserKeycloakId();
//        return userRepository.findByKeycloakUserId(keycloakId)
//                .orElseThrow(() -> new UserNotFoundException("Authenticated user not found in the database."));
//    }
//
//
//    public String getFileDownloadUrl(Long fileId) {
//        boolean canSeeFile = this.getVisibleFiles().stream().anyMatch(file -> file.getId().equals(fileId));
//
//        if (!canSeeFile) {
//            throw new ApiException("File not found or you do not have permission to view it.", HttpStatus.NOT_FOUND);
//        }
//
//        File file = fileRepository.findById(fileId).get();
//
//        try {
//            return minioClient.getPresignedObjectUrl(
//                    GetPresignedObjectUrlArgs.builder()
//                            .method(Method.GET)
//                            .bucket(file.getMinioInfo().getBucketName())
//                            .object(file.getMinioInfo().getObjectKey())
//                            .expiry(15, TimeUnit.MINUTES) // URL is valid for 15 minutes
//                            .build());
//        } catch (Exception e) {
//            throw new FileStorageException("Could not generate download URL for the file.", e);
//        }
//    }
//
//    public List<File> getVisibleFiles() {
//        User currentUser = getCurrentUser();
//        List<Long> friendIds = friendService.getMyFriends().stream().map(User::getId).collect(Collectors.toList());
//        return fileRepository.findFilesVisibleToUser(currentUser, friendIds);
//    }
//
//    @Transactional
//    public void deleteFile(Long fileId) {
//        User currentUser = getCurrentUser();
//        File file = fileRepository.findById(fileId)
//                .orElseThrow(() -> new ApiException("File not found.", HttpStatus.NOT_FOUND));
//
//        if (!file.getOwner().getId().equals(currentUser.getId())) {
//            throw new ApiException("You do not have permission to delete this file.", HttpStatus.FORBIDDEN);
//        }
//
//        try {
//            minioClient.removeObject(
//                    RemoveObjectArgs.builder()
//                            .bucket(file.getMinioInfo().getBucketName())
//                            .object(file.getMinioInfo().getObjectKey())
//                            .build());
//        } catch (Exception e) {
//            throw new FileStorageException("Could not delete file from storage. Please try again.", e);
//        }
//
//        fileRepository.delete(file);
//    }
//
//
//
//
//}