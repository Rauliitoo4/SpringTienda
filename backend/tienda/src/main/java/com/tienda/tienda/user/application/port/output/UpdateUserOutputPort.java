package com.tienda.tienda.user.application.port.output;

import com.tienda.tienda.user.domain.model.User;
import reactor.core.publisher.Mono;

public interface UpdateUserOutputPort {
    Mono<User> save(User user);
}
