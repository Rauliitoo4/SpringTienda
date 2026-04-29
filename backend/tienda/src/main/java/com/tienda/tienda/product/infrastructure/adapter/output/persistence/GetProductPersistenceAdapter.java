package com.tienda.tienda.product.infrastructure.adapter.output.persistence;

import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.domain.repository.GetProductRepository;
import com.tienda.tienda.product.infrastructure.adapter.output.persistence.mapper.ProductPersistenceMapper;
import com.tienda.tienda.product.infrastructure.adapter.output.persistence.repository.ProductR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class GetProductPersistenceAdapter implements GetProductRepository {

    private final ProductR2dbcRepository jpaRepository;
    private final ProductPersistenceMapper mapper;

    public GetProductPersistenceAdapter(ProductR2dbcRepository jpaRepository,
                                        ProductPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Product> findById(int id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Product> findAll() {
        return jpaRepository.findAll()
                .map(mapper::toDomain);
    }
}
