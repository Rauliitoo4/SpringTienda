package com.tienda.userservice.infrastructure.adapter.input.rest;

import com.tienda.user.model.LoginResponse;
import com.tienda.userservice.application.port.input.LoginInputPort;
import com.tienda.user.model.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class LoginRestAdapter {

    private final LoginInputPort loginInputPort;

    public LoginRestAdapter(LoginInputPort loginInputPort) {
        this.loginInputPort = loginInputPort;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> login(@RequestBody LoginRequest request) {
        return loginInputPort.execute(request.getEmail(), request.getPassword())
                .map(token -> {
                    LoginResponse response = new LoginResponse();
                    response.setToken(token);
                    return ResponseEntity.ok(response);
                })
                .defaultIfEmpty(ResponseEntity.status(401).build());
    }
}