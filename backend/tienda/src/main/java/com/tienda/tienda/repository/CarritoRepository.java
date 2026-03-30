package com.tienda.tienda.repository;

import com.tienda.tienda.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoRepository extends JpaRepository<Carrito, Integer>{
    
}
