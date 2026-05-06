package com.tienda.carritoservice.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel {
    private Integer id;
    private String name;
    private double price;
    private double finalPrice;
    private String imageUrl;
}