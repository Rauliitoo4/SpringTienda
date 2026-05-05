package com.tienda.userservice.application.port.input;

import com.tienda.tienda.user.domain.model.User;
import reactor.core.publisher.Mono;

public interface UpdateUserInputPort {
    Mono<User> execute(int id, User user);
}
