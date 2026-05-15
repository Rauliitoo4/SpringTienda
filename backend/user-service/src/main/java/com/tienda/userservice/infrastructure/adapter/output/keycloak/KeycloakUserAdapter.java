package com.tienda.userservice.infrastructure.adapter.output.keycloak;

import com.tienda.userservice.application.port.output.CreateKeycloakUserOutputPort;
import com.tienda.userservice.domain.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class KeycloakUserAdapter implements CreateKeycloakUserOutputPort {

    private final WebClient webClient;
    private final String realm;
    private final String clientId;
    private final String clientSecret;

    public KeycloakUserAdapter(
            @Value("${keycloak.admin.url}") String keycloakUrl,
            @Value("${keycloak.admin.realm}") String realm,
            @Value("${keycloak.admin.client-id}") String clientId,
            @Value("${keycloak.admin.client-secret}") String clientSecret) {
        this.webClient = WebClient.builder().baseUrl(keycloakUrl).build();
        this.realm = realm;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public Mono<Void> create(User user) {
        return getAdminToken()
                .flatMap(token -> createUser(token, user));
    }

    private Mono<String> getAdminToken() {
        return webClient.post()
                .uri("/realms/master/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("access_token"));
    }

    private Mono<Void> createUser(String token, User user) {
        Map<String, Object> keycloakUser = Map.of(
                "username", user.getUsername(),
                "email", user.getEmail(),
                "firstName", user.getName(),
                "lastName", user.getLastname(),
                "enabled", true,
                "emailVerified", true,
                "credentials", List.of(Map.of(
                        "type", "password",
                        "value", user.getPassword(),
                        "temporary", false
                ))
        );

        return webClient.post()
                .uri("/admin/realms/" + realm + "/users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(keycloakUser)
                .retrieve()
                .bodyToMono(Void.class);
    }
}