package com.tienda.tienda.lineacarrito.application.dto;

import com.tienda.tienda.product.application.dto.ProductDTO;
import lombok.Data;

@Data
public class LineaCarritoDTO {
    private int id;
    private double subtotal;
    private ProductDTO product;
    private int quantity;
    private int carritoId;
}
