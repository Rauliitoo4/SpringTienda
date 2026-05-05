package com.tienda.userservice.application.port.output;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserFavoritoOutputPort {
    Flux<Integer> findProductIdsByUserId(int userId);
    Mono<Integer> existsRelation(int userId, int productId);
    Mono<Void> insertRelation(int userId, int productId);
    Mono<Void> deleteRelation(int userId, int productId);
}
