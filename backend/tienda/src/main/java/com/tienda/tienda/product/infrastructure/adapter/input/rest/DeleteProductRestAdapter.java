package com.tienda.tienda.product.infrastructure.adapter.input.rest;

import com.tienda.tienda.product.application.port.input.DeleteProductInputPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/productos")
public class DeleteProductRestAdapter {

    private final DeleteProductInputPort deleteProductInputPort;

    public DeleteProductRestAdapter(DeleteProductInputPort deleteProductInputPort) {
        this.deleteProductInputPort = deleteProductInputPort;
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable int id) {
        return deleteProductInputPort.execute(id)
                .map(deleted -> deleted
                        ? ResponseEntity.<Void>noContent().build()
                        : ResponseEntity.<Void>notFound().build());
    }
}
