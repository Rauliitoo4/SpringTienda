package com.tienda.tienda.repository;

import com.tienda.tienda.model.LineaCarrito;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface LineaCarritoRepository extends ReactiveCrudRepository<LineaCarrito, Integer>{

    Flux<LineaCarrito> findByCarritoId(Integer carritoId);
}
