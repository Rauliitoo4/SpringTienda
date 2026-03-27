package com.tienda.tienda.dto;

import lombok.Data;

@Data
public class LineaCarritoDTO {
    private int id;
    private double subtotal;
    private ProductDTO producto;
    private int cantidad;
}
