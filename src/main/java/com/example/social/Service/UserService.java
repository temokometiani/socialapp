package com.example.social.Service;

import com.example.social.Repository.UserRepository;
import com.example.social.exception.ApiException;
import com.example.social.exception.UserNotFoundException;
import com.example.social.model.DTO.request.RegisterRequest;
import com.example.social.model.entity.User;
import com.example.social.util.SecurityUtils;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for user-related business logic, including registration,
 * profile retrieval, and searching.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Keycloak keycloakAdminClient;

    @Value("${keycloak.realm}")
    private String realm;

    @Transactional
    public void registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException("User with this email already exists.", HttpStatus.BAD_REQUEST);
        }

        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setUsername(request.getEmail());
        keycloakUser.setEmail(request.getEmail());
        keycloakUser.setFirstName(request.getName());
        keycloakUser.setLastName(request.getLastName());
        keycloakUser.setEnabled(true);
        keycloakUser.setEmailVerified(false); // Will be verified after 2FA

        Response response = keycloakAdminClient.realm(realm).users().create(keycloakUser);
        if (response.getStatus() != 201) {
            throw new ApiException("Could not create user in Keycloak. Status: " + response.getStatusInfo().getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String keycloakUserId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(request.getPassword());
        keycloakAdminClient.realm(realm).users().get(keycloakUserId).resetPassword(passwordCred);

        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setLastName(request.getLastName());
        newUser.setEmail(request.getEmail());
        newUser.setDob(request.getDob());
        newUser.setKeycloakUserId(keycloakUserId);

        userRepository.save(newUser);
    }

    public User getCurrentUser() {
//       String keycloakId = SecurityUtils.getCurrentUserKeycloakId();
       return userRepository.findById(12L).get();
    }


    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found."));
    }


    public List<User> searchUsers(String query) {
        return userRepository.findByNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query);
    }

    @Transactional
    public void deleteCurrentUser() {
        User currentUser = getCurrentUser();

        Response response = keycloakAdminClient.realm(realm).users().delete(currentUser.getKeycloakUserId());

        if (response.getStatus() != 204) {
            throw new ApiException("Could not delete user from Keycloak. Status: " + response.getStatusInfo().getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        userRepository.delete(currentUser);
    }

}