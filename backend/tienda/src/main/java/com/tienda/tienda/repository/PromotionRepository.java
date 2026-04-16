package com.tienda.tienda.repository;

import com.tienda.tienda.model.Carrito;
import com.tienda.tienda.model.LineaCarrito;
import com.tienda.tienda.model.Promotion;
import com.tienda.tienda.repository.port.PromotionRepositoryPort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PromotionRepository extends ReactiveCrudRepository<Promotion, Integer> , PromotionRepositoryPort {
    Mono<Promotion> save(Promotion promotion);
}
