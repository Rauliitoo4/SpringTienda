package com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.response.LineaCarritoResponse;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.mapper.ProductRestMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductRestMapper.class})
public interface LineaCarritoRestMapper {
    LineaCarritoResponse toResponse(LineaCarrito lineaCarrito);
}