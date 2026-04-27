package com.tienda.tienda.user.domain.repository;

import reactor.core.publisher.Mono;

public interface DeleteUserRepository {
    Mono<Boolean> existsById(int id);
    Mono<Void> deleteById(int id);
}
