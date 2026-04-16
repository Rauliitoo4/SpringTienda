package com.tienda.tienda.dto;

import lombok.Data;

@Data
public class PromotionDTO {
    private int id;
    private double discount;
    private String description;

    public void setDiscounç(double v) {

    }
}
