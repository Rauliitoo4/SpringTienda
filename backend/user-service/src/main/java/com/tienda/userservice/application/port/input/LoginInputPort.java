package com.tienda.userservice.application.port.input;

import com.tienda.userservice.domain.model.User;
import reactor.core.publisher.Mono;

public interface LoginInputPort {
    Mono<User> execute(String email, String password);
}