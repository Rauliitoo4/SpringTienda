package com.tienda.tienda.dto;

import lombok.Data;
import java.util.List;


@Data
public class CarritoDTO {
    private int id;
    private double total;
    private List<LineaCarritoDTO> lineas;
}
