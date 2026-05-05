package com.tienda.carritoservice.infrastructure.adapter.output.persistence;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.application.port.output.CreateLineaCarritoOutputPort;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.mapper.LineaCarritoPersistenceMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.repository.LineaCarritoR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CreateLineaCarritoPersistenceAdapter implements CreateLineaCarritoOutputPort {

    private final LineaCarritoR2dbcRepository r2dbcRepository;
    private final LineaCarritoPersistenceMapper mapper;

    public CreateLineaCarritoPersistenceAdapter(LineaCarritoR2dbcRepository r2dbcRepository, LineaCarritoPersistenceMapper mapper) {
        this.r2dbcRepository = r2dbcRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<LineaCarrito> save(LineaCarrito lineaCarrito) {
        return r2dbcRepository.save(mapper.toEntity(lineaCarrito))
                .map(mapper::toDomain);
    }
}