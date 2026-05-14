package com.tienda.userservice.application.port.input;

import reactor.core.publisher.Mono;

public interface LoginInputPort {
    Mono<String> execute(String email, String password);
}