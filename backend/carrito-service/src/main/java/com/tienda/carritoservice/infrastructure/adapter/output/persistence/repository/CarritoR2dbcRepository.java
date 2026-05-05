package com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.repository;

import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.entity.CarritoEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CarritoR2dbcRepository extends ReactiveCrudRepository<CarritoEntity, Integer> {
}