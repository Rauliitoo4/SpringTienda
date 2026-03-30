package com.tienda.tienda.repository;

import com.tienda.tienda.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Integer>{
    
}
