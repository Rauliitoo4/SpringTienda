package com.tienda.carritoservice.domain.model;

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
    private Size size;
    private Integer carritoId;
    private Integer productId;
}
