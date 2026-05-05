package com.tienda.productservice.application.usecase;

import com.tienda.productservice.application.port.input.DeleteProductInputPort;
import com.tienda.productservice.application.port.output.DeleteProductOutputPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeleteProductUseCase implements DeleteProductInputPort {

    private final DeleteProductOutputPort deleteProductOutputPort;

    public DeleteProductUseCase(DeleteProductOutputPort deleteProductOutputPort) {
        this.deleteProductOutputPort = deleteProductOutputPort;
    }

    public Mono<Boolean> execute(int id) {
        return deleteProductOutputPort.existsById(id)
                .flatMap(exists -> {
                    if (!exists) return Mono.just(false);
                    return deleteProductOutputPort.deleteById(id).thenReturn(true);
                });
    }
}