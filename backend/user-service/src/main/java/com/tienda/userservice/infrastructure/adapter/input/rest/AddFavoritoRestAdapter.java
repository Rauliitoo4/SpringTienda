package com.tienda.userservice.infrastructure.adapter.input.rest;

import com.tienda.userservice.application.port.input.AddFavoritoInputPort;
import com.tienda.userservice.infrastructure.adapter.input.rest.data.mapper.UserRestMapper;
import com.tienda.user.model.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/usuarios")
public class AddFavoritoRestAdapter {

    private final AddFavoritoInputPort addFavoritoInputPort;
    private final UserRestMapper userRestMapper;

    public AddFavoritoRestAdapter(AddFavoritoInputPort addFavoritoInputPort, UserRestMapper userRestMapper) {
        this.addFavoritoInputPort = addFavoritoInputPort;
        this.userRestMapper = userRestMapper;
    }

    @PostMapping("/{userId}/favoritos/{productId}")
    public Mono<ResponseEntity<UserResponse>> addFavorito(@PathVariable int userId, @PathVariable int productId) {
        return addFavoritoInputPort.execute(userId, productId)
                .map(user -> ResponseEntity.ok(userRestMapper.toResponse(user)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
