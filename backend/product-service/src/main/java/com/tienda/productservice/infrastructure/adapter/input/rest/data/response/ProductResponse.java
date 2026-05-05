package com.tienda.productservice.infrastructure.adapter.input.rest.data.response;

import com.tienda.productservice.domain.model.Category;
import com.tienda.productservice.domain.model.Size;
import com.tienda.productservice.infrastructure.adapter.input.rest.data.response.PromotionResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
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
    private List<Size> sizes;
    private Category category;
    private String createdAt;;
}
