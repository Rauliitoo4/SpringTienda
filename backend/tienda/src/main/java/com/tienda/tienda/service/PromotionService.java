package com.tienda.tienda.service;

import com.tienda.tienda.dto.*;
import com.tienda.tienda.model.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class PromotionService {
    
    private List<Promotion> promociones = new ArrayList<>();

    public PromotionDTO createPromotion(PromotionDTO dto) {
        Promotion promotion = new Promotion();
        promotion.setId(dto.getId());
        promotion.setDescuento(dto.getDescuento());
        promotion.setDescripcion(dto.getDescripcion());
        
        promociones.add(promotion);
        return convertToDTO(promotion);
    }

    public PromotionDTO updatePromotion(int id, PromotionDTO dto) {
        for (Promotion promo: promociones) {
            if (promo.getId() == id) {
                if (dto.getDescuento() >= 0) promo.setDescuento(dto.getDescuento());
                if (dto.getDescripcion() != null) promo.setDescripcion(dto.getDescripcion());
                return convertToDTO(promo);
            }
        }
        return null;
    }

    public boolean deletePromotion (int id) {
        return promociones.removeIf(p -> p.getId() == id);
    }

    public PromotionDTO getPromotionById (int id){
        return promociones.stream()
                    .filter(p -> p.getId() == id)
                    .findFirst()
                    .map(this::convertToDTO)
                    .orElse(null);
    }

    public List<PromotionDTO> getAllPromotions() {
        List<PromotionDTO> listDTO = new ArrayList<>();
        for (Promotion promotion : promociones){
            listDTO.add(convertToDTO(promotion));
        }
        return listDTO;
    }

    private PromotionDTO convertToDTO (Promotion promotion) {
        PromotionDTO dto = new PromotionDTO();
        dto.setId(promotion.getId());
        dto.setDescuento(promotion.getDescuento());
        dto.setDescripcion(promotion.getDescripcion());
        return dto;
    }
}
