//package com.example.social.Controller;
//import com.example.social.Service.FileService;
//import com.example.social.Mapper.FileMapper;
//import com.example.social.model.DTO.response.FileResponse;
//import com.example.social.model.enums.FileType;
//import com.example.social.model.enums.Visibility;
//import io.minio.errors.ErrorResponseException;
//import io.minio.errors.InsufficientDataException;
//import io.minio.errors.InternalException;
//import io.minio.errors.InvalidResponseException;
//import io.minio.errors.ServerException;
//import io.minio.errors.XmlParserException;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.util.List;
//import java.util.stream.Collectors;
//@RestController
//@RequestMapping("/api/files")
//@RequiredArgsConstructor
//@Tag(name = "5. Files", description = "APIs for uploading and managing files")
//@SecurityRequirement(name = "keycloak_oauth")
//@PreAuthorize("isAuthenticated()")
//public class FileController {
//
//    private final FileService fileService;
//    private final FileMapper fileMapper;
//
//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @Operation(summary = "Upload a new file to an album")
//    public ResponseEntity<FileResponse> uploadFile(@RequestParam("file") MultipartFile file,
//                                                   @RequestParam("albumId") Long albumId,
//                                                   @RequestParam("fileType") FileType fileType,
//                                                   @RequestParam("visibility") Visibility visibility) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
//        var savedFile = fileService.uploadFile(file, albumId, fileType, visibility);
//        return new ResponseEntity<>(fileMapper.toFileResponse(savedFile), HttpStatus.CREATED);
//    }
//
//    @GetMapping("/{fileId}/url")
//    @Operation(summary = "Get a temporary download URL for a file")
//    public ResponseEntity<String> getFileDownloadUrl(@PathVariable Long fileId) {
//        String url = fileService.getFileDownloadUrl(fileId);
//        return ResponseEntity.ok(url);
//    }
//
//    @DeleteMapping("/{fileId}")
//    @Operation(summary = "Delete a file (owner only)")
//    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) {
//        fileService.deleteFile(fileId);
//        return ResponseEntity.noContent().build();
//    }
//}