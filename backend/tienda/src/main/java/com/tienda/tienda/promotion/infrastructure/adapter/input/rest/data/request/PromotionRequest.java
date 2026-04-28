package com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class PromotionRequest {
    private double discount;
    private String description;
}
