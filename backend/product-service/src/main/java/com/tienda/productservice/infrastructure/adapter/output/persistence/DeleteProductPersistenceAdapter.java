package com.tienda.productservice.infrastructure.adapter.output.persistence;

import com.tienda.productservice.application.port.output.DeleteProductOutputPort;
import com.tienda.productservice.infrastructure.adapter.output.persistence.repository.ProductR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class DeleteProductPersistenceAdapter implements DeleteProductOutputPort {

    private final ProductR2dbcRepository r2dbcRepository;

    public DeleteProductPersistenceAdapter(ProductR2dbcRepository r2dbcRepository) {
        this.r2dbcRepository = r2dbcRepository;
    }

    @Override
    public Mono<Boolean> existsById(int id) {
        return r2dbcRepository.existsById(id);
    }

    @Override
    public Mono<Void> deleteById(int id) {
        return r2dbcRepository.deleteById(id);
    }
}
