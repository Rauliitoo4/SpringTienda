package com.tienda.productservice.infrastructure.adapter.input.rest.data.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromotionResponse {
    private Integer id;
    private double discount;
    private String description;
}