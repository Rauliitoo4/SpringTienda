package com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.mapper;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.entity.LineaCarritoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LineaCarritoPersistenceMapper {
    LineaCarritoEntity toEntity(LineaCarrito linea);
    @Mapping(target = "product", ignore = true)
    LineaCarrito toDomain(LineaCarritoEntity entity);
}