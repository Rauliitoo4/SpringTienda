package com.tienda.userservice.application.service;

import com.tienda.tienda.user.application.port.output.UserFavoritoOutputPort;
import com.tienda.tienda.user.domain.model.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class FavoritoLoader {

    private final UserFavoritoOutputPort userFavoritoOutputPort;

    public FavoritoLoader(UserFavoritoOutputPort userFavoritoOutputPort) {
        this.userFavoritoOutputPort = userFavoritoOutputPort;
    }

    public Mono<User> loadFavoritos(User user) {
        return userFavoritoOutputPort.findProductIdsByUserId(user.getId())
                .collectList()
                .doOnNext(user::setFavoritoIds)
                .thenReturn(user);
    }
}
