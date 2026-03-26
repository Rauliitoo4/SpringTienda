package com.tienda.tienda.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineaCarrito {
    private int id;
    private double subtotal;
    private Product producto;
    private int cantidad;
}
