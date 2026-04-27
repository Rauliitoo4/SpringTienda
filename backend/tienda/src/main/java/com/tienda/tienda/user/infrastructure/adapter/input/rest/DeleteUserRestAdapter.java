package com.tienda.tienda.user.infrastructure.adapter.input.rest;

import com.tienda.tienda.user.application.usecase.DeleteUserUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/usuarios")
public class DeleteUserRestAdapter {

    private final DeleteUserUseCase deleteUserUseCase;

    public DeleteUserRestAdapter(DeleteUserUseCase deleteUserUseCase) {
        this.deleteUserUseCase = deleteUserUseCase;
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable int id) {
        return deleteUserUseCase.execute(id)
                .map(deleted -> deleted
                        ? ResponseEntity.<Void>noContent().build()
                        : ResponseEntity.<Void>notFound().build());
    }
}