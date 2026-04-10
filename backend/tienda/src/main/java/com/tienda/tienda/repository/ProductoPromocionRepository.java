package com.tienda.tienda.repository;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoPromocionRepository extends ReactiveCrudRepository<ProductoPromocionRepository.ProductoPromocion, Integer> {

    @Query("SELECT promocion_id FROM producto_promocion WHERE producto_id = :productoId")
    Flux<Integer> findPromotionIdsByProductId(int productoId);

    @Query("DELETE FROM producto_promocion WHERE producto_id = :productoId AND promocion_id = :promocionId")
    Mono<Void> deleteByProductIdAndPromotionId(int productoId, int promocionId);

    @Query("SELECT COUNT(*) FROM producto_promocion WHERE producto_id = :productoId AND pomocion_id = :promocionId")
    Mono<Integer> existsRelation(int productoId, int promocionId);

    @Data
    @Table("producto_promocion")
    class ProductoPromocion {
        @Id
        private Integer id;
        private Integer productoId;
        private Integer promocionId;
    }
}
