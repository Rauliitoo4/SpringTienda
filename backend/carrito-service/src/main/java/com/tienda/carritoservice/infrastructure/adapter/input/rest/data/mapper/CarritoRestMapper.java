package com.tienda.carritoservice.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.carritoservice.domain.model.Carrito;
import com.tienda.carrito.model.CarritoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {LineaCarritoRestMapper.class})
public interface CarritoRestMapper {
    CarritoResponse toResponse(Carrito carrito);
}