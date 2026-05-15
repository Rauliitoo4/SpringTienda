package com.tienda.userservice.application.port.output;

import com.tienda.userservice.domain.model.User;
import reactor.core.publisher.Mono;

public interface CreateKeycloakUserOutputPort {
    Mono<Void> create(User user);
}