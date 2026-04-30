package com.tienda.tienda.user.infrastructure.adapter.input.rest;

import com.tienda.tienda.user.application.port.input.GetUserInputPort;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.mapper.UserRestMapper;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/usuarios")
public class GetUserRestAdapter {

    private final GetUserInputPort getUserInputPort;
    private final UserRestMapper mapper;

    public GetUserRestAdapter(GetUserInputPort getUserInputPort, UserRestMapper mapper) {
        this.getUserInputPort = getUserInputPort;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> getUserById(@PathVariable int id) {
        return getUserInputPort.execute(id)
                .map(user -> ResponseEntity.ok(mapper.toResponse(user)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Flux<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(getUserInputPort.executeAll()
                .map(mapper::toResponse));
    }
}