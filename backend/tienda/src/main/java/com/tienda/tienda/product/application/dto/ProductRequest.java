package com.tienda.tienda.product.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private double price;
    private String description;
    private String material;
    private String considerations;
    private String imageUrl;
}
