package com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.repository;

import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.entity.LineaCarritoEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface LineaCarritoR2dbcRepository extends ReactiveCrudRepository<LineaCarritoEntity, Integer> {

    @Query("SELECT * FROM lineas_carrito WHERE carrito_id = :carritoId")
    Flux<LineaCarritoEntity> findByCarritoId(int carritoId);
}