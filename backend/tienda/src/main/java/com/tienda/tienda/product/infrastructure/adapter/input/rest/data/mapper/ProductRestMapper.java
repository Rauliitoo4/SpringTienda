package com.tienda.tienda.product.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.request.ProductRequest;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.response.ProductResponse;
import com.tienda.tienda.promotion.application.dto.PromotionResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ProductRestMapper {

    public Product toDomain(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setFinalPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setMaterial(request.getMaterial());
        product.setConsiderations(request.getConsiderations());
        product.setImageUrl(request.getImageUrl());
        return product;
    }

    public ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setFinalPrice(product.getFinalPrice());
        response.setDescription(product.getDescription());
        response.setMaterial(product.getMaterial());
        response.setConsiderations(product.getConsiderations());
        response.setImageUrl(product.getImageUrl());

        List<PromotionResponse> promos = product.getPromotions() == null
                ? Collections.emptyList()
                : product.getPromotions().stream()
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
