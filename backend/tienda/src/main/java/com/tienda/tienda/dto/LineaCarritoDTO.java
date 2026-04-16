package com.tienda.tienda.dto;

import lombok.Data;

@Data
public class LineaCarritoDTO {
    private int id;
    private double subtotal;
    private ProductDTO product;
    private int quantity;
    private int carritoId;
}
