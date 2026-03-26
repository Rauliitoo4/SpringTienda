package com.tienda.tienda.dto;

import lombok.Data;
import java.util.List;
import com.tienda.tienda.model.Product;

@Data
public class CarritoDTO {
    private int id;
    private double total;
    private List<Product> productos;
}
