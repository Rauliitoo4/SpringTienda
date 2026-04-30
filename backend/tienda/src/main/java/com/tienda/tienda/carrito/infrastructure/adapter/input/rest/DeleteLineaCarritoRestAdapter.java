package com.tienda.tienda.carrito.infrastructure.adapter.input.rest;

import com.tienda.tienda.carrito.application.port.input.DeleteLineaCarritoInputPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/lineas")
public class DeleteLineaCarritoRestAdapter {

    private final DeleteLineaCarritoInputPort deleteLineaCarritoInputPort;

    public DeleteLineaCarritoRestAdapter(DeleteLineaCarritoInputPort deleteLineaCarritoInputPort) {
        this.deleteLineaCarritoInputPort = deleteLineaCarritoInputPort;
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteLinea(@PathVariable int id) {
        return deleteLineaCarritoInputPort.execute(id)
                .map(deleted -> deleted
                        ? ResponseEntity.<Void>noContent().build()
                        : ResponseEntity.<Void>notFound().build());
    }
}