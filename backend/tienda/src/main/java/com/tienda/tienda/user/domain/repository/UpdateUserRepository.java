package com.tienda.tienda.user.domain.repository;

import com.tienda.tienda.user.domain.model.User;
import reactor.core.publisher.Mono;

public interface UpdateUserRepository {
    Mono<User> save(User user);
}
