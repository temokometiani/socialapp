package com.example.social.Controller;
//import com.example.social.Mapper.AlbumMapper;
import com.example.social.Mapper.AlbumAccessMapper;
import com.example.social.Mapper.AlbumMapper;
import com.example.social.Mapper.FileMapper;
import com.example.social.Service.AlbumService;
//import com.example.social.Mapper.AlbumMapper;
import com.example.social.model.DTO.request.AlbumAccessRequest;
import com.example.social.model.DTO.request.AlbumCreateRequest;
import com.example.social.model.DTO.request.AlbumUpdateRequest;
import com.example.social.model.DTO.response.AlbumResponse;
import com.example.social.model.DTO.response.FileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.social.Service.FileService;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
@Tag(name = "4. Albums", description = "APIs for managing albums")
//@SecurityRequirement(name = "keycloak_oauth")
//@PreAuthorize("isAuthenticated()")
public class AlbumController {

    private final AlbumService albumService;
    private final FileService fileService;
    private final AlbumMapper albumMapper;
    private final FileMapper fileMapper;
    private final AlbumAccessMapper albumAccessMapper;


    @PostMapping
    @Operation(summary = "Create a new album")
    public ResponseEntity<AlbumResponse> createAlbum(@Valid @RequestBody AlbumCreateRequest request) {
        var album = albumService.createAlbum(request);
        return new ResponseEntity<>(albumMapper.toDto(album), HttpStatus.CREATED);

    }

    @GetMapping
    @Operation(summary = "Get all albums visible to me")
    public ResponseEntity<List<AlbumResponse>> getVisibleAlbums() {
        List<AlbumResponse> albums = albumService.getVisibleAlbums().stream()
                .map(albumMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(albums);
    }

    @GetMapping("/{albumId}/files")
    @Operation(summary = "Get all visible files within a specific album")
    public ResponseEntity<List<FileResponse>> getVisibleFilesForAlbum(@PathVariable Long albumId) {
        List<FileResponse> files = fileService.getVisibleFilesForAlbum(albumId).stream()
                .map(fileMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(files);
    }

    @PutMapping("/{albumId}")
    @Operation(summary = "Update an album's name or visibility (owner only)")
    public ResponseEntity<AlbumResponse> updateAlbum(@PathVariable Long albumId, @Valid @RequestBody AlbumUpdateRequest request) {
        var album = albumService.updateAlbum(albumId, request);
        return ResponseEntity.ok(albumMapper.toDto(album));
    }

    @PostMapping("/{albumId}/access")
    @Operation(summary = "Grant access to an album for another user (owner only)")
    public ResponseEntity<String> grantAlbumAccess(@PathVariable Long albumId, @Valid @RequestBody AlbumAccessRequest request) {
        albumService.grantAlbumAccess(albumId, request);
        return ResponseEntity.ok("Access granted successfully.");
    }

    @DeleteMapping("/{albumId}/access/{userId}")
    @Operation(summary = "Revoke access to an album from a user (owner only)")
    public ResponseEntity<Void> revokeAlbumAccess(@PathVariable Long albumId, @PathVariable Long userId) {
        albumService.revokeAlbumAccess(albumId, userId);
        return ResponseEntity.noContent().build();
    }
}