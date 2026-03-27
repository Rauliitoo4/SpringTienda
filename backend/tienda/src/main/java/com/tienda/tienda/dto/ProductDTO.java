package com.tienda.tienda.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductDTO {
    private int id;
    private String nombre;
    private double precio;
    private String descripcion;
    private String material;
    private String consideraciones;
    private List<PromotionDTO> promociones;
}
