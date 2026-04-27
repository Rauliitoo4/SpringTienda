package com.tienda.tienda.carrito.infrastructure.adapter.input.rest;

import com.tienda.tienda.carrito.application.usecase.DeleteLineaCarritoUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/lineas")
public class DeleteLineaCarritoRestAdapter {

    private final DeleteLineaCarritoUseCase deleteLineaCarritoUseCase;

    public DeleteLineaCarritoRestAdapter(DeleteLineaCarritoUseCase deleteLineaCarritoUseCase) {
        this.deleteLineaCarritoUseCase = deleteLineaCarritoUseCase;
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteLinea(@PathVariable int id) {
        return deleteLineaCarritoUseCase.execute(id)
                .map(deleted -> deleted
                        ? ResponseEntity.<Void>noContent().build()
                        : ResponseEntity.<Void>notFound().build());
    }
}