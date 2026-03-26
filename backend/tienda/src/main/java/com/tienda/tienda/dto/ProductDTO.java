package com.tienda.tienda.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductDTO {
    private int id;
    private String name;
    private Double price;
    private String descripcion;
    private String material;
    private String consideraciones;
    private List<Promotion> promociones;
}
