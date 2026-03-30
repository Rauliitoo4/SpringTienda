package com.tienda.tienda.service;

import com.tienda.tienda.dto.*;
import com.tienda.tienda.model.*;
import org.springframework.stereotype.Service;
import com.tienda.tienda.repository.PromotionRepository;

import java.util.List;
import java.util.ArrayList;

@Service
public class PromotionService {
    
    private final PromotionRepository promotionRepo;

    public PromotionService(PromotionRepository promotionRepo){
        this.promotionRepo = promotionRepo;
    }

    public PromotionDTO createPromotion(PromotionDTO dto) {
        Promotion promotion = convertToEntity(dto);
        promotionRepo.save(promotion);
        return convertToDTO(promotion);
    }

    public PromotionDTO updatePromotion(int id, PromotionDTO dto) {
        Promotion promotion = promotionRepo.findById(id).orElse(null);
        if (promotion == null) return null;

        if (dto.getDescuento() >= 0) promotion.setDescuento(dto.getDescuento());
        if (dto.getDescripcion() != null) promotion.setDescripcion(dto.getDescripcion());

        promotionRepo.save(promotion);
        return convertToDTO(promotion);
    }

    public boolean deletePromotion (int id) {
        if (!promotionRepo.existsById(id)) return false;
        promotionRepo.deleteById(id);
        return true;
    }

    public PromotionDTO getPromotionById (int id){
        return promotionRepo.findById(id)
                    .map(this::convertToDTO)
                    .orElse(null);
    }

    public List<PromotionDTO> getAllPromotions() {
        List <PromotionDTO> listDTO = new ArrayList<>();
        for (Promotion promotion : promotionRepo.findAll()) {
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

    private Promotion convertToEntity (PromotionDTO dto) {
        Promotion promo = new Promotion();
        promo.setDescripcion(dto.getDescripcion());
        promo.setDescuento(dto.getDescuento());
        return promo;
    }
}
