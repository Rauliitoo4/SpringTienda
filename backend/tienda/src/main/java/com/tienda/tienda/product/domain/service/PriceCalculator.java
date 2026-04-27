package com.tienda.tienda.product.domain.service;

import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.promotion.infrastructure.adapter.output.persistence.entity.PromotionEntity;
import org.springframework.stereotype.Component;

@Component
public class PriceCalculator {

    public void recalculateFinalPrice(Product product) {
        if (product.getPromotionEntities() == null || product.getPromotionEntities().isEmpty()) {
            product.setFinalPrice(product.getPrice());
            return;
        }
        double maxDescuento = product.getPromotionEntities().stream()
                .mapToDouble(PromotionEntity::getDiscount)
                .max()
                .orElse(0);
        product.setFinalPrice(product.getPrice() * (1 - maxDescuento / 100));
    }
}
