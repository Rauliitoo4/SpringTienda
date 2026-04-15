package com.tienda.tienda.dto.mapper;

import com.tienda.tienda.dto.CarritoDTO;
import com.tienda.tienda.model.Carrito;
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
