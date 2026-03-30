package com.tienda.tienda.controller;

import com.tienda.tienda.dto.PromotionDTO;
import com.tienda.tienda.service.PromotionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/promociones")
public class PromotionController {
    
    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService){
        this.promotionService = promotionService;
    }

    @GetMapping
    public List<PromotionDTO> getAllUsers() {
        return promotionService.getAllPromotions();
    }

    @GetMapping("/{id}")
    public PromotionDTO getPromotionById(@PathVariable int id) {
        return promotionService.getPromotionById(id);
    }

    @PostMapping
    public PromotionDTO createPromotion(@RequestBody PromotionDTO dto) {
        return promotionService.createPromotion(dto);
    }

    @PutMapping("/{id}")
    public PromotionDTO updatePromotion(@PathVariable int id, @RequestBody PromotionDTO dto) {
        return promotionService.updatePromotion(id, dto);
    }
    
    @DeleteMapping("/{id}")
    public boolean deletePromotion(@PathVariable int id) {
        return promotionService.deletePromotion(id);
    }
    
    
}
