package com.tienda.carritoservice.infrastructure.adapter.output.persistence.mapper;

import com.tienda.carritoservice.domain.model.LineaCarrito;
import com.tienda.carritoservice.infrastructure.adapter.output.persistence.entity.LineaCarritoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LineaCarritoPersistenceMapper {
    @Mapping(target = "size", expression = "java(linea.getSize() != null ? linea.getSize().name() : null)")
    LineaCarritoEntity toEntity(LineaCarrito linea);

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "size", expression = "java(entity.getSize() != null ? com.tienda.carritoservice.domain.model.Size.valueOf(entity.getSize()) : null)")
    LineaCarrito toDomain(LineaCarritoEntity entity);
}