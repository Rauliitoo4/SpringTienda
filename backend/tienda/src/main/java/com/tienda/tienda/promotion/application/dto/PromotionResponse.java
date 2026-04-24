package com.tienda.tienda.promotion.application.dto;

import lombok.Data;

@Data
public class PromotionResponse {
    private int id;
    private double discount;
    private String description;
}
