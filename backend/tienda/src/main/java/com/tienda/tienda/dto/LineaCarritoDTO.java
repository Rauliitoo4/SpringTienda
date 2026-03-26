package com.tienda.tienda.dto;

import lombok.Data;
import com.tienda.tienda.model.Product;

@Data
public class LineaCarritoDTO {
    private int id;
    private double subtotal;
    private Product producto;
    private int cantidad;
}
