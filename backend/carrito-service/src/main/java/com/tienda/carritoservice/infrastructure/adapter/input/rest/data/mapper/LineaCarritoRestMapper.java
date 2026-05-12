package com.tienda.carritoservice.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.carritoservice.domain.model.LineaCarrito;
import com.tienda.carrito.model.LineaCarritoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LineaCarritoRestMapper {
    LineaCarritoResponse toResponse(LineaCarrito lineaCarrito);
}