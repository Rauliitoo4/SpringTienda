package com.tienda.productservice.infrastructure.adapter.output.persistence;

import com.tienda.productservice.domain.model.Product;
import com.tienda.productservice.application.port.output.CreateProductOutputPort;
import com.tienda.productservice.infrastructure.adapter.output.persistence.mapper.ProductPersistenceMapper;
import com.tienda.productservice.infrastructure.adapter.output.persistence.repository.ProductR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CreateProductPersistenceAdapter implements CreateProductOutputPort {

    private final ProductR2dbcRepository r2dbcRepository;
    private final ProductPersistenceMapper mapper;

    public CreateProductPersistenceAdapter(ProductR2dbcRepository r2dbcRepository, ProductPersistenceMapper mapper) {
        this.r2dbcRepository = r2dbcRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Product> save(Product product) {
        return r2dbcRepository.save(mapper.toEntity(product))
                .map(mapper::toDomain);
    }
}
