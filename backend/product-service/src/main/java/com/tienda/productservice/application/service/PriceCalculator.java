package com.tienda.productservice.application.service;

import com.tienda.productservice.application.model.PromotionModel;
import com.tienda.productservice.domain.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PriceCalculator {

    public void recalculateFinalPrice(Product product, List<PromotionModel> promotions) {
        if (promotions == null || promotions.isEmpty()) {
            product.setFinalPrice(product.getPrice());
            return;
        }
        double maxDescuento = promotions.stream()
                .mapToDouble(PromotionModel::getDiscount)
                .max()
                .orElse(0);
        product.setFinalPrice(product.getPrice() * (1 - maxDescuento / 100));
    }
}