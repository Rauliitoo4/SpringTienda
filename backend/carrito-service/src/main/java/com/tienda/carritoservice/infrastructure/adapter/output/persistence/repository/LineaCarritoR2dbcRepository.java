package com.tienda.carritoservice.infrastructure.adapter.output.persistence.repository;

import com.tienda.carritoservice.domain.model.LineaCarrito;
import com.tienda.carritoservice.infrastructure.adapter.output.persistence.entity.LineaCarritoEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LineaCarritoR2dbcRepository extends ReactiveCrudRepository<LineaCarritoEntity, Integer> {

    @Query("SELECT * FROM lineas_carrito WHERE carrito_id = :carritoId")
    Flux<LineaCarritoEntity> findByCarritoId(int carritoId);

    @Query("SELECT * FROM lineas_carrito WHERE carrito_id = :carritoId AND product_id = :productId AND size = :size")
    Mono<LineaCarritoEntity> findByCarritoIdAndProductIdAndSize(int carritoId, int productId, String size);
}