package com.tienda.tienda.product.infrastructure.adapter.output.persistence;

import com.tienda.tienda.product.domain.repository.DeleteProductRepository;
import com.tienda.tienda.product.infrastructure.adapter.output.persistence.repository.ProductJpaRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class DeleteProductPersistenceAdapter implements DeleteProductRepository {

    private final ProductJpaRepository jpaRepository;

    public DeleteProductPersistenceAdapter(ProductJpaRepository jpaRepository) {
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
