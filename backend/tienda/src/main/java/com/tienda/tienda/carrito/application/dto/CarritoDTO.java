package com.tienda.tienda.carrito.application.dto;

import com.tienda.tienda.lineacarrito.application.dto.LineaCarritoDTO;
import lombok.Data;
import java.util.List;


@Data
public class CarritoDTO {
    private int id;
    private double total;
    private List<LineaCarritoDTO> lineas;
}
