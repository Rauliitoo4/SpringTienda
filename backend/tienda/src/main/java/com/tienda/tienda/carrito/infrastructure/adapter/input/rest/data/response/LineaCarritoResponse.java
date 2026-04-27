package com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.response;

import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.response.ProductResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineaCarritoResponse {
    private Integer id;
    private double subtotal;
    private int quantity;
    private Integer carritoId;
    private Integer productId;
    private ProductResponse product;
}
