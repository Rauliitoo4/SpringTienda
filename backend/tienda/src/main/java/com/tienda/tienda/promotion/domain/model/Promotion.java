package com.tienda.tienda.promotion.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {

    private Integer id;
    private double discount;
    private String description;
}
