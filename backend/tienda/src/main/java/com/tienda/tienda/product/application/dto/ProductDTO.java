package com.tienda.tienda.product.application.dto;

import java.util.List;

import com.tienda.tienda.promotion.application.dto.PromotionDTO;
import lombok.Data;

@Data
public class ProductDTO {
    private int id;
    private String name;
    private double price;
    private double finalPrice;
    private String description;
    private String material;
    private String considerations;
    private String imageUrl;
    private List<PromotionDTO> promotions;

    public void setPrrice(double v) {

    }
}
