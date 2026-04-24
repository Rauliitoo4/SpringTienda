package com.tienda.tienda.product.infraestructure.output.persistence.repository;

import com.tienda.tienda.product.infraestructure.output.persistence.entity.ProductEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductJpaRepository extends ReactiveCrudRepository<ProductEntity, Integer> {

}
