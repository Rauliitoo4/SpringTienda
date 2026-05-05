package com.tienda.productservice.infrastructure.adapter.input.rest.data.request;

import com.tienda.productservice.domain.model.Category;
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
    private Category category;
}
