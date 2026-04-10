package com.tienda.tienda.repository;

import com.tienda.tienda.model.Promotion;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PromotionRepository extends ReactiveCrudRepository<Promotion, Integer>{
    
}
