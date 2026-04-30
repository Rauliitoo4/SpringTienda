package com.tienda.tienda.user.infrastructure.adapter.input.rest;

import com.tienda.tienda.user.application.port.input.UpdateUserInputPort;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.mapper.UserRestMapper;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.request.UserRequest;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/usuarios")
public class UpdateUserRestAdapter {

    private final UpdateUserInputPort updateUserUseCase;
    private final UserRestMapper mapper;

    public UpdateUserRestAdapter(UpdateUserInputPort updateUserUseCase, UserRestMapper mapper) {
        this.updateUserUseCase = updateUserUseCase;
        this.mapper = mapper;
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> updateUser(@PathVariable int id,
                                                         @RequestBody UserRequest request) {
        return updateUserUseCase.execute(id, mapper.toDomain(request))
                .map(user -> ResponseEntity.ok(mapper.toResponse(user)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}