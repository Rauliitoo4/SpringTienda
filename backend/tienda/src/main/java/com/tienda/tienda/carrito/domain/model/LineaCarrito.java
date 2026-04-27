package com.tienda.tienda.carrito.domain.model;

import com.tienda.tienda.product.domain.model.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LineaCarrito {

    private Integer id;
    private double subtotal;
    private int quantity;
    private Integer carritoId;
    private Integer productId;
    private Product product;
}
