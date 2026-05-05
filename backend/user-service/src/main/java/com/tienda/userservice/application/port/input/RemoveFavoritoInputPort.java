package com.tienda.userservice.application.port.input;

import com.tienda.tienda.user.domain.model.User;
import reactor.core.publisher.Mono;

public interface RemoveFavoritoInputPort {
    Mono<User> execute(int userId, int productId);
}
