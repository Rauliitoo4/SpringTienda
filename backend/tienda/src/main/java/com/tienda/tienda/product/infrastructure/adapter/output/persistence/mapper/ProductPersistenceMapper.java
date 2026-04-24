package com.tienda.tienda.product.infrastructure.adapter.output.persistence.mapper;

import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.infrastructure.adapter.output.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductPersistenceMapper {

    public ProductEntity toEntity(Product p) {
        ProductEntity entity = new ProductEntity();
        entity.setId(p.getId());
        entity.setName(p.getName());
        entity.setPrice(p.getPrice());
        entity.setFinalPrice(p.getFinalPrice());
        entity.setDescription(p.getDescription());
        entity.setMaterial(p.getMaterial());
        entity.setConsiderations(p.getConsiderations());
        entity.setImageUrl(p.getImageUrl());
        return entity;
    }

    public Product toDomain(ProductEntity e) {
        Product product = new Product();
        product.setId(e.getId());
        product.setName(e.getName());
        product.setPrice(e.getPrice());
        product.setFinalPrice(e.getFinalPrice());
        product.setDescription(e.getDescription());
        product.setMaterial(e.getMaterial());
        product.setConsiderations(e.getConsiderations());
        product.setImageUrl(e.getImageUrl());
        return product;
    }
}