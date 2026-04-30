package com.tienda.tienda.user.application.port.input;

import com.tienda.tienda.user.domain.model.User;
import reactor.core.publisher.Mono;

public interface CreateUserInputPort {
    Mono<User> execute(User user);
}
