package com.tienda.tienda.lineacarrito.dto;

import com.tienda.tienda.product.dto.ProductDTO;
import lombok.Data;

@Data
public class LineaCarritoDTO {
    private int id;
    private double subtotal;
    private ProductDTO product;
    private int quantity;
    private int carritoId;
}
