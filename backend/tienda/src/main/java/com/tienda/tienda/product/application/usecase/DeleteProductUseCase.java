package com.tienda.tienda.product.application.usecase;

import com.tienda.tienda.product.domain.repository.DeleteProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeleteProductUseCase {

    private final DeleteProductRepository deleteProductRepository;

    public DeleteProductUseCase(DeleteProductRepository deleteProductRepository) {
        this.deleteProductRepository = deleteProductRepository;
    }

    public Mono<Boolean> execute(int id) {
        return deleteProductRepository.existsById(id)
                .flatMap(exists -> {
                    if (!exists) return Mono.just(false);
                    return deleteProductRepository.deleteById(id).thenReturn(true);
                });
    }
}