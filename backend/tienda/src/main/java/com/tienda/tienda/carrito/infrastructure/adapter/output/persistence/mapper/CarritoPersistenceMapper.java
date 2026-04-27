package com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.mapper;

import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.entity.CarritoEntity;
import org.springframework.stereotype.Component;

@Component
public class CarritoPersistenceMapper {

    public CarritoEntity toEntity(Carrito carrito) {
        CarritoEntity entity = new CarritoEntity();
        entity.setId(carrito.getId());
        entity.setTotal(carrito.getTotal());
        return entity;
    }

    public Carrito toDomain(CarritoEntity entity) {
        Carrito carrito = new Carrito();
        carrito.setId(entity.getId());
        carrito.setTotal(entity.getTotal());
        return carrito;
    }
}