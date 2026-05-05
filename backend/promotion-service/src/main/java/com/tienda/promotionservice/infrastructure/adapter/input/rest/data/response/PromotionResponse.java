package com.tienda.promotionservice.infrastructure.adapter.input.rest.data.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class PromotionResponse {
    private Integer id;
    private double discount;
    private String description;
}
