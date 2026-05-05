package com.tienda.carritoservice.infrastructure.adapter.output.persistence.mapper;

import com.tienda.carritoservice.domain.model.Carrito;
import com.tienda.carritoservice.infrastructure.adapter.output.persistence.entity.CarritoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarritoPersistenceMapper {
    CarritoEntity toEntity(Carrito carrito);
    @Mapping(target = "lineas", ignore = true)
    Carrito toDomain(CarritoEntity entity);
}