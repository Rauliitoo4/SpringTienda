package com.tienda.userservice.infrastructure.adapter.input.rest;

import com.tienda.tienda.user.application.port.input.DeleteUserInputPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/usuarios")
public class DeleteUserRestAdapter {

    private final DeleteUserInputPort deleteUserInputPort;

    public DeleteUserRestAdapter(DeleteUserInputPort deleteUserInputPort) {
        this.deleteUserInputPort = deleteUserInputPort;
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable int id) {
        return deleteUserInputPort.execute(id)
                .map(deleted -> deleted
                        ? ResponseEntity.<Void>noContent().build()
                        : ResponseEntity.<Void>notFound().build());
    }
}