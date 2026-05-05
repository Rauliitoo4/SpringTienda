package com.tienda.userservice.adapter.input.rest;

import com.tienda.tienda.user.application.port.input.RemoveFavoritoInputPort;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.mapper.UserRestMapper;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/usuarios")
public class RemoveFavoritoRestAdapter {

    private final RemoveFavoritoInputPort removeFavoritoInputPort;
    private final UserRestMapper userRestMapper;

    public RemoveFavoritoRestAdapter(RemoveFavoritoInputPort removeFavoritoInputPort, UserRestMapper userRestMapper) {
        this.removeFavoritoInputPort = removeFavoritoInputPort;
        this.userRestMapper = userRestMapper;
    }

    @DeleteMapping("/{userId}/favoritos/{productId}")
    public Mono<ResponseEntity<UserResponse>> removeFavorito(@PathVariable int userId, @PathVariable int productId) {
        return removeFavoritoInputPort.execute(userId, productId)
                .map(user -> ResponseEntity.ok(userRestMapper.toResponse(user)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
