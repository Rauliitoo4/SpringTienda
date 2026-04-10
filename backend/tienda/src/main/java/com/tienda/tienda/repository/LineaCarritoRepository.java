package com.tienda.tienda.repository;

import com.tienda.tienda.model.LineaCarrito;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface LineaCarritoRepository extends ReactiveCrudRepository<LineaCarrito, Integer>{
    
}
