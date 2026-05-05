package com.tienda.carritoservice.infrastructure.adapter.output.persistence;

import com.tienda.tienda.carrito.application.port.output.DeleteLineaCarritoOutputPort;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.repository.LineaCarritoR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class DeleteLineaCarritoPersistenceAdapter implements DeleteLineaCarritoOutputPort {

    private final LineaCarritoR2dbcRepository r2dbcRepository;

    public DeleteLineaCarritoPersistenceAdapter(LineaCarritoR2dbcRepository r2dbcRepository) {
        this.r2dbcRepository = r2dbcRepository;
    }

    @Override
    public Mono<Void> deleteById(int id) {
        return r2dbcRepository.deleteById(id);
    }
}