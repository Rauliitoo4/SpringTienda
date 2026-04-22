package com.tienda.tienda.carrito.dto;

import com.tienda.tienda.lineacarrito.dto.LineaCarritoDTO;
import lombok.Data;
import java.util.List;


@Data
public class CarritoDTO {
    private int id;
    private double total;
    private List<LineaCarritoDTO> lineas;
}
