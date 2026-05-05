package com.tienda.productservice.application.service;

import com.tienda.productservice.domain.model.Product;
import org.springframework.stereotype.Component;

@Component
public class PriceCalculator {

    public void recalculateFinalPrice(Product product) {
        product.setFinalPrice(product.getPrice());
    }
}