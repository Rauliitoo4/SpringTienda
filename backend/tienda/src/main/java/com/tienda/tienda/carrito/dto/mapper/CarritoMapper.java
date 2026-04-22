package com.tienda.tienda.carrito.dto.mapper;

import com.tienda.tienda.carrito.dto.CarritoDTO;
import com.tienda.tienda.lineacarrito.dto.mapper.LineaCarritoMapper;
import com.tienda.tienda.carrito.model.Carrito;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class CarritoMapper {

    private final LineaCarritoMapper lineaCarritoMapper;

    public CarritoMapper(LineaCarritoMapper lineaCarritoMapper){
        this.lineaCarritoMapper = lineaCarritoMapper;
    }

    public CarritoDTO toDTO (Carrito carrito){
        CarritoDTO dto = new CarritoDTO();
        dto.setId(carrito.getId());
        dto.setTotal(carrito.getTotal());

        if (carrito.getLineas() != null){
            dto.setLineas(carrito.getLineas().stream()
                    .map(lineaCarritoMapper::toDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setLineas(Collections.emptyList());
        }
        return dto;
    }
}
