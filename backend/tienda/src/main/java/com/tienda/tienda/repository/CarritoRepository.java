package com.tienda.tienda.repository;

import com.tienda.tienda.model.Carrito;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CarritoRepository extends ReactiveCrudRepository<Carrito, Integer>{
    
}
