package com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionResponse {
    private Integer id;
    private double discount;
    private String description;
}
