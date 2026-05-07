package com.tienda.carritoservice.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.carritoservice.domain.model.LineaCarrito;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.data.response.LineaCarritoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LineaCarritoRestMapper {
    LineaCarritoResponse toResponse(LineaCarrito lineaCarrito);
}