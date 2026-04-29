package com.tienda.tienda.product.infrastructure.adapter.output.persistence;

import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.domain.repository.UpdateProductRepository;
import com.tienda.tienda.product.infrastructure.adapter.output.persistence.mapper.ProductPersistenceMapper;
import com.tienda.tienda.product.infrastructure.adapter.output.persistence.repository.ProductR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UpdateProductPersistenceAdapter implements UpdateProductRepository {

    private final ProductR2dbcRepository jpaRepository;
    private final ProductPersistenceMapper mapper;

    public UpdateProductPersistenceAdapter(ProductR2dbcRepository jpaRepository,
                                           ProductPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Product> save(Product product) {
        return jpaRepository.save(mapper.toEntity(product))
                .map(mapper::toDomain);
    }
}
