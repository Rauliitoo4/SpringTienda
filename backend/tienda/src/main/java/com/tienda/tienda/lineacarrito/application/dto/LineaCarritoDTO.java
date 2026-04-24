package com.tienda.tienda.lineacarrito.application.dto;

import com.tienda.tienda.product.application.dto.ProductResponse;
import lombok.Data;

@Data
public class LineaCarritoDTO {
    private int id;
    private double subtotal;
    private ProductResponse product;
    private int quantity;
    private int carritoId;
}
