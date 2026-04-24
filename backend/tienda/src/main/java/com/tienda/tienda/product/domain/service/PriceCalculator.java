package com.tienda.tienda.product.domain.service;

import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.promotion.domain.Promotion;
import org.springframework.stereotype.Component;

@Component
public class PriceCalculator {

    public void recalculateFinalPrice(Product product) {
        if (product.getPromotions() == null || product.getPromotions().isEmpty()) {
            product.setFinalPrice(product.getPrice());
            return;
        }
        double maxDescuento = product.getPromotions().stream()
                .mapToDouble(Promotion::getDiscount)
                .max()
                .orElse(0);
        product.setFinalPrice(product.getPrice() * (1 - maxDescuento / 100));
    }
}
