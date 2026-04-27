package com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionRequest {
    private double discount;
    private String description;
}
