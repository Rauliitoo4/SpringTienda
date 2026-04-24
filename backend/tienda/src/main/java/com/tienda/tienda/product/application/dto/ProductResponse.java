package com.tienda.tienda.product.application.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import com.tienda.tienda.promotion.application.dto.PromotionResponse;

@Getter
@Setter
public class ProductResponse {
    Integer id;
    String name;
    double price;
    double finalPrice;
    String description;
    String material;
    String considerations;
    String imageUrl;
    List<PromotionResponse> promotions;

}
