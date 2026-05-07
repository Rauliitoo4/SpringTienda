package com.tienda.productservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Integer id;
    private String name;
    private double price;
    private double finalPrice;
    private String description;
    private String material;
    private String considerations;
    private String imageUrl;
    private List<Promotion> promotions = new ArrayList<>();
    private List<Size> sizes = new ArrayList<>(Arrays.asList(Size.values()));
    private Category category;
    private LocalDateTime createdAt = LocalDateTime.now();
}
