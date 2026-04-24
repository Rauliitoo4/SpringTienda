package com.tienda.tienda.product.application.mapper;

import com.tienda.tienda.product.application.dto.ProductResponse;
import com.tienda.tienda.promotion.application.dto.PromotionResponse;
import com.tienda.tienda.product.domain.model.Product;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ProductResponseMapper {

    public ProductResponse toResponse(Product p) {
        ProductResponse response = new ProductResponse();
        response.setId(p.getId());
        response.setName(p.getName());
        response.setPrice(p.getPrice());
        response.setFinalPrice(p.getFinalPrice());
        response.setDescription(p.getDescription());
        response.setMaterial(p.getMaterial());
        response.setConsiderations(p.getConsiderations());
        response.setImageUrl(p.getImageUrl());

        List<PromotionResponse> promos = p.getPromotions() == null
                ? Collections.emptyList()
                : p.getPromotions().stream()
                        .map(pr -> {
                            PromotionResponse promoResponse = new PromotionResponse();
                            promoResponse.setId(pr.getId());
                            promoResponse.setDescription(pr.getDescription());
                            promoResponse.setDiscount(pr.getDiscount());
                            return promoResponse;
                        })
                        .toList();
        response.setPromotions(promos);
        return response;
    }
}
