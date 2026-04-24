package com.tienda.tienda.product.infrastructure.adapter.input.rest;

import com.tienda.tienda.product.application.usecase.DeleteProductUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/productos")
public class DeleteProductRestAdapter {

    private final DeleteProductUseCase deleteProductUseCase;

    public DeleteProductRestAdapter(DeleteProductUseCase deleteProductUseCase) {
        this.deleteProductUseCase = deleteProductUseCase;
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable int id) {
        return deleteProductUseCase.execute(id)
                .map(deleted -> deleted
                        ? ResponseEntity.<Void>noContent().build()
                        : ResponseEntity.<Void>notFound().build());
    }
}
