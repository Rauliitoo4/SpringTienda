package com.tienda.tienda.user.domain.repository;

import com.tienda.tienda.user.domain.model.User;
import reactor.core.publisher.Mono;

public interface CreateUserRepository {
    Mono<User> save(User user);
}
