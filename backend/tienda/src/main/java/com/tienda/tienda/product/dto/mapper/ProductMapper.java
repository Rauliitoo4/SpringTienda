package com.tienda.tienda.product.dto.mapper;

import com.tienda.tienda.product.dto.ProductDTO;
import com.tienda.tienda.promotion.dto.PromotionDTO;
import com.tienda.tienda.product.model.Product;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product p) {
        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setPrice(p.getPrice());
        dto.setFinalPrice(p.getFinalPrice());
        dto.setDescription(p.getDescription());
        dto.setMaterial(p.getMaterial());
        dto.setConsiderations(p.getConsiderations());
        dto.setImageUrl(p.getImageUrl());

        if (p.getPromotions() != null) {
            List<PromotionDTO> promosDTO = p.getPromotions().stream().map(promo -> {
                PromotionDTO pDTO = new PromotionDTO();
                pDTO.setId(promo.getId());
                pDTO.setDescription(promo.getDescription());
                pDTO.setDiscount(promo.getDiscount());
                return pDTO;
            }).collect(Collectors.toList());
            dto.setPromotions(promosDTO);
        } else {
            dto.setPromotions(Collections.emptyList());
        }
        return dto;
    }

    public Product toEntity(ProductDTO dto){
        Product p = new Product();
        p.setName(dto.getName());
        p.setPrice(dto.getPrice());
        p.setFinalPrice(dto.getPrice());
        p.setDescription(dto.getDescription());
        p.setMaterial(dto.getMaterial());
        p.setConsiderations(dto.getConsiderations());
        p.setImageUrl(dto.getImageUrl());
        return p;
    }
}
