package com.example.social.Config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Social Platform API", version = "v1.0", description = "API documentation for the social media platform."),
        security = @SecurityRequirement(name = "keycloak_oauth")
)
@SecurityScheme(
        name = "keycloak_oauth",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        authorizationUrl = "http://localhost:9090/realms/social-realm/protocol/openid-connect/auth",
                        tokenUrl = "http://localhost:9090/realms/social-realm/protocol/openid-connect/token",
                        scopes = {
                                @OAuthScope(name = "openid", description = "Standard OpenID Connect scope"),
                                @OAuthScope(name = "profile", description = "Access to user profile information"),
                                @OAuthScope(name = "email", description = "Access to user's email address")
                        }
                )
        )
)
public class OpenApiConfig {
}

