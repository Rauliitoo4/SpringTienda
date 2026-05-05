package com.tienda.userservice.adapter.input.rest;

import com.tienda.tienda.user.application.port.input.CreateUserInputPort;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.mapper.UserRestMapper;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.request.UserRequest;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/usuarios")
public class CreateUserRestAdapter {

    private final CreateUserInputPort createUserInputPort;
    private final UserRestMapper mapper;

    public CreateUserRestAdapter(CreateUserInputPort createUserInputPort, UserRestMapper mapper) {
        this.createUserInputPort = createUserInputPort;
        this.mapper = mapper;
    }

    @PostMapping
    public Mono<ResponseEntity<UserResponse>> createUser(@RequestBody UserRequest request) {
        return createUserInputPort.execute(mapper.toDomain(request))
                .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(user)));
    }
}