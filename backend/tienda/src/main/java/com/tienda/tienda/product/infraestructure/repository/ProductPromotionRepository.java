package com.tienda.tienda.product.infraestructure.repository;

import com.tienda.tienda.product.application.port.ProductPromotionRepositoryPort;
import lombok.Data;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductPromotionRepository extends ReactiveCrudRepository<ProductPromotionRepository.ProductPromotion, Integer> , ProductPromotionRepositoryPort {

    @Query("SELECT promotion_id FROM producto_promocion WHERE product_id = :productId")
    Flux<Integer> findPromotionIdsByProductId(int productId);

    @Query("DELETE FROM producto_promocion WHERE product_id = :productId AND promotion_id = :promotionId")
    Mono<Void> deleteByProductIdAndPromotionId(int producoId, int promotionId);

    @Query("SELECT COUNT(*) FROM producto_promocion WHERE product_id = :productId AND promotion_id = :promotionId")
    Mono<Integer> existsRelation(int productId, int promotionId);

    @Query("DELETE FROM producto_promocion WHERE promotion_id = :promotionId")
    Mono<Void> deleteByPromotionId(int promotionId);

    @Query("INSERT INTO producto_promocion (product_id, promotion_id) VALUES (:productId, :promotionId)")
    Mono<Void> insertRelation(int productId, int promotionId);

    @Data
    @Table("producto_promocion")
    class ProductPromotion {
        @Column("product_id")
        private Integer productId;

        @Column("promotion_id")
        private Integer promotionId;
    }
}
