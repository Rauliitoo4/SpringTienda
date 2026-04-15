package com.tienda.tienda.dto.mapper;

import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.dto.PromotionDTO;
import com.tienda.tienda.model.Product;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product p) {
        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setPrecio(p.getPrecio());
        dto.setPrecioFinal(p.getPrecioFinal());
        dto.setDescripcion(p.getDescripcion());
        dto.setMaterial(p.getMaterial());
        dto.setConsideraciones(p.getConsideraciones());
        dto.setImagenUrl(p.getImagenUrl());

        if (p.getPromociones() != null) {
            List<PromotionDTO> promosDTO = p.getPromociones().stream().map(promo -> {
                PromotionDTO pDTO = new PromotionDTO();
                pDTO.setId(promo.getId());
                pDTO.setDescripcion(promo.getDescripcion());
                pDTO.setDescuento(promo.getDescuento());
                return pDTO;
            }).collect(Collectors.toList());
            dto.setPromociones(promosDTO);
        } else {
            dto.setPromociones(Collections.emptyList());
        }
        return dto;
    }

    public Product toEntity(ProductDTO dto){
        Product p = new Product();
        p.setNombre(dto.getNombre());
        p.setPrecio(dto.getPrecio());
        p.setPrecioFinal(dto.getPrecio());
        p.setDescripcion(dto.getDescripcion());
        p.setMaterial(dto.getMaterial());
        p.setConsideraciones(dto.getConsideraciones());
        p.setImagenUrl(dto.getImagenUrl());
        return p;
    }
}
