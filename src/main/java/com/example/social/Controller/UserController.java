package com.example.social.Controller;


import com.example.social.Service.UserService;
import com.example.social.Mapper.UserMapper;
import com.example.social.model.DTO.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "2. Users", description = "APIs for user management and profiles")
//@PreAuthorize("isAuthenticated()")   //BE CAREFUL
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    @Operation(summary = "Get current user's profile", description = "Returns the profile information of the currently authenticated user.")
    public ResponseEntity<UserResponse> getCurrentUser() {
        return ResponseEntity.ok(userMapper.toDto(userService.getCurrentUser()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user profile by ID", description = "Returns the profile of a user by their database ID.")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userMapper.toDto(userService.findUserById(id)));
    }

    @DeleteMapping("/me")
    @Operation(summary = "Delete current user's account", description = "Permanently deletes the account of the currently authenticated user from the system and Keycloak.")
    @ApiResponse(responseCode = "204", description = "User account deleted successfully.")
    public ResponseEntity<Void> deleteCurrentUser() {
        userService.deleteCurrentUser();
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
    @Operation(summary = "Search for users", description = "Searches for users by their first or last name (case-insensitive).")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam String query) {
        List<UserResponse> users = userService.searchUsers(query).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
}
