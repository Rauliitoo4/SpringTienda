package com.tienda.tienda.product.infrastructure.adapter.input.rest.data.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ProductRequest {
    String name;
    private double price;
    private String description;
    private String material;
    private String considerations;
    private String imageUrl;
}
