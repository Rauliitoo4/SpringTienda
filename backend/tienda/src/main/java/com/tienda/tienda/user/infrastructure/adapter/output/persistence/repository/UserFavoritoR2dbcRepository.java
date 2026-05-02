package com.tienda.tienda.user.infrastructure.adapter.output.persistence.repository;

import com.tienda.tienda.user.infrastructure.adapter.output.persistence.entity.UserFavoritoEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserFavoritoR2dbcRepository extends ReactiveCrudRepository<UserFavoritoEntity, Integer> {

    @Query("SELECT product_id FROM usuario_favoritos WHERE user_id = :userId")
    Flux<Integer> findProductIdsByUserId(int userId);

    @Query("SELECT COUNT(*) FROM usuario_favoritos WHERE user_id = :userId AND product_id = :productId")
    Mono<Integer> existsRelation(int userId, int productId);

    @Query("INSERT INTO usuario_favoritos (user_id, product_id) VALUES (:userId, :productId)")
    Mono<Void> insertRelation(int userId, int productId);

    @Query("DELETE FROM usuario_favoritos WHERE user_id = :userId AND product_id = :productId")
    Mono<Void> deleteRelation(int userId, int productId);
}