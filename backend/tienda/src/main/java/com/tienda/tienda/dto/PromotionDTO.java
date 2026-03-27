package com.tienda.tienda.dto;

import lombok.Data;

@Data
public class PromotionDTO {
    private int id;
    private double descuento;
    private String descripcion;
}
