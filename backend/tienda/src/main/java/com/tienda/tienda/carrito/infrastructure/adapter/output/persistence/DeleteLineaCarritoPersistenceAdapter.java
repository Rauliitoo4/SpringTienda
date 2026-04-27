package com.tienda.tienda.carrito.infrastructure.adapter.output.persistence;

import com.tienda.tienda.carrito.domain.repository.DeleteLineaCarritoRepository;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.repository.LineaCarritoJpaRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class DeleteLineaCarritoPersistenceAdapter implements DeleteLineaCarritoRepository {

    private final LineaCarritoJpaRepository jpaRepository;

    public DeleteLineaCarritoPersistenceAdapter(LineaCarritoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Mono<Void> deleteById(int id) {
        return jpaRepository.deleteById(id);
    }
}