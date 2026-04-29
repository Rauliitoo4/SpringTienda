package com.tienda.tienda.product.infrastructure.adapter.output.persistence;

import com.tienda.tienda.product.domain.repository.DeleteProductRepository;
import com.tienda.tienda.product.infrastructure.adapter.output.persistence.repository.ProductR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class DeleteProductPersistenceAdapter implements DeleteProductRepository {

    private final ProductR2dbcRepository jpaRepository;

    public DeleteProductPersistenceAdapter(ProductR2dbcRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Mono<Boolean> existsById(int id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public Mono<Void> deleteById(int id) {
        return jpaRepository.deleteById(id);
    }
}
