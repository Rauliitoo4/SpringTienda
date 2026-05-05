package com.tienda.productservice.infrastructure.adapter.output.persistence.repository;

import com.tienda.productservice.infrastructure.adapter.output.persistence.entity.ProductEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductR2dbcRepository extends ReactiveCrudRepository<ProductEntity, Integer> {

}
