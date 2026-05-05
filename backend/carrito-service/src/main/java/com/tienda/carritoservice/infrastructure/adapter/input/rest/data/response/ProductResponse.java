package com.tienda.carritoservice.infrastructure.adapter.input.rest.data.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Integer id;
    private String name;
    private double price;
    private double finalPrice;
    private String category;
    private String imageUrl;
}