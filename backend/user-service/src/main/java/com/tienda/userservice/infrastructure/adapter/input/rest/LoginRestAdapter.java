package com.tienda.userservice.infrastructure.adapter.input.rest;

import com.tienda.userservice.application.port.input.LoginInputPort;
import com.tienda.userservice.infrastructure.adapter.input.rest.data.mapper.UserRestMapper;
import com.tienda.userservice.infrastructure.adapter.input.rest.data.request.LoginRequest;
import com.tienda.userservice.infrastructure.adapter.input.rest.data.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class LoginRestAdapter {

    private final LoginInputPort loginInputPort;
    private final UserRestMapper mapper;

    public LoginRestAdapter(LoginInputPort loginInputPort, UserRestMapper mapper) {
        this.loginInputPort = loginInputPort;
        this.mapper = mapper;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<UserResponse>> login(@RequestBody LoginRequest request) {
        return loginInputPort.execute(request.getEmail(), request.getPassword())
                .map(user -> ResponseEntity.ok(mapper.toResponse(user)))
                .defaultIfEmpty(ResponseEntity.status(401).build());
    }
}