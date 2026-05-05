package com.tienda.carritoservice.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.mapper.LineaCarritoRestMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.response.CarritoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {LineaCarritoRestMapper.class})
public interface CarritoRestMapper {
    CarritoResponse toResponse(Carrito carrito);
}