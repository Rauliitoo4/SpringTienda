package com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.response.CarritoResponse;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.response.LineaCarritoResponse;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;

@Component
public class CarritoRestMapper {

    private final LineaCarritoRestMapper lineaCarritoRestMapper;

    public CarritoRestMapper(LineaCarritoRestMapper lineaCarritoRestMapper) {
        this.lineaCarritoRestMapper = lineaCarritoRestMapper;
    }

    public CarritoResponse toResponse(Carrito carrito) {
        CarritoResponse response = new CarritoResponse();
        response.setId(carrito.getId());
        response.setTotal(carrito.getTotal());

        List<LineaCarritoResponse> lineas = carrito.getLineas() == null
                ? Collections.emptyList()
                : carrito.getLineas().stream()
                  .map(lineaCarritoRestMapper::toResponse)
                  .toList();

        response.setLineas(lineas);
        return response;
    }
}