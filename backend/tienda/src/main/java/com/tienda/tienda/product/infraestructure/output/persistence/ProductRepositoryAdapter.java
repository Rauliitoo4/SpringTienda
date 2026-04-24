package com.tienda.tienda.product.infraestructure.output.persistence;

import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.domain.repository.ProductRepository;
import com.tienda.tienda.product.infraestructure.output.persistence.mapper.ProductEntityMapper;
import com.tienda.tienda.product.infraestructure.output.persistence.repository.ProductJpaRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository jpaRepository;
    private final ProductEntityMapper mapper;

    public ProductRepositoryAdapter(ProductJpaRepository jpaRepository, ProductEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Product> findById(int id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Product> save(Product product) {
        return jpaRepository.save(mapper.fromDomain(product))
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Product> findAll() {
        return jpaRepository.findAll()
                .map(mapper::toDomain);
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
