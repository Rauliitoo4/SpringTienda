package com.tienda.tienda.product.infrastructure.adapter.input.rest.data.response;

import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.response.PromotionResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductResponse {
    private Integer id;
    private String name;
    private double price;
    private double finalPrice;
    private String description;
    private String material;
    private String considerations;
    private String imageUrl;
    private List<PromotionResponse> promotions;
}
