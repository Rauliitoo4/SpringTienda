package com.tienda.tienda.carrito.infrastructure.adapter.output.persistence;

import com.tienda.tienda.carrito.domain.repository.DeleteLineaCarritoRepository;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.repository.LineaCarritoR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class DeleteLineaCarritoPersistenceAdapter implements DeleteLineaCarritoRepository {

    private final LineaCarritoR2dbcRepository jpaRepository;

    public DeleteLineaCarritoPersistenceAdapter(LineaCarritoR2dbcRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Mono<Void> deleteById(int id) {
        return jpaRepository.deleteById(id);
    }
}