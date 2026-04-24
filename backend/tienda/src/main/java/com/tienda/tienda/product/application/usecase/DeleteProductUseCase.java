package com.tienda.tienda.product.application.usecase;

import com.tienda.tienda.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeleteProductUseCase {

    private final ProductRepository productRepository;

    public DeleteProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Mono<Boolean> execute(int id) {
        return productRepository.existsById(id)
                .flatMap(exists -> {
                    if (!exists) return Mono.just(false);
                    return productRepository.deleteById(id).thenReturn(true);
                });
    }
}